package com.darksundev.esotericacraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.runes.RuneManager;
import com.darksundev.esotericacraft.runes.TeleportLink;

import net.minecraftforge.fml.loading.FMLPaths;

public class EsotericaWorldSave
{
	public static final String DATA_NAME = EsotericaCraft.modid + "_WorldData";
	public static EsotericaWorldSave instance;
	private static Path saveFilePath;

	public EsotericaWorldSave()
	{
		if (instance == null)
		{
			instance = this;
			saveFilePath = Paths.get(FMLPaths.GAMEDIR.get().toString(), "teleportLinks.txt");
			restoreData();
		}
		else
		{
			EsotericaCraft.logger.error("EsotericaWorldSave already instantiated!!");
		}
	}
	
	public static void restoreData()
	{
		HashMap<String, TeleportLink> teleportLinks = Deserialize();
		if (!(teleportLinks == null || teleportLinks.isEmpty()))
		{
			RuneList.teleportLinks = teleportLinks;
		}
	}
	public static void backupData()
	{
		Serialize(RuneList.teleportLinks);
	}
	
	private static void Serialize(HashMap<String, TeleportLink> data)
	{
		byte[] serializedData = SerializationUtils.serialize(data);
		try
		{
			FileUtils.writeByteArrayToFile(new File(saveFilePath.toString()), serializedData);
		}
		catch (IOException e)
		{
			EsotericaCraft.logger.error("Failed to serialize data!!");
			e.printStackTrace();
		}
	}
	private static HashMap<String, TeleportLink> Deserialize()
	{
		try
		{
			byte[] data = FileUtils.readFileToByteArray(new File(saveFilePath.toString()));
			return deserialize(data);
		}
		catch (IOException e)
		{
			EsotericaCraft.logger.error("Failed to deserialize data!!");
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private static byte[] serialize(Object obj) throws IOException
	{
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	private static HashMap<String, TeleportLink> deserialize(byte[] data)
	{
		try
		{
		    ByteArrayInputStream in = new ByteArrayInputStream(data);
		    ObjectInputStream is = new ObjectInputStream(in);
		    return (HashMap<String, TeleportLink>)is.readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
