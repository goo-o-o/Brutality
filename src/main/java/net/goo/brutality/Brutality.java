package net.goo.brutality;

import com.mojang.logging.LogUtils;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.magic.SpellCommands;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.CommonRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Brutality.MOD_ID)
public class Brutality {
    public static final String MOD_ID = "brutality";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Brutality(FMLJavaModLoadingContext modLoadingContext) {
        IEventBus modEventBus = modLoadingContext.getModEventBus();


        // Register features
        CommonRegistry.register(modEventBus);

        modLoadingContext.registerConfig(
                ModConfig.Type.COMMON,
                BrutalityCommonConfig.SPEC,
                "brutality-common.toml"
        );

        // Register client-only config
        modLoadingContext.registerConfig(
                ModConfig.Type.CLIENT,
                BrutalityClientConfig.SPEC,
                "brutality-client.toml"
        );

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        LOGGER.info("Brutality: Performing common setup");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        SpellCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Brutality: Server is starting!");
    }

}
