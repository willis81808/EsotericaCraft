package com.darksundev.esotericacraft.lists;

import java.util.HashMap;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.runes.Rune;
import com.darksundev.esotericacraft.runes.RuneManager;
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
			
			EsotericaWorldSave.get(context.getWorld());
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
			
			EsotericaWorldSave.get(context.getWorld());
		}
		
	};
	
	public static void registerRunes()
	{
		RuneManager.registerRune(TeleportTransmitter);
		RuneManager.registerRune(TeleportReceiver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune Component Blocks

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
		"block.minecraft.cobblestone",
		"block.minecraft.dirt"
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
	
	public static void registerBlocks()
	{
		// register mundane
		for (String mundane : mundaneBlocks)
		{
			RuneManager.registerTier(mundane, Tier.MUNDANE);
		}
		
		// register enchanted
		for (String enchanted : enchantedBlocks)
		{
			RuneManager.registerTier(enchanted, Tier.ENCHANTED);
		}
	}
}
