package com.darksundev.esotericacraft.commands;

import java.util.ArrayList;

import com.darksundev.esotericacraft.plugins.ServerNoticeManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class NoticeCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("notice")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.literal("add").then(Commands.argument("new_notice", StringArgumentType.string()).executes((cs) -> {
					String notice = StringArgumentType.getString(cs, "new_notice");
					return addNotice(cs.getSource(), notice);
				}))).then(Commands.literal("remove").then(Commands.argument("index", IntegerArgumentType.integer()).executes((cs) -> {
					int index = IntegerArgumentType.getInteger(cs, "index");
					return removeNotice(cs.getSource(), index);
				}))).then(Commands.literal("list").executes((cs) -> {
					return listNotices(cs.getSource());
				})));
	}
	
	private static int addNotice(CommandSource source, String notice)
	{
		ArrayList<String> notices = ServerNoticeManager.getNotices();
		// don't accept empty notices
		if (notice != null && notice.length() > 0)
		{
			// add notice if we don't already contain it
			if (!notices.contains(notice))
				notices.add(notice);
		}
		ServerNoticeManager.setNotices(notices);
		source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Notice added!"), true);
		return 1;
	}
	private static int removeNotice(CommandSource source, int index)
	{
		ArrayList<String> notices = ServerNoticeManager.getNotices();
		if (index < notices.size())
		{
			notices.remove(index);
			source.sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Notice Removed!"), true);
		}
		else
		{
			source.sendFeedback(new StringTextComponent(TextFormatting.RED + "Invalid index"), true);
		}
		ServerNoticeManager.setNotices(notices);
		return 1;
	}
	private static int listNotices(CommandSource source)
	{
		ArrayList<String> notices = ServerNoticeManager.getNotices();
		StringTextComponent component = new StringTextComponent("");
		for (int i = 0; i < notices.size(); i++)
		{
			component.appendText(TextFormatting.GRAY + (i + ") ") + TextFormatting.WHITE + notices.get(i));
			if (i < notices.size() - 1)
				component.appendText("\n");
		}
		source.sendFeedback(component, true);
		ServerNoticeManager.setNotices(notices);
		
		return 1;
	}
	
}
