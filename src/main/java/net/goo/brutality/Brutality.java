package net.goo.brutality;

import com.mojang.logging.LogUtils;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.event.BrutalityModParticlesHandler;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.ModRegistryManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Locale;

@Mod(Brutality.MOD_ID)
public class Brutality {
    public static final String MOD_ID = "brutality";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Brutality() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        // Register features
        ModRegistryManager.register(modEventBus);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                BrutalityCommonConfig.SPEC,
                "brutality-common.toml"
        );

        // Register client-only config
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT,
                BrutalityClientConfig.SPEC,
                "brutality-client.toml"
        );

        // Register network-related classes
        PacketHandler.register();  // Ensure packets are properly registered
        modEventBus.addListener(this::commonSetup);

        // Register general Forge event handlers (e.g., for item/ block registration)
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        event.enqueueWork(BrutalityModParticlesHandler::init);
        LOGGER.info("Brutality: Performing common setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Brutality: Server is starting!");
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MOD_ID, path.toLowerCase(Locale.ROOT));
    }


    @Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ConfigSetup {
        @SubscribeEvent
        public static void onLoad(final ModConfigEvent.Loading configEvent) {
            // Config loaded
        }

        @SubscribeEvent
        public static void onReload(final ModConfigEvent.Reloading configEvent) {
            // Config reloaded
        }
    }

}
