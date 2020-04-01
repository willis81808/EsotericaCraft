package com.darksundev.esotericacraft.runes;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
	private static final Predicate<BlockState> IS_NONE_TIER = (b) -> {
		return RuneManager.getMaterial(b.getBlock().getTranslationKey()).getTier() == Tier.NONE && b.getBlock() != Blocks.REDSTONE_WIRE;
	};
	
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
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (!super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks))
			return false;
		
		// validate
		if (!isValid(worldIn, pos, pattern))
			return false;
		ItemStack item = getEnchantableGarnet(player);
		if (item == null)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "No enchantable garnet found...");
			return false;
		}

		// play cast sound
		worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		
		// remove redstone block
		destroyOffering(worldIn, pos);
		
		// save cast position and dimension
		addData(item.getOrCreateTag(), worldIn.dimension.getType(), pos);
		
		return true;
	}
	
	private boolean isValid(World world, BlockPos pos, BlockState[][] pattern)
	{
		BlockPattern.PatternHelper thing = getRunePattern().match(world, pos);
		return thing != null;
	}
	private void destroyOffering(World worldIn, BlockPos pos)
	{
		// get redstone
		List<BlockPos> redstone = RuneManager.getAreaRadius(pos, 2, 0, 2)
				.filter(b -> { return worldIn.getBlockState(b).getBlock() == Blocks.REDSTONE_WIRE; })
				.map(b -> b.toImmutable())
				.collect(Collectors.toList());

		// destroy random amount from 0-3
		int sacrifice = EsotericaCraft.rng.nextInt(4);
		for (int i = 0; i < sacrifice; i++)
		{
			// get random position
			int index = EsotericaCraft.rng.nextInt(redstone.size());
			BlockPos b = redstone.get(index);
			redstone.remove(index);
			
			// remove block
			worldIn.destroyBlock(b, false);
			worldIn.notifyBlockUpdate(b, Blocks.REDSTONE_WIRE.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
			
			// show particles
			((ServerWorld)worldIn).spawnParticle(ParticleTypes.SMOKE, b.getX() + 0.5, b.getY() + 0.1, b.getZ() + 0.5, 10, 0, 0, 0, 0.1);
			((ServerWorld)worldIn).spawnParticle(ParticleTypes.POOF, b.getX() + 0.5, b.getY() + 0.25, b.getZ() + 0.5, 5, 0, 0, 0, 0);
		}
	}
	private BlockPattern getRunePattern()
	{
         return BlockPatternBuilder.start()
        		 .aisle(".+++.",
        				"+.+.+",
        				"++O++",
        				"+.+.+",
        				".+++.")
        		 .where('O', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK)))
        		 .where('+', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_WIRE)))
        		 .where('.', CachedBlockInfo.hasState(IS_NONE_TIER))
        		 .build();
	}
	
	@Override
	public boolean effectCanStack()
	{
		return false;
	}
	@Override
	public boolean requireGarnet()
	{
		return true;
	}
	@Override
	public String getNBTEffectTag() { return NBT_TAG; }
	@Override
	public void doAttackEntityEffect(AttackEntityEvent event, ItemStack item)
	{
		Entity target = event.getTarget();
		if (target instanceof PlayerEntity)
		{
			doRecallEffect(item, event.getEntity().world, (PlayerEntity)target);
			event.setCanceled(true);
		}
	}
	@Override
	public void doRightClickBlockEffect(RightClickItem event, ItemStack item)
	{
		/*
		// deserialize data
		CompoundNBT data = item.getTag();
		BlockPos pos = BlockPos.fromLong(data.getLong(getNBTEffectTag()+"_pos"));
		DimensionType dimension = DimensionType.getById(data.getInt(getNBTEffectTag()+"_dimension"));
		

		int usesRemaining = item.getTag().getInt(getNBTEffectTag()+"_charges");
		if (usesRemaining > 0)
		{
			// teleport
			teleport(pos, DimensionManager.getWorld(event.getWorld().getServer(), dimension, true, true), event.getPlayer());
			item.getTag().putInt(getNBTEffectTag()+"_charges", --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			data.remove(getNBTEffectTag());
			data.remove(getNBTEffectTag()+"_dimension");
			data.remove(getNBTEffectTag()+"_pos");
			item.getTag().remove(getNBTEffectTag()+"_charges");
		}
		*/
		doRecallEffect(item, event.getWorld(), event.getPlayer());
	}
	private void doRecallEffect(ItemStack item, World world, PlayerEntity player)
	{
		// deserialize data
		CompoundNBT data = item.getTag();
		BlockPos pos = BlockPos.fromLong(data.getLong(getNBTEffectTag()+"_pos"));
		DimensionType dimension = DimensionType.getById(data.getInt(getNBTEffectTag()+"_dimension"));
		

		int usesRemaining = item.getTag().getInt(getNBTEffectTag()+"_charges");
		if (usesRemaining > 0)
		{
			// teleport
			teleport(pos, DimensionManager.getWorld(world.getServer(), dimension, true, true), player);
			item.getTag().putInt(getNBTEffectTag()+"_charges", --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			data.remove(getNBTEffectTag());
			data.remove(getNBTEffectTag()+"_dimension");
			data.remove(getNBTEffectTag()+"_pos");
			item.getTag().remove(getNBTEffectTag()+"_charges");
		}
	}
	
	@Override
	public void addData(CompoundNBT nbt, Object... args)
	{
		nbt.putInt(getNBTEffectTag(), 1);
		
		// serialize dimension
		DimensionType dimension = (DimensionType) args[0];
		nbt.putInt(getNBTEffectTag()+"_dimension", dimension.getId());
		
		// serialize position
		BlockPos pos = (BlockPos) args[1];
		nbt.putLong(getNBTEffectTag()+"_pos", pos.toLong());
		
		// add enchantment uses
		if (nbt.getInt(getNBTEffectTag()+"_charges") != 0)
		{
			int oldCount = nbt.getInt(getNBTEffectTag()+"_charges");
			nbt.putInt(getNBTEffectTag()+"_charges", oldCount + 1);
		}
		else
		{
			nbt.putInt(getNBTEffectTag()+"_charges", 1);
		}
	}
	@Override
	public void displayTooltip(ItemTooltipEvent event)
	{
		CompoundNBT data = event.getItemStack().getTag();
		int uses = data.getInt(getNBTEffectTag()+"_charges");
		event.getToolTip().add(new StringTextComponent(TextFormatting.LIGHT_PURPLE + "Recall: " + TextFormatting.RESET + uses));
	}
	
	private static void teleport(BlockPos to, World world, PlayerEntity player)
	{
		// play cast sound
	    world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
		
		// preload destination chunk
		final ChunkPos chunkpos = new ChunkPos(to);
	    ((ServerWorld)world).getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
	    // teleport player
	    ((ServerPlayerEntity)player).teleport((ServerWorld)world, to.getX()+.5, to.getY()+.5, to.getZ()+.5, player.rotationYaw, player.prevRotationPitch);

	    player.giveExperiencePoints(0);
	    
	    world.playSound(null, to, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
	}

}
