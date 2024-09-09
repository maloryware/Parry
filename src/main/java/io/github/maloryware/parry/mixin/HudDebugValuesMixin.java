package io.github.maloryware.parry.mixin;

import io.github.maloryware.parry.ExtraClutter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudDebugValuesMixin {

	@Unique
	MinecraftClient client = MinecraftClient.getInstance();

	@Unique
	LivingEntity player = (LivingEntity) client.player;

	@Unique
	private String parryTimer;

	@Unique
	private String canParry;


	@Inject(method = "render", at = @At("HEAD"))
	public void renderDebugValues(GuiGraphics graphics, float tickDelta, CallbackInfo ci){


		if(player instanceof ServerPlayerEntity){

			parryTimer = String.valueOf(((ExtraClutter) client.player).parry$getParryTimeLeft());
			canParry = String.valueOf(((ExtraClutter) client.player).parry$canParry());

		}
		else {
			parryTimer = "N/A";
			canParry = "N/A";
		}

		graphics.drawText(
			client.textRenderer,
			Text.of(String.format("Parry timer: %s", parryTimer)),
			0, 0,
			16777215,
			true);

		graphics.drawText(
			client.textRenderer,
			Text.of(String.format("Can parry: %s", canParry)),
			0, 10,
			16777215,
			true);

	}
}
