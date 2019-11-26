package com.darksundev.esotericacraft.lists;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.registries.IForgeRegistry;

public class PotionList
{
	public static void registerPotions(IForgeRegistry<Potion> registry)
	{
		ItemStack potion = new ItemStack(Items.POTION);
		potion.getOrCreateTag().putString("Potion", "minecraft:awkward");
		BrewingRecipeRegistry.addRecipe(new MyIngredient(potion), Ingredient.fromStacks(new ItemStack(Items.CHORUS_FRUIT)), new ItemStack(ItemList.weird_potion));
		BrewingRecipeRegistry.addRecipe(Ingredient.fromStacks(new ItemStack(ItemList.weird_potion)), Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL)), new ItemStack(ItemList.blink_potion));
		
		//blink_potion = new Potion(new EffectInstance(EffectList.blink_effect))
		//		.setRegistryName(Registrar.location("blink_potion"));
		//registry.register(blink_potion);
		//PotionBrewing.addMix(Potions.AWKWARD, Items.ENDER_EYE, blink_potion);
	}
	
	public static class MyIngredient extends IngredientNBT {
		public MyIngredient(ItemStack stack) {
			super(stack);
		}
	}
}
