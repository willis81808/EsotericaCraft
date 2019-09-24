package com.darksundev.esotericacraft.dimension;

import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;

public class VoidChunkGenerator extends ChunkGenerator<VoidDimensionSettings>
{
	public VoidChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, VoidDimensionSettings generationSettingsIn)
	{
		super(worldIn, biomeProviderIn, generationSettingsIn);
	}
	@Override
	public void generateSurface(IChunk chunkIn) { }
	@Override
	public void makeBase(IWorld worldIn, IChunk chunkIn) { }
	@Override
	public int getGroundHeight() {
		return this.world.getSeaLevel() + 1;
	}
	@Override
	public int func_222529_a(int p_222529_1_, int p_222529_2_, Type p_222529_3_) {
		return 0;
	}

	
	@Override
	public void decorate(WorldGenRegion region)
	{
		int x = region.getMainChunkX();
		int z = region.getMainChunkZ();
		if (x == 0 && z == 0)
			region.setBlockState(DynamicDimension.origin.down(), Blocks.BEDROCK.getDefaultState(), 2);
	}
}
