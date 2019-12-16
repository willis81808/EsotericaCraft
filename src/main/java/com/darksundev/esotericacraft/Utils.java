package com.darksundev.esotericacraft;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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
	public static ITextComponent textComponentFromString(String string)
	{
		return new StringTextComponent(string);
	}
	public static Object pickRandom(Object...objects)
	{
		return objects[EsotericaCraft.rng.nextInt(objects.length)];
	}
}
