package com.darksundev.esotericacraft.commands;

import java.util.HashMap;
import java.util.UUID;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class BackCommand
{
	private static class DeathLocation
	{
		public DimensionType dimension;
		public BlockPos position;
		public DeathLocation(DimensionType dimension, BlockPos position)
		{
			this.dimension = dimension;
			this.position = position;
		}
	}
	private static HashMap<UUID, DeathLocation> mappings = new HashMap<UUID, DeathLocation>();
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("back")
				.executes((cs) ->
				{
					CommandSource source = cs.getSource();
					return executeCommand(source, source.asPlayer());
				}));
	}
	
	private static int executeCommand(CommandSource source, ServerPlayerEntity sender)
	{
		UUID id = sender.getUniqueID();
		if (mappings.containsKey(id))
		{
			DeathLocation data = mappings.get(id);
			teleport(data.position, source.getServer().getWorld(data.dimension), sender);
			mappings.remove(id);
			source.sendFeedback(new StringTextComponent("Returned to death location.").applyTextStyle(TextFormatting.GREEN), false);
		}
		else
		{
			source.sendFeedback(new StringTextComponent("Either no death location was found, or you've already used this command to return there once.").applyTextStyle(TextFormatting.RED), false);
		}
		return 1;
	}
	
	@SubscribeEvent
	public static void playerDeath(LivingDeathEvent event)
	{
		Entity entity = event.getEntity();
		if (!entity.world.isRemote() && entity instanceof PlayerEntity)
		{
			UUID id = entity.getUniqueID();
			if (mappings.containsKey(id))
				mappings.remove(id);
			mappings.put(id, new DeathLocation(entity.dimension, entity.getPosition()));
		}
	}
	
	private static void teleport(BlockPos to, World world, PlayerEntity player)
	{
		// preload destination chunk
		final ChunkPos chunkpos = new ChunkPos(to);
	    ((ServerWorld)world).getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
	    // teleport player
	    ((ServerPlayerEntity)player).teleport((ServerWorld)world, to.getX()+.5, to.getY()+.5, to.getZ()+.5, player.rotationYaw, player.prevRotationPitch);

	    player.giveExperiencePoints(0);
	}
}
