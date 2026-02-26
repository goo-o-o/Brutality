package net.goo.brutality.common.registry;

import com.mojang.serialization.Codec;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.goo.brutality.client.particle.providers.PointToPointParticleData;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrutalityParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Brutality.MOD_ID);

    private static RegistryObject<SimpleParticleType> registerSimpleParticle(String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(true));
    }

    public static final List<RegistryObject<SimpleParticleType>> COSMIC_PARTICLE = List.of(
            registerSimpleParticle("yellow_cosmic_particle"),
            registerSimpleParticle("purple_cosmic_particle")
    );

    public static final List<RegistryObject<SimpleParticleType>> MURASAMA_PARTICLE = List.of(
            registerSimpleParticle("murasama_particle_a"),
            registerSimpleParticle("murasama_particle_b"),
            registerSimpleParticle("murasama_particle_c")
    );


    public static final RegistryObject<SimpleParticleType> DEPTH_CRUSHER_PARTICLE =
            PARTICLE_TYPES.register("depth_crusher_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> BLUE_FLAME_PARTICLE =
            PARTICLE_TYPES.register("blue_flame_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WIZARDRY_PARTICLE =
            PARTICLE_TYPES.register("wizardry_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> CABBAGE_PARTICLE =
            PARTICLE_TYPES.register("cabbage_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WINTERMELON_PARTICLE =
            PARTICLE_TYPES.register("wintermelon_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SALT_PARTICLE =
            PARTICLE_TYPES.register("salt_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PEPPER_PARTICLE =
            PARTICLE_TYPES.register("pepper_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SLICKED_PARTICLE =
            PARTICLE_TYPES.register("slicked_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> OILED_PARTICLE =
            PARTICLE_TYPES.register("oiled_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STEAM_PARTICLE =
            PARTICLE_TYPES.register("steam_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PERFUME_PARTICLE =
            PARTICLE_TYPES.register("perfume_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> RHAAST_PARTICLE =
            PARTICLE_TYPES.register("rhaast_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SHADOW_ASSASIN_PARTICLE =
            PARTICLE_TYPES.register("shadow_assassin_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YIN_PARTICLE =
            PARTICLE_TYPES.register("yin_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YANG_PARTICLE =
            PARTICLE_TYPES.register("yang_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> ENRAGED_PARTICLE =
            PARTICLE_TYPES.register("enraged_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YIN_YANG_PARTICLE =
            PARTICLE_TYPES.register("yin_yang_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> BLACK_HOLE_PARTICLE =
            PARTICLE_TYPES.register("black_hole_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HEALING_PARTICLE =
            PARTICLE_TYPES.register("healing_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MIRACLE_BLIGHT_PARTICLE =
            PARTICLE_TYPES.register("miracle_blight_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> LAST_PRISM_RAY_PARTICLE =
            PARTICLE_TYPES.register("last_prism_ray_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> RADIATION_PARTICLE =
            PARTICLE_TYPES.register("radiation_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> NUCLEAR_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register("nuclear_explosion_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NUCLEAR_EXPLOSION_EMITTER =
            PARTICLE_TYPES.register("nuclear_explosion_emitter", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> NAPALM_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register("napalm_explosion_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NAPALM_EXPLOSION_EMITTER =
            PARTICLE_TYPES.register("napalm_explosion_emitter", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> BLOOD_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register("blood_explosion_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLOOD_EXPLOSION_EMITTER =
            PARTICLE_TYPES.register("blood_explosion_emitter", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLOOD_PARTICLE =
            PARTICLE_TYPES.register("blood_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> TERRA_PARTICLE =
            PARTICLE_TYPES.register("terra_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BIOMECH_REACTOR_PARTICLE =
            PARTICLE_TYPES.register("biomech_reactor_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SPARKLE_PARTICLE =
            PARTICLE_TYPES.register("sparkle_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> STAR_PARTICLE =
            PARTICLE_TYPES.register("star_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> TERRATOMERE_EXPLOSION =
            PARTICLE_TYPES.register("terratomere_explosion_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SHADOW_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("shadow_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SOUL_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("soul_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MURASAMA_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("murasama_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SUPERNOVA_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("supernova_sweep_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<ParticleType<FlatParticleData<?>>> EXPLOSION_MAGIC_CIRCLE_PARTICLE = PARTICLE_TYPES.register("explosion_magic_circle", () ->
            new ParticleType<>(false, FlatParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<FlatParticleData<?>> codec() { return FlatParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<FlatParticleData<?>>> MURASAMA_SLASH_PARTICLE = PARTICLE_TYPES.register("murasama_slash_particle", () ->
            new ParticleType<>(false, FlatParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<FlatParticleData<?>> codec() {
                    return FlatParticleData.CODEC;
                }
            });
    public static final RegistryObject<ParticleType<FlatParticleData<?>>> VOID_SLASH_PARTICLE = PARTICLE_TYPES.register("void_slash_particle", () ->
            new ParticleType<>(false, FlatParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<FlatParticleData<?>> codec() {
                    return FlatParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<FlatParticleData<?>>> HEXING_CIRCLE_PARTICLE = PARTICLE_TYPES.register("hexing_star_particle", () ->
            new ParticleType<>(false, FlatParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<FlatParticleData<?>> codec() {
                    return FlatParticleData.CODEC;
                }
            });


    public static final RegistryObject<ParticleType<PointToPointParticleData<?>>> STYGIAN_STEP_PARTICLE = PARTICLE_TYPES.register("stygian_step_particle", () ->
            new ParticleType<>(false, PointToPointParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<PointToPointParticleData<?>> codec() {
                    return PointToPointParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> HEAL_WAVE = PARTICLE_TYPES.register("heal_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> BLOOD_WAVE = PARTICLE_TYPES.register("blood_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> FROSTMOURNE_WAVE = PARTICLE_TYPES.register("frostmourne_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> ANTIMATTER_WAVE = PARTICLE_TYPES.register("antimatter_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> COSMIC_WAVE = PARTICLE_TYPES.register("cosmic_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> SONIC_BOOM_WAVE = PARTICLE_TYPES.register("sonic_boom_wave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<WaveParticleData<?>>> SHOCKWAVE = PARTICLE_TYPES.register("shockwave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });
    public static final RegistryObject<ParticleType<WaveParticleData<?>>> SEISMIC_SHOCKWAVE = PARTICLE_TYPES.register("seismic_shockwave", () ->
            new ParticleType<>(false, WaveParticleData.DESERIALIZER) {
                @Override
                public @NotNull Codec<WaveParticleData<?>> codec() {
                    return WaveParticleData.CODEC;
                }
            });


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
