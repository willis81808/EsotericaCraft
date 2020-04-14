package com.darksundev.esotericacraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.darksundev.esotericacraft.EsotericaCraft;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Inject(at = @At("HEAD"), method = "jump")
	public void onJump(CallbackInfo info)
	{
		PlayerEntity me = (PlayerEntity)(Object)this;
		if (!me.world.isRemote)
		{
			EsotericaCraft.messagePlayer(me, "Jumpe!");
		}
	}
}
