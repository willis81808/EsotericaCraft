package com.darksundev.esotericacraft.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber(modid = EsotericaCraft.modid)
public class PlayerLoginManager
{
	public static class UserProfile implements Serializable
	{
		private static final long serialVersionUID = 9073653257361832232L;
		private String nickname;
		private String password;
		private Long loginPosition;
		private boolean isOperator;
		
		// nickname getter & setter
		public UserProfile setNickname(String nickname)
		{
			this.nickname = nickname;
			return this;
		}
		public String getNickname()
		{
			return nickname;
		}
	
		// password getter & setter
		public UserProfile setPassword(String password)
		{
			this.password = password;
			return this;
		}
		public String getPassword()
		{
			return password;
		}
		
		// login position getter & setter
		public UserProfile setLoginPosition(BlockPos loginPosition)
		{
			this.loginPosition = loginPosition.toLong();
			return this;
		}
		public Long getLoginPosition()
		{
			return loginPosition;
		}
	}
	
	public static boolean isSingleplayer = false;
	
	// all online users
	private static ArrayList<PlayerEntity> activeUsers = new ArrayList<PlayerEntity>();
	// logged in users
	private static HashSet<String> authenticatedUsers = new HashSet<String>();
	// all user profile data
	public static HashMap<String, UserProfile> accounts = new HashMap<String, UserProfile>();

	private static void displayAuthenticationErrorMessage(PlayerEntity player)
	{
		EsotericaCraft.messagePlayer(player,
				TextFormatting.RED + "You must login first!");
	}
	
	public static UserProfile getProfile(PlayerEntity player)
	{
		return accounts.get(player.getCachedUniqueIdString());
	}

	public static boolean authenticateUser(PlayerEntity player, String password)
	{
		String uuid = player.getCachedUniqueIdString();
		UserProfile profile = accounts.get(uuid);
		boolean success = profile.getPassword().equals(password);
		if (success && !authenticatedUsers.contains(uuid))
		{
			authenticatedUsers.add(uuid);
			if (profile.isOperator)
			{
				MinecraftServer server = player.getServer();
				server.getCommandManager().handleCommand(server.getCommandSource(), "/op " + player.getName().getString());
			}
			
			// remove invulnerability
			player.setInvulnerable(false);
		}
		return success;
	}

	/*
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event)
	{
		for (PlayerEntity p : activeUsers)
		{
			if (!authenticatedUsers.contains(p.getCachedUniqueIdString()))
			{
				BlockPos pos = BlockPos.fromLong(accounts.get(p.getCachedUniqueIdString()).getLoginPosition());
				p.setPositionAndUpdate(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
			}
		}
	}
	
	@SubscribeEvent
	public static void onUserInteract(PlayerInteractEvent event)
	{
		if (event.getWorld().isRemote || isSingleplayer)
			return;
		
		if (!authenticatedUsers.contains(event.getEntityPlayer().getCachedUniqueIdString()) && event.isCancelable())
		{
			event.setCanceled(true);
			displayAuthenticationErrorMessage(event.getEntityPlayer());
		}
	}
	@SubscribeEvent
	public static void onUserInteract(BreakEvent event)
	{
		if (event.getWorld().isRemote() || isSingleplayer)
			return;
		
		if (!authenticatedUsers.contains(event.getPlayer().getCachedUniqueIdString()) && event.isCancelable())
		{
			event.setCanceled(true);
			displayAuthenticationErrorMessage(event.getPlayer());
		}
	}
	@SubscribeEvent
	public static void onUserInteract(EntityPlaceEvent event)
	{
		if (event.getWorld().isRemote() || isSingleplayer)
			return;
		
		if (event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (!authenticatedUsers.contains(player.getCachedUniqueIdString()) && event.isCancelable())
			{
				event.setCanceled(true);
				displayAuthenticationErrorMessage(player);
			}
		}
	}

	@SubscribeEvent
	public static void onUserLogin(PlayerLoggedInEvent event)
	{
		if (isSingleplayer)
			return;
		
		// record login
		PlayerEntity player = event.getPlayer();
		if (!activeUsers.contains(player))
			activeUsers.add(player);
		// make player invulnerable until they're authenticated
		player.setInvulnerable(true);

		// find user profile
		String uuid = player.getCachedUniqueIdString();
		UserProfile profile = accounts.get(uuid);
		if (profile == null && !accounts.containsKey(uuid))
		{
			// no profile found for this user!
			// create one
			profile = new PlayerLoginManager.UserProfile()
					.setNickname(player.getName().getString())
					.setPassword("password");
			
			// register account
			accounts.put(uuid, profile);
			
			// welcome player, and prompt them to add a password
			EsotericaCraft.messagePlayer(player, 
					TextFormatting.GREEN + "Welcome to EsotericaCraft, " +
					TextFormatting.RESET + player.getDisplayName().getFormattedText() + "! (Your default password is " + TextFormatting.GRAY + "'password')\n" +
					TextFormatting.RESET + "For more information, say: " +
					TextFormatting.RED + "/login help");
		}
		else
		{
			if (authenticatedUsers.contains(uuid))
				authenticatedUsers.remove(uuid);

			// welcome player, and prompt them to add a password
			EsotericaCraft.messagePlayer(player, 
					TextFormatting.GREEN + "Welcome back to EsotericaCraft, " +
					TextFormatting.RESET +  player.getDisplayName().getFormattedText() + "!\n"+
					"Please login to continue... For more information, say: " +
					TextFormatting.RED + "/login help");
		}
		
		profile.setLoginPosition(player.getPosition());
		
		// de op player (if they are normally an operator)
		MinecraftServer server = player.getServer();
		if (server.getOpPermissionLevel() == server.getPermissionLevel(server.getPlayerProfileCache().getProfileByUUID(player.getUniqueID())))
		{
			profile.isOperator = true;
			server.getCommandManager().handleCommand(server.getCommandSource(), "/deop " + player.getName().getString());
		}
		
	}
	@SubscribeEvent
	public static void onUserLogout(PlayerLoggedOutEvent event)
	{
		// de-authenticate user
		authenticatedUsers.remove(event.getPlayer().getCachedUniqueIdString());
		activeUsers.remove(event.getPlayer());
	}
	
	*/
}
