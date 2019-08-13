package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.Utils;
import com.darksundev.esotericacraft.plugins.SleepManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class SleepVoteCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("sleepvote")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.literal("percentage").then(Commands.argument("percent", IntegerArgumentType.integer()).executes((cs) -> {
					return setPercentage(cs.getSource(), IntegerArgumentType.getInteger(cs, "percent"));
				}))).then(Commands.literal("alerts").then(Commands.literal("true").executes((cs) -> {
					return toggleAlerts(cs.getSource(), cs.getSource().asPlayer(), true);
				})).then(Commands.literal("false").executes((cs) -> {
					return toggleAlerts(cs.getSource(), cs.getSource().asPlayer(), false);
				}))));
	}
	
	private static int setPercentage(CommandSource source, int value)
	{
		SleepManager.setSleepPercentage(value / 100D);
		source.sendFeedback(Utils.textComponentFromString(String.format("Sleep vote will now pass when %d", value) + "% of players sleep"), true);
		return 1;
	}
	private static int toggleAlerts(CommandSource source, ServerPlayerEntity sender, boolean enabled)
	{
		SleepManager.setAlertsEnabled(enabled);
		source.sendFeedback(Utils.textComponentFromString(String.format("Vote alerts are: %s", (enabled ? TextFormatting.GREEN + "enabled" : TextFormatting.RED + "disabled"))), true);
		return 1;
	}
}
