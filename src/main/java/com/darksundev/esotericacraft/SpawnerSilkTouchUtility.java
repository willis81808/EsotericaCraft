package com.darksundev.esotericacraft;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class SpawnerSilkTouchUtility
{
	@SubscribeEvent
	public static void onPlayerBreakBlock(BreakEvent event)
	{
		// unpack event
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		World w = player.world;
		BlockState b = w.getBlockState(pos);
		
		// are we breaking a spawner?
		if (b.getBlock() == Blocks.SPAWNER)
		{
			// check for a diamond pickaxe
			ItemStack tool = player.getHeldItemMainhand();
			if (tool.getItem() == Items.DIAMOND_PICKAXE)
			{
				// check for silk touch and efficiency V
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);
				if (enchantments != null && enchantments.containsKey(Enchantments.SILK_TOUCH) && enchantments.containsKey(Enchantments.EFFICIENCY))
				{
					if (enchantments.get(Enchantments.EFFICIENCY) == 5)
					{
						// drop spawner item in world
						ItemEntity item = new ItemEntity(w, pos.getX(), pos.getY() + .5, pos.getZ(), new ItemStack(Blocks.SPAWNER));
						w.addEntity(item);
					}
				}
			}
		}
	}
}
