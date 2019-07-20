package com.darksundev.esotericacraft;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EsotericaCraftPacketHandler
{
	private static final String PROTOCOL_VERSION = "1.1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(EsotericaCraft.modid, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	private static int packetID = 0;
	
	public static void registerConsumer(BiConsumer<RuneCastMessagePacket, Supplier<Context>> messageConsumer)
	{
		INSTANCE.registerMessage(packetID++, RuneCastMessagePacket.class, EsotericaCraftPacketHandler::messageEncoder, EsotericaCraftPacketHandler::messageDecoder, messageConsumer);
	}
	
	private static void messageEncoder(RuneCastMessagePacket msg, PacketBuffer buffer)
	{
		msg.writeToBuffer(buffer);
	}
	private static RuneCastMessagePacket messageDecoder(PacketBuffer buffer)
	{
		return RuneCastMessagePacket.fromBuffer(buffer);
	}

}
