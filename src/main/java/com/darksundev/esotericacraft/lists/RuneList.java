package com.darksundev.esotericacraft.lists;

import java.util.HashMap;

import com.darksundev.esotericacraft.runes.Dampen;
import com.darksundev.esotericacraft.runes.Disintegrate;
import com.darksundev.esotericacraft.runes.ExplorerRune;
import com.darksundev.esotericacraft.runes.PocketDimension;
import com.darksundev.esotericacraft.runes.RecallRune;
import com.darksundev.esotericacraft.runes.Rune;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.runes.RuneMaterial;
import com.darksundev.esotericacraft.runes.SoulTrap;
import com.darksundev.esotericacraft.runes.SoulTrapLite;
import com.darksundev.esotericacraft.runes.TeleportEraser;
import com.darksundev.esotericacraft.runes.TeleportLinkAdapter;
import com.darksundev.esotericacraft.runes.TeleportReceiver;
import com.darksundev.esotericacraft.runes.TeleportTransmitter;
import com.darksundev.esotericacraft.runes.WaypointPortal;

public class RuneList
{
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune and Rune Material registration
	public static void registerRunes()
	{
		RuneManager.registerRune(teleportTransmitter);
		RuneManager.registerRune(teleportReceiver);
		RuneManager.registerRune(waypointPortal);
		RuneManager.registerRune(teleportEraser);
		RuneManager.registerRune(soulTrap);
		RuneManager.registerRune(soulTrapLite);
		RuneManager.registerRune(dampen);
		RuneManager.registerRune(disintegrate);
		RuneManager.registerRune(pocketDimension);
		RuneManager.registerRune(recall);
		RuneManager.registerRune(explorer);
	}
	public static void registerAllRuneMaterials()
	{
		for (RuneMaterial block : runeMaterials)
		{
			RuneManager.registerRuneMaterial(block);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune Shape Recipes
	public static HashMap<String, TeleportLinkAdapter> teleportLinksBuffer = new HashMap<String, TeleportLinkAdapter>();
	
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
	private static final Rune teleportTransmitter = new TeleportTransmitter();
	
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
	public static final Rune teleportReceiver = new TeleportReceiver();
	
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 *  *: Redstone Required
	 * 
	 * 		- - - - -
	 * 		- * O * -
	 * 		- O M O -
	 * 		- * O * -
	 * 	    - - - - -
	 */	
	public static final Rune waypointPortal = new WaypointPortal();
	
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	O: Enchanted required
	 *  i: Redstone Torches
	 *  *: Redstone wire
	 * 
	 * 		- * - * -
	 * 		* i O i *
	 * 		- O O O -
	 * 		* i O i *
	 * 	    - * - * -
	 */	
	public static final Rune teleportEraser = new TeleportEraser();

	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required  (specifically Emerald blocks)
	 * 
	 * 		O - - - O
	 * 		- O - O -
	 * 		- - M - -
	 * 		- O - O -
	 *		O - - - O
	 */
	public static final Rune soulTrap = new SoulTrap();

	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	E: Emerald Block required
	 * 	O: Enchanted required
	 *  +: End Rod required
	 * 
	 * 		- - E - -
	 * 		- - + - -
	 * 		E + O + E
	 * 		- - + - -
	 *		- - E - -
	 */
	public static final Rune soulTrapLite = new SoulTrapLite();

	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	O: Enchanted required  (specifically Emerald or Diamond blocks)
	 *  X: Mundane required
	 * 
	 * 		- O O O -
	 * 		O - X - O
	 * 		O X X X O
	 * 		O - X - O
	 * 	    - O O O -
	 */	
	public static Rune dampen = new Dampen();

	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- - O - -
	 * 		- O - O -
	 *		O - M - O
	 * 		- O - O -
	 *		- - O - -
	 */
	public static Rune disintegrate = new Disintegrate();

	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 * 
	 * 		- E E E -
	 * 		E E O E E
	 * 		E O - O E
	 * 		E E O E E
	 * 		- E E E -
	 */
	public static Rune pocketDimension = new PocketDimension();

	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 *  +: Redstone dust required
	 * 
	 * 		- + + + -
	 * 		+ - + - +
	 * 		+ + E + +
	 * 		+ - + - +
	 * 		- + + + -
	 */
	public static Rune recall = new RecallRune();
	
	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 * 
	 * 		O - E - O
	 * 		- - O - -
	 * 		E O - O E
	 * 		- - O - -
	 * 		O - E - O
	 */
	public static Rune explorer = new ExplorerRune();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Rune Component Blocks
	private static final RuneMaterial[] runeMaterials =
	{
							/*-------- MIXED --------*/
		// Prismarine
		new RuneMaterial("block.minecraft.prismarine", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.prismarine_bricks", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_prismarine", 		Tier.ENCHANTED),
		// End
		new RuneMaterial("block.minecraft.end_stone_bricks", 		Tier.MUNDANE),
		new RuneMaterial("block.minecraft.end_stone", 				Tier.MUNDANE),
		new RuneMaterial("block.minecraft.end_rod", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purpur_block", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purpur_pillar", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.obsidian", 				Tier.ENCHANTED),
			
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
		new RuneMaterial("block.minecraft.bamboo",					Tier.MUNDANE),
		new RuneMaterial("block.minecraft.bamboo_sapling",			Tier.MUNDANE),
		
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
		new RuneMaterial("block.minecraft.nether_wart_block",		Tier.ENCHANTED),
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
		// Stairs
		new RuneMaterial("block.minecraft.oak_stairs", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_stairs",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_stairs",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_stairs",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.oak_stairs", 				Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cobblestone_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brick_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stone_brick_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.nether_brick_stairs", 	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.sandstone_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_stairs",			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.quartz_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_sandstone_stairs", 	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purpur_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.prismarine_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.prismarine_brick_stairs", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_prismarine_stairs", 	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.granite_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_granite_stairs", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.diorite_stairs", 			Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_diorite_stairs", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.andesite_stairs", 		Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_andesite_stairs",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_nether_brick_stairs", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.end_stone_brick_stairs", 	Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_stone_brick_stairs",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_cobblestone_stairs",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_sandstone_stairs", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_red_sandstone_stairs",Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_quartz_stairs", 	Tier.ENCHANTED),
		// Slabs
		new RuneMaterial("block.minecraft.red_sandstone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.purpur_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_stone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.sandstone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.petrified_oak_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cobblestone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stone_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.nether_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.quartz_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.oak_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.prismarine_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.prismarine_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_prismarine_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.granite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_granite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.diorite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_diorite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.andesite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.polished_andesite_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_nether_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.end_stone_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_stone_brick_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_cobblestone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_sandstone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_red_sandstone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.smooth_quartz_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cut_sandstone_slab", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.cut_red_sandstone_slab", Tier.ENCHANTED),
		// Walls
		new RuneMaterial("block.minecraft.cobblestone_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_cobblestone_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.granite_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.diorite_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.andesite_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.prismarine_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.stone_brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.mossy_stone_brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.end_stone_brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.nether_brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_nether_brick_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.sandstone_wall", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.red_sandstone_wall", Tier.ENCHANTED),
		// Fences
		new RuneMaterial("block.minecraft.oak_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.spruce_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.birch_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.jungle_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.acacia_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.dark_oak_fence", Tier.ENCHANTED),
		new RuneMaterial("block.minecraft.nether_brick_fence", Tier.ENCHANTED),
	};
	
}
