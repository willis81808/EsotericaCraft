package com.darksundev.esotericacraft.runes;

import java.util.HashSet;
import java.util.Set;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class WaypointPortal extends Rune
{
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 *  i: Redstone Torches
	 *  *: Redstone wire
	 * 
	 * 		- * - * -
	 * 		* i O i *
	 * 		- O M O -
	 * 		* i O i *
	 * 	    - * - * -
	 */	
	public WaypointPortal()
	{
		super("Waypoint_Portal", new Tier[][]{
			new Tier[]{Tier.NONE,	Tier.NONE, 		Tier.NONE, 		Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE, 	Tier.NONE,		Tier.NONE,		Tier.NONE,		Tier.NONE}
		});
	}

	@Override
	public void onCast(ItemUseContext context, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		// player didn't use redstone in corners, not a valid rune.
		// abort cast
		if (!isValid(context, pattern))
			return;
		
		// if we got here the cast was valid, attempt to teleport
		super.onCast(context, pattern, enchantBlocks, mundaneBlocks);
		
		StringBuilder strBuilder = new StringBuilder();
		for (BlockState markerBlock : enchantBlocks)
		{
			strBuilder.append(markerBlock.getBlock().getTranslationKey());
			strBuilder.append(';');
		}
		String key = strBuilder.toString();
		
		// this key has no corrosponding link
		if (!RuneList.teleportLinks.containsKey(key))
		{
			ShowMissingSignatureErrorMessage(context);
		}
		// existing key recognized
		else
		{
			// get link with this rune's signature
			TeleportLink link = RuneList.teleportLinks.get(key);
			if (link.receiver != -1)
			{
				TeleporterBase.teleport(context, BlockPos.fromLong(link.receiver));
			}
			else if (link.transmitter != -1)
			{
				TeleporterBase.teleport(context, BlockPos.fromLong(link.transmitter));
			}
			// something is wrong with this link, and it has neither a transmitter nor receiver...
			else
			{
				ShowMissingSignatureErrorMessage(context);
			}
		}
	}
	
	private boolean isValid(ItemUseContext context, BlockState[][] pattern)
	{
		// get (what should be) all redstone torches
		Set<String> torchCondition = new HashSet<String>();
		Set<String> torchArea = new HashSet<String>();
		torchArea.add(pattern[1][1].getBlock().getTranslationKey());
		torchArea.add(pattern[1][3].getBlock().getTranslationKey());
		torchArea.add(pattern[3][1].getBlock().getTranslationKey());
		torchArea.add(pattern[3][3].getBlock().getTranslationKey());
		torchCondition.add("block.minecraft.redstone_torch");
		boolean torchesValid = torchArea.equals(torchCondition);

		// get (what should be) all redstone wire
		Set<String> wireCondition = new HashSet<String>();
		Set<String> wireArea = new HashSet<String>();
		wireArea.add(pattern[1][0].getBlock().getTranslationKey());
		wireArea.add(pattern[0][1].getBlock().getTranslationKey());
		wireArea.add(pattern[3][0].getBlock().getTranslationKey());
		wireArea.add(pattern[0][3].getBlock().getTranslationKey());
		wireArea.add(pattern[1][4].getBlock().getTranslationKey());
		wireArea.add(pattern[4][1].getBlock().getTranslationKey());
		wireArea.add(pattern[4][3].getBlock().getTranslationKey());
		wireArea.add(pattern[3][4].getBlock().getTranslationKey());
		wireCondition.add("block.minecraft.redstone_wire");
		boolean redstoneValid = wireArea.equals(wireCondition);
		
		if (!torchesValid)
		{
			ShowInvalidRedstoneErrorMessage(context, "block.minecraft.redstone_torch", torchArea);
		}
		else if (!redstoneValid)
		{
			ShowInvalidRedstoneErrorMessage(context, "block.minecraft.redstone_wire", wireArea);
		}
		return  torchesValid && redstoneValid;
	}

	private void ShowInvalidRedstoneErrorMessage(ItemUseContext context, String expecting, Set<String> wrongArea)
	{
		wrongArea.remove(expecting);
		
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"The Aether resists!",
				TextFormatting.RED
			);
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"Your alignment is off! Where the Aether expected §a§l[" + expecting + "] §r§e§oinstead it found §r§a§c" + wrongArea.toString()
			);
	}
	private void ShowMissingSignatureErrorMessage(ItemUseContext context)
	{
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"The Aether resists!",
				TextFormatting.RED
			);
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"The waypoint failed to detect a linkage with this signature."
			);
	}
}
