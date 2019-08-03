package com.darksundev.esotericacraft.blocks;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EsotericaCraft.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StonePath extends CarpetBlock
{
	public static final float SPEED_FACTOR = 1.1f;
	public static final float DEFAULT_SPEED_MAGNITUDE = .3f;
	
	public StonePath()
	{
		super
		(
			DyeColor.WHITE,
			Block.Properties.create(Material.IRON)
				.hardnessAndResistance(1, 10)
				.sound(SoundType.STONE)
		);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		// exit if we aren't colliding with the player
		if (!(entityIn instanceof PlayerEntity))
			return;
		
		// get player values
		PlayerEntity player = (PlayerEntity)entityIn;
		
		//NewPlayerSpeed(player);
		OldPlayerSpeed(player);
	}
	private void OldPlayerSpeed(PlayerEntity player)
	{
		double x = player.getMotion().x;
		double z = player.getMotion().z;
		if (player.getMotion().y > 0)
		{
			double magnitude = Math.sqrt(x*x + z*z);
			
			// exit if already moving at default speed or less
			if (magnitude <= DEFAULT_SPEED_MAGNITUDE)
				return;
			
			x = x * DEFAULT_SPEED_MAGNITUDE / magnitude;
			z = z * DEFAULT_SPEED_MAGNITUDE / magnitude;
		}
		else
		{
			// apply speed factor to player motion
			x *= SPEED_FACTOR;
			z *= SPEED_FACTOR;
		}
		player.setMotion(x, player.getMotion().y, z);
	}
	@SuppressWarnings("unused")
	private void NewPlayerSpeed(PlayerEntity player)
	{
		Vec3d motion 		= player.getMotion();
		double speed		= Utils.vecMagnitude(motion);
		double x = player.chasingPosX - player.prevChasingPosX;
		double z = player.chasingPosZ - player.prevChasingPosZ;
		if (.05 < Math.sqrt(x*x + z*z))
		{
			// calculate new motion
			double scalar = Utils.lerp
			(
				speed,
				DEFAULT_SPEED_MAGNITUDE*SPEED_FACTOR,
				0.5
			);
			// apply new motion
			player.setMotion(motion.normalize().scale(scalar));
		}
	}
}