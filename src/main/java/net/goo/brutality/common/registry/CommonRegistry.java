package net.goo.brutality.common.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class CommonRegistry {
    public static void register(IEventBus modEventBus) {
        BrutalityCreativeModeTabs.register(modEventBus);
        BrutalityItems.register(modEventBus);
        BrutalityBlocks.register(modEventBus);
        BrutalityBlockEntities.register(modEventBus);
        BrutalityVillagers.register(modEventBus);
        BrutaltityLootModifiers.register(modEventBus);
        BrutalityEntities.register(modEventBus);
        BrutalitySounds.register(modEventBus);
        BrutalityEffects.register(modEventBus);
        BrutalityAttributes.register(modEventBus);
        BrutalityParticles.register(modEventBus);
        BrutalityMenuTypes.register(modEventBus);
        BrutalityRecipes.register(modEventBus);
    }


}
