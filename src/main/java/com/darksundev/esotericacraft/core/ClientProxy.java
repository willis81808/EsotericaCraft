package com.darksundev.esotericacraft.core;

import java.util.function.Supplier;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.RuneCastMessagePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientProxy implements IProxy
{

	@Override
	public void init()
	{
		// initialize network packet consumer
		EsotericaCraftPacketHandler.registerConsumer(ClientProxy::messageConsumer);
	}

	static void messageConsumer(RuneCastMessagePacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			World w = Minecraft.getInstance().world;
			msg.spawnParticle(w);
		});
	}
}
