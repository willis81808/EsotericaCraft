package com.darksundev.esotericacraft.runes;

import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

public class RecallRune extends Rune implements IItemEffect
{
	private static final String NBT_TAG = "recall_effect";
	
	/*
	 * 	-: Ignored
	 * 	E: ENCHANTED required
	 * 	O: MUNDANE required
	 *  +: Redstone dust required
	 * 
	 * 		- + + + -
	 * 		+ - + - +
	 * 		+ + E + +
	 * 		+ - + - +
	 * 		- + + + -
	 */
	public RecallRune()
	{
		super("Recall", new Tier[][]{
			new Tier[]{Tier.NONE,	Tier.NONE,	Tier.NONE,		Tier.NONE,	Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,	Tier.NONE,		Tier.NONE,	Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,	Tier.ENCHANTED,	Tier.NONE,	Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,	Tier.NONE,		Tier.NONE,	Tier.NONE},
			new Tier[]{Tier.NONE,	Tier.NONE,	Tier.NONE,		Tier.NONE,	Tier.NONE}
		});
	}

	@Override
	public void onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks);
		
		// validate
		if (!isValid(worldIn, pos, pattern))
			return;
		
		// get staff player used to cast this rune
		// note: preference given to the staff in the main hand, if the player is holding two
		ItemStack item = (player.getHeldItemMainhand().getItem() == ItemList.runing_staff) ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
		
		if (item == null || item.getItem() == Items.AIR)
		{
			EsotericaCraft.messagePlayer(player, "You must hold an item in your other hand to enchant...", TextFormatting.RED);
			return;
		}

		// remove redstone block
		worldIn.removeBlock(pos, false);
		
		// save cast position and dimension
		addData(item.getOrCreateTag(), worldIn.dimension.getType(), pos, player.getHeldItemOffhand().getItem() == ItemList.runing_staff);
	}
	
	private boolean isValid(World world, BlockPos pos, BlockState[][] pattern)
	{
		// enforce center block as redstone block
		if (pattern[2][2].getBlock() != Blocks.REDSTONE_BLOCK)
		{
			return false;
		}

		// check for redstone wires
		HashSet<Block> redstone = new HashSet<Block>();
		redstone.add(world.getBlockState(pos.north(1)).getBlock());
		redstone.add(world.getBlockState(pos.north(2)).getBlock());
		redstone.add(world.getBlockState(pos.south(1)).getBlock());
		redstone.add(world.getBlockState(pos.south(2)).getBlock());
		redstone.add(world.getBlockState(pos.east(1)).getBlock());
		redstone.add(world.getBlockState(pos.east(2)).getBlock());
		redstone.add(world.getBlockState(pos.west(1)).getBlock());
		redstone.add(world.getBlockState(pos.west(2)).getBlock());
		redstone.add(world.getBlockState(pos.add(2, 0, 1)).getBlock());
		redstone.add(world.getBlockState(pos.add(2, 0, -1)).getBlock());
		redstone.add(world.getBlockState(pos.add(-2, 0, 1)).getBlock());
		redstone.add(world.getBlockState(pos.add(-2, 0, -1)).getBlock());
		redstone.add(world.getBlockState(pos.add(1, 0, 2)).getBlock());
		redstone.add(world.getBlockState(pos.add(-1, 0, 2)).getBlock());
		redstone.add(world.getBlockState(pos.add(1, 0, -2)).getBlock());
		redstone.add(world.getBlockState(pos.add(-1, 0, -2)).getBlock());
		
		return redstone.size() == 1 && redstone.contains(Blocks.REDSTONE_WIRE);
	}

	@Override
	public String getNBTEffectTag() { return NBT_TAG; }
	@Override
	public void doAttackEntityEffect(AttackEntityEvent event, ItemStack item) { /* do nothing */ }
	@Override
	public void doRightClickBlockEffect(RightClickItem event, ItemStack item)
	{
		// deserialize data
		CompoundNBT data = item.getTag();
		int value = data.getInt(getNBTEffectTag());
		if (value == 1)
		{
			data.putInt(getNBTEffectTag(), 0);
			return;
		}
		BlockPos pos = BlockPos.fromLong(data.getLong(getNBTEffectTag()+"_pos"));
		DimensionType dimension = DimensionType.getById(data.getInt(getNBTEffectTag()+"_dimension"));
		
		// teleport
		teleport(pos, DimensionManager.getWorld(event.getWorld().getServer(), dimension, true, true), event.getPlayer());

		// remove data
		data.remove(getNBTEffectTag());
		data.remove(getNBTEffectTag()+"_dimension");
		data.remove(getNBTEffectTag()+"_pos");
	}
	@Override
	public void addData(CompoundNBT nbt, Object... args)
	{
		boolean mainhand = (boolean) args[2];
		nbt.putInt(getNBTEffectTag(), mainhand ? 0 : 1);
		
		// serialize dimension
		DimensionType dimension = (DimensionType) args[0];
		nbt.putInt(getNBTEffectTag()+"_dimension", dimension.getId());
		
		// serialize position
		BlockPos pos = (BlockPos) args[1];
		nbt.putLong(getNBTEffectTag()+"_pos", pos.toLong());
		
	}
	@Override
	public void displayTooltip(ItemTooltipEvent event)
	{
		event.getToolTip().add(new StringTextComponent("Recall").applyTextStyle(TextFormatting.LIGHT_PURPLE));
	}
	
	private static void teleport(BlockPos to, World world, PlayerEntity player)
	{
		// preload destination chunk
		final ChunkPos chunkpos = new ChunkPos(to);
	    ((ServerWorld)world).getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
	    // teleport player
	    ((ServerPlayerEntity)player).teleport((ServerWorld)world, to.getX()+.5, to.getY()+.5, to.getZ()+.5, player.rotationYaw, player.prevRotationPitch);
	}

}
