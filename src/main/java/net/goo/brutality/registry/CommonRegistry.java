package net.goo.brutality.registry;

import net.goo.brutality.magic.BrutalityModSpells;
import net.minecraftforge.eventbus.api.IEventBus;

public class CommonRegistry {
    public static void register(IEventBus modEventBus) {
        ModCreativeModTabs.register(modEventBus);
        BrutalityModItems.register(modEventBus);
        BrutalityModBlocks.register(modEventBus);
        BrutalityModBlockEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        BrutalityModEntities.register(modEventBus);
        BrutalityModSounds.register(modEventBus);
        BrutalityModMobEffects.register(modEventBus);
        BrutalityModAttributes.register(modEventBus);
        BrutalityModParticles.register(modEventBus);
        BrutalityMenuTypes.register(modEventBus);
        BrutalityRecipes.register(modEventBus);
    }


}
