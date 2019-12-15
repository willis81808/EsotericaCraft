package com.darksundev.esotericacraft.plugins;

import java.util.ArrayList;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class TaskManager
{
	private static ArrayList<Task> tasks = new ArrayList<Task>();
	
	public static interface TaskAction
	{
		public void execute();
	}
	public static class Task
	{
		public int waitTicks;
		public TaskAction action;
		
		public Task(int waitTicks, TaskAction action)
		{
			this.waitTicks = waitTicks;
			this.action = action;
		}
	}
	
	public static void enqueueTask(int ticks, TaskAction action)
	{
		tasks.add(new Task(ticks, action));
	}


	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event)
	{
		for (int i = tasks.size()-1; i >= 0; i--)
		{
			Task t = tasks.get(i);
			if (t.waitTicks-- == 0)
			{
				t.action.execute();
				tasks.remove(i);
			}
		}
	}
}