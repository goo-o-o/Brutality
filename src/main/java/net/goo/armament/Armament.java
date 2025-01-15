package net.goo.armament;

import com.mojang.logging.LogUtils;
import net.goo.armament.block.ModBlocks;
import net.goo.armament.entity.ModEntities;
import net.goo.armament.entity.client.ThrownZeusThunderboltRenderer;
import net.goo.armament.item.ModCreativeModTabs;
import net.goo.armament.item.ModItems;
import net.goo.armament.loot.ModLootModifiers;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.sound.ModSounds;
import net.goo.armament.villager.ModVillagers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Armament.MOD_ID)
public class Armament {
    public static final String MOD_ID = "armament";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Armament() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientModEvents {

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.THROWN_ZEUS_THUNDERBOLT.get(), ThrownZeusThunderboltRenderer::new);
        }
    }


}