package com.darksundev.esotericacraft.runes;

import java.io.Serializable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class TeleportLinkAdapter implements Serializable
{
	private static final long serialVersionUID = 1457065878488258892L;
	public String id;
	public TeleporterSide transmitter, receiver;

	public TeleportLinkAdapter(String id, BlockPos transmitter, DimensionType dimension1, BlockPos receiver, DimensionType dimension2)
	{
		this.id = id;
		this.transmitter = new TeleporterSide(transmitter == null ? -1 : transmitter.toLong(), dimension1 == null ? -2 : dimension1.getId());
		this.receiver = new TeleporterSide(receiver == null ? -1 : receiver.toLong(), dimension2 == null ? -2 : dimension2.getId());
	}
	public TeleportLinkAdapter(String id, TeleporterSide transmitter, TeleporterSide receiver)
	{
		this.id = id;
		this.transmitter = transmitter;
		this.receiver = receiver;
	}
	
	public static class TeleporterSide implements Serializable
	{
		private static final long serialVersionUID = -2010218567546858484L;
		public Long position;
		//public String dimension;
		public int dimension;
		
		public TeleporterSide(Long position, int dimension)
		{
			this.position = position;
			this.dimension = dimension;
		}
	}
}
