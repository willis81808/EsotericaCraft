package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.darksundev.esotericacraft.packets.RuneCastMessagePacket;

public class ServerProxy implements IProxy
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
					// do nothing
					context.get().setPacketHandled(true);
				});

		// openinv command data packet
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
					// do nothing
					context.get().setPacketHandled(true);
				});
	}

}
