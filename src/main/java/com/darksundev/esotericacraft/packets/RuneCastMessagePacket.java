package com.darksundev.esotericacraft.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RuneCastMessagePacket
{
	public enum ParticleType
	{
	    FIRE(0), SMOKE(1), LAVA(2);
	
	    private int _value;
	    ParticleType(int Value)
	    {
	        this._value = Value;
	    }
	    public int getValue()
	    {
	            return _value;
	    }
	    public static ParticleType fromInt(int i)
	    {
	        for (ParticleType b : ParticleType .values())
	        {
	            if (b.getValue() == i) { return b; }
	        }
	        return null;
	    }
	    
	}
	
	private ParticleType particle;
	private BlockPos pos;
	
	public RuneCastMessagePacket(BlockPos pos, ParticleType particle)
	{
		this.pos = pos;
		this.particle = particle;
	}

	public void writeToBuffer(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeByte(particle.getValue());
	}
	public boolean spawnParticle(World w)
	{
		IParticleData type;
		switch (particle)
		{
			case FIRE:
				type = ParticleTypes.FLAME;
				break;
			case SMOKE:
				type = ParticleTypes.SMOKE;
				break;
			case LAVA:
				type = ParticleTypes.LAVA;
				break;
			default:
				return false;
		}
		for (int i = 0; i < 10; i++)
		{
			w.addParticle(type, pos.getX() + .5, pos.getY() + .51, pos.getZ() + .5, 0, .1, 0);
		}
		return true;
	}
	public ParticleType getParticleType()
	{
		return particle;
	}
	public BlockPos getPos()
	{
		return pos;
	}

	public static RuneCastMessagePacket fromBuffer(PacketBuffer buffer)
	{
		return new RuneCastMessagePacket(buffer.readBlockPos(), ParticleType.fromInt(buffer.readByte()));
	}
}
