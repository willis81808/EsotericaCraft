package com.darksundev.esotericacraft.dimension;

import java.util.Random;

import javax.annotation.Nullable;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Registrar;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraftforge.common.DimensionManager;

/*
 * A huge thank you to Commoble for taking the time to document this:
 * https://gist.github.com/Commoble/ac7d7b57c9cbbfcae310c4ab110c3cc0
 */
public class DynamicVoidDimension extends Dimension
{
	public static final ChunkGeneratorType<VoidDimensionSettings, VoidChunkGenerator> VOID_CHUNK_GENERATOR = new ChunkGeneratorType<VoidDimensionSettings, VoidChunkGenerator>(VoidChunkGenerator::new, true, VoidDimensionSettings::new);
	public static final ChunkGeneratorType<EndGenerationSettings, FloatingIslandChunkGenerator> ISLAND_CHUNK_GENERATOR = new ChunkGeneratorType<EndGenerationSettings, FloatingIslandChunkGenerator>(FloatingIslandChunkGenerator::new, true, EndGenerationSettings::new);
	public static final String resourceLocation = EsotericaCraft.modid+":"+"dynamicvoiddimension";
	public static final BlockPos origin = new BlockPos(0, 130, 0);
	private static final Random rng = new Random();
	
	public boolean floatingIslands;
	
	public DynamicVoidDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn);
		floatingIslands = typeIn.getData().getBoolean(0);
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
		if (floatingIslands)
		{
			SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(Biomes.THE_VOID);
	        return ISLAND_CHUNK_GENERATOR.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), ISLAND_CHUNK_GENERATOR.createSettings());
		}
		else
		{
			SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(Biomes.THE_VOID);
	        return VOID_CHUNK_GENERATOR.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), VOID_CHUNK_GENERATOR.createSettings());
		}
	}
	
    @Nullable
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
        for (int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i) {
            for (int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j) {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null) {
                    return blockpos;
                }
            }
        }

        return null;
    }
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
    {
    	// look for safe spawn point
    	MutableBlockPos pos = new MutableBlockPos(origin);
    	while (world.isAirBlock(pos.down()))
    	{
    		pos.setPos(pos.getX(), pos.getY()-1, pos.getZ());
    	}
    	
    	// create bedrock block if none has been made already
    	boolean foundBedrock = false;
    	MutableBlockPos pos2 = new MutableBlockPos(pos.toImmutable());
    	while (!foundBedrock && pos2.getY() > 4)
    	{
    		pos2.setPos(pos2.getX(), pos2.getY()-1, pos2.getZ());
    		foundBedrock = world.getBlockState(pos2).getBlock() == Blocks.BEDROCK;
    	}
    	if (!foundBedrock)
    	{
        	this.world.setBlockState(pos.down(), Blocks.BEDROCK.getDefaultState());
        	this.world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), Blocks.BEDROCK.getDefaultState(), 3);
    	}
        return pos;
    }

    
	@Override
	public long getSeed()
	{
		rng.setSeed(stringToSeed(this.getDimension().getType().getRegistryName().getPath()));
		return rng.nextLong();
	}
	
	private static long stringToSeed(String s) {
	    if (s == null) {
	        return 0;
	    }
	    long hash = 0;
	    for (char c : s.toCharArray()) {
	        hash = 31L*hash + c;
	    }
	    return hash;
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) { return 0; }
	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		return new Vec3d(10/255d, 10/255d, 10/255d);
	}
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	@Override
	public boolean hasSkyLight()
	{
		return false;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	@Override
	public boolean doesXZShowFog(int x, int z) { return false; }

	public static DimensionType register(String id, Biome biome, boolean floatingIslands)
	{
		ResourceLocation rs = Registrar.location(id);
		if (DimensionType.byName(rs) != null)
		{
			EsotericaCraft.logger.info(String.format("The dimension '%s' is already registered!", id));
			return DimensionType.byName(rs);
		}
		
    	// format data
    	PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
    	buffer.writeBoolean(floatingIslands);
    	
    	// register dimension
        DimensionType dimension = DimensionManager.registerDimension(rs, Registrar.DYNAMIC_VOID_DIMENSION, buffer, false);
        return dimension;
	}
}