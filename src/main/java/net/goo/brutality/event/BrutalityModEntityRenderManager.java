package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.entity.*;
import net.goo.brutality.client.renderers.entity.fullbright.*;
import net.goo.brutality.client.renderers.entity.glowing.BrutalityGlowingTridentRenderer;
import net.goo.brutality.registry.ModEntities;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModEntityRenderManager {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_THUNDERBOLT_ENTITY.get(), BrutalityGlowingTridentRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_GUNGNIR_ENTITY.get(), BrutalityGlowingTridentRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_CABBAGE_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_WINTERMELON_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_KNIFE_ENTITY.get(), BrutalityTridentRenderer::new);


        event.registerEntityRenderer(ModEntities.PI_ENTITY.get(), BrutalityEntityRenderer::new);


        event.registerEntityRenderer(ModEntities.TERRA_BEAM.get(), SwordBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.BLACK_HOLE_ENTITY.get(), BrutalityFullbrightNoDepthEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.CRUEL_SUN_ENTITY.get(), BrutalityFullbrightAlphaNoDepthEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.EXPLOSION_RAY.get(), BrutalityRayRendererNoDepth::new);
        event.registerEntityRenderer(ModEntities.MAGIC_EXPLOSION.get(), BrutalityFullbrightEntityRenderer::new);


        event.registerEntityRenderer(ModEntities.SUPERNOVA_PORTAL.get(), BrutalityEndPortalEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SUPERNOVA_ASTEROID.get(), BrutalityFullbrightEntityRenderer::new);


        event.registerEntityRenderer(ModEntities.EXCALIBUR_BEAM.get(), SwordBeamRenderer::new);
        event.registerEntityRenderer(ModEntities.SWORD_WAVE.get(), SwordWaveRenderer::new);
        event.registerEntityRenderer(ModEntities.SUMMONED_STRAY.get(), StrayRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHT_ARROW.get(), BrutalityFullbrightNoDepthArrowRenderer::new);

    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.DASH_ABILITY_KEY.get());
    }


}