package net.goo.armament.registry;

import net.goo.armament.block.ModBlocks;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModRegistryManager {
    public static void register(IEventBus modEventBus) {
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);
    }
}
