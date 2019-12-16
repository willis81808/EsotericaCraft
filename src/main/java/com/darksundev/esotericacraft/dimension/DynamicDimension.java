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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraftforge.common.DimensionManager;

/*
 * A huge thank you to Commoble for taking the time to document this:
 * https://gist.github.com/Commoble/ac7d7b57c9cbbfcae310c4ab110c3cc0
 */
public class DynamicDimension extends Dimension
{
	public static final ChunkGeneratorType<VoidDimensionSettings, VoidChunkGenerator> VOID_CHUNK_GENERATOR = new ChunkGeneratorType<VoidDimensionSettings, VoidChunkGenerator>(VoidChunkGenerator::new, true, VoidDimensionSettings::new);
	public static final String resourceLocation = EsotericaCraft.modid+":"+"dynamicdimension";
	public static final BlockPos origin = new BlockPos(0, 130, 0);
	private static final Random rng = new Random();
	
	private Biome biomeType;
	
	@SuppressWarnings("deprecation")
	public DynamicDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn);
		int biome = typeIn.getData().getInt(0);
		
		biomeType = Registry.BIOME.getByValue(biome);
		EsotericaCraft.logger.info("Dimension Biome: " + biomeType.getRegistryName().toString());
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
        //SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(biomeType);
        //return ChunkGeneratorType.SURFACE.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), ChunkGeneratorType.SURFACE.createSettings());
		
		if (biomeType == Biomes.THE_VOID)
		{
			SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(biomeType);
	        return VOID_CHUNK_GENERATOR.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), VOID_CHUNK_GENERATOR.createSettings());
		}
		else
		{
			SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(biomeType);
			return ChunkGeneratorType.SURFACE.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), ChunkGeneratorType.SURFACE.createSettings());
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
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		if (this.biomeType == Biomes.THE_VOID)
		{
			return 0;
		}
		else
		{
		    double d0 = MathHelper.frac((double)worldTime / 24000.0D - 0.25D);
		    double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
		    return (float)(d0 * 2.0D + d1) / 3.0F;
		}
	}
	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		if (this.biomeType == Biomes.THE_VOID)
		{
			return new Vec3d(10/255d, 10/255d, 10/255d);
		}
		else
		{
			float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
			f = MathHelper.clamp(f, 0.0F, 1.0F);
			float f1 = 1.0F;
			float f2 = 0.84705883F;
			float f3 = 0.7529412F;
			f1 = f1 * (f * 0.91F + 0.09F);
			f2 = f2 * (f * 0.94F + 0.06F);
			f3 = f3 * (f * 0.94F + 0.06F);
			return new Vec3d((double)f1, (double)f2, (double)f3);
		}
	}
	@Override
	public boolean isSurfaceWorld()
	{
		return biomeType != Biomes.THE_VOID;
	}
	@Override
	public boolean hasSkyLight()
	{
		return biomeType != Biomes.THE_VOID;
	}

	@Override
	public boolean canRespawnHere()
	{
		//return this.biomeType != Biomes.THE_VOID;
		return false;
	}
	@Override
	public boolean doesXZShowFog(int x, int z) { return false; }

	@SuppressWarnings("deprecation")
	public static DimensionType register(String id, Biome biome)
	{
		ResourceLocation rs = Registrar.location(id);
		if (DimensionType.byName(rs) != null)
		{
			EsotericaCraft.logger.info(String.format("The dimension '%s' is already registered!", id));
			return DimensionType.byName(rs);
		}
		
    	// format data
		int data = Registry.BIOME.getId(biome);
    	PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
    	buffer.writeInt(data);
    	
    	// register dimension
        DimensionType dimension = DimensionManager.registerDimension(rs, Registrar.DYNAMIC_DIMENSION, buffer, false);
        return dimension;
	}
}