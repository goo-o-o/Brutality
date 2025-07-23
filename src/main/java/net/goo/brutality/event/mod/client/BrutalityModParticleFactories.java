package net.goo.brutality.event.mod.client;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.scythe.FallenScythe;
import net.goo.brutality.item.weapon.sword.MurasamaSword;
import net.goo.brutality.item.weapon.sword.ShadowstepSword;
import net.goo.brutality.item.weapon.sword.SupernovaSword;
import net.goo.brutality.particle.base.*;
import net.goo.brutality.particle.custom.*;
import net.goo.brutality.particle.custom.flat.ExplosionMagicCircleParticle;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.goo.brutality.particle.custom.flat.waves.FrostmourneWave;
import net.goo.brutality.particle.custom.flat.waves.HealWave;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrutalityModParticleFactories {


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        // Register sprite-based particles
        registerSpriteParticles(event,
                Pair.of(BrutalityModParticles.SUPERNOVA_PARTICLE.get(1).get(), SupernovaParticle.Provider::new),
                Pair.of(BrutalityModParticles.SUPERNOVA_PARTICLE.get(0).get(), SupernovaParticle.Provider::new),
                Pair.of(BrutalityModParticles.SUPERNOVA_SWEEP_PARTICLE.get(), SupernovaSweepParticle.Provider::new),

                Pair.of(BrutalityModParticles.STAR_PARTICLE.get(), StarParticle.Provider::new),


                Pair.of(BrutalityModParticles.CABBAGE_PARTICLE.get(), CabbageParticle.Provider::new),
                Pair.of(BrutalityModParticles.WINTERMELON_PARTICLE.get(), WintermelonParticle.Provider::new),
                Pair.of(BrutalityModParticles.SALT_PARTICLE.get(), SaltParticle.Provider::new),
                Pair.of(BrutalityModParticles.PEPPER_PARTICLE.get(), PepperParticle.Provider::new),
                Pair.of(BrutalityModParticles.SLICKED_PARTICLE.get(), SlickedParticle.Provider::new),
                Pair.of(BrutalityModParticles.OILED_PARTICLE.get(), OiledParticle.Provider::new),
                Pair.of(BrutalityModParticles.ENRAGED_PARTICLE.get(), EnragedParticle.Provider::new),
                Pair.of(BrutalityModParticles.STEAM_PARTICLE.get(), SteamParticle.Provider::new),

                Pair.of(BrutalityModParticles.RHAAST_PARTICLE.get(), RhaastParticle.Provider::new),
                Pair.of(BrutalityModParticles.SHADOW_ASSASIN_PARTICLE.get(), ShadowAssassinParticle.Provider::new),

                Pair.of(BrutalityModParticles.BLACK_HOLE_PARTICLE.get(), BlackholeParticle.Provider::new),
                Pair.of(BrutalityModParticles.MIRACLE_BLIGHT_PARTICLE.get(), MiracleBlightParticle.Provider::new),
                Pair.of(BrutalityModParticles.LAST_PRISM_RAY_PARTICLE.get(), LastPrismRayParticle.Provider::new),
                Pair.of(BrutalityModParticles.DEPTH_CRUSHER_PARTICLE.get(), DepthCrusherParticle.Provider::new),
                Pair.of(BrutalityModParticles.SHADOW_SWEEP_PARTICLE.get(), VoidSweepParticle.Provider::new),
                Pair.of(BrutalityModParticles.SOUL_SWEEP_PARTICLE.get(), SoulSweepParticle.Provider::new),
                Pair.of(BrutalityModParticles.MURASAMA_SWEEP_PARTICLE.get(), MurasamaSweepParticle.Provider::new),
                Pair.of(BrutalityModParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new),
                Pair.of(BrutalityModParticles.STARBURST_PARTICLE.get(), StarburstParticle.Provider::new),
                Pair.of(BrutalityModParticles.MURASAMA_PARTICLE.get(0).get(), MurasamaParticle.Provider::new),
                Pair.of(BrutalityModParticles.MURASAMA_PARTICLE.get(1).get(), MurasamaParticle.Provider::new),
                Pair.of(BrutalityModParticles.MURASAMA_PARTICLE.get(2).get(), MurasamaParticle.Provider::new),
                Pair.of(BrutalityModParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new),
                Pair.of(BrutalityModParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new),
                Pair.of(BrutalityModParticles.TERRATOMERE_EXPLOSION.get(), TerratomereExplosionParticle.Provider::new),
                Pair.of(BrutalityModParticles.EXOBLADE_FLASH_PARTICLE.get(), ExobladeFlashParticle.Provider::new),
                Pair.of(BrutalityModParticles.BIOMECH_REACTOR_PARTICLE.get(), BiomechReactorParticle.Provider::new),
                Pair.of(BrutalityModParticles.FLAT_PARTICLE.get(), FlatParticle.Provider::new),
                Pair.of(BrutalityModParticles.FROSTMOURNE_WAVE_PARTICLE.get(), FrostmourneWave.Provider::new),
                Pair.of(BrutalityModParticles.HEAL_WAVE_PARTICLE.get(), HealWave.Provider::new),
                Pair.of(BrutalityModParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new)
        );

        event.registerSpecial(BrutalityModParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionSeedParticle.Provider());
        event.registerSpecial(BrutalityModParticles.GENERIC_WORLD_ALIGNED_TRAIL_PARTICLE.get(), new AbstractWorldAlignedTrailParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.GENERIC_CAMERA_ALIGNED_TRAIL_PARTICLE.get(), new AbstractCameraAlignedTrailParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.CAMERA_ALIGNED_ORBITING_TRAIL_PARTICLE.get(), new CameraAlignedOrbitingTrailParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.GENERIC_TRIDENT_TRAIL_PARTICLE.get(), new GenericTridentTrailParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.RAINBOW_TRAIL_PARTICLE.get(), new RainbowTrailParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.CELESTIAL_STARBOARD_PARTICLE.get(), new CelestialStarboardParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.CREASE_OF_CREATION_PARTICLE.get(), new CreaseOfCreationParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.RUINED_PARTICLE.get(), new RuinedParticle.OrbFactory());
        event.registerSpecial(BrutalityModParticles.TERRATOMERE_PARTICLE.get(), new TerratomereParticle.OrbFactory());

        event.registerSpriteSet(BrutalityModParticles.GENERIC_MAGIC_CIRCLE_PARTICLE.get(), GenericMagicCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get(), ExplosionMagicCircleParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.EXPLOSION_AMBIENT_PARTICLE.get(), ExplosionAmbientParticle.Provider::new);
        event.registerSpriteSet(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(), MurasamaSlash.Provider::new);
    }

    // For standard SimpleParticleType particles
    @SafeVarargs
    private static void registerSpriteParticles(RegisterParticleProvidersEvent event,
                                                Pair<SimpleParticleType, ParticleEngine.SpriteParticleRegistration<SimpleParticleType>>... particles) {
        for (var pair : particles) {
            event.registerSpriteSet(pair.getLeft(), pair.getRight());
        }
    }




    private static Map<Class<? extends Item>, Supplier<ParticleOptions>> PARTICLE_SUPPLIERS;

    public static void init() {
        PARTICLE_SUPPLIERS = Map.of(
                FallenScythe.class, BrutalityModParticles.SOUL_SWEEP_PARTICLE::get,
                ShadowstepSword.class, BrutalityModParticles.SHADOW_SWEEP_PARTICLE::get,
                MurasamaSword.class, BrutalityModParticles.MURASAMA_SWEEP_PARTICLE::get,
                SupernovaSword.class, BrutalityModParticles.SUPERNOVA_SWEEP_PARTICLE::get
        );
    }

    public static Optional<ParticleOptions> getParticleForItem(Item item) {
        if (PARTICLE_SUPPLIERS == null) init();

        return PARTICLE_SUPPLIERS.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(item))
                .findFirst()
                .map(entry -> entry.getValue().get());
    }

}


