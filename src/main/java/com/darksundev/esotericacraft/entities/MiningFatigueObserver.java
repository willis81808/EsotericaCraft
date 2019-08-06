package com.darksundev.esotericacraft.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class MiningFatigueObserver extends RuneObserverEntity
{
	public MiningFatigueObserver(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
	public MiningFatigueObserver(EntityType<? extends RuneObserverEntity> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
	}

	@Override
	public void observeTick()
	{
		// get players in area
		AxisAlignedBB region = new AxisAlignedBB(getPosition());
		region = region.expand(30, 30, 30).expand(-30, -30, -30);
		for (PlayerEntity p : world.getEntitiesWithinAABB(PlayerEntity.class, region))
		{
			// apply fatigue to them
			p.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 35, 10));
		}
	}

}
