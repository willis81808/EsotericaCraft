package com.darksundev.esotericacraft.runes;

import java.util.ArrayList;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class Rune
{
	private String key, name;
	private Tier[][] pattern;
	
	public static String StringFromPattern(Tier[][] pattern)
	{
		StringBuilder newKey = new StringBuilder();
		for(int i=0; i<pattern.length; i++)
		{
			for (int j=0; j<pattern[0].length; j++)
			{
				switch (pattern[i][j])
				{
					case NONE:
						newKey.append(-1);
						break;
					case MUNDANE:
						newKey.append(0);
						break;
					case ENCHANTED:
						newKey.append(1);
						break;
				}
			}
			newKey.append(';');
		}
		return newKey.toString();
	}
	
	public Rune(String name, Tier[][] pattern)
	{
		this.name = name;
		this.pattern = pattern;
		key = StringFromPattern(pattern);
		System.out.println(key);
	}
	public String getKey()
	{
	  return key;
    }
	public Tier[][] getPattern()
	{
		return pattern;
	}
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (this instanceof IItemEffect)
		{
			IItemEffect me = (IItemEffect)this;
			
			// check for any rune effect tags
			// exit if already enchanted garnet found with unstackable enchantment
			ItemStack item = getEnchantableGarnet(player);
			if (item == null && me.requireGarnet())
			{
				EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
				EsotericaCraft.messagePlayer(player, "You must hold a garnet to receive your enchantment!");
				return false;
			}
			CompoundNBT data = item.getTag();
			if (data != null)
			{
				ArrayList<String> keys = new ArrayList<String>(data.keySet());
				for (int i = 0; i < keys.size(); i++)
				{
					if (RuneManager.nbtTagMap.containsKey(keys.get(i)))
					{
						EsotericaCraft.logger.info(keys.get(i));
						EsotericaCraft.logger.info(me.getNBTEffectTag());
						
						if (!keys.get(i).equals(me.getNBTEffectTag()))
						{
							EsotericaCraft.logger.info("Not equal...?");
							if (!((IItemEffect)RuneManager.nbtTagMap.get(keys.get(i))).effectCanStack() || !me.effectCanStack())
							{
								EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
								EsotericaCraft.messagePlayer(player, "These enchantments cannot be stacked on the same garnet...");
								return false;
							}
						}
					}
				}
			}
		}
		
		EsotericaCraft.logger.info("Cast Rune: " + name);
		return true;
	}
	protected ItemStack getEnchantableGarnet(PlayerEntity player)
	{
		Item main = player.getHeldItemMainhand().getItem();
		Item off = player.getHeldItemOffhand().getItem();
		
		if (main == ItemList.runing_staff && off == ItemList.garnet)
		{
			return player.getHeldItemOffhand();
		}
		else if (off == ItemList.runing_staff && main == ItemList.garnet)
		{
			return player.getHeldItemMainhand();
		}

		return null;
	}
}