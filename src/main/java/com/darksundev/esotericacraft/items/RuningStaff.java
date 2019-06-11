package com.darksundev.esotericacraft.items;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.SerializationUtils;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneCast;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.darksundev.esotericacraft.runes.TeleportLink;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLPaths;

public class RuningStaff extends Item
{
	private static final String TELEPORT_KEY = "TeleportLinks";
	
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
		World world = context.getWorld();
		BlockPos rootPos = context.getPos();
		ItemStack item = context.getItem();
		if (!world.isRemote)
		{
			// deserialize data
			if (!item.hasTag())
			{
				item.setTag(new CompoundNBT());
				item.getTag().putByteArray(TELEPORT_KEY, SerializationUtils.serialize(RuneList.teleportLinks));
			}
			else
			{
				//byte[] data = item.getTag().getByteArray(TELEPORT_KEY);
				//RuneList.teleportLinks = SerializationUtils.deserialize(data);
			}
			
			// get cast root
			BlockState block = world.getBlockState(rootPos);
			BlockState[][] area = new BlockState[5][5];
			area[2][2] = block;

			// log block clicked
			EsotericaCraft.logger.info(block.getBlock().getTranslationKey());
			
			// get area
			for (int x = -2; x <= 2; x++)
			{
				int xVal = rootPos.getX() + x;
				for (int z = -2; z <= 2; z++)
				{
					BlockState b;
					if (!(x == 0 && z == 0))
					{
						int zVal = rootPos.getZ() + z;
						b = world.getBlockState(new BlockPos(xVal, rootPos.getY(), zVal));
						area[2+x][2+z] = b;
					}
					else 
					{
						b = block;
					}
				}
			}

			// look for rune in area
			RuneCast cast = RuneManager.getRune(area);
			if (cast.getRune() != null)
			{
				// cast rune
				cast.getRune().onCast(context, area, cast.getEnchantBlocks());
				
				// backup rune data
				EsotericaWorldSave.backupData();
			}
		}
		return super.onItemUse(context);
	}

}
