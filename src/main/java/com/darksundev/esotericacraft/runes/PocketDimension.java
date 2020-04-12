package com.darksundev.esotericacraft.runes;

import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Registrar;
import com.darksundev.esotericacraft.Utils;
import com.darksundev.esotericacraft.dimension.DynamicDimension;
import com.darksundev.esotericacraft.dimension.DynamicVoidDimension;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.tags.MyTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class PocketDimension extends Rune
{
	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 * 
	 * 		- E E E -
	 * 		E E O E E
	 * 		E O - O E
	 * 		E E O E E
	 * 		- E E E -
	 */
	public PocketDimension()
	{
		super("Pocket_Dimension", new Tier[][]{
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.ENCHANTED,	Tier.ENCHANTED},
			new Tier[]{Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE,		Tier.MUNDANE,	Tier.ENCHANTED},
			new Tier[]{Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.ENCHANTED,	Tier.ENCHANTED},
			new Tier[]{Tier.NONE, 		Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.NONE}
		});
	}
	
	@SubscribeEvent
	public static void onPlayerRightClickBlock(RightClickEmpty event)
	{
		// enforce server-side only
		if (event.getWorld().isRemote)
			return;
		
		// if player right clicked bedrock in a pocket dimension
		if (event.getWorld().dimension.getType().getId() > 1 && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEDROCK)
		{
			checkLeavePocketDimension(event.getWorld(), event.getPos(), (ServerPlayerEntity)event.getPlayer());
		}
	}
	@SubscribeEvent
	public static void onPlayerRightClickBlock(RightClickItem event)
	{
		// enforce server-side only
		if (event.getWorld().isRemote)
			return;
		
		// if player right clicked bedrock in a pocket dimension
		if (event.getWorld().dimension.getType().getId() > 1 && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEDROCK)
		{
			checkLeavePocketDimension(event.getWorld(), event.getPos(), (ServerPlayerEntity)event.getPlayer());
		}
	}
	@SubscribeEvent
	public static void onPlayerRightClickBlock(RightClickBlock event)
	{
		// enforce server-side only
		if (event.getWorld().isRemote)
			return;
		
		// if player right clicked bedrock in a pocket dimension
		if (event.getWorld().dimension.getType().getId() > 1 && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEDROCK)
		{
			checkLeavePocketDimension(event.getWorld(), event.getPos(), (ServerPlayerEntity)event.getPlayer());
		}
	}
	private static void checkLeavePocketDimension(World world, BlockPos position, ServerPlayerEntity player)
	{
		// if player right clicked bedrock in a pocket dimension
		if (!player.isSneaking() && world.dimension.getType().getId() > 1 && world.getBlockState(position).getBlock() == Blocks.BEDROCK)
		{
			// get parent dimension
			String[] parts = world.dimension.getType().getRegistryName().getPath().split("_");
			DimensionType dimension = DimensionType.getById(Integer.parseInt(parts[0]));
			ServerWorld dimensionWorld = world.getServer().getWorld(dimension);
			
			// get spawn location above pocket dimension's parent rune
			BlockPos runePos = BlockPos.fromLong(Long.parseLong(parts[1]));
			MutableBlockPos pos = new MutableBlockPos(runePos);
	    	while (!dimensionWorld.isAirBlock(pos))
	    	{
	    		pos.setPos(pos.getX(), pos.getY()+1, pos.getZ());
	    	}
	    	
			// teleport player to this pocket dimension's parent rune
			//dimensionWorld.getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, new ChunkPos(pos), 1, player.getEntityId());
			TeleporterBase.preloadChunk(pos, dimensionWorld, player);
			player.teleport(dimensionWorld, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, player.rotationYaw, player.rotationPitch);
		}
	}
	
	@Override
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		// construct pocket dimension name from position and dimension of this rune
		String dimensionId = String.format("%d_%d", worldIn.dimension.getType().getId(), pos.toLong());
		if (DimensionType.byName(Registrar.location(dimensionId)) == null)
		{
			// check rune valid
			boolean valid = checkValid(worldIn, pos, pattern, enchantBlocks);
			if (!valid)
			{
				return false;
			}
			else
			{
				// take offering
				convertOfferingBlocks(worldIn, pos, pattern);
				player.playSound(SoundEvents.BLOCK_CONDUIT_ACTIVATE, 1, 1);
			}
		}
		// register or get dimension
		BiomeType type = getBiomeType(pattern);
		DimensionType dimension;
		if (type.biome == Biomes.THE_VOID)
		{
			dimension = DynamicVoidDimension.register(dimensionId, type.biome, type.floatingIsland);
		}
		else
		{
			dimension = DynamicDimension.register(dimensionId, type.biome, type.floatingIsland);
		}
		
		// get spawn point in pocket dimension
		ServerWorld dimensionWorld = worldIn.getServer().getWorld(dimension);
		BlockPos teleportDestination = dimensionWorld.dimension.findSpawn(0, 0, false);

		// teleport player
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
		//dimensionWorld.getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, new ChunkPos(teleportDestination), 1, player.getEntityId());
		TeleporterBase.preloadChunk(pos, dimensionWorld, player);
		serverPlayer.teleport(dimensionWorld, teleportDestination.getX()+.5, teleportDestination.getY(), teleportDestination.getZ()+.5, player.rotationYaw, player.rotationPitch);
			
		return true;
	}
	private static class BiomeType
	{
		public Biome biome;
		public boolean floatingIsland;
		public BiomeType(Biome biome, boolean floatingIsland) {
			this.biome = biome;
			this.floatingIsland = floatingIsland;
		}
	}
	private static BiomeType getBiomeType(BlockState[][] pattern)
	{
		HashSet<Block> blocks = new HashSet<Block>();
		blocks.add(pattern[1][2].getBlock());
		blocks.add(pattern[2][1].getBlock());
		blocks.add(pattern[2][3].getBlock());
		blocks.add(pattern[3][2].getBlock());
		if (blocks.size() == 1)
		{
			Block b = blocks.iterator().next();
			
			BiomeType result = new BiomeType(Biomes.THE_VOID, false);
			
			if (BlockTags.PLANKS.contains(b))
			{
				if (b == Blocks.DARK_OAK_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.DARK_FOREST, Biomes.DARK_FOREST);
				else if (b == Blocks.OAK_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.FOREST, Biomes.FOREST, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SWAMP, Biomes.SWAMP_HILLS);
				else if (b == Blocks.BIRCH_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS);
				else if (b == Blocks.ACACIA_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU);
				else if (b == Blocks.SPRUCE_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.TAIGA, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.TAIGA_HILLS, Biomes.TAIGA_MOUNTAINS);
				else if (b == Blocks.JUNGLE_PLANKS)
					result.biome = (Biome)Utils.pickRandom(Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE_HILLS, Biomes.MODIFIED_JUNGLE);
			}
			else if (b == Blocks.RED_SANDSTONE)
			{
				result.biome = (Biome)Utils.pickRandom(
						Biomes.BADLANDS,
						Biomes.BADLANDS_PLATEAU,
						Biomes.ERODED_BADLANDS,
						Biomes.MODIFIED_BADLANDS_PLATEAU,
						Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU,
						Biomes.WOODED_BADLANDS_PLATEAU);
			}
			else if (MyTags.SANDSTONE.contains(b))
			{
				result.biome = (Biome)Utils.pickRandom(
						Biomes.DESERT,
						Biomes.DESERT_HILLS,
						Biomes.DESERT_LAKES);
			}
			//else if (MyTags.BAMBOO.contains(b))
			//{
			//	result.biome = (Biome)Utils.pickRandom(
			//			Biomes.BAMBOO_JUNGLE,
			//			Biomes.BAMBOO_JUNGLE_HILLS);
			//}
			else if (b == Blocks.COBBLESTONE)
			{
				result.biome = (Biome)Utils.pickRandom(
						Biomes.GRAVELLY_MOUNTAINS,
						Biomes.MODIFIED_GRAVELLY_MOUNTAINS);
			}
			else if (b == Blocks.END_STONE)
			{
				result.biome = Biomes.THE_VOID;
				result.floatingIsland = true;
			}
			
			return result;
		}
		
		return new BiomeType(Biomes.THE_VOID, false);
	}
	
	private void convertOfferingBlocks(World world, BlockPos position, BlockState[][] pattern)
	{
		HashSet<BlockPos> offeringBlockPositions = new HashSet<BlockPos>();
		MutableBlockPos mutablePos = new MutableBlockPos(position);
		offeringBlockPositions.add(mutablePos.east(3).toImmutable());
		offeringBlockPositions.add(mutablePos.west(3).toImmutable());
		offeringBlockPositions.add(mutablePos.north(3).toImmutable());
		offeringBlockPositions.add(mutablePos.south(3).toImmutable());
		offeringBlockPositions.add(mutablePos.add(-2, 0, -1).toImmutable());
		offeringBlockPositions.add(mutablePos.add(-1, 0, -2).toImmutable());
		offeringBlockPositions.add(mutablePos.add(-2, 0, 1).toImmutable());
		offeringBlockPositions.add(mutablePos.add(-1, 0, 2).toImmutable());
		offeringBlockPositions.add(mutablePos.add(1, 0, 2).toImmutable());
		offeringBlockPositions.add(mutablePos.add(2, 0, 1).toImmutable());
		offeringBlockPositions.add(mutablePos.add(2, 0, -1).toImmutable());
		offeringBlockPositions.add(mutablePos.add(1, 0, 0-2).toImmutable());
		for (BlockPos blockPos : offeringBlockPositions) {
			world.setBlockState(blockPos, Blocks.OBSIDIAN.getDefaultState());
		}
	}
	
	private boolean checkValid(World world, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks)
	{
		// check for diamond blocks
		HashSet<Block> blocks = new HashSet<Block>();
		blocks.add(pattern[0][1].getBlock());
		blocks.add(pattern[1][0].getBlock());
		blocks.add(pattern[1][1].getBlock());
		blocks.add(pattern[0][3].getBlock());
		blocks.add(pattern[1][3].getBlock());
		blocks.add(pattern[1][4].getBlock());
		blocks.add(pattern[3][0].getBlock());
		blocks.add(pattern[3][1].getBlock());
		blocks.add(pattern[4][1].getBlock());
		blocks.add(pattern[4][3].getBlock());
		blocks.add(pattern[3][3].getBlock());
		blocks.add(pattern[3][4].getBlock());
		if (blocks.size() == 1 && blocks.contains(Blocks.DIAMOND_BLOCK))
		{
			blocks.clear();
			
			// check for gold blocks
			blocks.add(pattern[2][0].getBlock());
			blocks.add(pattern[0][2].getBlock());
			blocks.add(pattern[2][4].getBlock());
			blocks.add(pattern[4][2].getBlock());
			MutableBlockPos pos$mutable = new MutableBlockPos(pos);
			blocks.add(world.getBlockState(pos$mutable.add(3, 0, 0)).getBlock());
			pos$mutable.setPos(pos);
			blocks.add(world.getBlockState(pos$mutable.add(-3, 0, 0)).getBlock());
			pos$mutable.setPos(pos);
			blocks.add(world.getBlockState(pos$mutable.add(0, 0, 3)).getBlock());
			pos$mutable.setPos(pos);
			blocks.add(world.getBlockState(pos$mutable.add(0, 0, -3)).getBlock());
			pos$mutable.setPos(pos);
			
			return blocks.size() == 1 && blocks.contains(Blocks.GOLD_BLOCK);
		}
		else
		{
			return false;
		}
	}
}
