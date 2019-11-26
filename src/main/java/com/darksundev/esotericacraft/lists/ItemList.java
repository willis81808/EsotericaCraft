package com.darksundev.esotericacraft.lists;

import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.items.FakePotion;
import com.darksundev.esotericacraft.items.DiamondDust;
import com.darksundev.esotericacraft.items.Garnet;
import com.darksundev.esotericacraft.items.PogoStick;
import com.darksundev.esotericacraft.items.RuningStaff;
import com.darksundev.esotericacraft.items.TeleportArrowItem;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemList
{
	public static Item stone_path_block;
	public static Item auto_rune_caster_block;
	public static Item pogo_stick, green_pogo_stick, blue_pogo_stick, yellow_pogo_stick;
	public static Item runing_staff;
	public static Item diamond_dust;
	public static Item garnet;
	public static Item sacraficial_fire;
	public static Item teleport_arrow;
	public static Item weird_potion, blink_potion;
	
	private static Item.Properties category = new Item.Properties().group(ItemGroup.MISC);
	
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		// blocks
		stone_path_block = new BlockItem(BlockList.stone_path_block, category).setRegistryName(BlockList.stone_path_block.getRegistryName());
		auto_rune_caster_block = new BlockItem(BlockList.auto_rune_caster_block, category).setRegistryName(BlockList.auto_rune_caster_block.getRegistryName());
		sacraficial_fire = new BlockItem(BlockList.sacraficial_fire, category).setRegistryName(BlockList.sacraficial_fire.getRegistryName());
		
		// pogo sticks
		pogo_stick = new PogoStick(category).setRegistryName(Registrar.location("pogo_stick"));
		green_pogo_stick = new PogoStick(category).setRegistryName(Registrar.location("green_pogo_stick"));
		blue_pogo_stick = new PogoStick(category).setRegistryName(Registrar.location("blue_pogo_stick"));
		yellow_pogo_stick = new PogoStick(category).setRegistryName(Registrar.location("yellow_pogo_stick"));
		
		// runing staff
		runing_staff = new RuningStaff(category).setRegistryName(Registrar.location("runing_staff"));

		// cafting items and materials
		diamond_dust = new DiamondDust(category).setRegistryName(Registrar.location("diamond_dust"));
		garnet = new Garnet(category).setRegistryName(Registrar.location("garnet"));

		// arrows
		teleport_arrow = new TeleportArrowItem(category).setRegistryName(Registrar.location("teleport_arrow"));
		
		// misc
		weird_potion = new FakePotion(category, false).setRegistryName(Registrar.location("weird_potion"));
		blink_potion = new FakePotion(category, true).setRegistryName(Registrar.location("blink_potion"));
		
		/* Registration */
		registry.registerAll(
			// blocks
			stone_path_block,
			auto_rune_caster_block,
			sacraficial_fire,
			
			// items
			pogo_stick, green_pogo_stick, blue_pogo_stick, yellow_pogo_stick,
			runing_staff,
			diamond_dust,
			garnet,
			teleport_arrow,
			weird_potion, blink_potion
		);
	}
}
