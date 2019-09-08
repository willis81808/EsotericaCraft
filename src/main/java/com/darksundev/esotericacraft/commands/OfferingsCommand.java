package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.plugins.OfferingManager;
import com.darksundev.esotericacraft.plugins.OfferingManager.Offering;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class OfferingsCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("offerings")
				.requires(cs -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.literal("clear").executes(cs -> {
					OfferingManager.clearOfferings();
					cs.getSource().sendFeedback(new StringTextComponent("Offerings Cleared").applyTextStyle(TextFormatting.RED), true);
					return 1;
				})).executes(cs -> {
					return listOfferings(cs.getSource());
				}));
	}
	
	private static int listOfferings(CommandSource source)
	{
		for (Offering offering : OfferingManager.getOfferings())
		{
			source.sendFeedback(new StringTextComponent(offering.toString()), false);
			source.sendFeedback(new StringTextComponent("~ ~ ~ ~ ~ ~ ~ ~ ~ ~").applyTextStyle(TextFormatting.AQUA), false);
		}
		return 1;
	}
}
