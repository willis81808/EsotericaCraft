package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.HashMap;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;

import net.minecraft.block.BlockState;

public class RuneManager
{
	private static RuneManager instance;
	public static enum Tier { NONE, MUNDANE, ENCHANTED }
	
	private static HashMap<String, Rune> patternMap = new HashMap<String, Rune>();
	private static HashMap<String, RuneMaterial> blockTierMap = new HashMap<String, RuneMaterial>();

	public RuneManager()
	{
		if (instance == null)
		{
			instance = this;
		}
		else
		{
			EsotericaCraft.logger.error("Trying to create a second instance of RuneManager!");
		}
		
		RuneList.registerRunes();
		RuneList.registerAllRuneMaterials();
	}
	
	public static void registerRune(Rune r)
	{
		String key = r.getKey();
		if (!patternMap.containsKey(key))
		{
			patternMap.put(key, r);
		}
	}
	public static void registerRuneMaterial(RuneMaterial material)
	{
		if (!blockTierMap.containsKey(material.blockID))
		{
			blockTierMap.put(material.blockID, material);
		}
	}
	
	public static Tier getTier(String blockId)
	{
		RuneMaterial result = blockTierMap.get(blockId);
		if (result == null)
			return Tier.NONE;
		else
			return result.tier;
	}
	public static RuneCast getRune(BlockState[][] blocks)
	{
		// construct pattern from passed blocks
		ArrayList<BlockState> enchantBlocks = new ArrayList<BlockState>();
		Tier[][] pattern = new Tier[blocks.length][blocks[0].length];
		for (int x = 0; x < blocks.length; x++)
		{
			for (int y = 0; y < blocks[0].length; y++)
			{
				Tier t = getTier(blocks[x][y].getBlock().getTranslationKey());
				pattern[x][y] = t;
				if (t == Tier.ENCHANTED)
					enchantBlocks.add(blocks[x][y]);
			}
		}

		// return rune of pattern or null if no pattern found
		String key = Rune.StringFromPattern(pattern);
		return new RuneCast(key, patternMap.get(key), enchantBlocks.toArray(new BlockState[] {}));
	}
}