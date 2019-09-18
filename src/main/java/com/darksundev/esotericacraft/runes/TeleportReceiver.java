package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;
import com.darksundev.esotericacraft.runes.TeleportLinkAdapter.TeleporterSide;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class TeleportReceiver extends TeleporterBase
{

	/*
	 * 	-: Ignored
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- M M M -
	 * 		M M O M M
	 * 		M O - O M
	 * 		M M O M M
	 * 		- M M M -
	 */
	public TeleportReceiver()
	{
		super("Teleport_Receiver", new Tier[][]{
			new Tier[]{Tier.NONE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.NONE},
			new Tier[]{Tier.MUNDANE,Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.MUNDANE},
			new Tier[]{Tier.MUNDANE,Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.MUNDANE},
			new Tier[]{Tier.MUNDANE,Tier.MUNDANE,	Tier.ENCHANTED,	Tier.MUNDANE,	Tier.MUNDANE},
			new Tier[]{Tier.NONE, 	Tier.MUNDANE,	Tier.MUNDANE,	Tier.MUNDANE,	Tier.NONE}
		});
	}

	@Override
	protected TeleporterSide getThisSide(TeleportLinkAdapter link) {
		return link.receiver;
	}

	@Override
	protected void setThisSide(TeleportLinkAdapter link, TeleporterSide side) {
		link.receiver = side;
	}

	@Override
	protected TeleporterSide getOtherSide(TeleportLinkAdapter link) {
		return link.transmitter;
	}

	@Override
	protected void setOtherSide(TeleportLinkAdapter link, TeleporterSide side) {
		link.transmitter = side;
	}

	@Override
	protected TeleportLinkAdapter makeNewLink(String key, BlockPos firstLink, DimensionType dimension) {
		return new TeleportLinkAdapter(key, null, null, firstLink, dimension);
	}

}
