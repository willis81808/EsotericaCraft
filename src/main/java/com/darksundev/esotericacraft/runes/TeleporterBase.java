package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
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
					teleport(context, BlockPos.fromLong(getOtherSide(link)));
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
					teleport(context, BlockPos.fromLong(getOtherSide(link)));
				}
			}
			// this portal signature has already been linked to another rune!
			else
			{
				// send warning message to casting player
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"The Aether resists!",
						TextFormatting.RED
						);
				EsotericaCraft.messagePlayer(context.getPlayer(),
						"This path must already be in use..."
						);
				return;
			}
		}
	}
	
	private void teleport(ItemUseContext context, BlockPos target)
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
				return;
			}
		}
		
		context.getPlayer().setPositionAndUpdate(
				target.getX()+.5,
				target.getY()+.5,
				target.getZ()+.5
				);
	}

	protected abstract long getThisSide(TeleportLink link);
	protected abstract void setThisSide(TeleportLink link, long value);
	protected abstract long getOtherSide(TeleportLink link);
	protected abstract void setOtherSide(TeleportLink link, long value);
	protected abstract TeleportLink makeNewLink(String key, BlockPos firstLink);
}
