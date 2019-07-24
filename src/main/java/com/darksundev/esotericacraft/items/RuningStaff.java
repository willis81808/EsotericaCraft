package com.darksundev.esotericacraft.items;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.RuneCastMessagePacket;
import com.darksundev.esotericacraft.RuneCastMessagePacket.ParticleType;
import com.darksundev.esotericacraft.runes.RuneCast;
import com.darksundev.esotericacraft.runes.RuneManager;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class RuningStaff extends Item
{
	public RuningStaff(Properties properties)
	{
		super
		(
			properties.maxStackSize(1)
		);
	}
	
	private BlockState[][] getArea(World world, BlockPos rootPos)
	{
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
		
		return area;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{		
		World world = context.getWorld();
		BlockPos rootPos = context.getPos();

		if (!world.isRemote)
		{
			// look for rune in area
			BlockState[][] area = getArea(world, rootPos);
			RuneCast cast = RuneManager.getRune(area);
			
			// try looking above selected area if first try didn't work
			// (this covers a situation where the player hasn't filled in the center
			// of the rune, and has to select the block below the center)
			if (cast.getRune() == null)
			{
				area = getArea(world, rootPos.up());
				cast = RuneManager.getRune(area);
			}
			
			// cast rune if valid
			if (cast.getRune() != null)
			{
				// spawn fire particles on sucessfull cast
				EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new RuneCastMessagePacket(rootPos, ParticleType.FIRE));
				
				// cast rune
				cast.getRune().onCast(context, area, cast.getEnchantBlocks(), cast.getMundaneBlocks());
				
				// backup rune data
				EsotericaWorldSave.backupData();
			}
			else
			{
				// spawn smoke particles when invalid rune/area is selected
				EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new RuneCastMessagePacket(rootPos, ParticleType.SMOKE));
			}
		}
		return super.onItemUse(context);
	}

}
