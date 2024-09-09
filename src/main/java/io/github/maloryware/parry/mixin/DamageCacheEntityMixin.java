package io.github.maloryware.parry.mixin;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class DamageCacheEntityMixin {

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void cacheDamage(CallbackInfo ci) {
		final int delay = 10;

		final FloatArrayList[] queuedDamage = new FloatArrayList[delay];
		for (int i = 0; i < delay; i++) {
			queuedDamage[i] = new FloatArrayList();
		}
	}


}
