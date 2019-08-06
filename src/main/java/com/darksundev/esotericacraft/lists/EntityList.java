package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.entities.MiningFatigueObserver;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class EntityList
{
	public static EntityType<MiningFatigueObserver> rune_observer;

	@SuppressWarnings("unchecked")
	public static void registerEntities(IForgeRegistry<EntityType<?>> registry)
	{
		rune_observer = (EntityType<MiningFatigueObserver>) EntityType.Builder.<MiningFatigueObserver>
			create(MiningFatigueObserver::new, EntityClassification.MISC)
			.size(.01f, .01f)
			.build("rune_observer")
			.setRegistryName(Registrar.location("rune_observer"));
		
		registry.register(rune_observer);
	}
}
