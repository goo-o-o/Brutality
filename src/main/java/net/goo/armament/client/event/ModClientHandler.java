package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.entity.*;
import net.goo.armament.registry.ModEntities;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientHandler {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_THUNDERBOLT_ENTITY.get(), ArmaGlowingTridentRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_GUNGNIR_ENTITY.get(), ArmaGlowingTridentRenderer::new);
        event.registerEntityRenderer(ModEntities.TERRA_BEAM.get(), SwordBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.BLACK_HOLE_ENTITY.get(), ArmaEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SUPERNOVA_PORTAL.get(), ArmaEndPortalEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SUPERNOVA_ASTEROID.get(), ArmaGlowingEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.EXCALIBUR_BEAM.get(), SwordBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.SWORD_WAVE.get(), SwordWaveRenderer::new);
        event.registerEntityRenderer(ModEntities.SUMMONED_STRAY.get(), StrayRenderer::new);

    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.DASH_ABILITY_KEY.get());
    }


}