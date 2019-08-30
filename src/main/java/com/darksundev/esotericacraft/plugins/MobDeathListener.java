package com.darksundev.esotericacraft.plugins;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class MobDeathListener
{

	@SubscribeEvent
	public static void rollBlazeDropOnDrowned(LivingDeathEvent event)
	{
		// extract event
		LivingEntity entity = event.getEntityLiving();
		Vec3d position = entity.getPositionVec();
		World world = entity.getEntityWorld();
		if (world.isRemote)
			return;
		
		// 50/50 chance of spawning blaze rod if this event was for a blaze who drowned
		if (entity.getType() == EntityType.BLAZE && event.getSource() == DamageSource.DROWN && EsotericaCraft.rng.nextBoolean())
		{
			// drop spawner item in world
			ItemEntity item = new ItemEntity(world, position.x, position.y + .5, position.z, new ItemStack(Items.BLAZE_ROD));
			world.addEntity(item);
		}
	}
}
