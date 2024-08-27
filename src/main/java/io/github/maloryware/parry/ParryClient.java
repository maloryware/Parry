package io.github.maloryware.parry;

import io.github.maloryware.parry.models.PredicateProvider;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ParryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
		PredicateProvider.register();
    }
}
