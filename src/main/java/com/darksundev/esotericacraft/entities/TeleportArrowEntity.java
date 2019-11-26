package com.darksundev.esotericacraft.entities;

import com.darksundev.esotericacraft.lists.ItemList;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TeleportArrowEntity extends ArrowEntity
{

	public TeleportArrowEntity(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	public TeleportArrowEntity(EntityType<? extends ArrowEntity> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
	}
	public TeleportArrowEntity(World worldIn, LivingEntity shooter)
	{
		super(worldIn, shooter);
	}

	@Override
	protected ItemStack getArrowStack()
	{
		return new ItemStack(ItemList.teleport_arrow);
	}
	@Override
	protected void arrowHit(LivingEntity living)
	{
		doTeleport();
	}
	@Override
	public void tick()
	{
		super.tick();
		if (this.inGround && this.timeInGround == 0)
		{
			doTeleport();
		}
	}
	
	private void doTeleport()
	{
		world.playSound((PlayerEntity)null, getShooter().getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		getShooter().setPositionAndUpdate(posX, posY, posZ);
		world.playSound((PlayerEntity)null, posX, posY, posZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		this.remove();
	}
}
