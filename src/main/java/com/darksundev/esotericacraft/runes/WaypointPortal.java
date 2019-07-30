package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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
	public void onCast(PlayerEntity player, World world, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		// player didn't use redstone in corners, not a valid rune.
		// abort cast
		if (!isValid(player, pattern))
			return;
		
		// if we got here the cast was valid, attempt to teleport
		super.onCast(player, world, pos, pattern, enchantBlocks, mundaneBlocks);
		
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
			ShowMissingSignatureErrorMessage(player);
		}
		// existing key recognized
		else
		{
			// get link with this rune's signature
			TeleportLink link = RuneList.teleportLinks.get(key);
			if (link.receiver != -1)
			{
				simpleTeleport(world, player, pos, BlockPos.fromLong(link.receiver));
			}
			else if (link.transmitter != -1)
			{
				simpleTeleport(world, player, pos, BlockPos.fromLong(link.transmitter));
			}
			// something is wrong with this link, and it has neither a transmitter nor receiver...
			else
			{
				ShowMissingSignatureErrorMessage(player);
			}
		}
	}
	private void simpleTeleport(World world, PlayerEntity player, BlockPos from, BlockPos to)
	{
		// find valid area on other side of teleport linkage
		int attempts = 0;
		boolean valid = false;
		while (!valid)
		{
			to = to.up();
			BlockPos next = to.up();
			BlockState destination1 = world.getBlockState(to);
			BlockState destination2 = world.getBlockState(next);
			valid = destination1.isAir(world, to) && destination2.isAir(world, next);
			
			attempts ++;
			if (attempts > 10)
			{
				if (player != null)
				{
					EsotericaCraft.messagePlayer(player,
							"The Aether resists!",
							TextFormatting.RED
						);
					EsotericaCraft.messagePlayer(player,
							"A safe place could not be found on the other side."
						);
				}
				return;
			}
		}
		
		// valid area found- do teleport
		if (player == null || player.isSneaking())
		{
			// player is sneaking- teleport entities and not player
			List<Entity> entities = getEntitiesToTeleport(world, from);
			if (entities.size() == 0)
			{
				entities = getEntitiesToTeleport(world, to);
			}
			
			for (Entity entity : entities)
			{
				entity.setPositionAndUpdate(
						to.getX()+.5,
						to.getY()+.5,
						to.getZ()+.5
					);
			}
		}
		else
		{
			// player wasn't sneaking- teleport player
			player.setPositionAndUpdate(
					to.getX()+.5,
					to.getY()+.5,
					to.getZ()+.5
				);
		}
	}

	private static List<Entity> getEntitiesToTeleport(World world, BlockPos root)
	{
		EsotericaCraft.logger.info("Found entities above rune:");
		AxisAlignedBB searchArea = new AxisAlignedBB(root).expand(2, 2, 2);
		
		// items
		List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, searchArea);
		for (ItemEntity i : items)
			EsotericaCraft.logger.info(i.getDisplayName().getFormattedText());
		
		// mobs
		List<LivingEntity> mobs = world.getEntitiesWithinAABB(LivingEntity.class, searchArea);
		for (LivingEntity m : mobs)
			EsotericaCraft.logger.info(m.getDisplayName().getFormattedText());

		// minecarts
		List<AbstractMinecartEntity> minecarts = world.getEntitiesWithinAABB(AbstractMinecartEntity.class, searchArea);
		for (AbstractMinecartEntity m : minecarts)
			EsotericaCraft.logger.info(m.getDisplayName().getFormattedText());

		// boats
		List<BoatEntity> boats = world.getEntitiesWithinAABB(BoatEntity.class, searchArea);
		for (BoatEntity b : boats)
			EsotericaCraft.logger.info(b.getDisplayName().getFormattedText());
		
		// combine lists into one array
		List<Entity> all = new ArrayList<Entity>(items);
		all.addAll(mobs);
		all.addAll(minecarts);
		all.addAll(boats);
		return all;
	}
	
	private boolean isValid(PlayerEntity player, BlockState[][] pattern)
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
		
		if (player != null)
		{
			if (!torchesValid)
			{
				ShowInvalidRedstoneErrorMessage(player, "block.minecraft.redstone_torch", torchArea);
			}
			else if (!redstoneValid)
			{
				ShowInvalidRedstoneErrorMessage(player, "block.minecraft.redstone_wire", wireArea);
			}
		}
		
		return  torchesValid && redstoneValid;
	}

	private void ShowInvalidRedstoneErrorMessage(PlayerEntity player, String expecting, Set<String> wrongArea)
	{
		if (player == null)
			return;
		
		wrongArea.remove(expecting);
		
		EsotericaCraft.messagePlayer(player,
				"The Aether resists!",
				TextFormatting.RED
			);
		EsotericaCraft.messagePlayer(player,
				"Your alignment is off! Where the Aether expected §a§l[" + expecting + "] §r§e§oinstead it found §r§a§c" + wrongArea.toString()
			);
	}
	private void ShowMissingSignatureErrorMessage(PlayerEntity player)
	{
		if (player == null)
			return;
		
		EsotericaCraft.messagePlayer(player,
				"The Aether resists!",
				TextFormatting.RED
			);
		EsotericaCraft.messagePlayer(player,
				"The waypoint failed to detect a linkage with this signature."
			);
	}
}
