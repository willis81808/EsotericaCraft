package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.List;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaWorldSave;
import com.darksundev.esotericacraft.lists.BlockList;
import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.runes.TeleportLinkAdapter.TeleporterSide;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.DimensionManager;

public abstract class TeleporterBase extends Rune
{

	public TeleporterBase(String name, Tier[][] pattern)
	{
		super(name, pattern);
	}
	

	@Override
	public void onCast(PlayerEntity player, World world, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		super.onCast(player, world, pos, pattern, enchantBlocks, mundaneBlocks);
		
		// prevent teleporter from being activated by hand if it is using an Auto Rune Caster
		if (player != null && pattern[2][2].getBlock() == BlockList.auto_rune_caster_block)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "This rune cannot be activated by hand.");
			return;
		}
		
		StringBuilder strBuilder = new StringBuilder();
		for (BlockState markerBlock : enchantBlocks)
		{
			strBuilder.append(markerBlock.getBlock().getTranslationKey());
			strBuilder.append(';');
		}
		String key = strBuilder.toString();
		
		// this is a new key
		if (!RuneList.teleportLinksBuffer.containsKey(key))
		{
			TeleportLinkAdapter link = makeNewLink(key, pos, world.dimension.getType());
			RuneList.teleportLinksBuffer.put(key, link);
		}
		// existing key recognized
		else
		{
			// get link with this rune's signature
			TeleportLinkAdapter link = RuneList.teleportLinksBuffer.get(key);
			
			// link hasn't been made yet
			if (getThisSide(link).position == -1)
			{
				// link signature to this rune
				setThisSide(link, new TeleporterSide(pos.toLong(), world.dimension.getType().getId()));

				// if both portals are linked then teleport player
				if (getOtherSide(link).position != -1)
				{
					DimensionType dimension = DimensionType.getById(getOtherSide(link).dimension);
					if (player != null && player.dimension != dimension)
					{
						player.changeDimension(dimension);
						teleport(DimensionManager.getWorld(world.getServer(), dimension, true, true), player, pos, BlockPos.fromLong(getOtherSide(link).position), link);
					}
					else
					{
						teleport(world, player, pos, BlockPos.fromLong(getOtherSide(link).position), link);
					}
				}
			}
			// this rune has already been linked
			else if (getThisSide(link).position == pos.toLong() ||
					getThisSide(link).position == pos.up().toLong() ||
					getThisSide(link).position == pos.down().toLong())
			{
				// if both sides are linked then teleport player
				if (getOtherSide(link).position != -1)
				{
					DimensionType dimension = DimensionType.getById(getOtherSide(link).dimension);
					if (player != null && player.dimension != dimension)
					{
						//player.changeDimension(dimension);
						teleport(DimensionManager.getWorld(world.getServer(), dimension, true, true), player, pos, BlockPos.fromLong(getOtherSide(link).position), link);
					}
					else
					{
						teleport(world, player, pos, BlockPos.fromLong(getOtherSide(link).position), link);
					}
				}
			}
			// this portal signature has already been linked to another rune!
			else
			{
				// there is already a linked pair of portals with this signature
				if (getOtherSide(link).position != -1)
				{
					if (player != null)
					{
						// send warning message to casting player
						EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
						EsotericaCraft.messagePlayer(player, "This path must already be in use...");
					}
				}
				// this signature has been activated in the past, but has not been linked to a second teleporter yet
				// therefor we overrite the old partial link with a new one
				else
				{
					RuneList.teleportLinksBuffer.put(key, makeNewLink(key, pos, world.dimension.getType()));
				}
			}
		}
		
		// backup rune data
		EsotericaWorldSave.backupData();
	}
	
	private List<Entity> getEntitiesToTeleport(World world, BlockPos root)
	{
		AxisAlignedBB searchArea = new AxisAlignedBB(root).expand(2, 2, 2).expand(-2, 0, -2);
		
		// get all valid mobs
		List<LivingEntity> mobs = new ArrayList<LivingEntity>();
		for (LivingEntity m : world.getEntitiesWithinAABB(LivingEntity.class, searchArea))
		{
			boolean acceptable = true;
			if (m instanceof PlayerEntity)
			{
				acceptable = !((PlayerEntity)m).isSneaking();
			}
			if (acceptable) mobs.add(m);
		}
		
		// get all other valid entities
		List<Entity> all = new ArrayList<Entity>(mobs);
		all.addAll(world.getEntitiesWithinAABB(ItemEntity.class, searchArea));				// add all items
		all.addAll(world.getEntitiesWithinAABB(AbstractMinecartEntity.class, searchArea));	// add minecarts
		all.addAll(world.getEntitiesWithinAABB(BoatEntity.class, searchArea));				// add boats
		return all;
	}
	
	private BlockPos getValidTeleportPoint(World world, PlayerEntity player, BlockPos target)
	{
		int attempts = 0;
		boolean valid = false;
		while (!valid)
		{
			target = target.up();
			BlockPos next = target.up();
			BlockState destination1 = world.getBlockState(target);
			BlockState destination2 = world.getBlockState(next);
			valid = destination1.isAir(world, target) && destination2.isAir(world, next);
			
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
				return null;
			}
		}
		
		return target;
	}
	
	private void teleport(World world, PlayerEntity player, BlockPos from, BlockPos to, TeleportLinkAdapter link)
	{
		// play sound
		world.playSound((PlayerEntity)null, from.getX(), from.getY(), from.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		
		
		// find valid area on other side of teleport linkage
		to = getValidTeleportPoint(world, player, to);
		
		// valid area found- do teleport
		if (player == null || player.isSneaking())
		{
			World thisSideWorld = DimensionManager.getWorld(world.getServer(), DimensionType.getById(getThisSide(link).dimension), true, true);
			// player is sneaking- teleport entities and not player
			List<Entity> entities = getEntitiesToTeleport(thisSideWorld, from);
			if (entities.size() == 0)
			{
				entities = getEntitiesToTeleport(world, BlockPos.fromLong(getOtherSide(link).position));
				to = getValidTeleportPoint(thisSideWorld, player, BlockPos.fromLong(getThisSide(link).position));
				for (Entity entity : entities)
				{
					if (entity.dimension != thisSideWorld.dimension.getType())
					{
						player.sendMessage(new StringTextComponent(String.format("Old Dimension:%s, New Dimension:%s", 
								entity.dimension.getRegistryName().toString(), 
								thisSideWorld.dimension.getType().getRegistryName().toString())));
						entity.changeDimension(thisSideWorld.dimension.getType());
					}
					entity.setPositionAndUpdate(
							to.getX()+.5,
							to.getY()+.5,
							to.getZ()+.5
						);
				}
			}
			else
			{
				for (Entity entity : entities)
				{
					if (entity.dimension != world.dimension.getType())
					{
						player.sendMessage(new StringTextComponent(String.format("Old Dimension:%s, New Dimension:%s", 
								entity.dimension.getRegistryName().toString(), 
								world.dimension.getType().getRegistryName().toString())));
						entity.changeDimension(world.dimension.getType());
					}
					entity.setPositionAndUpdate(
							to.getX()+.5,
							to.getY()+.5,
							to.getZ()+.5
						);
				}
			}
		}
		else
		{
		    // teleport player
		    preloadChunk(to, world, player);
		    ((ServerPlayerEntity)player).teleport((ServerWorld)world, to.getX()+.5, to.getY()+.5, to.getZ()+.5, player.rotationYaw, player.prevRotationPitch);
		    
		    // resets the player's XP visualizer upon teleporting between dimensions
		    player.giveExperiencePoints(0);
		}

		// play sound
		world.playSound((PlayerEntity)null, to.getX(), to.getY(), to.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}
	
	private static void preloadChunk(BlockPos pos, World world, PlayerEntity player)
	{
		// preload chunk containing `pos` and all surrounding chunk in a radius of 1
		ChunkPos.getAllInBox(new ChunkPos(pos), 1).forEach(chunk -> {
		    ((ServerWorld)world).getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, chunk, 1, player.getEntityId());
		});
	}

	protected abstract TeleporterSide getThisSide(TeleportLinkAdapter link);
	protected abstract void setThisSide(TeleportLinkAdapter link, TeleporterSide side);
	protected abstract TeleporterSide getOtherSide(TeleportLinkAdapter link);
	protected abstract void setOtherSide(TeleportLinkAdapter link, TeleporterSide side);
	protected abstract TeleportLinkAdapter makeNewLink(String key, BlockPos firstLink, DimensionType dimension);
}
