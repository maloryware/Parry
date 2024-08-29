package io.github.maloryware.parry.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ExtraClutter{




	@Unique
	private boolean canParry;

	@Override
	public boolean parry$canParry(){
		return this.canParry;
	}

    @Shadow public abstract boolean blockedByShield(DamageSource source);

	// why am i using both activeItemStack and manually checking with getActiveItem()???
	// i may be stupid

	@Shadow
	public abstract boolean isBlocking();

	@Shadow
	public abstract ItemStack getActiveItem();

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Shadow
	public abstract void writeCustomDataToNbt(NbtCompound nbt);

	@Unique private DamageSource cachedSource = null;
    @Unique private boolean appearBlocking = false;

	@Unique
	private int parryTimeLeft = 20;

	@Unique
	private boolean isSwordBlocking() {
		return (this.isBlocking() && this.getActiveItem().getItem() instanceof SwordItem);
	}

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void tickParry(CallbackInfo ci){
		if(isSwordBlocking()){
			if(this.parryTimeLeft > 0) parryTimeLeft--;
			else this.canParry = false;
		}
		else {
			if(this.parryTimeLeft < 20) parryTimeLeft++;
			if(this.parryTimeLeft == 20 && !this.canParry) {
				this.canParry = true;
			}
		}

	}

    @Inject(at = @At(value = "HEAD"), method = "isBlocking", cancellable = true)
    public void swordBlocking(CallbackInfoReturnable<Boolean> cir) {
        if(isSwordBlocking()) {
			appearBlocking = true;
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "damage")
    public void cacheDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.cachedSource = source;
    }


    @WrapMethod(method = "damage")
    private boolean applySwordBlockProtection(DamageSource source, float amount, Operation<Boolean> original)  {
		boolean apply;
        appearBlocking = true;
        apply = isSwordBlocking() && this.blockedByShield(cachedSource);
		appearBlocking = false;
		if(apply) {
			if(this.canParry){
				return false;
				}
			else return original.call(source, amount*0.2F);
		}
		else return original.call(source, amount);
    }

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}
