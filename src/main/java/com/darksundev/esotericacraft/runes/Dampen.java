package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.commands.ModOverrideCommand;
import com.darksundev.esotericacraft.entities.MiningFatigueObserver;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class Dampen extends Rune
{
	private static final String NBT_DATA_TAG = "dampen_rune_owner";
	
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	O: Enchanted required  (specifically Emerald or Diamond blocks)
	 *  X: Mundane required
	 * 
	 * 		- O O O -
	 * 		O - X - O
	 * 		O X X X O
	 * 		O - X - O
	 * 	    - O O O -
	 */	
	public Dampen()
	{
		super("dampen", new Tier[][]{
			new Tier[]{Tier.NONE,		Tier.ENCHANTED, Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.NONE, 		Tier.MUNDANE,	Tier.NONE,		Tier.ENCHANTED},
			new Tier[]{Tier.ENCHANTED,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.ENCHANTED},
			new Tier[]{Tier.ENCHANTED,	Tier.NONE, 		Tier.MUNDANE,	Tier.NONE,		Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED, Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.NONE}
		});
	}

	@Override
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (!super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks))
			return false;

		// ensure valid cast
		if (!isValidCast(player, worldIn, enchantBlocks, mundaneBlocks))
			return false;

		// toggle effect
		AxisAlignedBB searchArea = new AxisAlignedBB(pos);
		List<MiningFatigueObserver> observers = worldIn.getEntitiesWithinAABB(MiningFatigueObserver.class, searchArea);
		if (observers.size() > 0)
		{
			// remove existing rune observer
			if (isLinkedTo(player, observers.get(0).getPosition()))
			{
				removeLink(player, observers.get(0).getPosition());	//remove ownership data from staff
				observers.get(0).remove();

				EsotericaCraft.messagePlayer(player, "Rune Disabled");
			}
			else
			{
				EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
				EsotericaCraft.messagePlayer(player, "The rune has another master...");
				return false;
			}
		}
		else
		{
			// make new rune observer
			MiningFatigueObserver observer = new MiningFatigueObserver(worldIn, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
			worldIn.addEntity(observer);
			
			// log rune in new owner's staff
			linkOwner(player, observer.getPosition());
			
			EsotericaCraft.messagePlayer(player, "Rune Enabled");
		}
		
		return true;
	}
	
	private boolean isValidCast(PlayerEntity player, World worldIn, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		// enforce server-side only
		if (worldIn.isRemote || enchantBlocks == null || mundaneBlocks == null)
			return false;
		
		// prepare rune components
		Set<BlockState> enchant = new HashSet<BlockState>();
		Collections.addAll(enchant, enchantBlocks);
		Set<BlockState> mundane = new HashSet<BlockState>();
		Collections.addAll(mundane, mundaneBlocks);
		
		// enforce only one type of enchanted and mundane block at a time
		boolean isSingleType = enchant.size() == 1 && mundane.size() == 1;
		if (!isSingleType)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "Your rune is impure!");
			return false;
		}
		// enforce only [ diamond, emerald ] as enchanted block options
		boolean isValidEnchantedBlock = enchantBlocks[0].getBlock() == Blocks.DIAMOND_BLOCK || enchantBlocks[0].getBlock() == Blocks.EMERALD_BLOCK;
		if (!isValidEnchantedBlock)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "Your offering is insufficient!");
			return false;
		}

		return true;
	}
	
	private void removeLink(PlayerEntity player, BlockPos runePos)
	{
		ItemStack staff = player.getHeldItemMainhand();
		if (staff.getItem() != ItemList.runing_staff)
		{
			staff = player.getHeldItemOffhand();
		}
		if (staff.getItem() == ItemList.runing_staff)
		{
			// get staff data
			CompoundNBT data = staff.getTag();
			if (data == null)
				return;
			
			// get staff's list of ownerships
			long[] ownerships = data.getLongArray(NBT_DATA_TAG);
			
			// construct new list
			long pos = runePos.toLong();
			List<Long> newData = new ArrayList<Long>();
			if (ownerships != null && ownerships.length > 0)
			{
				// filter only ownerships other than this into the new list
				for (long o : ownerships)
				{
					if (o != pos)
						newData.add(o);
				}
				if (newData.size() == 0)
				{
					data.remove(NBT_DATA_TAG);
				}
				else
				{
					data.putLongArray(NBT_DATA_TAG, newData);
				}
			}
		}
	}
	private void linkOwner(PlayerEntity player, BlockPos runePos)
	{
		ItemStack staff = player.getHeldItemMainhand();
		if (staff.getItem() != ItemList.runing_staff)
		{
			staff = player.getHeldItemOffhand();
		}
		if (staff.getItem() == ItemList.runing_staff)
		{
			CompoundNBT data = staff.getTag();
			if (data == null)
				data = new CompoundNBT();
			long[] ownerships = data.getLongArray(NBT_DATA_TAG);
			if (ownerships != null && ownerships.length > 0)
			{
				long[] buffer = new long[ownerships.length+1];
				System.arraycopy(ownerships, 0, buffer, 0, ownerships.length);
				ownerships = buffer;
			}
			else
			{
				ownerships = new long[1];
			}
			ownerships[ownerships.length-1] = runePos.toLong();
			data.putLongArray(NBT_DATA_TAG, ownerships);
			staff.setTag(data);
		}
	}
	private boolean isLinkedTo(PlayerEntity player, BlockPos runePos)
	{
		// OP override
		if (ModOverrideCommand.hasOverridePermission(player))
			return true;
		
		ItemStack staff = player.getHeldItemMainhand();
		if (staff.getItem() != ItemList.runing_staff)
		{
			staff = player.getHeldItemOffhand();
		}
		if (staff.getItem() == ItemList.runing_staff)
		{
			// attempt to find ownership data on staff
			CompoundNBT data = staff.getTag();
			if (data == null)
				return false;
			long[] positions = data.getLongArray(NBT_DATA_TAG);
			if (positions != null && positions.length > 0)
			{
				// ownership data found
				for (long l : positions)
				{
					BlockPos ownerPos = BlockPos.fromLong(l);
					if (runePos.equals(ownerPos))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
