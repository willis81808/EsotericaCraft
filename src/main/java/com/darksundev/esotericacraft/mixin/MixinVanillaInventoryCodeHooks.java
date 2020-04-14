package com.darksundev.esotericacraft.mixin;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.darksundev.esotericacraft.plugins.SignShopManager;
import com.darksundev.esotericacraft.plugins.SignShopManager.SignShopData;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

@Mixin(VanillaInventoryCodeHooks.class)
public abstract class MixinVanillaInventoryCodeHooks
{
	@Shadow
	private static LazyOptional<Pair<IItemHandler, Object>> getItemHandler(IHopper hopper, Direction hopperFacing)
    {
        double x = hopper.getXPos() + (double) hopperFacing.getXOffset();
        double y = hopper.getYPos() + (double) hopperFacing.getYOffset();
        double z = hopper.getZPos() + (double) hopperFacing.getZOffset();
        return VanillaInventoryCodeHooks.getItemHandler(hopper.getWorld(), x, y, z, hopperFacing.getOpposite());
    }
	
	@Overwrite
    public static Boolean extractHook(IHopper dest)
	{
		BlockPos pos = new BlockPos(dest.getXPos(), dest.getYPos() + 1, dest.getZPos());
		BlockState above = dest.getWorld().getBlockState(pos);
		if (Blocks.CHESTS_WOODEN.contains(above.getBlock()))
		{
			// is there a sign nearby?
			SignTileEntity sign = SignShopManager.findSign(pos, dest.getWorld());

			// check again for double chests
			if (sign == null && above.get(ChestBlock.TYPE) != ChestType.SINGLE)
			{
				Direction dir = ChestBlock.getDirectionToAttached(above);
				sign = SignShopManager.findSign(pos.add(dir.getDirectionVec()), dest.getWorld());
			}
			
			// sign found
			if (sign != null)
			{
				// is the sign a valid shop sign?
				SignShopData data = new SignShopData(sign.signText);
				if (data.isValidShop)
				{
					return false;
				}
			}
		}
		
        return getItemHandler(dest, Direction.UP)
                .map(itemHandlerResult -> {
                    IItemHandler handler = itemHandlerResult.getKey();

                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack extractItem = handler.extractItem(i, 1, true);
                        if (!extractItem.isEmpty())
                        {
                            for (int j = 0; j < dest.getSizeInventory(); j++)
                            {
                                ItemStack destStack = dest.getStackInSlot(j);
                                if (dest.isItemValidForSlot(j, extractItem) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize() && destStack.getCount() < dest.getInventoryStackLimit() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack)))
                                {
                                    extractItem = handler.extractItem(i, 1, false);
                                    if (destStack.isEmpty())
                                        dest.setInventorySlotContents(j, extractItem);
                                    else
                                    {
                                        destStack.grow(1);
                                        dest.setInventorySlotContents(j, destStack);
                                    }
                                    dest.markDirty();
                                    return true;
                                }
                            }
                        }
                    }

                    return false;
                })
                .orElse(null); // TODO bad null
	}
}
