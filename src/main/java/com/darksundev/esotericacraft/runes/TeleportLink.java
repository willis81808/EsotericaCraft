package com.darksundev.esotericacraft.runes;

import java.io.Serializable;

import net.minecraft.util.math.BlockPos;

public class TeleportLink implements Serializable
{
	private static final long serialVersionUID = 7062332256548713629L;
	
	public String id;
	public Long transmitter, receiver;	// should be converted to/from BlockPos

	public TeleportLink(String id, BlockPos transmitter, BlockPos receiver)
	{
		this.id = id;
		this.transmitter = transmitter!=null ? transmitter.toLong() : -1;
		this.receiver = receiver!=null ? receiver.toLong() : -1;
	}
	public TeleportLink(String id, Long transmitter, Long receiver)
	{
		this.id = id;
		this.transmitter = transmitter;
		this.receiver = receiver;
	}
}
