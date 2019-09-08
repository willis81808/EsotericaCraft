package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.blocks.AutoRuneCaster;
import com.darksundev.esotericacraft.blocks.SacrificialFire;
import com.darksundev.esotericacraft.blocks.StonePath;

import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockList
{
	public static Block stone_path_block;
	public static Block auto_rune_caster_block;
	public static Block sacraficial_fire;
	
	public static void registerBlocks(IForgeRegistry<Block> registry)
	{
		// stone path
		stone_path_block = new StonePath();
		stone_path_block.setRegistryName(Registrar.location("stone_path_block"));
		
		// auto rune caster
		auto_rune_caster_block = new AutoRuneCaster();
		auto_rune_caster_block.setRegistryName(Registrar.location("auto_rune_caster_block"));
		
		// sacraficial fire
		sacraficial_fire = new SacrificialFire();
		sacraficial_fire.setRegistryName(Registrar.location("sacraficial_fire"));
		
		// register blocks
		registry.registerAll(stone_path_block, auto_rune_caster_block, sacraficial_fire);
	}
}
