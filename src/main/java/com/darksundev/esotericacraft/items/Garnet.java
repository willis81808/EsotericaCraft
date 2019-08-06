package com.darksundev.esotericacraft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class Garnet extends Item
{

	public Garnet(Properties properties) 
	{
		super
		(
			properties.maxStackSize(64)
		);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		/*
		World w = context.getWorld();
		BlockPos pos = context.getPos();
		if (!w.isRemote)
		{
			w.addEntity(new RuneObserverEntity(w, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5));
		}
		*/
		
		return super.onItemUse(context);
	}

}
