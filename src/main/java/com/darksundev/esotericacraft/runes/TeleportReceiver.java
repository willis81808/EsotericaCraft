package com.darksundev.esotericacraft.runes;

import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.util.math.BlockPos;

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
	protected long getThisSide(TeleportLink link) { return link.receiver; }
	@Override
	protected void setThisSide(TeleportLink link, long value) { link.receiver = value; }
	@Override
	protected long getOtherSide(TeleportLink link) { return link.transmitter; }
	@Override
	protected void setOtherSide(TeleportLink link, long value) { link.transmitter = value;}

	@Override
	protected TeleportLink makeNewLink(String key, BlockPos firstLink) {
		return new TeleportLink(key, null, firstLink);
	}

}
