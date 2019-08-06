package com.darksundev.esotericacraft.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CloseInventoryCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("closeinventory")
				.requires((cs) -> 
				{
					return cs.hasPermissionLevel(2);
				})
				.then(Commands.argument("target", EntityArgument.player())
						.executes((cs) ->
						{
							CommandSource source = cs.getSource();
							return executeCommand(source, source.asPlayer(), EntityArgument.getPlayer(cs, "target"));
						})));
	}
	
	private static int executeCommand(CommandSource source, ServerPlayerEntity sender, ServerPlayerEntity target)
	{
		// send admin their normal inventory
		OpenInventoryCommand.clearListeners(target);
		OpenInventoryCommand.resetInventory(sender);
		return 1;
	}
}
