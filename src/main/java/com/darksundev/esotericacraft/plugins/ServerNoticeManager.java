package com.darksundev.esotericacraft.plugins;

import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class ServerNoticeManager
{
	private static HashSet<String> notices = new HashSet<String>();
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		for (String n : notices)
		{
			// send notice to player
			EsotericaCraft.messagePlayer(event.getPlayer(), String.format("NOTICE: %s", n));
		}
	}
	
	public static void addNotice(String notice)
	{
		if (!notices.contains(notice))
			notices.add(notice);
	}
	public static HashSet<String> getNotices()
	{
		return notices;
	}
	public static void setNotices(HashSet<String> notices)
	{
		ServerNoticeManager.notices.clear();
		ServerNoticeManager.notices.addAll(notices);
	}
}
