package com.darksundev.esotericacraft.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.plugins.SignShopManager;
import com.darksundev.esotericacraft.plugins.SignShopManager.SignShopData;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.Tags.Blocks;

@Mixin(HopperTileEntity.class)
public abstract class MixinHopperTileEntity extends LockableLootTileEntity implements IHopper, ITickableTileEntity
{
	protected MixinHopperTileEntity(TileEntityType<?> typeIn) { super(typeIn); }
	
	@Shadow
	private int transferCooldown;
	@Shadow
	private long tickedGameTime;
	@Shadow
	abstract boolean isOnTransferCooldown();
	@Shadow
	abstract boolean updateHopper(Supplier<Boolean> p_200109_1_);
	@Shadow
	public abstract void setTransferCooldown(int ticks);
	
	private boolean isBelowChestShop()
	{
		BlockPos pos = getPos().up();
		BlockState above = world.getBlockState(pos);

		if (Blocks.CHESTS_WOODEN.contains(above.getBlock()))
		{
			EsotericaCraft.logger.info("Chest found");
			
			// is there a sign nearby?
			SignTileEntity sign = SignShopManager.findSign(pos, world);

			// check again for double chests
			if (sign == null && above.get(ChestBlock.TYPE) != ChestType.SINGLE)
			{
				Direction dir = ChestBlock.getDirectionToAttached(above);
				sign = SignShopManager.findSign(pos.add(dir.getDirectionVec()), world);
			}
			
			// sign found
			if (sign != null)
			{
				EsotericaCraft.logger.info("Found Sign");
				
				// is the sign a valid shop sign?
				SignShopData data = new SignShopData(sign.signText);
				if (data.isValidShop)
				{
					EsotericaCraft.logger.info("Is Shop");
					return false;
				}
			}
		}

		HopperTileEntity me = (HopperTileEntity)(Object)this;
		return HopperTileEntity.pullItems(me);
	}
	
	@Overwrite
	public void tick() {
		if (this.world != null && !this.world.isRemote)
		{
			--this.transferCooldown;
			this.tickedGameTime = this.world.getGameTime();
			if (!this.isOnTransferCooldown())
			{
				this.setTransferCooldown(0);
				this.updateHopper(this::isBelowChestShop);
			}
		}
	}
}
