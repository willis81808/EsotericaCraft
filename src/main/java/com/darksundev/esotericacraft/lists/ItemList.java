package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.items.PogoStick;
import com.darksundev.esotericacraft.items.RuningStaff;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemList
{
	public static Item stone_path_block;
	public static Item auto_rune_caster_block;
	public static Item pogo_stick;
	public static Item runing_staff;
	public static Item diamond_dust;
	public static Item garnet;
	
	private static Item.Properties category = new Item.Properties().group(ItemGroup.MISC);
	
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		/* Instantiation */
		
		// blocks
		stone_path_block = new BlockItem(BlockList.stone_path_block, category)
				.setRegistryName(BlockList.stone_path_block.getRegistryName());
		auto_rune_caster_block = new BlockItem(BlockList.auto_rune_caster_block, category)
				.setRegistryName(BlockList.auto_rune_caster_block.getRegistryName());
		
		// tools
		pogo_stick = new PogoStick(category)
				.setRegistryName(Registrar.location("pogo_stick"));
		runing_staff = new RuningStaff(category)
				.setRegistryName(Registrar.location("runing_staff"));

		// cafting Items
		diamond_dust = new RuningStaff(category)
				.setRegistryName(Registrar.location("diamond_dust"));
		garnet = new RuningStaff(category)
				.setRegistryName(Registrar.location("garnet"));

		
		/* Registration */
		registry.registerAll(stone_path_block, auto_rune_caster_block, pogo_stick, runing_staff, diamond_dust, garnet);
	}
}
