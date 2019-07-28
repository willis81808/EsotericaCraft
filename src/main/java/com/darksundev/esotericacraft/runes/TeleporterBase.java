package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.List;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public abstract class TeleporterBase extends Rune
{

	public TeleporterBase(String name, Tier[][] pattern)
	{
		super(name, pattern);
	}
	

	@Override
	public void onCast(ItemUseContext context, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		super.onCast(context, pattern, enchantBlocks, mundaneBlocks);
		
		StringBuilder strBuilder = new StringBuilder();
		for (BlockState markerBlock : enchantBlocks)
		{
			strBuilder.append(markerBlock.getBlock().getTranslationKey());
			strBuilder.append(';');
		}
		String key = strBuilder.toString();
		
		// this is a new key
		if (!RuneList.teleportLinks.containsKey(key))
		{
			TeleportLink link = makeNewLink(key, context.getPos());
			RuneList.teleportLinks.put(key, link);
		}
		// existing key recognized
		else
		{
			// get link with this rune's signature
			TeleportLink link = RuneList.teleportLinks.get(key);
			
			// link hasn't been made yet
			if (getThisSide(link) == -1)
			{
				// link signature to this rune
				setThisSide(link, context.getPos().toLong());

				// if both portals are linked then teleport player
				if (getOtherSide(link) != -1)
				{
					teleport(context, BlockPos.fromLong(getOtherSide(link)), link);
				}
			}
			// this rune has already been linked
			else if (getThisSide(link) == context.getPos().toLong() ||
					getThisSide(link) == context.getPos().up().toLong() ||
					getThisSide(link) == context.getPos().down().toLong())
			{
				// if both sides are linked then teleport player
				if (getOtherSide(link) != -1)
				{
					teleport(context, BlockPos.fromLong(getOtherSide(link)), link);
				}
			}
			// this portal signature has already been linked to another rune!
			else
			{
				// there is already a linked pair of portals with this signature
				if (getOtherSide(link) != -1)
				{
					// send warning message to casting player
					EsotericaCraft.messagePlayer(context.getPlayer(),
							"The Aether resists!",
							TextFormatting.RED
						);
					EsotericaCraft.messagePlayer(context.getPlayer(),
							"This path must already be in use..."
						);
				}
				// this signature has been activated in the past, but has not been linked to a second teleporter yet
				// therefor we overrite the old partial link with a new one
				else
				{
					RuneList.teleportLinks.put(key, makeNewLink(key, context.getPos()));
				}
			}
		}
	}
	
	private static List<Entity> getEntitiesToTeleport(ItemUseContext context, BlockPos root)
	{
		EsotericaCraft.logger.info("Found entities above rune:");
		AxisAlignedBB searchArea = new AxisAlignedBB(root).expand(2, 2, 2);
		
		// items
		List<ItemEntity> items = context.getWorld().getEntitiesWithinAABB(ItemEntity.class, searchArea);
		for (ItemEntity i : items)
			EsotericaCraft.logger.info(i.getDisplayName().getFormattedText());
		
		// mobs
		List<LivingEntity> mobs = context.getWorld().getEntitiesWithinAABB(LivingEntity.class, searchArea);
		for (LivingEntity m : mobs)
			EsotericaCraft.logger.info(m.getDisplayName().getFormattedText());

		// minecarts
		List<AbstractMinecartEntity> minecarts = context.getWorld().getEntitiesWithinAABB(AbstractMinecartEntity.class, searchArea);
		for (AbstractMinecartEntity m : minecarts)
			EsotericaCraft.logger.info(m.getDisplayName().getFormattedText());

		// boats
		List<BoatEntity> boats = context.getWorld().getEntitiesWithinAABB(BoatEntity.class, searchArea);
		for (BoatEntity b : boats)
			EsotericaCraft.logger.info(b.getDisplayName().getFormattedText());
		
		// combine lists into one array
		List<Entity> all = new ArrayList<Entity>(items);
		all.addAll(mobs);
		all.addAll(minecarts);
		all.addAll(boats);
		return all;
	}
	
	private BlockPos getValidTeleportPoint(ItemUseContext context, BlockPos target)
	{
		World w = context.getWorld();
		int attempts = 0;
		boolean valid = false;
		while (!valid)
		{
			target = target.up();
			BlockPos next = target.up();
			BlockState destination1 = w.getBlockState(target);
			BlockState destination2 = w.getBlockState(next);
			valid = destination1.isAir(context.getWorld(), target) && destination2.isAir(context.getWorld(), next);
			
			attempts ++;
			if (attempts > 10)
			{
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"The Aether resists!",
						TextFormatting.RED
					);
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"A safe place could not be found on the other side."
					);
				return null;
			}
		}
		return target;
	}
	
	private void teleport(ItemUseContext context, BlockPos target, TeleportLink link)
	{
		// find valid area on other side of teleport linkage
		target = getValidTeleportPoint(context, target);
		
		
		/*
		World w = context.getWorld();
		int attempts = 0;
		boolean valid = false;
		while (!valid)
		{
			target = target.up();
			BlockPos next = target.up();
			BlockState destination1 = w.getBlockState(target);
			BlockState destination2 = w.getBlockState(next);
			valid = destination1.isAir(context.getWorld(), target) && destination2.isAir(context.getWorld(), next);
			
			attempts ++;
			if (attempts > 10)
			{
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"The Aether resists!",
						TextFormatting.RED
					);
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"A safe place could not be found on the other side."
					);
				return;
			}
		}
		*/
		
		// valid area found- do teleport
		if (context.getPlayer().isSneaking())
		{
			// player is sneaking- teleport entities and not player
			List<Entity> entities = getEntitiesToTeleport(context, context.getPos());
			if (entities.size() == 0)
			{
				entities = getEntitiesToTeleport(context, BlockPos.fromLong(getOtherSide(link)));
				target = getValidTeleportPoint(context, BlockPos.fromLong(getThisSide(link)));
				for (Entity entity : entities)
				{
					entity.setPositionAndUpdate(
							target.getX()+.5,
							target.getY()+.5,
							target.getZ()+.5
						);
				}
			}
			else
			{
				for (Entity entity : entities)
				{
					entity.setPositionAndUpdate(
							target.getX()+.5,
							target.getY()+.5,
							target.getZ()+.5
						);
				}
			}
		}
		else
		{
			// player wasn't sneaking- teleport player
			context.getPlayer().setPositionAndUpdate(
					target.getX()+.5,
					target.getY()+.5,
					target.getZ()+.5
				);
		}
	}

	protected abstract long getThisSide(TeleportLink link);
	protected abstract void setThisSide(TeleportLink link, long value);
	protected abstract long getOtherSide(TeleportLink link);
	protected abstract void setOtherSide(TeleportLink link, long value);
	protected abstract TeleportLink makeNewLink(String key, BlockPos firstLink);
}
