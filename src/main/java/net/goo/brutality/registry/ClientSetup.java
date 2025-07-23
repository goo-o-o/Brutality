package net.goo.brutality.registry;

import net.goo.brutality.event.mod.BrutalityModParticleFactories;
import net.goo.brutality.util.BetterCombatIntegration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistry {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatIntegration.register();
        }

        event.enqueueWork(BrutalityModParticleFactories::init);

    }
}
