package com.darksundev.esotericacraft.runes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulTrap extends Rune
{
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		O - - - O
	 * 		- O - O -
	 * 		- - M - -
	 * 		- O - O -
	 * 	    O - - - O
	 */	
	public SoulTrap()
	{
		super("Soul_Trap", new Tier[][]{
			new Tier[]{Tier.ENCHANTED,	Tier.NONE, 		Tier.NONE, 		Tier.NONE,		Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.NONE,		Tier.NONE,		Tier.MUNDANE,	Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.ENCHANTED, 	Tier.NONE,		Tier.NONE,		Tier.NONE,	Tier.ENCHANTED}
		});

	}

	private static List<LivingEntity> getMobs(World world, BlockPos pos)
	{
		AxisAlignedBB searchArea = new AxisAlignedBB(pos).expand(2, 2, 2);
		
		// mobs
		List<LivingEntity> mobs = world.getEntitiesWithinAABB(LivingEntity.class, searchArea);
		for (LivingEntity m : mobs)
			EsotericaCraft.logger.info(m.getDisplayName().getFormattedText());
		// iterate over array backwards
		for (int i = mobs.size(); i-- > 0; )
		{
			// remove any entities that are players
			if (mobs.get(i) instanceof PlayerEntity)
				mobs.remove(i);
		}
		
		return mobs;
	}

	@Override
	public void onCast(PlayerEntity player, World world, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		super.onCast(player, world, pos, pattern, enchantBlocks, mundaneBlocks);
		
		// allow only emerald to be used
		Set<BlockState> enchant = new HashSet<BlockState>();
		Collections.addAll(enchant, enchantBlocks);
		if (enchant.size() != 1 || enchant.iterator().next().getBlock() != Blocks.EMERALD_BLOCK)
			return;
		
		// attempt to find entities above the runet
		List<LivingEntity> entities = getMobs(world, pos);

		// valid entities found
		if (entities.size() > 0)
		{
			// pick first entity and convert them to an egg
			LivingEntity chosenOne = entities.get(0);
			ItemStack item = new ItemStack(getEgg(chosenOne.getType()));
			
			// spawn item in world
			BlockPos p = chosenOne.getPosition();
			world.addEntity(new ItemEntity(world, p.getX(), p.getY() + .5, p.getZ(), item));
			
			// destroy and replace entity with egg
			EsotericaCraft.logger.info("Converting " + chosenOne.getDisplayName().getFormattedText() + " to an egg");
			chosenOne.remove();
		}
	}
	public static SpawnEggItem getEgg(@Nullable EntityType<?> type) {
	      return SpawnEggItem.EGGS.get(type);
	   }
}
