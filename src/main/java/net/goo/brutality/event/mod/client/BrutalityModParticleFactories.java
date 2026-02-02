package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.particle.base.WaveParticle;
import net.goo.brutality.client.particle.custom.*;
import net.goo.brutality.client.particle.custom.flat.HexingCircleParticle;
import net.goo.brutality.client.particle.custom.flat.MurasamaSlashParticle;
import net.goo.brutality.client.particle.custom.flat.StygianStepParticle;
import net.goo.brutality.client.particle.custom.flat.VoidSlashParticle;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.client.particle.FlameParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModParticleFactories {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        // Register sprite-based particles
        event.registerSpriteSet(BrutalityParticles.COSMIC_PARTICLE.get(1).get(), CosmicParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.COSMIC_PARTICLE.get(0).get(), CosmicParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SUPERNOVA_SWEEP_PARTICLE.get(), SupernovaSweepParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.STAR_PARTICLE.get(), StarParticle.Provider::new);


        event.registerSpriteSet(BrutalityParticles.CABBAGE_PARTICLE.get(), CabbageParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.WINTERMELON_PARTICLE.get(), WintermelonParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SALT_PARTICLE.get(), SaltParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.PEPPER_PARTICLE.get(), PepperParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SLICKED_PARTICLE.get(), SlickedParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.OILED_PARTICLE.get(), OiledParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.ENRAGED_PARTICLE.get(), EnragedParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.YIN_YANG_PARTICLE.get(), YinYangParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.STEAM_PARTICLE.get(), SteamParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.PERFUME_PARTICLE.get(), PerfumeParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.RHAAST_PARTICLE.get(), RhaastParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SHADOW_ASSASIN_PARTICLE.get(), ShadowAssassinParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.YIN_PARTICLE.get(), YinParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.YANG_PARTICLE.get(), YangParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.BLACK_HOLE_PARTICLE.get(), BlackholeParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.HEALING_PARTICLE.get(), HealingParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.MIRACLE_BLIGHT_PARTICLE.get(), MiracleBlightParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.LAST_PRISM_RAY_PARTICLE.get(), LastPrismRayParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.DEPTH_CRUSHER_PARTICLE.get(), DepthCrusherParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SHADOW_SWEEP_PARTICLE.get(), VoidSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SOUL_SWEEP_PARTICLE.get(), SoulSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.MURASAMA_SWEEP_PARTICLE.get(), MurasamaSweepParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.MURASAMA_PARTICLE.get(0).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.MURASAMA_PARTICLE.get(1).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.MURASAMA_PARTICLE.get(2).get(), MurasamaParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.TERRATOMERE_EXPLOSION.get(), TerratomereExplosionParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.BIOMECH_REACTOR_PARTICLE.get(), BiomechReactorParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.BLUE_FLAME_PARTICLE.get(), FlameParticle.SmallFlameProvider::new);
        event.registerSpriteSet(BrutalityParticles.WIZARDRY_PARTICLE.get(), WizardryParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get(), FlatParticleData.FlatParticleProvider::new);
        event.registerSpriteSet(BrutalityParticles.MURASAMA_SLASH_PARTICLE.get(), MurasamaSlashParticle.MurasamaSlashParticleProvider::new);
        event.registerSpriteSet(BrutalityParticles.VOID_SLASH_PARTICLE.get(), VoidSlashParticle.VoidSlashParticleProvider::new);
        event.registerSpriteSet(BrutalityParticles.HEXING_CIRCLE_PARTICLE.get(), HexingCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.STYGIAN_STEP_PARTICLE.get(), StygianStepParticle.Provider::new);

        event.registerSpriteSet(BrutalityParticles.BLOOD_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.HEAL_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.FROSTMOURNE_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.COSMIC_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.SONIC_WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(BrutalityParticles.ANTIMATTER_WAVE.get(), WaveParticle.Provider::new);

        event.registerSpecial(BrutalityParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionParticleEmitter.Provider());
        event.registerSpriteSet(BrutalityParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new);
        event.registerSpecial(BrutalityParticles.BLOOD_EXPLOSION_EMITTER.get(), new BloodExplosionEmitter.Provider());
        event.registerSpriteSet(BrutalityParticles.BLOOD_EXPLOSION_PARTICLE.get(), BloodExplosionParticle.Provider::new);
        event.registerSpecial(BrutalityParticles.NAPALM_EXPLOSION_EMITTER.get(), new NapalmExplosionParticleEmitter.Provider());
        event.registerSpriteSet(BrutalityParticles.NAPALM_EXPLOSION_PARTICLE.get(), NapalmExplosionParticle.Provider::new);

    }
}


