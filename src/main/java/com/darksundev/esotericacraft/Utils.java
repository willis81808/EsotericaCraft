package com.darksundev.esotericacraft;

import com.mojang.brigadier.Message;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;

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
		// construct message
		return TextComponentUtils.toTextComponent(new Message() {
			@Override
			public String getString() { return string; }
		});
	}
}
