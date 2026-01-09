package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.particle.base.WaveParticle;
import net.goo.brutality.particle.custom.*;
import net.goo.brutality.particle.custom.flat.HexingCircleParticle;
import net.goo.brutality.particle.custom.flat.MurasamaSlashParticle;
import net.goo.brutality.particle.custom.flat.StygianStepParticle;
import net.goo.brutality.particle.custom.flat.VoidSlashParticle;
import net.goo.brutality.particle.providers.FlatParticleData;
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
        event.registerSpriteSet(BrutalityModParticles.YIN_YANG_PARTICLE.get(), YinYangParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.STEAM_PARTICLE.get(), SteamParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.PERFUME_PARTICLE.get(), PerfumeParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.RHAAST_PARTICLE.get(), RhaastParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SHADOW_ASSASIN_PARTICLE.get(), ShadowAssassinParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.YIN_PARTICLE.get(), YinParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.YANG_PARTICLE.get(), YangParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.BLACK_HOLE_PARTICLE.get(), BlackholeParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.HEALING_PARTICLE.get(), HealingParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MIRACLE_BLIGHT_PARTICLE.get(), MiracleBlightParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.LAST_PRISM_RAY_PARTICLE.get(), LastPrismRayParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.DEPTH_CRUSHER_PARTICLE.get(), DepthCrusherParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SHADOW_SWEEP_PARTICLE.get(), VoidSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SOUL_SWEEP_PARTICLE.get(), SoulSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_SWEEP_PARTICLE.get(), MurasamaSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(0).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(1).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_PARTICLE.get(2).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.TERRATOMERE_EXPLOSION.get(), TerratomereExplosionParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.BIOMECH_REACTOR_PARTICLE.get(), BiomechReactorParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get(), FlatParticleData.FlatParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(), MurasamaSlashParticle.MurasamaSlashParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.VOID_SLASH_PARTICLE.get(), VoidSlashParticle.VoidSlashParticleProvider::new);
        event.registerSpriteSet(BrutalityModParticles.HEXING_CIRCLE_PARTICLE.get(), HexingCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.STYGIAN_STEP_PARTICLE.get(), StygianStepParticle.Provider::new);

        event.registerSpriteSet(BrutalityModParticles.BLOOD_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.HEAL_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.FROSTMOURNE_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.COSMIC_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.SONIC_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.ANTIMATTER_WAVE.get(), WaveParticle.Provider::new);

        event.registerSpecial(BrutalityModParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionParticleEmitter.Provider());
        event.registerSpriteSet(BrutalityModParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new);
        event.registerSpecial(BrutalityModParticles.BLOOD_EXPLOSION_EMITTER.get(), new BloodExplosionEmitter.Provider());
        event.registerSpriteSet(BrutalityModParticles.BLOOD_EXPLOSION_PARTICLE.get(), BloodExplosionParticle.Provider::new);
        event.registerSpecial(BrutalityModParticles.NAPALM_EXPLOSION_EMITTER.get(), new NapalmExplosionParticleEmitter.Provider());
        event.registerSpriteSet(BrutalityModParticles.NAPALM_EXPLOSION_PARTICLE.get(), NapalmExplosionParticle.Provider::new);

    }
}


