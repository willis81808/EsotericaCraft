package com.darksundev.esotericacraft.core;

import com.darksundev.esotericacraft.EsotericaCraftPacketHandler;

public class ServerProxy implements IProxy
{

	@Override
	public void init()
	{
		EsotericaCraftPacketHandler.registerConsumer((msg, ctx) -> {});
	}

}
