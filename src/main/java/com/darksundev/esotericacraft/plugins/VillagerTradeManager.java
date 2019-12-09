package com.darksundev.esotericacraft.plugins;

import java.util.Random;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.ItemList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class VillagerTradeManager
{

	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event)
	{
		// give master level Clerics a chance of selling 1 diamond dust in exchange for 1 emerald block
		if (event.getType() == VillagerProfession.CLERIC) {
			event.getTrades().get(5).add(new ITrade() {
				@Override
				public MerchantOffer getOffer(Entity trader, Random rand)
				{
					ItemStack emerald = new ItemStack(Items.EMERALD_BLOCK);
					ItemStack diamond_dust = new ItemStack(ItemList.diamond_dust);
					return new MerchantOffer(emerald, diamond_dust, 16, 10, 1);
				}
			});
		}
	}
}
