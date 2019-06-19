package com.darksundev.esotericacraft.lists;

import java.util.HashMap;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.runes.Rune;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.darksundev.esotericacraft.runes.RuneMaterial;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.runes.TeleportLink;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;

public class RuneList {
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune Shape Recipes

	public static HashMap<String, TeleportLink> teleportLinks = new HashMap<String, TeleportLink>();
	
	/*
	 * 	-: Ignored
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- M O M -
	 * 		M M M M M
	 * 		O M - M O
	 * 		M M M M M
	 * 		- M O M -
	 */
	public static final Rune TeleportTransmitter = new Rune("Teleport_Transmitter", new Tier[][]{
		new Tier[]{Tier.NONE, 		Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE},
		new Tier[]{Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE},
		new Tier[]{Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE,		Tier.MUNDANE,	Tier.ENCHANTED},
		new Tier[]{Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE},
		new Tier[]{Tier.NONE, 		Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE}
	}) {

		@Override
		public void onCast(ItemUseContext context, BlockState[][] pattern, BlockState[] enchantBlocks)
		{
			super.onCast(context, pattern, enchantBlocks);
			
			StringBuilder str = new StringBuilder();
			for (BlockState markerBlock : enchantBlocks)
			{
				str.append(markerBlock.getBlock().getTranslationKey());
				str.append(';');
			}
			String key = str.toString();

			if (!teleportLinks.containsKey(key))
			{
				TeleportLink link = new TeleportLink(key, context.getPos(), null);
				teleportLinks.put(key, link);
			}
			else
			{
				TeleportLink link = teleportLinks.get(key);
				if (link.transmitter != -1)
					EsotericaCraft.logger.info("Overwriting portal receiver");
				link.transmitter = context.getPos().toLong();
				
				if (link.receiver != -1)
				{
					// teleport player
					BlockPos recieverPos = BlockPos.fromLong(link.receiver);
					context.getPlayer().setPositionAndUpdate(
						recieverPos.getX(),
						recieverPos.getY()+1,
						recieverPos.getZ()
					);
				}
			}
		}
		
	};
	/*
	 * 	-: Ignored
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- M M M -
	 * 		M M O M M
	 * 		M O - O M
	 * 		M M O M M
	 * 		- M M M -
	 */
	public static final Rune TeleportReceiver = new Rune("Teleport_Receiver", new Tier[][]{
		new Tier[]{Tier.NONE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.NONE},
		new Tier[]{Tier.MUNDANE,Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.MUNDANE},
		new Tier[]{Tier.MUNDANE,Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.MUNDANE},
		new Tier[]{Tier.MUNDANE,Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.MUNDANE},
		new Tier[]{Tier.NONE, 	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.NONE}
	}) {

		@Override
		public void onCast(ItemUseContext context, BlockState[][] pattern, BlockState[] enchantBlocks)
		{
			super.onCast(context, pattern, enchantBlocks);
			
			StringBuilder str = new StringBuilder();
			for (BlockState markerBlock : enchantBlocks)
			{
				str.append(markerBlock.getBlock().getTranslationKey());
				str.append(';');
			}
			String key = str.toString();

			if (!teleportLinks.containsKey(key))
			{
				TeleportLink link = new TeleportLink(key, null, context.getPos());
				teleportLinks.put(key, link);
			}
			else
			{
				TeleportLink link = teleportLinks.get(key);
				if (link.receiver != -1)
					EsotericaCraft.logger.info("Overwriting portal receiver");
				link.receiver = context.getPos().toLong();

				if (link.transmitter != -1)
				{
					// teleport player
					BlockPos transmitterPos = BlockPos.fromLong(link.transmitter);
					context.getPlayer().setPositionAndUpdate(
						transmitterPos.getX(),
						transmitterPos.getY()+1,
						transmitterPos.getZ()
					);
				}
			}
		}
		
	};
	
	public static void registerRunes()
	{
		RuneManager.registerRune(TeleportTransmitter);
		RuneManager.registerRune(TeleportReceiver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune Component Blocks
	private static final RuneMaterial[] runeMaterials =
	{
							/*-------- MUNDANE --------*/
		// Basics
		new RuneMaterial("block.minecraft.red_sandstone", 			Tier.MUNDANE),
		new RuneMaterial("block.minecraft.sandstone", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.cobblestone",				Tier.MUNDANE),
		// Planks
		new RuneMaterial("block.minecraft.oak_planks", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.spruce_planks", 			Tier.MUNDANE),
		new RuneMaterial("block.minecraft.birch_planks", 			Tier.MUNDANE),
		new RuneMaterial("block.minecraft.jungle_planks", 			Tier.MUNDANE),
		new RuneMaterial("block.minecraft.acacia_planks", 			Tier.MUNDANE),
		new RuneMaterial("block.minecraft.dark_oak_planks", 		Tier.MUNDANE),
		// Stones
		new RuneMaterial("block.minecraft.granite", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.diorite", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.andesite", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.polished_granite",		Tier.MUNDANE),
		new RuneMaterial("block.minecraft.polished_diorite",		Tier.MUNDANE),
		new RuneMaterial("block.minecraft.polished_andesite",		Tier.MUNDANE),
		
							/*-------- ENCHANTED --------*/
		// Misc
		new RuneMaterial("block.minecraft.bricks",		 			Tier.ENCHANTED),
		// Glass
		new RuneMaterial("block.minecraft.glass",		 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.white_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_stained_glass",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_stained_glass",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_stained_glass",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_stained_glass",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_stained_glass",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_stained_glass",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_stained_glass",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_stained_glass",		Tier.ENCHANTED),
		// Wool
		new RuneMaterial("block.minecraft.white_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_wool",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_wool",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_wool",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_wool",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_wool",				Tier.ENCHANTED),
		// Terracotta
		new RuneMaterial("block.minecraft.terracotta",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.white_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_terracotta",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_terracotta",		Tier.ENCHANTED),
		// Glazed Terracotta
		new RuneMaterial("block.minecraft.white_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_glazed_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_glazed_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_glazed_terracotta",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_glazed_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_glazed_terracotta",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_glazed_terracotta",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_glazed_terracotta",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_glazed_terracotta",		Tier.ENCHANTED),
		// Concrete
		new RuneMaterial("block.minecraft.white_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_concrete",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_concrete",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_concrete",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_concrete",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_concrete",				Tier.ENCHANTED),
		// Concrete Powder
		new RuneMaterial("block.minecraft.white_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.orange_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.magenta_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_blue_concrete_powder",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.yellow_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.lime_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.pink_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gray_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.light_gray_concrete_powder",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cyan_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purple_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.blue_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brown_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.green_concrete_powder",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_concrete_powder",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.black_concrete_powder",		Tier.ENCHANTED),
		// Resource Blocks
		new RuneMaterial("block.minecraft.diamond_block", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.emerald_block", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.redstone_block", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.gold_block", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.iron_block",	 			Tier.ENCHANTED),
		// Nether
		new RuneMaterial("block.minecraft.quartz_block", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.chiseled_quartz_block",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.quartz_pillar", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_quartz", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.netherrack",	 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.glowstone",	 			Tier.ENCHANTED),
		// Polished or Cut Sandstone
		new RuneMaterial("block.minecraft.cut_sandstone", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.chiseled_sandstone",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_sandstone", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cut_red_sandstone", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.chiseled_red_sandstone",	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_red_sandstone",	Tier.ENCHANTED),
		// Woods
		new RuneMaterial("block.minecraft.oak_wood", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_wood",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_wood", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_wood", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_wood",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_wood",			Tier.ENCHANTED),
		// Logs
		new RuneMaterial("block.minecraft.oak_log", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_log",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_log", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_log", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_log",				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_log",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_oak_log", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_spruce_log",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_birch_log", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_jungle_log", 	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_acacia_log",		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stripped_dark_oak_log",	Tier.ENCHANTED),
	};
	private static final String[] mundaneBlocks =
	{
		// sandstone
		"block.minecraft.sandstone",
		"block.minecraft.red_sandstone",
		
		// planks
		"block.minecraft.oak_planks",
		"block.minecraft.spruce_planks",
		"block.minecraft.birch_planks",
		"block.minecraft.jungle_planks",
		"block.minecraft.acacia_planks",
		"block.minecraft.dark_oak_planks",
		
		// basics
		"block.minecraft.cobblestone"
	};
	private static final String[] enchantedBlocks =
	{
		// nether
		"block.minecraft.quartz_block",
		"block.minecraft.chiseled_quartz_block",
		"block.minecraft.quartz_pillar",
		"block.minecraft.smooth_quartz",
		"block.minecraft.netherrack",
		"block.minecraft.glowstone",
			
		// ore blocks
		"block.minecraft.diamond_block",
		"block.minecraft.emerald_block",
		"block.minecraft.redstone_block",
		"block.minecraft.gold_block",
		"block.minecraft.iron_block",

		// cut/polished sandstone
		"block.minecraft.cut_sandstone",
		"block.minecraft.chiseled_sandstone",
		"block.minecraft.smooth_sandstone",
		"block.minecraft.cut_red_sandstone",
		"block.minecraft.chiseled_red_sandstone",
		"block.minecraft.smooth_red_sandstone",
		
		// woods
		"block.minecraft.oak_wood",
		"block.minecraft.spruce_wood",
		"block.minecraft.birch_wood",
		"block.minecraft.jungle_wood",
		"block.minecraft.acacia_wood",
		"block.minecraft.dark_oak_wood"
	};
	
	public static void registerAllRuneMaterials()
	{
		for (RuneMaterial block : runeMaterials)
		{
			RuneManager.registerRuneMaterial(block);
		}
		
		/* Old block registration method
		// register mundane
		for (String mundane : mundaneBlocks)
		{
			RuneManager.registerRuneMaterial(mundane, Tier.MUNDANE);
		}
		
		// register enchanted
		for (String enchanted : enchantedBlocks)
		{
			RuneManager.registerRuneMaterial(enchanted, Tier.ENCHANTED);
		}
		*/
	}
}
