package com.darksundev.esotericacraft.commands;

import java.util.HashMap;
import java.util.List;

import com.darksundev.esotericacraft.Utils;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;

public class InventoryCommand
{
	private static HashMap<String, IContainerListener> listeners = new HashMap<String, IContainerListener>();
	private static HashMap<String, List<ItemStack>> inventories = new HashMap<String, List<ItemStack>>();
	private static HashMap<ServerPlayerEntity, ServerPlayerEntity> viewMapping = new HashMap<ServerPlayerEntity, ServerPlayerEntity>();
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("invsee")
				.requires((cs) -> {
					return cs.hasPermissionLevel(2);
				}).then(Commands.argument("target", EntityArgument.player()).executes((cs) -> {
					return openInventory(cs.getSource(), cs.getSource().asPlayer(), EntityArgument.getPlayer(cs, "target"));
				})).executes((cs) -> {
					return closeInventory(cs.getSource(), cs.getSource().asPlayer());
				}));
	}
	
	private static int closeInventory(CommandSource source, ServerPlayerEntity sender)
	{
		// send admin their normal inventory
		ServerPlayerEntity target = viewMapping.get(sender);
		viewMapping.remove(sender);
		clearListeners(sender);
		clearListeners(target);
		resetInventory(sender);
		source.sendFeedback(new StringTextComponent("Closed ").appendSibling(target.getDisplayName()).appendText("'s inventory"), true);
		return 1;
	}
	private static int openInventory(CommandSource source, ServerPlayerEntity sender, ServerPlayerEntity target)
	{
		if (listeners.containsKey(target.getCachedUniqueIdString()) || inventories.containsKey(sender.getCachedUniqueIdString()))
		{
			if (sender.getCachedUniqueIdString() != target.getCachedUniqueIdString()) {
				if (!viewMapping.containsKey(sender) && viewMapping.get(sender).getCachedUniqueIdString() == target.getCachedUniqueIdString()) {
					return closeInventory(source, sender);
				}
				else {
					closeInventory(source, sender);
					return openInventory(source, sender, target);
				}
			}
			source.sendFeedback(Utils.textComponentFromString("You cannot view your own inventory..."), true);
			return 1;
		}
		else if (sender.getCachedUniqueIdString() == target.getCachedUniqueIdString()) {
			source.sendFeedback(Utils.textComponentFromString("You cannot view your own inventory..."), true);
			return 1;
		}
		
		viewMapping.put(sender, target);
		
		// make sender's inventory look like target's
		inventories.put(sender.getCachedUniqueIdString(), sender.container.getInventory());
		setInventory(sender, target.container.getInventory());

		// listen for changes to target's inventory & keep admin's view up to date
		IContainerListener targetListener = new IContainerListener() {
			@Override
			public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) { }
			@Override
			public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) { }
			@Override
			public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
				setInventory(sender, containerToSend.getInventory());
			}
		};
		target.container.addListener(targetListener);
		listeners.put(target.getCachedUniqueIdString(), targetListener);
		
		// listen to changes in admin's inventory and update client
		IContainerListener adminListener = new IContainerListener() {
			@Override
			public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) { }
			@Override
			public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) { }
			@Override
			public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
				setInventory(target, containerToSend.getInventory());
			}
		};
		sender.container.addListener(adminListener);
		listeners.put(sender.getCachedUniqueIdString(), adminListener);
		
		source.sendFeedback(new StringTextComponent("Opened ").appendSibling(target.getDisplayName()).appendText("'s inventory"), true);
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
	
	private static void setInventory(ServerPlayerEntity player, List<ItemStack> items)
	{
		player.container.clear();
		player.openContainer.detectAndSendChanges();
		for(int i = 0; i < items.size(); ++i)
		{
			player.container.getSlot(i).putStack(items.get(i));
		}
	}
	public static void resetInventory(ServerPlayerEntity sender)
	{
		String UID = sender.getCachedUniqueIdString();
		List<ItemStack> inv = inventories.get(UID);
		setInventory(sender, inv);
		inventories.remove(UID);
	}

}
