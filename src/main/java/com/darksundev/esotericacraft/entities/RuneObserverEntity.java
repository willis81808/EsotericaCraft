package com.darksundev.esotericacraft.entities;

import com.darksundev.esotericacraft.lists.EntityList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public abstract class RuneObserverEntity extends MobEntity
{
	protected double x, y, z;

	public RuneObserverEntity(World world, double x, double y, double z)
	{
		super(EntityList.rune_observer, world);

		this.x = x;
		this.y = y;
		this.z = z;

		updatePosition();
	}
	public RuneObserverEntity(EntityType<? extends MobEntity> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
	}
	
	@Override
	public void baseTick()
	{
		observeTick();
		
		// do normal tick
		super.baseTick();
	}
	public abstract void observeTick();
	
	private void updatePosition()
	{
		setPosition(x, y, z);
	}
	
	@Override
	public boolean attackable() {
		return false;
	}
	@Override
	public boolean hasNoGravity() {
		return true;
	}
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	@Override
	public boolean canBeRiddenInWater() {
		return false;
	}
	@Override
	protected boolean canDropLoot() {
		return false;
	}
	@Override
	public boolean addPotionEffect(EffectInstance p_195064_1_) {
		return false;
	}
	@Override
	public boolean canEntityBeSeen(Entity entityIn) {
		return false;
	}
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	@Override
	public boolean canBePushed() {
		return false;
	}
	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}
	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}
	@Override
	public boolean isInvisible() {
		return true;
	}
	@Override
	public boolean isInvulnerable() {
		return true;
	}
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}
	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}
	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return false;
	}
	@Override
	public boolean canUpdate() {
		return true;
	}
}
