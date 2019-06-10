package com.darksundev.esotericacraft.runes;

import net.minecraft.block.BlockState;

public class RuneCast
{
	private Rune rune;
	private BlockState[] enchantBlocks;
	public RuneCast(Rune rune, BlockState[] enchantBlocks)
	{
		this.rune = rune;
		this.enchantBlocks = enchantBlocks;
	}
	public Rune getRune() {
		return rune;
	}
	public BlockState[] getEnchantBlocks() {
		return enchantBlocks;
	}
}
