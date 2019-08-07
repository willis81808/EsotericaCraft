package com.darksundev.esotericacraft.plugins;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class SleepManager
{
	private interface ChangeTimeCommand {
		public void execute();
	}
	
	private static final Random rng = new Random();
	private static long ticks = 0;
	private static LinkedList<ChangeTimeCommand> commands = new LinkedList<ChangeTimeCommand>();

	// TODO: serialize and save this data to a file, preferrably human readable-editable
	private static final long MORNING = 1000;
	private static boolean alertsEnabled = true;
	private static double percentage = .32;											// this percentage of players or more must be sleeping to skip the night
	private static List<PlayerEntity> sleeping = new ArrayList<PlayerEntity>();		// list of sleeping players (aka sleep-vote tally)
	private static String[] flavorText =											// flavor text for sleeping alerts
		{
			"started counting sheep",
			"wants to catch some Zs",
			"is all tucked in",
			"got in bed",
			"hit the hay",
			"hit the sack",
			"is pooped",
			"dropped off to sleep",
			"wants their beauty sleep",
			"is ready to turn in",
			"is fast asleep",
			"is sound asleep",
			"couldn't keep their eyes open",
		};
	
	/* Minecraft Event Listeners */
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event)
	{
		ticks++;
		
		if (ticks % 150 == 0 && !commands.isEmpty())
		{
			commands.getLast().execute();;
			commands.removeLast();
		}
	}
	@SubscribeEvent
	public static void onPlayerSleep(PlayerSleepInBedEvent event)
	{
		PlayerEntity player = event.getEntityPlayer();
		playerEnteredBed(player);
	}
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event)
	{
		PlayerEntity player = event.getEntityPlayer();
		if(!player.world.isRemote())
		{
			playerLeftBed(player);
		}
	}

	public static void setAlertsEnabled(boolean enabled)
	{
		alertsEnabled = enabled;
	}
	public static boolean getAlertsEnabled()
	{
		return alertsEnabled;
	}

	public static void setSleepPercentage(double percentage)
	{
		SleepManager.percentage = Math.max(0, Math.min(1, percentage));
	}
	public static double getSleepPercentage()
	{
		return SleepManager.percentage;
	}
	
	// Tracking votes & sleepers
	public static boolean playerEnteredBed(PlayerEntity player)
	{
		Boolean isDay = player.getServer().getWorld(DimensionType.OVERWORLD).isDaytime();
		if (sleeping.contains(player))
		{
			// player is already in sleeping list...?
			return false;
		}
		else if (!isDay)
		{
			// add player to list
			sleeping.add(player);
			
			// alert server of new sleep vote
			ITextComponent alert = player.getDisplayName().appendText(" " + getSleepText());
			EsotericaCraft.messageAllPlayers(player.getServer().getPlayerList(), alert, TextFormatting.WHITE);
			
			// check if vote passed
			forceSleepCheck(player.getServer());
			return true;
		}
		else
		{
			return false;
		}
	}
	public static boolean playerLeftBed(PlayerEntity player)
	{
		if (!sleeping.contains(player))
		{
			return false;
		}
		else
		{
			sleeping.remove(player);
			return true;
		}
	}

	// Return random flavor text
	private static String getSleepText()
	{
		return flavorText[rng.nextInt(flavorText.length)];
	}
	
	// Tally votes and change time to day if vote is passing
	private static void forceSleepCheck(MinecraftServer server)
	{
		PlayerList players = server.getPlayerList();
		int playerCount = players.getOnlinePlayerNames().length;
		if ((double)sleeping.size() / playerCount >= percentage)
		{
			EsotericaCraft.messageAllPlayers(players,
					String.format("%s/%s Vote passed! Setting time to day", sleeping.size(), playerCount)
				);

			//schedule a change time command
			commands.add(() -> {
				server.getWorld(DimensionType.OVERWORLD).setDayTime(MORNING);
				sleeping.clear();
			});
		}
		else if (alertsEnabled)
		{
			EsotericaCraft.messageAllPlayers(players,
					String.format("%s/%s players sleeping. %s more votes needed to skip the night", sleeping.size(), playerCount, Math.ceil(playerCount * percentage) - sleeping.size())
				);
		}
	}
}
