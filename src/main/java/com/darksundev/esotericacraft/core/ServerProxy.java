package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;
import com.darksundev.esotericacraft.packets.PlayerInventoryMessagePacket;
import com.darksundev.esotericacraft.packets.RuneCastMessagePacket;
import com.darksundev.esotericacraft.packets.DimensionDataPacket;

import net.minecraftforge.common.DimensionManager;

public class ServerProxy implements IProxy
{

	@Override
	public void init()
	{
		// register dimension created event packet
		EsotericaCraftPacketHandler.registerConsumer(DimensionDataPacket.class,
				// encoder
				(msg, buffer) ->
				{
					msg.encode(buffer);
				},
				// decoder
				(buffer) ->
				{ 
					return DimensionDataPacket.decode(buffer);
				},
				// consumer
				(msg, context) -> {
					// do nothing
				});
		
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
				});
	}

}
