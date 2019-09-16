package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.runes.TeleportLinkAdapter.TeleporterSide;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class TeleportTransmitter extends TeleporterBase
{

	/*
	 * 	-: Ignored
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- M O M -
	 * 		M M M M M
	 * 		O M - M O
	 * 		M M M M M
	 * 		- M O M -
	 */
	public TeleportTransmitter()
	{
		super("Teleport_Transmitter", new Tier[][]{
			new Tier[]{Tier.NONE, 		Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE},
			new Tier[]{Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE},
			new Tier[]{Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE,		Tier.MUNDANE,	Tier.ENCHANTED},
			new Tier[]{Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE},
			new Tier[]{Tier.NONE, 		Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.NONE}
		});
	}

	@Override
	protected TeleporterSide getThisSide(TeleportLinkAdapter link) {
		return link.transmitter;
	}

	@Override
	protected void setThisSide(TeleportLinkAdapter link, BlockPos value, DimensionType dimension) {
		link.transmitter = new TeleporterSide(value.toLong(), dimension.getId());
	}

	@Override
	protected TeleporterSide getOtherSide(TeleportLinkAdapter link) {
		return link.receiver;
	}

	@Override
	protected void setOtherSide(TeleportLinkAdapter link, BlockPos value, DimensionType dimension) {
		link.receiver = new TeleporterSide(value.toLong(), dimension.getId());
	}

	@Override
	protected TeleportLinkAdapter makeNewLinkAdapter(String key, BlockPos firstLink, DimensionType dimension) {
		return new TeleportLinkAdapter(key, firstLink, dimension, null, null);
	}

}
