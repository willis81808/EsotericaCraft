package com.darksundev.esotericacraft.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;

public class FloatingIslandChunkGenerator extends EndChunkGenerator
{
	public FloatingIslandChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, EndGenerationSettings generationSettingsIn)
	{
		super(worldIn, biomeProviderIn, getSettings(generationSettingsIn));
	}
	
	private static EndGenerationSettings getSettings(EndGenerationSettings generationSettingsIn)
	{
		generationSettingsIn.setDefaultBlock(Blocks.NETHERRACK.getDefaultState());
		return generationSettingsIn;
	}
}
