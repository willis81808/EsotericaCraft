package com.darksundev.esotericacraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.darksundev.esotericacraft.core.ClientProxy;
import com.darksundev.esotericacraft.core.IProxy;
import com.darksundev.esotericacraft.core.ServerProxy;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.mojang.brigadier.Message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EsotericaCraft.modid)
@SuppressWarnings("unused")
public class EsotericaCraft
{
	public static EsotericaCraft instance;
	private static RuneManager runeManager;
	private static EsotericaWorldSave modSaveData;
	
	public static final String modid = "esotericacraft";
	public static final Logger logger = LogManager.getLogger(modid);
	
	public static IProxy proxy = DistExecutor.runForDist(
		() -> () -> new ClientProxy(),
		() -> () -> new ServerProxy()
	);
	
	public EsotericaCraft()
	{
		instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(FMLCommonSetupEvent event)
	{
		runeManager = new RuneManager();
		modSaveData = new EsotericaWorldSave();
		
		RuneList.registerRunes();
		logger.info("Registered Runes");
		
		// run sided initialization
		proxy.init();
	}
	
	public static void messagePlayer(PlayerEntity player, String message, TextFormatting...formattings)
	{
		// construct message
		ITextComponent text = TextComponentUtils.toTextComponent(new Message() {
			@Override
			public String getString() { return message; }
		});
		if (formattings == null || formattings.length > 0)
		{
			text.applyTextStyles(formattings);
		}
		else
		{
			text.applyTextStyles(TextFormatting.YELLOW, TextFormatting.ITALIC);
		}
		// send message
		player.sendMessage(text);
	}
}
