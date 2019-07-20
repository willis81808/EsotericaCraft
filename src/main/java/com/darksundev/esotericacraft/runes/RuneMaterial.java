package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;

public class RuneMaterial
{
	private String blockID;
	private Tier tier;
	
	public RuneMaterial(String blockID, Tier tier)
	{
		this.blockID = blockID;
		this.tier = tier;
	}

	public Tier getTier()
	{
		return tier;
	}
	public String getBlockID()
	{
		return blockID;
	}
}
