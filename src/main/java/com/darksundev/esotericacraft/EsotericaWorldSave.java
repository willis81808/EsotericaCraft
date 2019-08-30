package com.darksundev.esotericacraft;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import com.darksundev.esotericacraft.lists.RuneList;
import com.darksundev.esotericacraft.plugins.PlayerLoginManager;
import com.darksundev.esotericacraft.plugins.PlayerLoginManager.UserProfile;
import com.darksundev.esotericacraft.runes.TeleportLink;

import net.minecraftforge.fml.loading.FMLPaths;

public class EsotericaWorldSave
{
	public static final String DATA_NAME = EsotericaCraft.modid + "_WorldData";
	public static EsotericaWorldSave instance;
	private static Path teleportLinksFilePath, userProfileFilePath;

	public EsotericaWorldSave()
	{
		if (instance == null)
		{
			instance = this;
			teleportLinksFilePath = Paths.get(FMLPaths.GAMEDIR.get().toString(), "teleportLinks.txt");
			userProfileFilePath = Paths.get(FMLPaths.GAMEDIR.get().toString(), "userProfiles.txt");
			restoreData();
		}
		else
		{
			EsotericaCraft.logger.error("EsotericaWorldSave already instantiated!!");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void restoreData()
	{
		HashMap<String, TeleportLink> teleportLinks = (HashMap<String, TeleportLink>)Deserialize(teleportLinksFilePath);
		if (!(teleportLinks == null || teleportLinks.isEmpty()))
		{
			RuneList.teleportLinks = teleportLinks;
		}
		HashMap<String, UserProfile> accounts = (HashMap<String, UserProfile>)Deserialize(userProfileFilePath);
		if (!(accounts == null || accounts.isEmpty()))
		{
			PlayerLoginManager.accounts = accounts;
		}
	}
	public static void backupData()
	{
		Serialize(RuneList.teleportLinks, teleportLinksFilePath);
		Serialize(PlayerLoginManager.accounts, userProfileFilePath);
	}
	
	private static void Serialize(Serializable data, Path filePath)
	{
		byte[] serializedData = SerializationUtils.serialize(data);
		try
		{
			FileUtils.writeByteArrayToFile(new File(filePath.toString()), serializedData);
		}
		catch (IOException e)
		{
			EsotericaCraft.logger.error("Failed to serialize data!!");
			e.printStackTrace();
		}
	}
	private static Object Deserialize(Path filePath)
	{
		try
		{
			byte[] data = FileUtils.readFileToByteArray(new File(filePath.toString()));
		    ByteArrayInputStream in = new ByteArrayInputStream(data);
		    ObjectInputStream is = new ObjectInputStream(in);
		    return is.readObject();
		}
		catch (IOException e)
		{
			EsotericaCraft.logger.error("Failed to deserialize data!!");
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			EsotericaCraft.logger.error("Failed to deserialize data!!");
			e.printStackTrace();
			return null;
		}
	}
}
