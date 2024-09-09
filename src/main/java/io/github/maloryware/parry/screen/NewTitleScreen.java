package io.github.maloryware.parry.screen;

import dev.lambdaurora.spruceui.screen.SpruceScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NewTitleScreen extends SpruceScreen {

	public Screen parent;

	public NewTitleScreen(Text title, Screen parent) {
		super(title);
		this.parent = parent;
	}


	@Override
	protected void init(){
		super.init();


	}

}
