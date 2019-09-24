package com.darksundev.esotericacraft.packets;

import io.netty.util.IntSupplier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistries;

public class DimensionDataPacket implements IntSupplier
{
    public final int id;
    public final ResourceLocation name;
    public final ModDimension dimension;
    public final boolean skyLight;

    private int loginIndex;

    public DimensionDataPacket(DimensionType dimensionType) {
        this.id = dimensionType.getId();
        this.name = DimensionType.getKey(dimensionType);
        this.dimension = dimensionType.getModType();
        this.skyLight = dimensionType.func_218272_d();
    }

    DimensionDataPacket(int id, ResourceLocation name, ModDimension dimension, boolean skyLight) {
        this.id = id;
        this.name = name;
        this.dimension = dimension;
        this.skyLight = skyLight;
    }

    void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }

    int getLoginIndex() {
    	return this.loginIndex;
    }
    
	@Override
	public int get() throws Exception {
		return this.loginIndex;
	}

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.id);
        buffer.writeResourceLocation(this.name);
        buffer.writeResourceLocation(this.dimension.getRegistryName());
        buffer.writeBoolean(this.skyLight);
    }

    public static DimensionDataPacket decode(PacketBuffer buffer) {
        int id = buffer.readInt();
        ResourceLocation name = buffer.readResourceLocation();
        ModDimension dimension = ForgeRegistries.MOD_DIMENSIONS.getValue(buffer.readResourceLocation());
        boolean skyLight = buffer.readBoolean();

        return new DimensionDataPacket(id, name, dimension, skyLight);
    }
 }