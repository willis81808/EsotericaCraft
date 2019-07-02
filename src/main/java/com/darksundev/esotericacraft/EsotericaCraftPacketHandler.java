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
	    (x) -> true,
	    (x) -> true
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

	/*
	public static void registerClient()
	{
		INSTANCE.registerMessage(packetID++, RuneCastMessagePacket.class, EsotericaCraftPacketHandler::messageEncoder, EsotericaCraftPacketHandler::messageDecoder, EsotericaCraftPacketHandler::messageConsumer);
	}
	public static void registerServer()
	{
		INSTANCE.registerMessage(packetID++, RuneCastMessagePacket.class, EsotericaCraftPacketHandler::messageEncoder, EsotericaCraftPacketHandler::messageDecoder, (msg, ctx) -> {});
	}
	@OnlyIn(Dist.CLIENT)
	private static void messageConsumer(RuneCastMessagePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			World w = Minecraft.getInstance().world;
			msg.spawnParticle(w);
		});
	}
	*/	
}
