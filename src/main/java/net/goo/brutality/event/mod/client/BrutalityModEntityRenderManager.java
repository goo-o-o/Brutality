//package net.goo.brutality.event.mod.client;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.client.renderers.block.BrutalityBlockRenderer;
//import net.goo.brutality.client.renderers.entity.*;
//import net.goo.brutality.client.renderers.entity.fullbright.*;
//import net.goo.brutality.client.renderers.entity.glowing.BrutalityAbstractEmissivePhysicsProjectileRenderer;
//import net.goo.brutality.client.renderers.entity.glowing.BrutalityEmissiveTridentRenderer;
//import net.goo.brutality.registry.BrutalityModEntities;
//import net.goo.brutality.registry.ModBlockEntities;
//import net.minecraft.client.renderer.entity.StrayRenderer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.EntityRenderersEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public class BrutalityModEntityRenderManager {
//    @SubscribeEvent
//    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_THUNDERBOLT_ENTITY.get(), BrutalityEmissiveTridentRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_GUNGNIR_ENTITY.get(), BrutalityEmissiveTridentRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.EXOBLADE_BEAM.get(), BrutalityFullbrightAlphaNoDepthTridentRenderer::new);
//
//
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_CABBAGE_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_BUTTER_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_APPLE_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_DURIAN_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_BANANA_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_BIOMECH_REACTOR.get(), BrutalityAbstractEmissivePhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_WINTERMELON_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_STYROFOAM_CUP.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_DEPTH_CRUSHER.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.THROWN_KNIFE_ENTITY.get(), BrutalityTridentRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.STAR_ENTITY.get(), BrutalityEmissiveShurikenRenderer::new);
//
//        event.registerEntityRenderer(BrutalityModEntities.PI_ENTITY.get(), BrutalityEntityRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.DEPTH_CRUSHER_PROJECTILE.get(), BrutalityEntityRenderer::new);
//
//        event.registerEntityRenderer(BrutalityModEntities.TERRA_BEAM.get(), SwordBeamRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.BLACK_HOLE_ENTITY.get(), BrutalityFullbrightNoDepthEntityRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.CRUEL_SUN_ENTITY.get(), BrutalityFullbrightAlphaNoDepthEntityRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.EXPLOSION_RAY.get(), BrutalityFullbrightRayRendererNoDepth::new);
//        event.registerEntityRenderer(BrutalityModEntities.LAST_PRISM_RAY.get(), BrutalityLastPrismRayRenderer::new);
//
//
////        event.registerEntityRenderer(BrutalityModEntities.SUPERNOVA_PORTAL.get(), BrutalityEndPortalEntityRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.SUPERNOVA_ASTEROID.get(), BrutalityFullbrightEntityRenderer::new);
//
//        event.registerEntityRenderer(BrutalityModEntities.SPECTRAL_MAW_ENTITY.get(), BrutalityEmissiveTridentRenderer::new);
//
////        event.registerEntityRenderer(BrutalityModEntities.EXCALIBUR_BEAM.get(), SwordBeamRenderer::new);
////        event.registerEntityRenderer(BrutalityModEntities.SWORD_WAVE.get(), SwordWaveRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.SUMMONED_STRAY.get(), StrayRenderer::new);
//        event.registerEntityRenderer(BrutalityModEntities.LIGHT_ARROW.get(), BrutalityFullbrightNoDepthArrowRenderer::new);
//
//
//        event.registerBlockEntityRenderer(ModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
//        event.registerBlockEntityRenderer(ModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityBlockRenderer::new);
//    }
//
//
//
//}