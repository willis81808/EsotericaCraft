package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Rune
{
	private String key, name;
	private Tier[][] pattern;
	
	public static String StringFromPattern(Tier[][] pattern)
	{
		StringBuilder newKey = new StringBuilder();
		for(int i=0; i<pattern.length; i++)
		{
			for (int j=0; j<pattern[0].length; j++)
			{
				switch (pattern[i][j])
				{
					case NONE:
						newKey.append(-1);
						break;
					case MUNDANE:
						newKey.append(0);
						break;
					case ENCHANTED:
						newKey.append(1);
						break;
				}
			}
			newKey.append(';');
		}
		return newKey.toString();
	}
	
	public Rune(String name, Tier[][] pattern)
	{
		this.name = name;
		this.pattern = pattern;
		key = StringFromPattern(pattern);
		System.out.println(key);
	}
	public String getKey()
	{
	  return key;
    }
	public Tier[][] getPattern()
	{
		return pattern;
	}
	//public void onCast(ItemUseContext context, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	public void onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		EsotericaCraft.logger.info("Cast Rune: " + name);
	}
}