package com.darksundev.esotericacraft.dimension;

import javax.annotation.Nullable;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Registrar;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.ChunkPos;
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
	public static final BlockPos origin = new BlockPos(0, 128, 0);
	
	private Biome biomeType;

	public DynamicDimension(World worldIn, DimensionType typeIn)
	{
		super(worldIn, typeIn);
		int biome = typeIn.getData().getInt(0);
		biomeType = Registry.BIOME.getByValue(biome);
		if (biomeType == null) biomeType = Biomes.ERODED_BADLANDS;
		EsotericaCraft.logger.info("Dimension Biome: " + biomeType.getRegistryName().toString());
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator()
	{
        //SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(biomeType);
        //return ChunkGeneratorType.SURFACE.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), ChunkGeneratorType.SURFACE.createSettings());
		
		SingleBiomeProviderSettings singlebiomeprovidersettings = BiomeProviderType.FIXED.createSettings().setBiome(biomeType);
        return VOID_CHUNK_GENERATOR.create(this.world, BiomeProviderType.FIXED.create(singlebiomeprovidersettings), VOID_CHUNK_GENERATOR.createSettings());
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
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) {
    	MutableBlockPos pos = new MutableBlockPos(origin);
    	while (!world.isAirBlock(pos))
    	{
    		pos.setPos(pos.getX(), pos.getY()+1, pos.getZ());
    	}
        return pos;
    }

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		return new Vec3d(10/255d, 10/255d, 10/255d);
	}
	@Override
	public boolean isSurfaceWorld() { return false; }
	@Override
	public boolean canRespawnHere() { return false; }
	@Override
	public boolean doesXZShowFog(int x, int z) { return false; }

	public static DimensionType register(String id, Biome biome)
	{
		ResourceLocation rs = Registrar.location(id);
		if (DimensionType.byName(rs) != null)
		{
			EsotericaCraft.logger.info(String.format("The dimension '%s' is already registered!", id));
			return DimensionType.byName(rs);
		}
		
    	// format data
    	PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
    	int data = Registry.BIOME.getId(biome);
    	buffer.writeInt(data);
    	
    	// register dimension
        DimensionType dimension = DimensionManager.registerDimension(rs, Registrar.DYNAMIC_DIMENSION, buffer, false);
        DynamicDimensionManager.notifyPlayersOfNewDimension(dimension);		// send new dimension packet to all players
        return dimension;
	}
}