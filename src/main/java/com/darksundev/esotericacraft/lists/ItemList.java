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
	public static Item pogo_stick;
	public static Item runing_staff;
	
	private static Item.Properties category = new Item.Properties().group(ItemGroup.MISC);
	
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		// build path block item
		stone_path_block = new BlockItem(BlockList.stone_path_block, category)
				.setRegistryName(BlockList.stone_path_block.getRegistryName());
		
		// build pogo stick
		pogo_stick = new PogoStick(category)
				.setRegistryName(Registrar.location("pogo_stick"));
		
		// runing staff
		runing_staff = new RuningStaff(category)
				.setRegistryName(Registrar.location("runing_staff"));
		
		// register items
		registry.registerAll(stone_path_block, pogo_stick, runing_staff);
	}
}
