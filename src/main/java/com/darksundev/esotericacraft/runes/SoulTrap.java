package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
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
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulTrap extends Rune
{
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required (specifically Emerald blocks)
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
		
		// attempt to find entities above the rune
		List<LivingEntity> entities = getMobs(world, pos);

		// valid entities found
		if (entities.size() > 0)
		{
			// pick first entity and convert them to an egg
			LivingEntity chosenOne = entities.get(0);

			EntityType<?> type = chosenOne.getType();
			boolean invalid = (type == EntityType.PILLAGER) || (type == EntityType.ILLUSIONER) || (type == EntityType.EVOKER) || (type == EntityType.SHULKER) || (type == EntityType.TRADER_LLAMA) || (type == EntityType.VEX) || (type == EntityType.VINDICATOR) || (type == EntityType.WANDERING_TRADER) || (type == EntityType.WITHER) || (type == EntityType.RAVAGER) || (type == EntityType.ELDER_GUARDIAN) || (type == EntityType.WITHER);
			if (invalid) return;
			
			ItemStack item = new ItemStack(getEgg(chosenOne.getType()));	
			if (item != null || item.getItem() != Items.AIR)
			{
				// spawn item in world
				BlockPos p = chosenOne.getPosition();
				world.addEntity(new ItemEntity(world, p.getX(), p.getY() + .5, p.getZ(), item));
				
				// destroy and replace entity with egg
				EsotericaCraft.logger.info(String.format("Converting %s to an egg", chosenOne.getDisplayName().getFormattedText()));
				chosenOne.remove();
				
				// roll to consume emerald blocks
				rollConsumeBlocks(world, pos);
			}
		}
	}
	
	private static void rollConsumeBlocks(World world, BlockPos pos)
	{
		int count = EsotericaCraft.rng.nextInt(4);
		if (count > 0)
		{
			ArrayList<BlockPos> offerings = getOfferings(pos);
			for (int i=0; i<count; i++)
			{
				BlockPos p = offerings.get(EsotericaCraft.rng.nextInt(offerings.size()));
				world.removeBlock(p, false);
				world.notifyBlockUpdate(p, Blocks.EMERALD_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
				offerings.remove(p);
			}
		}
	}
	private static ArrayList<BlockPos> getOfferings(BlockPos pos)
	{
		ArrayList<BlockPos> offerings = new ArrayList<BlockPos>();
		offerings.add(pos.north().east());
		offerings.add(pos.north().west());
		offerings.add(pos.south().east());
		offerings.add(pos.south().west());
		offerings.add(pos.north().east().north().east());
		offerings.add(pos.north().west().north().west());
		offerings.add(pos.south().east().south().east());
		offerings.add(pos.south().west().south().west());
		return offerings;
	}
	
	public static SpawnEggItem getEgg(@Nullable EntityType<?> type)
	{
			return SpawnEggItem.EGGS.get(type);
	}
}
