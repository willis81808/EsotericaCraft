package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.darksundev.esotericacraft.packets.RuneCastMessagePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientProxy implements IProxy
{

	@Override
	public void init()
	{
		// register rune particle packet
		EsotericaCraftPacketHandler.registerConsumer(RuneCastMessagePacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.writeToBuffer(buffer);
				},
				// decoder
				(buffer) ->
				{ 
					return RuneCastMessagePacket.fromBuffer(buffer);
				},
				// consumer
				(msg, context) -> {
					context.get().enqueueWork(() ->
					{
						World w = Minecraft.getInstance().world;
						msg.spawnParticle(w);
					});
				});

		// reset inventory packet received
		EsotericaCraftPacketHandler.registerConsumer(PlayerInventoryMessagePacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.writeToBuffer(buffer);
				},
				// decoder
				(buffer) ->
				{
					return PlayerInventoryMessagePacket.fromBuffer(buffer);
				},
				// consumer
				(msg, context) ->
				{
					context.get().enqueueWork(() ->
					{
						Minecraft.getInstance().player.container.setAll(msg.items);
					});
				});
	}
}
