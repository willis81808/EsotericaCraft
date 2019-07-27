package com.darksundev.esotericacraft.runes;

import java.util.HashSet;
import java.util.Set;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TeleportEraser extends Rune
{
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	O: Enchanted required
	 *  i: Redstone Torches
	 *  *: Redstone wire
	 * 
	 * 		- * - * -
	 * 		* i O i *
	 * 		- O O O -
	 * 		* i O i *
	 * 	    - * - * -
	 */	
	public TeleportEraser()
	{
		super("Teleport_Eraser", new Tier[][]{
			new Tier[]{Tier.NONE,	Tier.NONE, 		Tier.NONE, 		Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.NONE},
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
			if (markerBlock != pattern[2][2])
			{
				strBuilder.append(markerBlock.getBlock().getTranslationKey());
				strBuilder.append(';');
			}
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
			RuneList.teleportLinks.remove(key);
			World w = context.getWorld();
			
			// delete emerald
			w.removeBlock(context.getPos(), false);
			// delete redstone
			w.removeBlock(context.getPos().north().west(), false);
			w.removeBlock(context.getPos().north().east(), false);
			w.removeBlock(context.getPos().south().west(), false);
			w.removeBlock(context.getPos().south().east(), false);
			w.notifyBlockUpdate(context.getPos().north().west(), Blocks.REDSTONE_TORCH.getDefaultState(), Blocks.AIR.getDefaultState(), 1);
			w.notifyBlockUpdate(context.getPos().north().east(), Blocks.REDSTONE_TORCH.getDefaultState(), Blocks.AIR.getDefaultState(), 1);
			w.notifyBlockUpdate(context.getPos().south().west(), Blocks.REDSTONE_TORCH.getDefaultState(), Blocks.AIR.getDefaultState(), 1);
			w.notifyBlockUpdate(context.getPos().south().east(), Blocks.REDSTONE_TORCH.getDefaultState(), Blocks.AIR.getDefaultState(), 1);
			ShowSuccessMessage(context);
		}
	}
	
	private boolean isValid(ItemUseContext context, BlockState[][] pattern)
	{
		// check for emerald in the proper place
		boolean emeraldValid = pattern[2][2].getBlock().getTranslationKey() != "block.minecraft.emerald_block";
		
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
		
		if (!emeraldValid)
		{
			EsotericaCraft.messagePlayer(context.getPlayer(),
					"The Aether resists!",
					TextFormatting.RED
				);
			EsotericaCraft.messagePlayer(context.getPlayer(),
					"You need a more valuable offering for this magic..."
				);
		}
		else if (!torchesValid)
		{
			ShowInvalidRedstoneErrorMessage(context, "block.minecraft.redstone_torch", torchArea);
		}
		else if (!redstoneValid)
		{
			ShowInvalidRedstoneErrorMessage(context, "block.minecraft.redstone_wire", wireArea);
		}
		return  emeraldValid && torchesValid && redstoneValid;
	}

	private void ShowSuccessMessage(ItemUseContext context)
	{
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"The Aether settles...",
				TextFormatting.BLUE
			);
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"Portals of the given signature can now be moved/replaced"
			);
	}
	private void ShowInvalidRedstoneErrorMessage(ItemUseContext context, String expecting, Set<String> wrongArea)
	{
		wrongArea.remove(expecting);
		
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"The Aether resists!",
				TextFormatting.RED
			);
		EsotericaCraft.messagePlayer(context.getPlayer(),
				"Your alignment is off! Where the Aether expected §a[" + expecting + "] §r§e§oinstead it found §r§a§4" + wrongArea.toString()
			);
	}
	private void ShowMissingSignatureErrorMessage(ItemUseContext context)
		{
			EsotericaCraft.messagePlayer(context.getPlayer(),
					"The Aether resists!",
					TextFormatting.RED
				);
			EsotericaCraft.messagePlayer(context.getPlayer(),
					"The there exists no link of that signature to be erased..."
				);
		}
}
