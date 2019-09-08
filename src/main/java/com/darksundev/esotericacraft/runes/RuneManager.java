package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;
import java.util.HashMap;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.RuneList;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class RuneManager
{
	private static RuneManager instance;
	public static enum Tier { NONE, MUNDANE, ENCHANTED }
	
	public static HashMap<String, Rune> nbtTagMap = new HashMap<String, Rune>();
	private static HashMap<String, Rune> patternMap = new HashMap<String, Rune>();
	private static HashMap<String, RuneMaterial> blockTierMap = new HashMap<String, RuneMaterial>();

	public RuneManager()
	{
		if (instance == null)
		{
			instance = this;
		}
		else
		{
			EsotericaCraft.logger.error("Trying to create a second instance of RuneManager!");
		}
		
		RuneList.registerRunes();
		RuneList.registerAllRuneMaterials();
	}

	@SubscribeEvent
	public static void onPlayerRightClickBlock(RightClickItem event)
	{
		// server side only
		if (event.getWorld().isRemote)
			return;		
		
		// attempt to cast rune effects with currently held items
		attemptExecuteRuneEffect(event, event.getItemStack());
	}
	@SubscribeEvent
	public static void onPlayerAttackEntity(AttackEntityEvent event)
	{
		// server side only
		if (event.getEntity().world.isRemote)
			return;		
		
		// attempt to cast rune effects with currently held items
		attemptExecuteRuneEffect(event, event.getPlayer().getHeldItemMainhand());
	}
	
	private static boolean attemptExecuteRuneEffect(RightClickItem event, ItemStack item)
	{
		CompoundNBT data = item.getTag();
		
		boolean executed = false;

		// check for any rune effect tags
		if (data != null)
		{
			for (String key : data.keySet())
			{
				if (nbtTagMap.containsKey(key))
				{
					// execute rune effect, and pass this item to the event
					((IItemEffect)nbtTagMap.get(key)).doRightClickBlockEffect(event, item);;
					executed = true;
				}
			}
		}
		
		return executed;
	}
	private static boolean attemptExecuteRuneEffect(AttackEntityEvent event, ItemStack item)
	{
		CompoundNBT data = item.getTag();
		boolean executed = false;

		// check for any rune effect tags
		if (data != null)
		{
			for (String key : data.keySet())
			{
				if (nbtTagMap.containsKey(key))
				{
					// execute rune effect, and pass this item to the event
					((IItemEffect)nbtTagMap.get(key)).doAttackEntityEffect(event, item);;
					executed = true;
				}
			}
		}
		
		return executed;
	}	

	public static void registerRune(Rune r)
	{
		// map runes to their pattern keys
		String key = r.getKey();
		if (!patternMap.containsKey(key))
		{
			patternMap.put(key, r);
		}
		// map runes with item effects to their effect tag
		if (r instanceof IItemEffect)
		{
			IItemEffect i = (IItemEffect)r;
			nbtTagMap.put(i.getNBTEffectTag(), r);
		}
	}
	public static void registerRuneMaterial(RuneMaterial material)
	{
		if (!blockTierMap.containsKey(material.getBlockID()))
		{
			blockTierMap.put(material.getBlockID(), material);
		}
	}

	public static BlockState[][] getArea(World world, BlockPos rootPos)
	{
		BlockState block = world.getBlockState(rootPos);
		BlockState[][] area = new BlockState[5][5];
		area[2][2] = block;

		// log block clicked
		EsotericaCraft.logger.info(block.getBlock().getTranslationKey());
		
		// get area
		for (int x = -2; x <= 2; x++)
		{
			int xVal = rootPos.getX() + x;
			for (int z = -2; z <= 2; z++)
			{
				BlockState b;
				if (!(x == 0 && z == 0))
				{
					int zVal = rootPos.getZ() + z;
					b = world.getBlockState(new BlockPos(xVal, rootPos.getY(), zVal));
					area[2+x][2+z] = b;
				}
				else 
				{
					b = block;
				}
			}
		}
		
		return area;
	}
	
	public static RuneMaterial getMaterial(String blockId)
	{
		RuneMaterial result = blockTierMap.get(blockId);
		if (result == null)
		{
			result = new RuneMaterial(blockId, Tier.NONE);
		}
		return result;
	}
	public static RuneCast getRune(BlockState[][] blocks)
	{
		// construct pattern from passed blocks
		ArrayList<BlockState> enchantBlocks = new ArrayList<BlockState>();
		ArrayList<BlockState> mundaneBlocks = new ArrayList<BlockState>();
		Tier[][] pattern = new Tier[blocks.length][blocks[0].length];
		for (int x = 0; x < blocks.length; x++)
		{
			for (int y = 0; y < blocks[0].length; y++)
			{
				RuneMaterial mat = getMaterial(blocks[x][y].getBlock().getTranslationKey());
				// the material is recognized
				if (mat != null)
				{
					pattern[x][y] = mat.getTier();
					switch (mat.getTier())
					{
						case ENCHANTED:
							enchantBlocks.add(blocks[x][y]);
							break;
						case MUNDANE:
							mundaneBlocks.add(blocks[x][y]);
							break;
						case NONE:
							// do nothing
							break;
					}
				}
				// the block type is not a RuneMaterial
				else
				{
					pattern[x][y] = Tier.NONE;
				}
			}
		}

		// return rune of pattern or null if no pattern found
		String key = Rune.StringFromPattern(pattern);
		return new RuneCast(
				key,
				patternMap.get(key),
				enchantBlocks.toArray(new BlockState[] {}),
				mundaneBlocks.toArray(new BlockState[] {}) );
	}
}