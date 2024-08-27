package io.github.maloryware.parry.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.maloryware.parry.config.ParryConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected ItemStack activeItemStack;
    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract boolean blockedByShield(DamageSource source);

	@Shadow
	protected int itemUseTimeLeft;
	@Unique private DamageSource cachedSource = null;
    @Unique private boolean appearBlocking = false;

    @Inject(at = @At(value = "HEAD"), method = "isBlocking", cancellable = true)
    public void swordBlocking(CallbackInfoReturnable<Boolean> cir) {
        var item = this.activeItemStack.getItem();
        if(item instanceof SwordItem) {
			appearBlocking = true;
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "damage")
    public void cacheDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.cachedSource = source;
    }


    @ModifyReturnValue(at = @At(value = "RETURN"), method = "damage")
    private boolean applySwordBlockProtection(boolean original)  {
        var item = this.activeItemStack.getItem();
		boolean apply;
        appearBlocking = true;
        apply = item instanceof SwordItem && this.isUsingItem() && this.blockedByShield(cachedSource);
		appearBlocking = false;
		this.itemUseTimeLeft = this.activeItemStack.getMaxUseTime();
		return original & !apply;
    }

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
}
