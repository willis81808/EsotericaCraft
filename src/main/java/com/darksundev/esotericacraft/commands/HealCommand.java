package com.darksundev.esotericacraft.commands;

import java.util.Collection;
import java.util.HashSet;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class HealCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("heal")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.argument("target", EntityArgument.entities()).executes((cs) -> {
					CommandSource source = cs.getSource();
					return executeCommand(source, EntityArgument.getPlayers(cs, "target"));
				})).executes((cs) -> {
					CommandSource source = cs.getSource();
					HashSet<ServerPlayerEntity> targets = new HashSet<ServerPlayerEntity>();
					targets.add(source.asPlayer());
					return executeCommand(source, targets);
				}));
	}
	private static int executeCommand(CommandSource source, Collection<ServerPlayerEntity> targets)
	{
		targets.forEach(p -> {
			p.setHealth(p.getMaxHealth());
			source.sendFeedback(new StringTextComponent("Healed ").appendSibling(p.getDisplayName()), true);
		});
		return 1;
	}
}
