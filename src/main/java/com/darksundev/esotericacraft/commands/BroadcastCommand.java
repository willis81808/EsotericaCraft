package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;

public class BroadcastCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("broadcast")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.argument("message", StringArgumentType.string()).executes((cs) -> {
					return Broadcast(cs.getSource(), StringArgumentType.getString(cs, "message"));
				})));
	}
	private static int Broadcast(CommandSource source, String message)
	{
		EsotericaCraft.messageAllPlayers(source.getServer().getPlayerList(), message, TextFormatting.RED, TextFormatting.ITALIC);
		return 1;
	}
}
