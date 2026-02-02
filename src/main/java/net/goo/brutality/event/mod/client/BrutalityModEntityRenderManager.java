package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.block.BrutalityGeoBlockRenderer;
import net.goo.brutality.client.renderers.entity.*;
import net.goo.brutality.client.renderers.layers.BrutalityAutoColorShiftFullbrightNoDepthLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightAlphaLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.goo.brutality.util.ModResources.rainbowColor;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModEntityRenderManager {


    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BrutalityEntities.THROWN_THUNDERBOLT_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.THROWN_GUNGNIR_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.SPECTRAL_MAW_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.EXOBLADE_BEAM.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.PHOTON.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityEntities.CHAIR_SEAT.get(), BrutalityEntityRenderer::new);

        event.registerEntityRenderer(BrutalityEntities.CANNONBALL_CABBAGE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.STICK_OF_BUTTER.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.CRIMSON_DELIGHT.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.GOLDEN_PHOENIX.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.CAVENDISH.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.WINTER_MELON.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.STYROFOAM_CUP.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.MUG.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.CINDER_BLOCK.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.STICKY_BOMB.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.BEACH_BALL.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.BLAST_BARREL.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.SCULK_GRENADE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.HOLY_HAND_GRENADE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.STICKY_DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.BOUNCY_DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.PERFUME_BOTTLE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.ABSOLUTE_ZERO.get(), BrutalityAbstractPhysicsTridentRenderer::new);

        event.registerEntityRenderer(BrutalityEntities.ICE_CUBE.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.PERMAFROST_CUBE.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.BIOMECH_REACTOR.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(
                        new AutoGlowingGeoLayer<>(renderer)
                )));

        event.registerEntityRenderer(BrutalityEntities.DEPTH_CRUSHER.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.THROWN_KNIFE_ENTITY.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.OVERCLOCKED_TOASTER.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.TOAST.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.FATE_CARD.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.VAMPIRE_KNIFE.get(), context -> new BrutalityTridentRenderer<>(context, renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityEntities.STAR_ENTITY.get(), context -> new BrutalityShurikenRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.CRESCENT_DART_ENTITY.get(), context -> new BrutalityShurikenRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.PI_ENTITY.get(), BrutalityEntityRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.ABYSS_PROJECTILE.get(), BrutalityEntityRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.BLOODSLASH.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.SHADOWFLAME_SLASH.get(), BrutalityTridentRenderer::new);

        event.registerEntityRenderer(BrutalityEntities.HEALING_PROJECTILE.get(), NoOpRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.BLACK_HOLE_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.CRUEL_SUN_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityEntities.EXPLOSION_RAY.get(), context -> new BrutalitySkyRayRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));




        event.registerEntityRenderer(BrutalityEntities.LAST_PRISM_RAY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoColorShiftFullbrightNoDepthLayer<>(renderer, 0.1F, rainbowColor[0], rainbowColor[1], rainbowColor[2], rainbowColor[3], rainbowColor[4], rainbowColor[5], rainbowColor[6]));
                }));

        event.registerEntityRenderer(BrutalityEntities.EXTINCTION_ENTITY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityEntities.PIERCING_MOONLIGHT_ENTITY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityEntities.RHONGOMYNIAD_RAY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityEntities.SUPERNOVA_ASTEROID.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityEntities.SUMMONED_STRAY.get(), StrayRenderer::new);

        event.registerEntityRenderer(BrutalityEntities.LIGHT_ARROW.get(), context -> new BrutalityArrowRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));


        event.registerBlockEntityRenderer(BrutalityBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);


        event.registerEntityRenderer(BrutalityEntities.GRAVITIC_IMPLOSION_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.COSMIC_CATACLYSM_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityEntities.METEOR_SHOWER_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityEntities.STAR_STREAM_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityEntities.SINGULARITY_SHIFT_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityEntities.ANNIHILATION_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.BRIMSPIKE_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityEntities.CHTHONIC_CAPSULE_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(BrutalityEntities.DESTRUCTION_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityEntities.LIGHT_BINDING.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityEntities.CRESCENT_SCYTHE_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


    }


}