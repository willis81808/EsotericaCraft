package com.darksundev.esotericacraft.commands;

import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.Type;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
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
			invisiblePlayers.remove(player);
			source.sendFeedback(new StringTextComponent("You are now visible!").applyTextStyles(TextFormatting.RED, TextFormatting.ITALIC), true);
		}
		else
		{
			for (ServerPlayerEntity p : source.getServer().getPlayerList().getPlayers())
			{
				p.removeTrackingPlayer(player);
			}
			invisiblePlayers.add(player);
			source.sendFeedback(new StringTextComponent("You are now vanished!").applyTextStyles(TextFormatting.GREEN, TextFormatting.ITALIC), true);
		}
		return 1;
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		if (event.type != Type.SERVER)
			return;
		
		EsotericaCraft.logger.info(event.player.getName().toString());
		for (ServerPlayerEntity p : invisiblePlayers)
		{
			event.player.removeTrackingPlayer(p);
		}
	}
}
