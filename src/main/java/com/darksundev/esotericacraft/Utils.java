package com.darksundev.esotericacraft;

import net.minecraft.util.math.Vec3d;

public class Utils
{
	public static double lerp(double a, double b, double f) 
	{
	    return (a * (1.0 - f)) + (b * f);
	}
	public static double vecMagnitude(Vec3d vector)
	{
		return Math.sqrt(vector.x*vector.x + vector.y*vector.y + vector.z*vector.z);
	}
}
