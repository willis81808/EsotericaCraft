package com.darksundev.esotericacraft.items;

import com.darksundev.esotericacraft.entities.TeleportArrowEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TeleportArrowItem extends ArrowItem
{

	public TeleportArrowItem(Properties builder)
	{
		super(builder);
	}

	@Override
	public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter)
	{
		return new TeleportArrowEntity(worldIn, shooter);
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player)
	{
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
}
