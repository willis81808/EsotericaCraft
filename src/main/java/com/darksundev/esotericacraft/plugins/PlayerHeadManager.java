package com.darksundev.esotericacraft.plugins;

import java.util.Map;

import com.darksundev.esotericacraft.EsotericaCraft;

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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class PlayerHeadManager
{
	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event)
	{
		// only proceed if executing on server, and if attacking and attacked entities are players
		if (event.getEntity().world.isRemote || !(event.getEntity() instanceof PlayerEntity) || !(event.getSource().getImmediateSource() instanceof PlayerEntity))
			return;
		
		// check for sweeping edge diamond axe
		ItemStack item = ((PlayerEntity)event.getSource().getImmediateSource()).getHeldItemMainhand();
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
		if (item.getItem() == Items.DIAMOND_AXE && enchantments != null && enchantments.containsKey(Enchantments.SWEEPING))
		{
			// create head
			ItemStack head = new ItemStack(Blocks.PLAYER_HEAD, 1);
			head.getOrCreateTag().putString("SkullOwner", ((PlayerEntity)event.getEntity()).getDisplayName().getString());
			
			// place head at death point
			World w = event.getEntity().world;
			BlockPos pos = event.getEntity().getPosition();
			w.addEntity(new ItemEntity(w, pos.getX(), pos.getY()+0.5, pos.getZ(), head));
		}
	}
}
