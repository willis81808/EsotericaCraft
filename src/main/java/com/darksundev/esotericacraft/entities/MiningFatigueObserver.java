package com.darksundev.esotericacraft.entities;

import java.util.ArrayList;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.commands.ModOverrideCommand;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class MiningFatigueObserver extends RuneObserverEntity
{
	private static ArrayList<MiningFatigueObserver> instances = new ArrayList<MiningFatigueObserver>();
	public static final int RANGE = 30;
	
	public MiningFatigueObserver(World world, double x, double y, double z)
	{
		super(world, x, y, z);
		startTracking(this);
	}
	public MiningFatigueObserver(EntityType<? extends RuneObserverEntity> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
		startTracking(this);
	}
	
	public static void startTracking(MiningFatigueObserver observer)
	{
		if (observer.world.isRemote)
			return;
		if (!instances.contains(observer))
			instances.add(observer);
	}
	public static void stopTracking(MiningFatigueObserver observer)
	{
		if (observer.world.isRemote)
			return;
		instances.remove(observer);
	}

	@Override
	public void onRemovedFromWorld()
	{
		super.onRemovedFromWorld();
		stopTracking(this);
	}
	
	@Override
	public void onAddedToWorld()
	{
		super.onAddedToWorld();
		startTracking(this);
	}
	@Override
	public void observeTick()
	{
		// server side only
		if (world.isRemote)
			return;

		// get players in area
		AxisAlignedBB region = new AxisAlignedBB(getPosition());
		region = region.expand(RANGE, 256, RANGE).expand(-RANGE, -256, -RANGE);
		for (PlayerEntity p : world.getEntitiesWithinAABB(PlayerEntity.class, region))
		{
			// don't apply effect to players running override
			if (!ModOverrideCommand.hasOverridePermission(p))
			{
				// is player within range?
				if (distanceBetween(p.getPosition(), this.getPosition()) <= RANGE)
				{
					// apply fatigue	
					p.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 40, 1));
				}
			}
		}
	}
	
	private static double distanceBetween(BlockPos p1, BlockPos p2)
	{
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2));
	}

	/* Entity Spawn event */

	@SubscribeEvent
	public static void onMobSpawnAttempt(LivingSpawnEvent.CheckSpawn event)
	{
		if (event.getEntityLiving() instanceof PlayerEntity || event.isSpawner() || event.getSpawnReason() != SpawnReason.NATURAL)
		{
			return;
		}

		switch (event.getSpawnReason())
		{
		case NATURAL:
		case CHUNK_GENERATION:
		case EVENT:
		case PATROL:
		case REINFORCEMENT:
			// cancel all natural spawns within radius of active dampen runes
			BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
			for (MiningFatigueObserver i : instances)
			{
				//if (pos.withinDistance(p.getPosition(), RANGE+1))
				if (distanceBetween(pos, i.getPosition()) <= RANGE)
				{
					event.setResult(Result.DENY);
				}
			}
			break;
		default:
			break;
		}
	}
	
	/* Block break events */
	@SubscribeEvent
	public static void onEntityPlaceEvent(EntityPlaceEvent event)
	{
		// only run on server side
		if (event.getWorld().isRemote())
			return;
		
		// don't apply effect to players running override
		
		if (event.getEntity() instanceof PlayerEntity && ModOverrideCommand.hasOverridePermission((PlayerEntity)event.getEntity()))
			return;
		
		BlockPos pos = event.getPos();
		for (MiningFatigueObserver i : instances)
		{
			// cancel event if player within range of active rune
			
			//if (pos.withinDistance(p.getPosition(), RANGE+1))
			if (distanceBetween(pos, i.getPosition()) <= RANGE)
			{
				event.setCanceled(true);
				return;
			}
		}
	}	@SubscribeEvent
	public static void onBreakEvent(BreakEvent event)
	{
		// exit if client side, or if player is in override mode
		if (event.getWorld().isRemote() || ModOverrideCommand.hasOverridePermission(event.getPlayer()))
			return;
		
		BlockPos pos = event.getPos();
		for (MiningFatigueObserver i : instances)
		{
			// cancel event if player within range of active rune
			//if (pos.withinDistance(p.getPosition(), RANGE+1))
			if (distanceBetween(pos, i.getPosition()) <= RANGE)
			{
				event.setCanceled(true);
				return;
			}
		}
	}
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Start event)
	{
		// exit if client side
		if (event.getWorld().isRemote())
			return;
		
		//event.getExplosion().getAffectedBlockPositions().forEach(pos -> {});
		BlockPos pos = new BlockPos(event.getExplosion().getPosition());
		for (MiningFatigueObserver i : instances)
		{
			// cancel event if player within range of active rune
			//if (pos.withinDistance(p.getPosition(), RANGE+1 + 10))
			if (distanceBetween(pos, i.getPosition()) <= RANGE)
			{
				event.setCanceled(true);
				return;
			}
		}
		
	}
}
