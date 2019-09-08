package com.darksundev.esotericacraft.blocks;

import java.util.Random;

import com.darksundev.esotericacraft.plugins.OfferingManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SacrificialFire extends FireBlock
{
	public SacrificialFire()
	{
		super(Properties.from(Blocks.FIRE));
	}

	@Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
        if (oldState.getBlock() != state.getBlock())
        {
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
        }
    }
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
	{
		// do nothing
	}
	@Override
	public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos) { return true;	}
	@Override
	public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side) { return true; }
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		if (worldIn.isRemote || entityIn instanceof PlayerEntity)
			return;
		
		if (entityIn instanceof ItemEntity)
		{
			// log offering
			ItemStack stack = ((ItemEntity)entityIn).getItem();
			if (stack.getItem() == Items.WRITTEN_BOOK)
			{
				OfferingManager.finalizeOffering(worldIn, stack);
		        LightningBoltEntity lightningboltentity = new LightningBoltEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), false);
		        worldIn.getServer().getWorld(worldIn.getDimension().getType()).addLightningBolt(lightningboltentity);
			}
			else
			{
				OfferingManager.acceptOffering(stack);
			}
			
			// delete entity
			entityIn.remove();
		}
		if (entityIn instanceof MobEntity)
		{
			// log offering
			OfferingManager.acceptOffering((MobEntity)entityIn);
			
			// delete entity
			entityIn.remove();
		}

	}	
}
