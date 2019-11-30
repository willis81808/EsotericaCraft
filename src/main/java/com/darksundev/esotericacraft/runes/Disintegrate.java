package com.darksundev.esotericacraft.runes;

import java.util.Collections;
import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.lists.ItemList;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

public class Disintegrate extends Rune implements IItemEffect
{
	private static final String NBT_TAG = "disintegrate_effect";
	private static final int ENCHANTMENT_USES = 2;
	private static final int RANGE = 50;
	
	/*
	 * 	-: Neither Mundane nor Enchanted
	 * 	M: Mundane required
	 * 	O: Enchanted required
	 * 
	 * 		- - O - -
	 * 		- O - O -
	 *		O - M - O
	 * 		- O - O -
	 *		- - O - -
	 */
	public Disintegrate()
	{
		super("Disintegrate", new Tier[][]{
			new Tier[]{Tier.NONE,		Tier.NONE, 		Tier.ENCHANTED, Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.NONE,		Tier.MUNDANE,	Tier.NONE,		Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.ENCHANTED,	Tier.NONE},
			new Tier[]{Tier.NONE, 		Tier.NONE,		Tier.ENCHANTED,	Tier.NONE,		Tier.NONE}
		});
	}

	@Override
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (!super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks))
			return false;

		// look for an enchantable garnet in caster's hand
		ItemStack garnet = getEnchantableGarnet(player);
		if (garnet == null)
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "No enchantable garnet found...");
			return false;
		}
		
		// enchant garnet
		int strength = getStrength(enchantBlocks);
		if (strength > 0)
		{
			worldIn.playSound((PlayerEntity)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0F, 1.0F);
			addData(garnet.getOrCreateTag(), strength);
			
			removeBlock(worldIn, pos.north().north(), enchantBlocks[0]);
			removeBlock(worldIn, pos.north().east(), enchantBlocks[0]);
			removeBlock(worldIn, pos.north().west(), enchantBlocks[0]);
			removeBlock(worldIn, pos.east().east(), enchantBlocks[0]);
			removeBlock(worldIn, pos.west().west(), enchantBlocks[0]);
			removeBlock(worldIn, pos.south().east(), enchantBlocks[0]);
			removeBlock(worldIn, pos.south().west(), enchantBlocks[0]);
			removeBlock(worldIn, pos.south().south(), enchantBlocks[0]);
		}
		else 
		{
			EsotericaCraft.messagePlayer(player, "The Aether resists!", TextFormatting.RED);
			EsotericaCraft.messagePlayer(player, "Either your rune is impure, your offering is not valuable enough, or both!");
		}
		
		return true;
	}

	private void removeBlock(World world, BlockPos pos, BlockState old)
	{
		world.destroyBlock(pos, false);
		world.notifyBlockUpdate(pos, old, Blocks.AIR.getDefaultState(), 3);
	}
	
	private int getStrength(BlockState[] enchantBlocks)
	{
		HashSet<BlockState> names = new HashSet<BlockState>();
		Collections.addAll(names, enchantBlocks);
		
		if (names.size() == 1)
		{
			Block b = enchantBlocks[0].getBlock();
			if (Blocks.DIAMOND_BLOCK == b)
			{
				return 4;
			}
			else if (Blocks.EMERALD_BLOCK == b)
			{
				return 3;
			}
			else if (Blocks.GOLD_BLOCK == b)
			{
				return 2;
			}
			else if (Blocks.IRON_BLOCK == b)
			{
				return 1;
			}
		}
		
		return 0;
	}
	private ItemStack getEnchantableGarnet(PlayerEntity player)
	{
		Item main = player.getHeldItemMainhand().getItem();
		Item off = player.getHeldItemOffhand().getItem();
		
		if (main == ItemList.runing_staff && off == ItemList.garnet)
		{
			return player.getHeldItemOffhand();
		}
		else if (off == ItemList.runing_staff && main == ItemList.garnet)
		{
			return player.getHeldItemMainhand();
		}

		return null;
	}
	private Vec3d getTargetPoint(PlayerEntity player, World world)
	{
		// get look direction, and range
		Vec3d lookDir = player.getLookVec().mul(RANGE, RANGE, RANGE);
		// get eye position
		Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
		// get point we're looking at
		Vec3d vec3d1 = new Vec3d(player.posX + lookDir.x, player.posY + (double)player.getEyeHeight() + lookDir.y, player.posZ + lookDir.z);
		RayTraceResult result = world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, player));
		if (result.getType() != RayTraceResult.Type.MISS)
		{
			// return ray hit point
			return result.getHitVec();
		}
		else
		{
			// ray hit nothing
			return null;
		}
	}
	
	@Override
	public boolean effectCanStack()
	{
		return false;
	}
	@Override
	public String getNBTEffectTag()
	{
		return NBT_TAG;
	}
	@Override
	public void doRightClickBlockEffect(RightClickItem event, ItemStack item)
	{
		PlayerEntity p = event.getPlayer();
		World w = event.getWorld();
		if (p.getHeldItemMainhand() != item)
			return;
		
		int usesRemaining = item.getTag().getInt(getNBTEffectTag());
		if (usesRemaining > 0)
		{
			// spawn lightning
			Vec3d pos = getTargetPoint(p, w);
			if (pos == null)
					return;
	        LightningBoltEntity lightningboltentity = new LightningBoltEntity(w, pos.getX(), pos.getY(), pos.getZ(), false);
	        w.getServer().getWorld(w.getDimension().getType()).addLightningBolt(lightningboltentity);
			item.getTag().putInt(getNBTEffectTag(), --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			item.getTag().remove(getNBTEffectTag());
		}
	}
	@Override
	public void addData(CompoundNBT nbt, Object... args)
	{
		int strength = (int) args[0];
		if (nbt.getInt(getNBTEffectTag()) != 0)
		{
			int oldCount = nbt.getInt(getNBTEffectTag());
			nbt.putInt(getNBTEffectTag(), oldCount + ENCHANTMENT_USES * strength);
		}
		else
		{
			nbt.putInt(getNBTEffectTag(), ENCHANTMENT_USES * strength);
		}
	}
	@Override
	public void displayTooltip(ItemTooltipEvent event)
	{
		CompoundNBT data = event.getItemStack().getTag();
		int uses = data.getInt(getNBTEffectTag());
		event.getToolTip().add(new StringTextComponent(TextFormatting.LIGHT_PURPLE + "Disintegrate: " + TextFormatting.RESET + uses));
	}
	@Override
	public void doAttackEntityEffect(AttackEntityEvent event, ItemStack item)
	{
		int usesRemaining = item.getTag().getInt(getNBTEffectTag());
		BlockPos pos = event.getEntity().getPosition();
		if (usesRemaining > 0)
		{
			// spawn lightning
			World w = event.getEntity().world;
	        LightningBoltEntity lightningboltentity = new LightningBoltEntity(w, pos.getX(), pos.getY(), pos.getZ(), false);
	        w.getServer().getWorld(w.getDimension().getType()).addLightningBolt(lightningboltentity);
			item.getTag().putInt(getNBTEffectTag(), --usesRemaining);
		}
		
		if (usesRemaining == 0)
		{
			// out of usages, remove enchantment data
			item.getTag().remove(getNBTEffectTag());
		}
	}
}
