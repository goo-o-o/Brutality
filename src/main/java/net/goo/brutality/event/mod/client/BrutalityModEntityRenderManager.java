package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.block.BrutalityGeoBlockRenderer;
import net.goo.brutality.client.renderers.entity.*;
import net.goo.brutality.client.renderers.layers.BrutalityAutoColorShiftFullbrightNoDepthLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightAlphaLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.goo.brutality.registry.BrutalityModEntities;
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
        event.registerEntityRenderer(BrutalityModEntities.THROWN_THUNDERBOLT_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.THROWN_GUNGNIR_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.SPECTRAL_MAW_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.EXOBLADE_BEAM.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.PHOTON.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityModEntities.CHAIR_SEAT.get(), BrutalityEntityRenderer::new);

        event.registerEntityRenderer(BrutalityModEntities.CANNONBALL_CABBAGE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.STICK_OF_BUTTER.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.CRIMSON_DELIGHT.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.GOLDEN_PHOENIX.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.CAVENDISH.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.WINTER_MELON.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.STYROFOAM_CUP.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.MUG.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.CINDER_BLOCK.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.STICKY_BOMB.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.BEACH_BALL.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.BLAST_BARREL.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.SCULK_GRENADE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.HOLY_HAND_GRENADE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.STICKY_DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.BOUNCY_DYNAMITE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.PERFUME_BOTTLE.get(), BrutalityAbstractPhysicsTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.ABSOLUTE_ZERO.get(), BrutalityAbstractPhysicsTridentRenderer::new);

        event.registerEntityRenderer(BrutalityModEntities.ICE_CUBE.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.PERMAFROST_CUBE.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.BIOMECH_REACTOR.get(), context -> new BrutalityAbstractPhysicsTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(
                        new AutoGlowingGeoLayer<>(renderer)
                )));

        event.registerEntityRenderer(BrutalityModEntities.DEPTH_CRUSHER.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.THROWN_KNIFE_ENTITY.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.OVERCLOCKED_TOASTER.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.TOAST.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.FATE_CARD.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.VAMPIRE_KNIFE.get(), context -> new BrutalityTridentRenderer<>(context, renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityModEntities.STAR_ENTITY.get(), context -> new BrutalityShurikenRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.CRESCENT_DART_ENTITY.get(), context -> new BrutalityShurikenRenderer<>(context,
                renderer -> renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.PI_ENTITY.get(), BrutalityEntityRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.ABYSS_PROJECTILE.get(), BrutalityEntityRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.BLOODSLASH.get(), BrutalityTridentRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.SHADOWFLAME_SLASH.get(), BrutalityTridentRenderer::new);

        event.registerEntityRenderer(BrutalityModEntities.BLACK_HOLE_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.CRUEL_SUN_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityModEntities.EXPLOSION_RAY.get(), context -> new BrutalitySkyRayRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));




        event.registerEntityRenderer(BrutalityModEntities.LAST_PRISM_RAY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoColorShiftFullbrightNoDepthLayer<>(renderer, 0.1F, rainbowColor[0], rainbowColor[1], rainbowColor[2], rainbowColor[3], rainbowColor[4], rainbowColor[5], rainbowColor[6]));
                }));

        event.registerEntityRenderer(BrutalityModEntities.EXTINCTION_ENTITY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityModEntities.PIERCING_MOONLIGHT_ENTITY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityModEntities.RHONGOMYNIAD_RAY.get(), context -> new BrutalityRayRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityModEntities.SUPERNOVA_ASTEROID.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityModEntities.SUMMONED_STRAY.get(), StrayRenderer::new);

        event.registerEntityRenderer(BrutalityModEntities.LIGHT_ARROW.get(), context -> new BrutalityArrowRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));


        event.registerBlockEntityRenderer(BrutalityModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);
        event.registerBlockEntityRenderer(BrutalityModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), BrutalityGeoBlockRenderer::new);


        event.registerEntityRenderer(BrutalityModEntities.GRAVITIC_IMPLOSION_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.COSMIC_CATACLYSM_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityModEntities.METEOR_SHOWER_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityModEntities.STAR_STREAM_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));
        event.registerEntityRenderer(BrutalityModEntities.SINGULARITY_SHIFT_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> {
                    renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
                    renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer));
                }));

        event.registerEntityRenderer(BrutalityModEntities.ANNIHILATION_ENTITY.get(), context -> new BrutalityAbstractPhysicsProjectileRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.BRIMSPIKE_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer))));

        event.registerEntityRenderer(BrutalityModEntities.CHTHONIC_CAPSULE_ENTITY.get(), BrutalityAbstractPhysicsProjectileRenderer::new);
        event.registerEntityRenderer(BrutalityModEntities.DESTRUCTION_ENTITY.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(renderer))));


        event.registerEntityRenderer(BrutalityModEntities.LIGHT_BINDING.get(), context -> new BrutalityEntityRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));
        event.registerEntityRenderer(BrutalityModEntities.CRESCENT_SCYTHE_ENTITY.get(), context -> new BrutalityTridentRenderer<>(context,
                renderer -> renderer.addRenderLayer(new BrutalityAutoFullbrightLayer<>(renderer))));


    }


}