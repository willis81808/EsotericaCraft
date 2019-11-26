package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.entities.MiningFatigueObserver;
import com.darksundev.esotericacraft.entities.TeleportArrowEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class EntityList
{
	public static EntityType<MiningFatigueObserver> rune_observer;
	public static EntityType<TeleportArrowEntity> teleport_arrow_entity;

	@SuppressWarnings("unchecked")
	public static void registerEntities(IForgeRegistry<EntityType<?>> registry)
	{
		rune_observer = (EntityType<MiningFatigueObserver>) EntityType.Builder.<MiningFatigueObserver>
			create(MiningFatigueObserver::new, EntityClassification.MISC)
			.size(.01f, .01f)
			.build("rune_observer")
			.setRegistryName(Registrar.location("rune_observer"));
		
		teleport_arrow_entity = (EntityType<TeleportArrowEntity>) EntityType.Builder.<TeleportArrowEntity>
			create(TeleportArrowEntity::new, EntityClassification.MISC)
			.build("teleport_arrow_entity")
			.setRegistryName(Registrar.location("teleport_arrow_entity"));
		
		registry.registerAll(rune_observer, teleport_arrow_entity);
	}
}
