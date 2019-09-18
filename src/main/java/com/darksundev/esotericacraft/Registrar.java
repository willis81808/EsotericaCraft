package com.darksundev.esotericacraft;

import com.darksundev.esotericacraft.lists.BlockList;
import com.darksundev.esotericacraft.lists.EntityList;
import com.darksundev.esotericacraft.lists.ItemList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = EsotericaCraft.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Registrar
{
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		EsotericaCraft.logger.info("Items registered.");
		ItemList.registerItems(event.getRegistry());
	}
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		EsotericaCraft.logger.info("Blocks registered");
		BlockList.registerBlocks(event.getRegistry());
	}
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
	{
		final IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		EntityList.registerEntities(registry);
		
	}
	
	public static ResourceLocation location(String name)
	{
		return new ResourceLocation(EsotericaCraft.modid, name);
	}
}
