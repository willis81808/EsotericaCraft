package com.darksundev.esotericacraft.commands;

import java.util.HashMap;
import java.util.List;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.Utils;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.PacketDistributor;

public class OpenInventoryCommand
{
	private static HashMap<String, IContainerListener> listeners = new HashMap<String, IContainerListener>();
	private static HashMap<String, List<ItemStack>> inventories = new HashMap<String, List<ItemStack>>();
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("openinventory")
				.requires((cs) -> 
				{
					return cs.hasPermissionLevel(2);
				})
				.then(Commands.argument("target", EntityArgument.player())
						.executes((cs) ->
						{
							CommandSource source = cs.getSource();
							return executeCommand(source, source.asPlayer(), EntityArgument.getPlayer(cs, "target"));
						})));
	}
	
	private static int executeCommand(CommandSource source, ServerPlayerEntity sender, ServerPlayerEntity target)
	{
		if (listeners.containsKey(target.getCachedUniqueIdString()) || inventories.containsKey(sender.getCachedUniqueIdString()))
		{
			source.sendFeedback(Utils.textComponentFromString("That player's inventory is already being monitored..."), true);
			return 1;
		}
		
		// make sender's inventory look like target's
		inventories.put(sender.getCachedUniqueIdString(), sender.container.getInventory());
		sender.sendContainerToPlayer(target.container);

		// listen for changes to target's inventory & keep admin's view up to date
		IContainerListener listener = new IContainerListener() {
			@Override
			public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {}
			@Override
			public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {}
			@Override
			public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
			{
				sender.sendSlotContents(target.openContainer, slotInd, stack);
			}
		};
		target.container.addListener(listener);
		listeners.put(target.getCachedUniqueIdString(), listener);
		return 1;
	}
	
	public static IContainerListener getListener(ServerPlayerEntity target)
	{
		if (!listeners.containsKey(target.getCachedUniqueIdString()))
			return null;
		else
			return listeners.get(target.getCachedUniqueIdString());
	}
	public static void clearListeners(ServerPlayerEntity target)
	{
		IContainerListener l = getListener(target);
		if (l != null)
		{
			target.container.listeners.remove(l);
			listeners.remove(target.getCachedUniqueIdString());
		}
	}
	public static void resetInventory(ServerPlayerEntity sender)
	{
		String UID = sender.getCachedUniqueIdString();
		List<ItemStack> inv = inventories.get(UID);
		EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> { return sender; }), new PlayerInventoryMessagePacket(inv));
		inventories.remove(UID);
	}
}
