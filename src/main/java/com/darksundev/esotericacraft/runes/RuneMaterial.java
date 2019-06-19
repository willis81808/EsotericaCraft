package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;

public class RuneMaterial
{
	public String blockID;
	public Tier tier;
	
	public RuneMaterial(String blockID, Tier tier)
	{
		this.blockID = blockID;
		this.tier = tier;
	}
}
