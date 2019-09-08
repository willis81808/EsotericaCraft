package com.darksundev.esotericacraft.items;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.plugins.SignShopManager.SignShopData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		TileEntity te = world.getTileEntity(pos.up());
		
		exit:
		if (te instanceof SignTileEntity)
		{
			EsotericaCraft.logger.info("Edit sign...");
			SignTileEntity ste = (SignTileEntity) te;
			
			// check for sign shop, and verify ownership prior to editing sign
			SignShopData shop = new SignShopData(ste.signText);
			if (shop.isValidShop && !shop.owner.equals(player.getDisplayName().getString()))
					break exit;
			
			ste.setPlayer(player);
			ste.setEditable(true);
			player.openSignEditor(ste);
		}
		return super.onItemUse(context);
	}
	
	
}
