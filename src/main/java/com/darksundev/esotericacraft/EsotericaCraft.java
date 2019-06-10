package com.darksundev.esotericacraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EsotericaCraft.modid)
public class EsotericaCraft
{
	public static EsotericaCraft instance;
	private static RuneManager runeManager;
	
	public static final String modid = "esotericacraft";
	public static final Logger logger = LogManager.getLogger(modid);
	
	public EsotericaCraft()
	{
		instance = this;
		runeManager = new RuneManager();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		RuneList.registerRunes();
		logger.info("Registered Runes");
	}
}
