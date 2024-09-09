package io.github.maloryware.parry.mixin;

import io.github.maloryware.parry.ExtraClutter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)


public class ItemMixin {


	@Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
	public void allowSwordUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		var stack = user.getStackInHand(hand);
		var offStack = user.getStackInHand(Hand.OFF_HAND);

		if (stack.getItem() instanceof SwordItem) {


			if (!(((ExtraClutter) user).parry$canParry())) {
				cir.cancel();
			} else {
				if (offStack.getItem() instanceof ShieldItem) {
					user.setCurrentHand(Hand.OFF_HAND);
					cir.cancel();
				} else if (user.getItemUseTimeLeft() == 0 & !user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
					user.getActiveItem().finishUsing(world, user);
					user.getItemCooldownManager().set(stack.getItem(), 20);
					cir.cancel();
				} else {
					user.setCurrentHand(hand);
					user.setSprinting(false);
					cir.setReturnValue(TypedActionResult.success(stack, false));
				}
			}

		}
	}
}

	/*
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

	 */
