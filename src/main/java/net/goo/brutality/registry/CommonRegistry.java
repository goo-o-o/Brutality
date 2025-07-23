package net.goo.brutality.registry;

import net.goo.brutality.magic.SpellRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class CommonRegistry {
    public static void register(IEventBus modEventBus) {
        ModCreativeModTabs.register(modEventBus);
        BrutalityModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        BrutalityModEntities.register(modEventBus);
        BrutalityModSounds.register(modEventBus);
        BrutalityModMobEffects.register(modEventBus);
        ModAttributes.register(modEventBus);
        SpellRegistry.register();
        BrutalityModParticles.register(modEventBus);
    }


}
