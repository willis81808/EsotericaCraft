package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SmiteCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("smite")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes((cs) -> {
					return smitePlayer(cs.getSource(), EntityArgument.getPlayer(cs, "target"), IntegerArgumentType.getInteger(cs, "count"));
				}))));
	}

	private static int smitePlayer(CommandSource source, ServerPlayerEntity target, int count)
	{
		// deal damage to player
		target.setHealth(target.getMaxHealth() - count);
		EsotericaCraft.messageAllPlayers(
				target.server.getPlayerList(),
				target.getDisplayName().appendSibling(new StringTextComponent(" has invoked the wrath of the Gods!").applyTextStyle(TextFormatting.RED)));
		
		// strike with lightning
		BlockPos pos = target.getPosition();
        LightningBoltEntity lightningboltentity = new LightningBoltEntity(target.world, pos.getX(), pos.getY(), pos.getZ(), false);
        target.world.getServer().getWorld(target.world.getDimension().getType()).addLightningBolt(lightningboltentity);
		
		return 1;
	}
}
