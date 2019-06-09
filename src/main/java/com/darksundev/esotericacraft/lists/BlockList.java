package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.blocks.StonePath;

import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockList
{
	public static Block stone_path_block;
	
	public static void registerBlocks(IForgeRegistry<Block> registry)
	{
		// build path block
		stone_path_block = new StonePath();
		stone_path_block.setRegistryName(Registrar.location("stone_path_block"));
		
		// register blocks
		registry.registerAll(stone_path_block);
	}
}
