package com.darksundev.esotericacraft;

import com.darksundev.esotericacraft.dimension.DynamicDimension;
import com.darksundev.esotericacraft.dimension.DynamicModDimension;
import com.darksundev.esotericacraft.lists.BlockList;
import com.darksundev.esotericacraft.lists.EntityList;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.lists.PotionList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = EsotericaCraft.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Registrar
{
    @ObjectHolder(DynamicDimension.resourceLocation)
    public static final DynamicModDimension DYNAMIC_DIMENSION = null;

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
	//@SubscribeEvent
	//public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event)
	//{
	//	  IRecipeSerializer.CRAFTING_SHAPELESS.
	//}
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event)
	{
		PotionList.registerPotions(event.getRegistry());
	}
    @SubscribeEvent
	public static void onDimensionRegistryEvent(RegistryEvent.Register<ModDimension> event)
	{
		event.getRegistry().register(new DynamicModDimension().setRegistryName(DynamicDimension.resourceLocation));
	}
    @SubscribeEvent
	public static void onDimensionChunkGeneratorRegistryEvent(RegistryEvent.Register<ChunkGeneratorType<?,?>> event)
	{
    	DynamicDimension.VOID_CHUNK_GENERATOR.setRegistryName(location("void_chunk_generator"));
    	event.getRegistry().register(DynamicDimension.VOID_CHUNK_GENERATOR);
	}
	
	public static ResourceLocation location(String name)
	{
		return new ResourceLocation(EsotericaCraft.modid, name);
	}
}
