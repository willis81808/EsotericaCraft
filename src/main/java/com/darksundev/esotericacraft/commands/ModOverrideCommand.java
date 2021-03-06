package com.darksundev.esotericacraft.commands;

import java.util.HashSet;
import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ModOverrideCommand
{
	private static Set<String> players = new HashSet<String>();
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("override")
				.requires((cs) -> 
				{
					return cs.hasPermissionLevel(2);
				})
				.executes((cs) ->
				{
					CommandSource source = cs.getSource();
					return executeCommand(source, source.asPlayer());
				}));
	}
	
	private static int executeCommand(CommandSource source, ServerPlayerEntity sender)
	{
		String id = sender.getCachedUniqueIdString();
		if (players.contains(id))
		{
			players.remove(id);
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "Disabled " + TextFormatting.RESET + "Override for ").appendSibling(sender.getDisplayName()), true);
		}
		else
		{
			players.add(id);
			source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Enabled " + TextFormatting.RESET + "Override for ").appendSibling(sender.getDisplayName()), true);
		}
		return 1;
	}
	
	public static boolean hasOverridePermission(PlayerEntity player)
	{
		return players.contains(player.getCachedUniqueIdString());
	}
}
