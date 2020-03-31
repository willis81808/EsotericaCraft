package com.darksundev.esotericacraft.commands;

import java.util.HashSet;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;

public class VanishCommand
{
	private static HashSet<ServerPlayerEntity> invisiblePlayers = new HashSet<ServerPlayerEntity>();
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("vanish")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).executes((cs) -> {
					CommandSource source = cs.getSource();
					return executeCommand(source, source.asPlayer());
				}));
	}
	
	private static int executeCommand(CommandSource source, ServerPlayerEntity player)
	{
		if (invisiblePlayers.contains(player))
		{
			ChunkManager manager = ((ServerWorld)player.world).getChunkProvider().chunkManager;
			manager.track(player);
			for(ChunkManager.EntityTracker tracker : manager.entities.values())
			{
				tracker.updateTrackingState(player);
			}
			invisiblePlayers.remove(player);
			source.sendFeedback(new StringTextComponent("You are now visible!").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), true);
		}
		else
		{
			invisiblePlayers.add(player);
			
			ChunkManager.EntityTracker tracker = player.getServerWorld().getChunkProvider().chunkManager.entities.remove(player.getEntityId());
			if (tracker != null) {
				tracker.removeAllTrackers();
			}
			
			source.sendFeedback(new StringTextComponent("You are now vanished!").applyTextStyles(TextFormatting.GREEN, TextFormatting.ITALIC), true);
		}
		return 1;
	}
}
