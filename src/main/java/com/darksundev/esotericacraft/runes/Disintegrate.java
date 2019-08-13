package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class Disintegrate extends Rune implements IItemEffect
{
	private static final String NBT_TAG = "disintegrate_effect";
	private static final int ENCHANTMENT_USES = 5;
	
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- - O - -
	 * 		- O - O -
	 *		O - M - O
	 * 		- O - O -
	 *		- - O - -
	 */
	public Disintegrate()
	{
		super("Disintegrate", new Tier[][]{
			new Tier[]{Tier.NONE,		Tier.NONE, 		Tier.ENCHANTED, Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.NONE,		Tier.MUNDANE,	Tier.NONE,		Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.NONE, 		Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,	Tier.NONE}
		});
	}

	@Override
	public void onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks);

		// look for an enchantable garnet in caster's hand
		ItemStack garnet = getEnchantableGarnet(player);
		if (garnet == null)
		{
			EsotericaCraft.messagePlayer(player, "No enchantable garnet found...");
			return;
		}
		
		// enchant garnet
		EsotericaCraft.messagePlayer(player, "Disintegrate", TextFormatting.DARK_RED);
		addData(garnet.getOrCreateTag());
	}

	private ItemStack getEnchantableGarnet(PlayerEntity player)
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

	
	@Override
	public String getNBTEffectTag()
	{
		return NBT_TAG;
	}

	@Override
	public void doRightClickBlockEffect(RightClickBlock event, ItemStack item)
	{
		if (event.getEntityPlayer().getHeldItemMainhand() != item)
			return;
		
		int usesRemaining = item.getTag().getInt(getNBTEffectTag());
		BlockPos pos = event.getPos();
		if (usesRemaining > 0)
		{
			// spawn lightning
	        LightningBoltEntity lightningboltentity = new LightningBoltEntity(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), false);
	        event.getWorld().getServer().getWorld(event.getWorld().getDimension().getType()).addLightningBolt(lightningboltentity);
			item.getTag().putInt(getNBTEffectTag(), --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			item.getTag().remove(getNBTEffectTag());
		}
	}

	@Override
	public void addData(CompoundNBT nbt)
	{
		nbt.putInt(getNBTEffectTag(), ENCHANTMENT_USES);
	}

	@Override
	public void displayTooltip(ItemTooltipEvent event)
	{
		CompoundNBT data = event.getItemStack().getTag();
		int uses = data.getInt(getNBTEffectTag());
		event.getToolTip().add(new StringTextComponent(TextFormatting.LIGHT_PURPLE + "Disintegrate: " + TextFormatting.RESET + uses));
	}

	@Override
	public void doAttackEntityEffect(AttackEntityEvent event, ItemStack item)
	{
		int usesRemaining = item.getTag().getInt(getNBTEffectTag());
		BlockPos pos = event.getEntity().getPosition();
		if (usesRemaining > 0)
		{
			// spawn lightning
			World w = event.getEntity().world;
	        LightningBoltEntity lightningboltentity = new LightningBoltEntity(w, pos.getX(), pos.getY(), pos.getZ(), false);
	        w.getServer().getWorld(w.getDimension().getType()).addLightningBolt(lightningboltentity);
			item.getTag().putInt(getNBTEffectTag(), --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			item.getTag().remove(getNBTEffectTag());
		}
	}
}
