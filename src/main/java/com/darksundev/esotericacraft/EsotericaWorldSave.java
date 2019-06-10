package com.darksundev.esotericacraft;

import org.apache.commons.lang3.SerializationUtils;

import com.darksundev.esotericacraft.lists.RuneList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class EsotericaWorldSave extends WorldSavedData
{
	public static final String DATA_NAME = EsotericaCraft.modid + "_WorldData";
	public static EsotericaWorldSave instance;
	
	public EsotericaWorldSave()
	{
		super(DATA_NAME);
	}
	public EsotericaWorldSave(String name)
	{
		super(name);
	}
	
	public static EsotericaWorldSave get(World world)
	{
		ServerWorld s = world.getServer().getWorld(DimensionType.OVERWORLD);
		DimensionSavedDataManager saveManager = s.getSavedData();
		
		if (instance == null)
		{
			instance = new EsotericaWorldSave();
		}
		
		saveManager.set(instance);
		return instance;
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		byte[] data = nbt.getByteArray("TeleportLinks");
		RuneList.teleportLinks = SerializationUtils.deserialize(data);
		
	}
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		byte[] data = SerializationUtils.serialize(RuneList.teleportLinks);
		compound.putByteArray("TeleportLinks", data);
		return compound;
	}

}
