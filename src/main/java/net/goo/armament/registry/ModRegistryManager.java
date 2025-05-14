package net.goo.armament.registry;

import net.goo.armament.util.BetterCombatIntegration;
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
        ModEffects.register(modEventBus);
        ModAttributes.register(modEventBus);
        BetterCombatIntegration.register();
    }
}
