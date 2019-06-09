package com.darksundev.esotericacraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(EsotericaCraft.modid)
public class EsotericaCraft
{
	public static final String modid = "esotericacraft";
	public static final Logger logger = LogManager.getLogger(modid);
	public static EsotericaCraft instance;
	
	public EsotericaCraft()
	{
		instance = this;
		MinecraftForge.EVENT_BUS.register(this);
	}
}
