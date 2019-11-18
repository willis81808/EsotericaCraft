package com.darksundev.esotericacraft.plugins;

import java.util.Map;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SquidEntity;
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
		if (event.getEntity().world.isRemote || !(event.getSource().getImmediateSource() instanceof PlayerEntity))
			return;
		
		// check for sweeping edge diamond axe
		ItemStack item = ((PlayerEntity)event.getSource().getImmediateSource()).getHeldItemMainhand();
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
		if (item.getItem() == Items.DIAMOND_AXE && enchantments != null && enchantments.containsKey(Enchantments.SHARPNESS))
		{
			// create head
			ItemStack head = null;
			Entity entity = event.getEntity();
			if (entity instanceof PlayerEntity)
			{
				//head = new ItemStack(Blocks.PLAYER_HEAD, 1);
				//head.getOrCreateTag().putString("SkullOwner", ((PlayerEntity)event.getEntity()).getDisplayName().getString());
				head = makePlayerHeadItem(((PlayerEntity)event.getEntity()).getDisplayName().getString());
			}
			else if (entity instanceof SkeletonEntity)
			{
				head = new ItemStack(Blocks.SKELETON_SKULL, 1);
			}
			else if (entity instanceof CreeperEntity)
			{
				head = new ItemStack(Blocks.CREEPER_HEAD, 1);
			}
			else if (entity instanceof ZombiePigmanEntity)
			{
				head = makePlayerHeadItem("MHF_PigZombie");
			}
			else if (entity instanceof ZombieEntity)
			{
				head = new ItemStack(Blocks.ZOMBIE_HEAD, 1);
			}
			else if (entity instanceof MooshroomEntity)
			{
				head = makePlayerHeadItem("MHF_MushroomCow");
			}
			else if (entity instanceof CowEntity)
			{
				head = makePlayerHeadItem("MHF_Cow");
			}
			else if (entity instanceof ChickenEntity)
			{
				head = makePlayerHeadItem("MHF_Chicken");
			}
			else if (entity instanceof PigEntity)
			{
				head = makePlayerHeadItem("MHF_Pig");
			}
			else if (entity instanceof SheepEntity)
			{
				head = makePlayerHeadItem("MHF_Sheep");
			}
			else if (entity instanceof IronGolemEntity)
			{
				head = makePlayerHeadItem("MHF_Golem");
			}
			else if (entity instanceof VillagerEntity)
			{
				head = makePlayerHeadItem("MHF_Villager");
			}
			else if (entity instanceof SquidEntity)
			{
				head = makePlayerHeadItem("MHF_Squid");
			}
			else if (entity instanceof CaveSpiderEntity)
			{
				head = makePlayerHeadItem("MHF_CaveSpider");
			}
			else if (entity instanceof SpiderEntity)
			{
				head = makePlayerHeadItem("MHF_Spider");
			}
			else if (entity instanceof BlazeEntity)
			{
				head = makePlayerHeadItem("MHF_Blaze");
			}
			else if (entity instanceof EndermanEntity)
			{
				head = makePlayerHeadItem("MHF_Enderman");
			}
			
			// place head at death point
			if (head != null)
			{
				World w = event.getEntity().world;
				BlockPos pos = event.getEntity().getPosition();
				w.addEntity(new ItemEntity(w, pos.getX(), pos.getY()+0.5, pos.getZ(), head));
			}
		}
	}
	
	private static ItemStack makePlayerHeadItem(String name)
	{
		ItemStack head = new ItemStack(Blocks.PLAYER_HEAD, 1);
		head.getOrCreateTag().putString("SkullOwner", name);
		return head;
	}
}
