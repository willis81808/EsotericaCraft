package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.plugins.PlayerLoginManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class LoginCommand
{

	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("login")
				.then(Commands.literal("help").executes((cs) -> {
					return help(cs.getSource(), cs.getSource().asPlayer());
				})).then(Commands.argument("password", StringArgumentType.string()).executes((cs) -> {
					return attemptLogin(cs.getSource(), cs.getSource().asPlayer(), StringArgumentType.getString(cs, "password"));
				})).then(Commands.literal("change").then(
						Commands.argument("old_password", StringArgumentType.string()).then(
						Commands.argument("new_password", StringArgumentType.string()).executes((cs) -> {
							return changePassword(cs.getSource(), cs.getSource().asPlayer(), StringArgumentType.getString(cs, "old_password"), StringArgumentType.getString(cs, "new_password"));
				})))));
	}
	
	private static int help(CommandSource source, PlayerEntity player)
	{
		EsotericaCraft.messagePlayer(player,
				"\nType '/login <password>' to login\n"
				+ "Type '/login change <old password> <new password>' to change your password");
		return 1;
	}
	
	private static int attemptLogin(CommandSource source, PlayerEntity player, String password)
	{
		if (PlayerLoginManager.authenticateUser(player, password))
		{
			source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Player authenticated"), true);
		}
		else
		{
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "Failed to authenticate user"), true);
		}
		return 1;
	}
	public static int changePassword(CommandSource source, PlayerEntity player, String oldPassword, String newPassword)
	{
		if (PlayerLoginManager.authenticateUser(player, oldPassword))
		{
			// old password is correct, now change it to new password
			PlayerLoginManager.getProfile(player).setPassword(newPassword);
			source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Password successfully updated"), true);
		}
		else
		{
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "Failed to verify old password! No changes made"), true);
		}
		return 1;
	}
}
