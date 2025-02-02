package net.goo.armament;

import com.mojang.logging.LogUtils;
import net.goo.armament.block.ModBlocks;
import net.goo.armament.entity.ModEntities;
import net.goo.armament.item.ModCreativeModTabs;
import net.goo.armament.item.ModItems;
import net.goo.armament.item.custom.TruthseekerSwordItemXpHandler;
import net.goo.armament.loot.ModLootModifiers;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.sound.ModSounds;
import net.goo.armament.villager.ModVillagers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Armament.MOD_ID)
public class Armament {
    public static final String MOD_ID = "armament";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Armament() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        // Register features
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);

        // Item specific features
        TruthseekerSwordItemXpHandler.register();

        // Register network-related classes
        PacketHandler.register();  // Ensure packets are properly registered
        modEventBus.addListener(this::commonSetup);

        // Register general Forge event handlers (e.g., for item/ block registration)
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Armament: Performing common setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Armament: Server is starting!");
    }

}
