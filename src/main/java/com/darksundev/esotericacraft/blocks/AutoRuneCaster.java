package com.darksundev.esotericacraft.blocks;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.runes.RuneCast;
import com.darksundev.esotericacraft.runes.RuneManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AutoRuneCaster extends Block
{
	private boolean isPowered = false;
	
	public AutoRuneCaster()
	{
		super
		(
			Block.Properties.create(Material.IRON)
				.hardnessAndResistance(1, 10)
				.sound(SoundType.STONE)
				.lightValue(10)
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		
		// check for any change to OUR redstone state
		boolean newPowerStatus = worldIn.isBlockPowered(pos);
		if (isPowered != newPowerStatus)
		{
			isPowered = newPowerStatus;
			
			if (isPowered)
				castRune(worldIn, pos);
		}
	}
	
	private void castRune(World world, BlockPos rootPos)
	{
		if (!world.isRemote)
		{
			// look for rune in area
			BlockState[][] area = RuneManager.getArea(world, rootPos);
			RuneCast cast = RuneManager.getRune(area);
			
			// cast rune if valid
			if (cast.getRune() != null)
			{
				// spawn fire particles on sucessfull cast
				//EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new RuneCastMessagePacket(rootPos, ParticleType.FIRE));
				
				// cast rune
				cast.getRune().onCast(null, world, rootPos, area, cast.getEnchantBlocks(), cast.getMundaneBlocks());
				
				// backup rune data
				EsotericaWorldSave.backupData();
			}
			else
			{
				// spawn smoke particles when invalid rune/area is selected
				//EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new RuneCastMessagePacket(rootPos, ParticleType.SMOKE));
			}
		}
	}
}
