package net.goo.brutality.registry;

import net.goo.brutality.event.mod.BrutalityModParticleFactories;
import net.goo.brutality.magic.SpellRegistry;
import net.goo.brutality.util.BetterCombatIntegration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModRegistryManager {
    public static void register(IEventBus modEventBus) {
        ModCreativeModTabs.register(modEventBus);
        BrutalityModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        BrutalityModEntities.register(modEventBus);
        BrutalityModSounds.register(modEventBus);
        BrutalityModParticles.register(modEventBus);
        BrutalityModMobEffects.register(modEventBus);
        ModAttributes.register(modEventBus);
        SpellRegistry.register();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatIntegration.register();
        }

        event.enqueueWork(BrutalityModParticleFactories::init);

    }
}
