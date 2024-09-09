package io.github.maloryware.parry;

import com.google.common.eventbus.EventBus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parry implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Parry");

    @Override
    public void onInitialize(ModContainer mod) {
        LOGGER.info("Hello Quilt world from {}! Stay fresh!", mod.metadata().name());
    }
}
