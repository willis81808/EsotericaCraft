package com.darksundev.esotericacraft.runes;

import net.minecraft.block.BlockState;

public class RuneCast
{
	private Rune rune;
	private BlockState[] enchantBlocks, mundaneBlocks;
	private String key;
	
	public RuneCast(String key, Rune rune, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		this.key = key;
		this.rune = rune;
		this.enchantBlocks = enchantBlocks;
	}
	public Rune getRune()
	{
		return rune;
	}
	public BlockState[] getEnchantBlocks()
	{
		return enchantBlocks;
	}
	public BlockState[] getMundaneBlocks()
	{
		return mundaneBlocks;
	}
	public String getKey()
	{
		return key;
	}
}
