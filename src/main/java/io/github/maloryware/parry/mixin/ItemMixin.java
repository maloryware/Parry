package io.github.maloryware.parry.mixin;

import io.github.maloryware.parry.ExtraClutter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)


public class ItemMixin {


    @Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
    public void allowSwordUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

		if(player instanceof ServerPlayerEntity user){
			if (user.getStackInHand(hand).getItem() instanceof SwordItem) {
				var stack = user.getStackInHand(hand);
				if (user.getStackInHand(Hand.OFF_HAND).getItem() instanceof ShieldItem) {
					user.setCurrentHand(Hand.OFF_HAND);
					cir.cancel();
				}

				user.setCurrentHand(hand);
				user.setSprinting(false);
				if (!(((ExtraClutter) user).parry$canParry())) {
					cir.setReturnValue(TypedActionResult.success(stack));
				} else {
					cir.setReturnValue(TypedActionResult.fail(stack));
					if (user.getItemUseTimeLeft() <= 0 & !user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
						user.getItemCooldownManager().set(stack.getItem(), 20);
					}

				}
			}
		}
	}
    @Inject(at = @At(value = "HEAD"), method = "getMaxUseTime", cancellable = true)
    public void applySwordUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if(stack.getItem() instanceof SwordItem) {
            cir.setReturnValue(20);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "getUseAction", cancellable = true)
    public void returnBlockUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if(stack.getItem() instanceof SwordItem) {
            cir.setReturnValue(UseAction.BLOCK);
        }
    }

}
