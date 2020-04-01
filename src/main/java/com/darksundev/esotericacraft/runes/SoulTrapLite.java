package com.darksundev.esotericacraft.runes;

import java.util.HashSet;

import com.darksundev.esotericacraft.EsotericaCraft;
import com.darksundev.esotericacraft.runes.RuneManager.Tier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;

public class SoulTrapLite extends Rune implements IItemEffect
{
	private static final String NBT_TAG = "soul_trap_effect";
	public SoulTrapLite()
	{
		super("Soul Trap Lite", new Tier[][]{
			new Tier[]{Tier.NONE,		Tier.NONE,		Tier.ENCHANTED,		Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE,		Tier.NONE,		Tier.ENCHANTED,		Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.ENCHANTED,	Tier.ENCHANTED,	Tier.ENCHANTED,			Tier.ENCHANTED,	Tier.ENCHANTED},
			new Tier[]{Tier.NONE,		Tier.NONE,		Tier.ENCHANTED,		Tier.NONE,		Tier.NONE},
			new Tier[]{Tier.NONE, 		Tier.NONE,		Tier.ENCHANTED,		Tier.NONE,		Tier.NONE}
		});
	}

	
	
	@Override
	public boolean onCast(PlayerEntity player, World worldIn, BlockPos pos, BlockState[][] pattern, BlockState[] enchantBlocks, BlockState[] mundaneBlocks)
	{
		if (!super.onCast(player, worldIn, pos, pattern, enchantBlocks, mundaneBlocks))
			return false;
		
		if (!validate(pattern))
			return false;
		
		// get staff player used to cast this rune
		// note: preference given to the staff in the main hand, if the player is holding two
		//ItemStack staff = (player.getHeldItemMainhand().getItem() == ItemList.runing_staff) ? player.getHeldItemMainhand() : player.getHeldItemOffhand();

		ItemStack garnet = getEnchantableGarnet(player);
		// add charge to garnet
		addData(garnet.getOrCreateTag(), 1);
		
		// delete the enchant blocks
		removeOfferingBlocks(worldIn, pos);
		
		return true;
	}

	private boolean validate(BlockState[][] pattern)
	{
		HashSet<String> workingSet = new HashSet<String>();

		// check end rods
		workingSet.add(pattern[1][2].getBlock().getRegistryName().toString());
		workingSet.add(pattern[3][2].getBlock().getRegistryName().toString());
		workingSet.add(pattern[2][1].getBlock().getRegistryName().toString());
		workingSet.add(pattern[2][3].getBlock().getRegistryName().toString());
		if (workingSet.size() > 1 || pattern[1][2].getBlock() != Blocks.END_ROD)
			return false;
		
		workingSet.clear();
		
		// check emerald blocks
		workingSet.add(pattern[0][2].getBlock().getRegistryName().toString());
		workingSet.add(pattern[4][2].getBlock().getRegistryName().toString());
		workingSet.add(pattern[2][0].getBlock().getRegistryName().toString());
		workingSet.add(pattern[2][4].getBlock().getRegistryName().toString());
		if (workingSet.size() > 1 || pattern[0][2].getBlock() != Blocks.EMERALD_BLOCK)
			return false;
		
		return true;
	}
	private void removeOfferingBlocks(World world, BlockPos pos)
	{
		world.removeBlock(pos.north().north(), false);
		world.removeBlock(pos.south().south(), false);
		world.removeBlock(pos.east().east(), false);
		world.removeBlock(pos.west().west(), false);
		world.notifyBlockUpdate(pos.north().north(), Blocks.EMERALD_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
		world.notifyBlockUpdate(pos.south().south(), Blocks.EMERALD_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
		world.notifyBlockUpdate(pos.east().east(), Blocks.EMERALD_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
		world.notifyBlockUpdate(pos.west().west(), Blocks.EMERALD_BLOCK.getDefaultState(), Blocks.AIR.getDefaultState(), 3);
	}

	// Interface methods
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
	public String getNBTEffectTag()
	{
		return NBT_TAG;
	}
	@Override
	public void doRightClickBlockEffect(RightClickItem event, ItemStack item)
	{
		// nothing
	}
	@Override
	public void doAttackEntityEffect(AttackEntityEvent event, ItemStack item)
	{
		// only apply to mobs
		EntityType<?> type = event.getTarget().getType();
		boolean invalid = (event.getTarget() instanceof PlayerEntity || type == EntityType.IRON_GOLEM || type == EntityType.PILLAGER) || (type == EntityType.ILLUSIONER) || (type == EntityType.EVOKER) || (type == EntityType.SHULKER) || (type == EntityType.TRADER_LLAMA) || (type == EntityType.VEX) || (type == EntityType.VINDICATOR) || (type == EntityType.WANDERING_TRADER) || (type == EntityType.WITHER) || (type == EntityType.RAVAGER) || (type == EntityType.ELDER_GUARDIAN) || (type == EntityType.WITHER);
		if (invalid) return;
		
		// convert entity into egg
		Entity entity = event.getTarget();
		World world = entity.world;
		ItemStack egg = new ItemStack(SoulTrap.getEgg(type));
		if (egg != null || egg.getItem() != Items.AIR)
		{
			// spawn item in world
			BlockPos p = entity.getPosition();
			world.addEntity(new ItemEntity(world, p.getX(), p.getY() + .5, p.getZ(), egg));
			entity.remove();
			EsotericaCraft.logger.info(String.format("Converting %s to an egg", entity.getDisplayName().getFormattedText()));
			event.setCanceled(true);

			// remove charge from staff
			int usesRemaining = item.getTag().getInt(getNBTEffectTag());
			if (usesRemaining > 0)
			{
				item.getTag().putInt(getNBTEffectTag(), --usesRemaining);
			}
			if (usesRemaining == 0)
			{
				// out of usages, remove enchantment data
				item.getTag().remove(getNBTEffectTag());
			}
		}
	}
	@Override
	public void addData(CompoundNBT nbt, Object... args)
	{
		int strength = (int) args[0];
		if (nbt.getInt(getNBTEffectTag()) != 0)
		{
			int oldCount = nbt.getInt(getNBTEffectTag());
			nbt.putInt(getNBTEffectTag(), oldCount + strength);
		}
		else
		{
			nbt.putInt(getNBTEffectTag(), strength);
		}
	}
	@Override
	public void displayTooltip(ItemTooltipEvent event)
	{
		CompoundNBT data = event.getItemStack().getTag();
		int uses = data.getInt(getNBTEffectTag());
		event.getToolTip().add(new StringTextComponent(TextFormatting.LIGHT_PURPLE + "Soul Trap: " + TextFormatting.RESET + uses));
	}
}
