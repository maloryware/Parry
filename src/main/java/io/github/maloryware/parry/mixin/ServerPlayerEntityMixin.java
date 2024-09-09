package io.github.maloryware.parry.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.maloryware.parry.ExtraClutter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntity implements ExtraClutter {




	@Unique
	private boolean canParry;

	protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean parry$canParry(){
		return this.canParry;
	}

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Shadow
	@Final
	private static Logger LOGGER;

	@Shadow
	public abstract void playSound(SoundEvent event, SoundCategory category, float volume, float pitch);

	@Shadow
	public abstract boolean changeGameMode(GameMode gameMode);

	@Unique
	private final int maxParryTime = 20;
	@Unique
	private final int minParryTime = 0;

	@Unique
	private int parryTimeLeft = maxParryTime;

	@Unique
	private void resetParry(){
		this.parryTimeLeft = maxParryTime;
	}

	@Unique
	private void denyParry(){
		this.parryTimeLeft = minParryTime;
	}

	@Unique
	public boolean isSwordBlocking(){
		return this.getActiveItem().getItem() instanceof SwordItem;
	}

	@Unique
	private boolean blockedLastTick = false;

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickParry(CallbackInfo ci){
		boolean bl = isSwordBlocking();
		if(bl){
			if(this.parryTimeLeft > minParryTime) parryTimeLeft--;
			else {
				this.canParry = false;
			}
			blockedLastTick = true;
		}
		else {
			if (blockedLastTick && parryTimeLeft > 0) {
				this.denyParry();
			}
			if(this.parryTimeLeft < maxParryTime) parryTimeLeft++;
			if(this.parryTimeLeft == maxParryTime) {
				this.canParry = true;
			}
			blockedLastTick = false;
		}
		if(1 < parryTimeLeft && parryTimeLeft < 19) LOGGER.info("BLOCKING: {} - parryTimeLeft: {} - canParry: {}", bl, this.parryTimeLeft, this.canParry);
		else LOGGER.info("Parriable changed to {}, timeLeft: {}", this.canParry, this.parryTimeLeft);
	}


    @WrapMethod(method = "damage")
    private boolean applySwordBlockProtection(DamageSource source, float amount, Operation<Boolean> original)  {
		boolean apply;
        apply = isSwordBlocking() && this.blockedByShield(source);
		if(apply) {
			if(this.canParry){
				LOGGER.info("---PARRIED---");
				this.resetParry();
				this.playSound(SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 10 ,1);
				return false;
				}
			else return original.call(source, amount*0.2F);
		}
		else return original.call(source, amount);
    }


}
