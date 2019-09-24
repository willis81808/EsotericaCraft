package com.darksundev.esotericacraft.dimension;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.packets.DimensionDataPacket;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = EsotericaCraft.modid)
public class DynamicDimensionManager
{
	@SubscribeEvent
	public static void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (event.getPlayer().world.isRemote)
			return;
		
		// send packet for each dimension
		for (DimensionType d : DimensionType.getAll()) {
			EsotericaCraft.logger.info(String.format("Sending dimension '%d' as a packet to client", d.getId()));
			if (d.getId() > 1)
				EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new DimensionDataPacket(d));
		}
	}
	
	public static void notifyPlayersOfNewDimension(DimensionType dimension)
	{
		EsotericaCraftPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new DimensionDataPacket(dimension));
	}
}
