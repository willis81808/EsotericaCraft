package com.darksundev.esotericacraft.plugins;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;

import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class OfferingManager
{
	private static HashSet<Offering> offerings = new HashSet<Offering>();
	private static HashMap<String, Integer> buffer = new HashMap<String, Integer>();
	
	public static void acceptOffering(ItemStack stack)
	{
		String key = stack.getDisplayName().getString();
		
		int value = 0;
		if (buffer.containsKey(key))
			value = buffer.get(key);
		
		buffer.put(key, value + stack.getCount());
	}
	public static void acceptOffering(MobEntity entity)
	{
		String key = entity.getDisplayName().getString();
		
		int value = 0;
		if (buffer.containsKey(key))
			value = buffer.get(key);
		
		buffer.put(key, ++value);
	}
	public static void finalizeOffering(World world, ItemStack book)
	{
		CompoundNBT tag = book.getTag().copy();
		StringBuilder sb = new StringBuilder();

		// pages
		ListNBT pages = tag.getList("pages", NBT.TAG_STRING);
		for (int i=0; i<pages.size(); i++) {
			String p = pages.getString(i);
			sb.append(p.subSequence(9, p.length() - 2));
			if (i < pages.size())
				sb.append(EsotericaCraft.NEW_LINE);
		}
		
		Offering o = new Offering(tag.getString("author"), sb.toString(), buffer);
		offerings.add(o);
		buffer.clear();

		EsotericaCraft.messageAllPlayers(world.getServer().getPlayerList(), "An offering has been made!", TextFormatting.RED, TextFormatting.ITALIC);
		EsotericaWorldSave.backupData();
	}
	public static void clearOfferings()
	{
		offerings.clear();
		EsotericaWorldSave.backupData();
	}
	
	public static HashSet<Offering> getOfferings()
	{
		return offerings;
	}
	public static void restoreOfferings(HashSet<Offering> data)
	{
		offerings = data;
	}
	
	public static class Offering implements Serializable
	{
		private static final long serialVersionUID = -3144706033308745915L;
		public String practitioner;
		public String request;
		public HashMap<String, Integer> sacrifices;
		
		@SuppressWarnings("unchecked")
		public Offering(String practitioner, String request, HashMap<String, Integer> sacrifices)
		{
			this.practitioner = practitioner;
			this.request = request;
			this.sacrifices = (HashMap<String, Integer>) sacrifices.clone();
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			
			sb.append(TextFormatting.YELLOW);
			sb.append("Practitioner: ");
			sb.append(TextFormatting.RESET);
			sb.append(practitioner);
			sb.append(EsotericaCraft.NEW_LINE);

			sb.append(TextFormatting.YELLOW);
			sb.append("Request Text: ");
			sb.append(TextFormatting.RESET);
			sb.append(request);

			sb.append(TextFormatting.YELLOW);
			sb.append("Sacrifices: ");
			sb.append(TextFormatting.RESET);
			sb.append(EsotericaCraft.NEW_LINE);
			for (String offering : sacrifices.keySet())
			{
				sb.append(String.format("%s: %d", offering, sacrifices.get(offering)));
				sb.append(EsotericaCraft.NEW_LINE);
			}
			
			return sb.toString();
		}
	}
}
