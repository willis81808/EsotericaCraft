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
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		TileEntity te = world.getTileEntity(pos.up());

		// open menu
		player.openContainer(new SimpleNamedContainerProvider((id, pInv, p) -> {
	         return ContainerList.TEST_CONTAINER.create(id, pInv);
	      }, new StringTextComponent("")));
		*/
		return super.onItemUse(context);
	}
}
