package io.github.maloryware.parry.mixin;

import io.github.maloryware.parry.screen.NewTitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class RedirectToMyTitleScreenMixin extends Screen {


	protected RedirectToMyTitleScreenMixin(Text title) {
		super(title);
	}

	// redirect to custom title screen
	ButtonWidget redirectButton = ButtonWidget.builder(Text.of("Go to new menu"), (btn) -> {
		this.client.getToastManager().add(
			SystemToast.create(this.client, SystemToast.Type.PERIODIC_NOTIFICATION, Text.of("Switched to new menu."), Text.of("Press [ESCAPE] to return to the original title screen."))
		);
		this.client.setScreen(new NewTitleScreen(Text.of("Title???"),this));
	}).positionAndSize(this.width / 2 + 100, this.height / 2, 120, 20).build();




	@Inject(method = "initWidgetsNormal", at = @At("RETURN"))
	private void addCustomButton(int y, int spacingY, CallbackInfo ci){
		this.addDrawableChild(redirectButton);
	}
}
