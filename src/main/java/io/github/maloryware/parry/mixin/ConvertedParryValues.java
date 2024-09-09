package io.github.maloryware.parry.mixin;

import com.mojang.authlib.GameProfile;
import io.github.maloryware.parry.ExtraClutter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class ConvertedParryValues  implements ExtraClutter {
	public ConvertedParryValues(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Override
	public boolean parry$canParry() {
		return false;
	}

	@Override
	public int parry$getParryTimeLeft() {
		return 0;
	}

	@Override
	public void parry$setParryTimeLeft(int i) {

	}


}
