package com.darksundev.esotericacraft.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.plugins.TaskManager;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class ExecuteOnLoginCommand
{
	private static HashMap<UUID, ArrayList<String>> playerCommandMap = new HashMap<UUID, ArrayList<String>>();

	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("onlogin")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.argument("target", StringArgumentType.string()).then(Commands.argument("command", StringArgumentType.string()).executes((cs) -> {
					return addCommandToPlayer(cs.getSource(), StringArgumentType.getString(cs, "target"), StringArgumentType.getString(cs, "command"));
				}))));
	}
	
	private static int addCommandToPlayer(CommandSource cs, String player, String command)
	{
		GameProfile profile = cs.getServer().getPlayerProfileCache().getGameProfileForUsername(player);
		
		UUID id = profile.getId();
		
		if (!playerCommandMap.containsKey(id))
		{
			playerCommandMap.put(id, new ArrayList<String>());
		}
		ArrayList<String> commands = playerCommandMap.get(id);
		commands.add(command);
		playerCommandMap.put(id, commands);
		
		cs.sendFeedback(new StringTextComponent(String.format("Will execute command when player %s joins the game", player)).applyTextStyle(TextFormatting.GREEN), true);
		
		return 1;
	}
	

	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		UUID id = event.getPlayer().getUniqueID();
		if (playerCommandMap.containsKey(id))
		{
			MinecraftServer server = event.getPlayer().getServer();
			TaskManager.enqueueTask(100, () -> {
				for (String command : playerCommandMap.get(id))
				{
					server.getCommandManager().handleCommand(server.getCommandSource(), command);
				}
				playerCommandMap.remove(id);
			});
		}
	}
}
