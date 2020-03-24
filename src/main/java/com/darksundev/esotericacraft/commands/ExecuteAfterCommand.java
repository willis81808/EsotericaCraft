package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.plugins.TaskManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ExecuteAfterCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("executeafter")
				.requires((cs) -> 
				{
					return cs.hasPermissionLevel(2);
				})
				.then(Commands.argument("delay", IntegerArgumentType.integer())
				.then(Commands.argument("command", StringArgumentType.string()).executes((cs) -> {
					return scheduleCommand(cs.getSource(), IntegerArgumentType.getInteger(cs, "delay"), StringArgumentType.getString(cs, "command"));
				}))));
	}
	
	private static int scheduleCommand(CommandSource source, int delay, String command)
	{
		TaskManager.enqueueTask(delay, () ->
		{
			source.getServer().getCommandManager().handleCommand(source, command);
		});
		return 1;
	}
}
