package com.darksundev.esotericacraft.items;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class RuningStaff extends Item
{

	public RuningStaff(Properties properties)
	{
		super
		(
			properties.maxStackSize(1)
		);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		// get the block the player activated
		BlockState block = context.getWorld().getBlockState(context.getPos());
		EsotericaCraft.logger.info(block.getBlock().getNameTextComponent().getFormattedText());
		
		return super.onItemUse(context);
	}

}
