package com.darksundev.esotericacraft.commands;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.dimension.DynamicDimension;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class DimensionCommand
{
	private static final ITextComponent returnedText = new StringTextComponent("Returned to overworld");
	private static final ITextComponent doNothingText = new StringTextComponent("Already in overworld...");
	private static final ITextComponent errorText = new StringTextComponent("Error!").applyTextStyle(TextFormatting.RED);
	
	private static BlockPos overworldPos = BlockPos.ZERO;
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("dimension")
			.requires((cs) -> {
				return cs.hasPermissionLevel(2);
			})
			.executes(cs -> {
				return returnToOverworld(cs.getSource());
			})
			.then(Commands.argument("id", StringArgumentType.string()).executes(cs -> {
				return createDimension(cs.getSource(), StringArgumentType.getString(cs, "id"));
			})));
	}
	
	private static int createDimension(CommandSource source, String dimensionId)
	{
		try
		{
			//Biome b = Biomes.THE_VOID;
			Biome b = Biomes.THE_VOID;
			switch (EsotericaCraft.rng.nextInt(6))
			{
			case 0:
				b = Biomes.DESERT_HILLS;
				break;
			case 1:
				b = Biomes.GRAVELLY_MOUNTAINS;
				break;
			case 2:
				b = Biomes.BAMBOO_JUNGLE;
				break;
			case 3:
				b = Biomes.BADLANDS;
				break;
			case 4:
				b = Biomes.FOREST;
				break;
			}
			
			ServerPlayerEntity player = source.asPlayer();
			DimensionType dimension = DynamicDimension.register(dimensionId, b, false);
			
			if (source.getWorld().dimension.getType() == DimensionType.OVERWORLD)
				overworldPos = player.getPosition();
			
			ServerWorld dimensionWorld = source.getServer().getWorld(dimension);
			BlockPos teleportDestination = dimensionWorld.dimension.findSpawn(0, 0, false);
			player.teleport(dimensionWorld, teleportDestination.getX()+0.5, teleportDestination.getY(), teleportDestination.getZ()+0.5, player.rotationYaw, player.rotationPitch);
		}
		catch (CommandSyntaxException e)
		{
			e.printStackTrace();
			source.sendFeedback(errorText, true);
		}
		return 1;
	}
	private static int returnToOverworld(CommandSource source)
	{
		try
		{
			ServerPlayerEntity player = source.asPlayer();
			if (player.dimension != DimensionType.OVERWORLD)
			{
				source.asPlayer().teleport(
						source.getServer().getWorld(DimensionType.OVERWORLD),
						overworldPos.getX(),
						overworldPos.getY(),
						overworldPos.getZ(),
						player.rotationYaw,
						player.rotationPitch);

				source.sendFeedback(returnedText, true);
			}
			else
			{
				source.sendFeedback(doNothingText, true);
			}

		}
		catch (CommandSyntaxException e)
		{
			e.printStackTrace();
			source.sendFeedback(errorText, true);
		}
		return 1;
	}
}
