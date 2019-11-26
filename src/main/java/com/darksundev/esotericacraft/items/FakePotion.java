package com.darksundev.esotericacraft.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FakePotion extends Item
{
	private boolean hasEffect;
	
	public FakePotion(Properties properties, boolean hasEffect)
	{
		super(properties.maxStackSize(1));
		this.hasEffect = hasEffect;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return hasEffect;
	}
}
