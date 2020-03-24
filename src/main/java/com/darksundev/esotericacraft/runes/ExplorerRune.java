package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ExplorerRune extends Rune
{
	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 * 
	 * 		O - E - O
	 * 		- - O - -
	 * 		E O - O E
	 * 		- - O - -
	 * 		O - E - O
	 */
	public ExplorerRune()
	{
		super("explorer", new Tier[][]{
			new Tier[]{Tier.MUNDANE,	Tier.NONE, 		Tier.ENCHANTED,	Tier.NONE,		Tier.MUNDANE},
			new Tier[]{Tier.NONE,		Tier.NONE, 		Tier.MUNDANE,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE,		Tier.MUNDANE,	Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.NONE, 		Tier.MUNDANE,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.MUNDANE,	Tier.NONE, 		Tier.ENCHANTED,	Tier.NONE,		Tier.MUNDANE}
		});
	}

	@Override
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (!super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks))
			return false;
		
		if (worldIn.dimension.getType() != DimensionType.OVERWORLD)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "This magic is unstable in alternate dimensions!");
			return false;
		}

		boolean valid = false;
		int range = 100000;
		while (!valid)
		{
			int x = EsotericaCraft.rng.nextInt(range) - (range/2);
			int z = EsotericaCraft.rng.nextInt(range) - (range/2);
			searching:
			for (int i = 256; i > 0; i--) {
				BlockPos lookingAt = new BlockPos(x, i, z);
				BlockState state = worldIn.getBlockState(lookingAt);
				if (!worldIn.isAirBlock(lookingAt))
				{
					if (state.isSolid() && worldIn.isAirBlock(lookingAt.up()))
					{
						worldIn.playSound((PlayerEntity)null, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
						TeleporterBase.preloadChunk(lookingAt.up(), worldIn, player);
						player.setPositionAndUpdate(x + .5, i + 1, z + .5);
						worldIn.playSound((PlayerEntity)null, x, i + 1, z, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
						valid = true;
					}
					break searching;
				}
			}
		}
		return true;
	}

	
}
