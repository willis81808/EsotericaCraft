package com.darksundev.esotericacraft.plugins;

import java.util.List;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Utils;
import com.darksundev.esotericacraft.commands.ModOverrideCommand;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class SignShopManager
{
	@SubscribeEvent
	public static void onPlayerRightClickBlock(RightClickBlock event)
	{		
		// server side only
		if (event.getWorld().isRemote)
			return;
		
		PlayerEntity player = event.getPlayer();
		World w = event.getWorld();
		BlockState b = w.getBlockState(event.getPos());

		// only allow hand with item to activate this (if we have air in one hand and an item in the other)
		ItemStack item = event.getItemStack();
		if (item != player.getHeldItemMainhand())
			return;
		if (b.getBlock() instanceof WallSignBlock)
		{
			// get sign entity
			SignTileEntity s = (SignTileEntity) event.getWorld().getTileEntity(event.getPos());
			
			// is sign valid?
			SignShopData data = new SignShopManager.SignShopData(s.signText);
			if (data.isValidShop)
			{
				// look for chest nearby
				ChestTileEntity chest = findChest(event.getPos(), event.getWorld());
				if (chest == null)
				{
					// couldn't find a chest for this shop :(
					EsotericaCraft.messagePlayer(player, "Shop sign is valid, but no chest could be found :(");
					return;
				}
				else
				{
					// check stock
					ResourceLocation r = new ResourceLocation("minecraft:"+data.give.name);
					if (!ForgeRegistries.ITEMS.containsKey(r))
					{
						return;
					}
					event.setUseItem(Result.DENY);
					Item product = ForgeRegistries.ITEMS.getValue(r);
					int stockSize = 0;
					for (ItemStack i : chest.getItems()) {
						if (i.getItem() == product)
							stockSize += i.getCount();
					}
					// info-only activation
					if (player.isSneaking() && !w.isRemote)
					{
						EsotericaCraft.messagePlayer(player, " ", TextFormatting.RESET);
						EsotericaCraft.messagePlayer(player, "~~ Shop ~~", TextFormatting.DARK_GREEN);
						EsotericaCraft.messagePlayer(player, Utils.textComponentFromString("Owner: ").appendText(data.owner));
						EsotericaCraft.messagePlayer(player,
								Utils.textComponentFromString("Stocked: ")
									.appendSibling(Utils.textComponentFromString(stockSize >= data.give.count ? "Yes" : "No")
											.applyTextStyle(stockSize >= data.give.count ? TextFormatting.GREEN : TextFormatting.RED)));
						EsotericaCraft.messagePlayer(player,
								String.format("They want: %d %s.", data.ask.count, data.ask.name), TextFormatting.GRAY, TextFormatting.ITALIC);
						EsotericaCraft.messagePlayer(player,
								String.format("They offer: %d %s.", data.give.count, data.give.name), TextFormatting.GRAY, TextFormatting.ITALIC);
						EsotericaCraft.messagePlayer(player, " ", TextFormatting.RESET);
						return;
					}
					
					// attempt to process purchase
					if (data.owner.equals(player.getDisplayName().getString()))
					{
						// we own this shop
						EsotericaCraft.messagePlayer(player, "Owner recognized", TextFormatting.GREEN);
					}
					else
					{
						// we are a customer
						ItemStack payment = player.getHeldItemMainhand();
						String itemName = payment.getItem().getRegistryName().toString();
						if (itemName.equalsIgnoreCase("minecraft:" + data.ask.name))
						{
							if (stockSize >= data.give.count)
							{
								// do we have enough to buy?
								int buyerMoney = payment.getCount();
								if (buyerMoney >= data.ask.count)
								{
									// add to chest inventory and
									// remove sold items from chest
									int needAddPaymentCount = data.ask.count;
									int needRemoveProductCount = data.give.count;
									List<ItemStack> chestInv = chest.getItems();
									for (int i = 0; i < chestInv.size() ; i++) {
										ItemStack slot = chestInv.get(i);
										if (needAddPaymentCount > 0)
										{
											// found a slot with enough space
											if (slot.getItem() == payment.getItem() && slot.getCount() < slot.getMaxStackSize())
											{
												int diff = Math.min(needAddPaymentCount, slot.getMaxStackSize() - slot.getCount());
												slot.setCount(slot.getCount() + diff);
												needAddPaymentCount -= diff;
											}
											else if (slot.isEmpty())
											{
												ItemStack paymentPortion = payment.copy();
												paymentPortion.setCount(needAddPaymentCount);
												chest.setInventorySlotContents(i, paymentPortion);
												needAddPaymentCount = 0;
											}
										}
										if (needRemoveProductCount > 0)
										{
											if (slot.getItem() == product)
											{
												int diff = Math.min(needRemoveProductCount, slot.getCount());
												slot.setCount(slot.getCount() - diff);
												needRemoveProductCount -= diff;
											}
										}
										
										if (needAddPaymentCount == 0 && needRemoveProductCount == 0)
											break;
									}

									// take payment from player
									payment.setCount(buyerMoney - data.ask.count);
									// give player their goods
									ItemStack goods = new ItemStack(product);
									goods.setCount(data.give.count);
									player.addItemStackToInventory(goods);
									
								}
							}
							else
							{
								EsotericaCraft.messagePlayer(player, "Out of stock!", TextFormatting.RED);
							}
						}
					}
				}
			}
			else
			{
				// this sign is either not a shop, or an invalid one
				EsotericaCraft.logger.info("Invalid Shop");
			}
		}
		else if (Blocks.CHESTS_WOODEN.contains(b.getBlock()))
		{
			// is there a sign nearby?
			SignTileEntity sign = findSign(event.getPos(), w);
			if (sign != null)
			{
				// is the sign a valid shop sign?
				SignShopData data = new SignShopData(sign.signText);
				if (data.isValidShop)
				{
					// is this player the shop owner?
					if (data.owner.equals(player.getDisplayName().getString()) || ModOverrideCommand.hasOverridePermission(player))
					{
						event.setUseBlock(Result.ALLOW);
					}
					else
					{
						// this player doesn't own the shop, stop them from opening the chest
						EsotericaCraft.messagePlayer(player, "This shop belongs to " + data.owner, TextFormatting.RED);
						event.setCanceled(true);
						event.setUseBlock(Result.DENY);
					}
				}
			}
		}
		
	}
	@SubscribeEvent
	public static void onPlayerBreakBlock(BreakEvent event)
	{
		boolean validUse = true;

		// unpack event
		PlayerEntity player = event.getPlayer();
		World w = player.world;
		BlockState b = w.getBlockState(event.getPos());
		
		// trying to break a chest or wall sign?
		if (b.getBlock() instanceof WallSignBlock)
		{
			// is this a valid shop sign?
			SignTileEntity sign = (SignTileEntity) w.getTileEntity(event.getPos());
			SignShopData data = new SignShopData(sign.signText);
			if (data.isValidShop)
			{
				// is this player the shop owner?
				if (!data.owner.equals(player.getDisplayName().getString()))
				{
					validUse = false;
				}
			}
		}
		else if (Blocks.CHESTS_WOODEN.contains(b.getBlock()))
		{
			// is there a sign nearby?
			SignTileEntity sign = findSign(event.getPos(), w);
			if (sign != null)
			{
				// is the sign a valid shop sign?
				SignShopData data = new SignShopData(sign.signText);
				if (data.isValidShop)
				{
					// is this player the shop owner?
					if (!data.owner.equals(player.getDisplayName().getString()))
					{
						validUse = false;
					}
				}
			}
		}
		
		if (!validUse && !ModOverrideCommand.hasOverridePermission(player))
		{
			event.setCanceled(true);;
		}
	}
	
	private static ChestTileEntity findChest(BlockPos pos, World world)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		tile = world.getTileEntity(pos.north());
		if (tile instanceof ChestTileEntity)
			return (ChestTileEntity) tile;
		tile = world.getTileEntity(pos.south());
		if (tile instanceof ChestTileEntity)
			return (ChestTileEntity) tile;
		tile = world.getTileEntity(pos.east());
		if (tile instanceof ChestTileEntity)
			return (ChestTileEntity) tile;
		tile = world.getTileEntity(pos.west());
		if (tile instanceof ChestTileEntity)
			return (ChestTileEntity) tile;
		
		return null;
	}
	private static SignTileEntity findSign(BlockPos pos, World world)
	{
		TileEntity tile = world.getTileEntity(pos);
		
		tile = world.getTileEntity(pos.north());
		if (tile instanceof SignTileEntity)
			return (SignTileEntity) tile;
		tile = world.getTileEntity(pos.south());
		if (tile instanceof SignTileEntity)
			return (SignTileEntity) tile;
		tile = world.getTileEntity(pos.east());
		if (tile instanceof SignTileEntity)
			return (SignTileEntity) tile;
		tile = world.getTileEntity(pos.west());
		if (tile instanceof SignTileEntity)
			return (SignTileEntity) tile;
		
		return null;
	}
	
	public static class SignShopData
	{
		class Offer
		{
			public int count;
			public String name;
			
			public Offer(int count, String name)
			{
				this.count = count;
				this.name = name;
			}
		}
		
		public boolean isValidShop;
		public String owner;
		public Offer ask, give;
		
		public SignShopData(ITextComponent[] lines)
		{
			// convert to strings
			String[] text = new String[4];
			for (int i = 0; i < text.length; i++) {
				text[i] = lines[i].getFormattedText();
			}
			
			// parse text
			if (text[0].equalsIgnoreCase("[Shop]"))
			{
				owner = text[1];
				try {
					// get asking line
					String[] askingLine = text[2].split(" ");
					
					ask = new Offer(Integer.parseInt(askingLine[0]), askingLine[1]);
					// get give line
					String[] offerLine = text[3].split(" ");
					give = new Offer(Integer.parseInt(offerLine[0]), offerLine[1]);
					
					// mark as valid shop and return
					isValidShop = true;
					return;
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			
			isValidShop = false;
		}
	}
}
