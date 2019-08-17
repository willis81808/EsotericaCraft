package com.darksundev.esotericacraft.runes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

public interface IItemEffect
{
	String getNBTEffectTag();
	void doRightClickBlockEffect(RightClickItem event, ItemStack item);
	void doAttackEntityEffect(AttackEntityEvent event, ItemStack item);
	void addData(CompoundNBT nbt, Object... args);
	void displayTooltip(ItemTooltipEvent event);
}
