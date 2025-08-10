package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.particle.base.FlatParticle;
import net.goo.brutality.particle.base.GenericMagicCircleParticle;
import net.goo.brutality.particle.custom.*;
import net.goo.brutality.particle.custom.flat.ExplosionMagicCircleParticle;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.goo.brutality.particle.providers.TrailParticleData;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModParticleFactories {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        // Register sprite-based particles
        event.registerSpriteSet(BrutalityModParticles.COSMIC_PARTICLE.get(1).get(), CosmicParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.COSMIC_PARTICLE.get(0).get(), CosmicParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SUPERNOVA_SWEEP_PARTICLE.get(), SupernovaSweepParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.STAR_PARTICLE.get(), StarParticle.Provider::new);


        event.registerSpriteSet(BrutalityModParticles.CABBAGE_PARTICLE.get(), CabbageParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.WINTERMELON_PARTICLE.get(), WintermelonParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SALT_PARTICLE.get(), SaltParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.PEPPER_PARTICLE.get(), PepperParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SLICKED_PARTICLE.get(), SlickedParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.OILED_PARTICLE.get(), OiledParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.ENRAGED_PARTICLE.get(), EnragedParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.STEAM_PARTICLE.get(), SteamParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.RHAAST_PARTICLE.get(), RhaastParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SHADOW_ASSASIN_PARTICLE.get(), ShadowAssassinParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.BLACK_HOLE_PARTICLE.get(), BlackholeParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MIRACLE_BLIGHT_PARTICLE.get(), MiracleBlightParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.LAST_PRISM_RAY_PARTICLE.get(), LastPrismRayParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.DEPTH_CRUSHER_PARTICLE.get(), DepthCrusherParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SHADOW_SWEEP_PARTICLE.get(), VoidSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SOUL_SWEEP_PARTICLE.get(), SoulSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_SWEEP_PARTICLE.get(), MurasamaSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.STARBURST_PARTICLE.get(), StarburstParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(0).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(1).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(2).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.TERRATOMERE_EXPLOSION.get(), TerratomereExplosionParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.EXOBLADE_FLASH_PARTICLE.get(), ExobladeFlashParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.BIOMECH_REACTOR_PARTICLE.get(), BiomechReactorParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.FLAT_PARTICLE.get(), FlatParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new);


        event.registerSpriteSet(BrutalityModParticles.HEAL_WAVE.get(), WaveParticleData.WaveParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.FROSTMOURNE_WAVE.get(), WaveParticleData.WaveParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.COSMIC_WAVE.get(), WaveParticleData.WaveParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.ANTIMATTER_WAVE.get(), WaveParticleData.WaveParticleProvider::new);

        event.registerSpriteSet(BrutalityModParticles.TRAIL_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.RAINBOW_TRAIL_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.CELESTIAL_STARBOARD_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.RUINED_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.CREASE_OF_CREATION_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.TERRATOMERE_PARTICLE.get(),
                TrailParticleData.TrailParticleProvider::new);

        event.registerSpecial(BrutalityModParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionSeedParticle.Provider());
//        event.registerSpecial(BrutalityModParticles.GENERIC_WORLD_ALIGNED_TRAIL_PARTICLE.get(), new AbstractWorldAlignedTrailParticle.OrbFactory());
//        event.registerSpecial(BrutalityModParticles.CAMERA_ALIGNED_ORBITING_TRAIL_PARTICLE.get(), new CameraAlignedOrbitingTrailParticle.OrbFactory());
//        event.registerSpecial(BrutalityModParticles.GENERIC_TRIDENT_TRAIL_PARTICLE.get(), new GenericTridentTrailParticle.OrbFactory());
//        event.registerSpecial(BrutalityModParticles.CREASE_OF_CREATION_PARTICLE.get(), new CreaseOfCreationParticle.OrbFactory());
//        event.registerSpecial(BrutalityModParticles.TERRATOMERE_PARTICLE.get(), new TerratomereParticle.OrbFactory());

        event.registerSpriteSet(BrutalityModParticles.GENERIC_MAGIC_CIRCLE_PARTICLE.get(), GenericMagicCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get(), ExplosionMagicCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.EXPLOSION_AMBIENT_PARTICLE.get(), ExplosionAmbientParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(), MurasamaSlash.Provider::new);
    }
}


