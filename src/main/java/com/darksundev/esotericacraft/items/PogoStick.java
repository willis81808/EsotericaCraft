package com.darksundev.esotericacraft.items;

import com.darksundev.esotericacraft.lists.ItemList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PogoStick extends Item
{
	public static final double JUMP_FORCE = 1;
	private static final String FALL_DISTANCE_KEY = "fallDistance";
	
	public PogoStick(Properties properties)
	{
		super
		(
			properties.maxStackSize(1)
		);
	}
	
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (worldIn.isRemote || !(entityIn instanceof PlayerEntity))
			return;
		
		// create NBT tag if item doesn't have one already
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
			stack.getTag().putFloat(FALL_DISTANCE_KEY, 0f);
		}
		
		CompoundNBT compound = stack.getTag();
		PlayerEntity playerIn = (PlayerEntity) entityIn;
		
		// check if player is holding a pogo stick in either hand
		if (playerIn.getHeldItemMainhand().getItem() == ItemList.pogo_stick ||
			playerIn.getHeldItemOffhand().getItem() == ItemList.pogo_stick)
		{
			// update fall distance if falling
			if (playerIn.fallDistance > 0)
			{
				compound.putFloat(FALL_DISTANCE_KEY, playerIn.fallDistance);
			}
			// bounce player if we just hit the ground
			else if (playerIn.onGround && compound.getFloat(FALL_DISTANCE_KEY) > .5f)
			{
				float fallDistance = compound.getFloat(FALL_DISTANCE_KEY);
				compound.putFloat(FALL_DISTANCE_KEY, 0);

				if (!playerIn.isSneaking())
					Jump(playerIn, fallDistance);
			}
			
			// stop fall damage
			playerIn.fallDistance = 0;
		}
		else
		{
			compound.putFloat(FALL_DISTANCE_KEY, 0);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);

		if (playerIn.onGround && !worldIn.isRemote)
			Jump(playerIn);
		
		return result;
	}
	
	private void Jump(PlayerEntity player) { Jump(player, 1); }
	private void Jump(PlayerEntity player, double modifier)
	{
		player.addVelocity(0, JUMP_FORCE * modifier, 0);
		player.velocityChanged = true;
	}
}