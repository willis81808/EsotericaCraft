package com.darksundev.esotericacraft.plugins;

import java.util.ArrayList;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class ServerNoticeManager
{
	private static ArrayList<String> notices = new ArrayList<String>();
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		for (String n : notices)
		{
			// send notice to player
			EsotericaCraft.messagePlayer(event.getPlayer(), TextFormatting.RESET + (TextFormatting.GRAY + "NOTICE: ") + TextFormatting.YELLOW + TextFormatting.ITALIC + n + "\n");
		}
	}
	
	public static ArrayList<String> getNotices()
	{
		return notices;
	}
	public static void setNotices(ArrayList<String> notices)
	{
		if (notices != ServerNoticeManager.notices)
		{
			ServerNoticeManager.notices.clear();
			ServerNoticeManager.notices.addAll(notices);
		}
	}
}
