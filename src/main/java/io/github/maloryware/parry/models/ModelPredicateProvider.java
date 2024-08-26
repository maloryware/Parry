package io.github.maloryware.parry.models;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModelPredicateProvider {
	public static void register(){

		Registries.ITEM.forEach(ModelPredicateProvider::registerParriableItems);

	}

	private static void registerParriableItems(Item item){

		if (item instanceof SwordItem) {
			ModelPredicateProviderRegistry.register(item, new Identifier("parrying"),
				((itemStack, clientWorld, livingEntity, i) ->
                    livingEntity != null
						&& livingEntity.getActiveItem() == itemStack
                        && livingEntity.isUsingItem() ? 1.0F : 0.0F
				));


		}

	}
}
