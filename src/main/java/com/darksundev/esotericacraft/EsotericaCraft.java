package com.darksundev.esotericacraft;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.darksundev.esotericacraft.commands.InventoryCommand;
import com.darksundev.esotericacraft.commands.LoginCommand;
import com.darksundev.esotericacraft.commands.ModOverrideCommand;
import com.darksundev.esotericacraft.commands.NoticeCommand;
import com.darksundev.esotericacraft.commands.OfferingsCommand;
import com.darksundev.esotericacraft.commands.SleepVoteCommand;
import com.darksundev.esotericacraft.core.ClientProxy;
import com.darksundev.esotericacraft.core.IProxy;
import com.darksundev.esotericacraft.core.ServerProxy;
import com.darksundev.esotericacraft.gui.TestContainerGui;
import com.darksundev.esotericacraft.lists.ContainerList;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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
	public static final Random rng = new Random();
	public static final String NEW_LINE = "\n";
	
	//public static IProxy proxy = DistExecutor.runForDist( () -> () -> new ClientProxy(), () -> () -> new ServerProxy() );
	
	public EsotericaCraft()
	{
		instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
		MinecraftForge.EVENT_BUS.register(EsotericaCraft.class);
	}
	
	private void setup(FMLCommonSetupEvent event)
	{
		runeManager = new RuneManager();
		modSaveData = new EsotericaWorldSave();
		RuneList.registerRunes();
		logger.info("Registered Runes");
		
		// register client side packets
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientProxy.init());
		
	}
	private void clientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(ContainerList.TEST_CONTAINER, TestContainerGui::new );
	}
	
    public void serverStarting(FMLServerStartingEvent event)
    {
    	CommandDispatcher<CommandSource> dispatch = event.getCommandDispatcher();

    	// register commands
    	InventoryCommand.register(dispatch);
    	ModOverrideCommand.register(dispatch);
    	SleepVoteCommand.register(dispatch);
    	NoticeCommand.register(dispatch);
    	LoginCommand.register(dispatch);
    	OfferingsCommand.register(dispatch);
    }
	
	public static void messagePlayer(PlayerEntity player, String message, TextFormatting... formattings)
	{
		// construct message
		ITextComponent text = Utils.textComponentFromString(message);
		if (formattings != null && formattings.length > 0)
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
	public static void messagePlayer(PlayerEntity player, ITextComponent message, TextFormatting... formattings)
	{
		if (formattings != null && formattings.length > 0)
		{
			message.applyTextStyles(formattings);
		}

		// send message
		player.sendMessage(message);
	}
	public static void messageAllPlayers(PlayerList players, String message, TextFormatting... formattings)
	{
		// construct message
		ITextComponent text = Utils.textComponentFromString(message);
		if (formattings != null && formattings.length > 0)
		{
			text.applyTextStyles(formattings);
		}
		else
		{
			text.applyTextStyles(TextFormatting.YELLOW, TextFormatting.ITALIC);
		}
		// send message
		players.sendMessage(text);
	}
	public static void messageAllPlayers(PlayerList players, ITextComponent message, TextFormatting... formattings)
	{
		if (formattings != null && formattings.length > 0)
		{
			message.applyTextStyles(formattings);
		}

		// send message
		players.sendMessage(message);
	}
}
