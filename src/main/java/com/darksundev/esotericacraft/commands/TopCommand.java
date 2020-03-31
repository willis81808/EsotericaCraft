package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.runes.TeleporterBase;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TopCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("top")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).executes((cs) -> {
					CommandSource source = cs.getSource();
					return executeCommand(source, source.asPlayer());
				}));
	}
	private static int executeCommand(CommandSource source, ServerPlayerEntity player)
	{
		searching:
		for (int i = 256; i > 0; i--) {
			BlockPos lookingAt = new BlockPos(player.posX, i, player.posZ);
			BlockState state = player.world.getBlockState(lookingAt);
			if (!player.world.isAirBlock(lookingAt))
			{
				if (player.world.isAirBlock(lookingAt.up()))
				{
					TeleporterBase.preloadChunk(lookingAt.up(), player.world, player);
					player.setPositionAndUpdate(lookingAt.getX(), i + 1, lookingAt.getZ());
				}
				break searching;
			}
		}
		return 1;
	}
}
