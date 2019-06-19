package com.darksundev.esotericacraft;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EsotericaCraftPacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(EsotericaCraft.modid, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	private static int packetID = 0;
	
	public static void register()
	{
		INSTANCE.registerMessage(packetID++, RuneCastMessagePacket.class, EsotericaCraftPacketHandler::messageEncoder, EsotericaCraftPacketHandler::messageDecoder, EsotericaCraftPacketHandler::messageConsumer);
	}
	
	
	private static void messageEncoder(RuneCastMessagePacket msg, PacketBuffer buffer)
	{
		msg.writeToBuffer(buffer);
	}
	private static RuneCastMessagePacket messageDecoder(PacketBuffer buffer)
	{
		return RuneCastMessagePacket.fromBuffer(buffer);
	}
	private static void messageConsumer(RuneCastMessagePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			World w = Minecraft.getInstance().world;
			msg.spawnParticle(w);
		});
	}
}
