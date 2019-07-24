package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.util.math.BlockPos;

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
	protected long getThisSide(TeleportLink link) { return link.transmitter; }
	@Override
	protected void setThisSide(TeleportLink link, long value) { link.transmitter = value; }
	@Override
	protected long getOtherSide(TeleportLink link) { return link.receiver; }
	@Override
	protected void setOtherSide(TeleportLink link, long value) { link.receiver = value;}

	@Override
	protected TeleportLink makeNewLink(String key, BlockPos firstLink) {
		return new TeleportLink(key, firstLink, null);
	}

}
