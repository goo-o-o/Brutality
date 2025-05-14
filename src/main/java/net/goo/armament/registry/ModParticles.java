package net.goo.armament.registry;

import com.mojang.serialization.Codec;
import net.goo.armament.Armament;
import net.goo.armament.particle.base.FlatParticleWithData;
import net.goo.armament.particle.base.GenericMagicCircleParticle;
import net.goo.armament.particle.base.GenericParticleWithData;
import net.goo.armament.particle.custom.*;
import net.goo.armament.particle.custom.flat.ExplosionMagicCircleParticle;
import net.goo.armament.particle.custom.flat.MurasamaSlash;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Armament.MOD_ID);


    public static final RegistryObject<SimpleParticleType> SUPERNOVA_SWORD_PARTICLE =
            PARTICLE_TYPES.register("supernova_sword_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLACK_HOLE_PARTICLE =
            PARTICLE_TYPES.register("black_hole_entity_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> ZAP_PARTICLE =
            PARTICLE_TYPES.register("zap_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RADIATION_PARTICLE =
            PARTICLE_TYPES.register("radiation_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> STARBURST_PARTICLE =
            PARTICLE_TYPES.register("starburst_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> MURASAMA_PARTICLE =
            PARTICLE_TYPES.register("murasama_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> NUCLEAR_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register("nuclear_explosion_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NUCLEAR_EXPLOSION_EMITTER =
            PARTICLE_TYPES.register("nuclear_explosion_emitter", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> FLAT_PARTICLE =
            PARTICLE_TYPES.register("flat_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> FROSTMOURNE_WAVE_PARTICLE =
            PARTICLE_TYPES.register("frostmourne_wave_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HEAL_WAVE_PARTICLE =
            PARTICLE_TYPES.register("heal_wave_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> POKER_CHIP_RED_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_red_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_BLUE_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_blue_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_GREEN_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_green_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_YELLOW_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_yellow_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> TERRA_PARTICLE =
            PARTICLE_TYPES.register("terra_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SPARKLE_PARTICLE =
            PARTICLE_TYPES.register("sparkle_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SHADOW_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("shadow_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SOUL_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("soul_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MURASAMA_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("murasama_sweep_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<ParticleType<GenericTridentTrail.OrbData>> GENERIC_TRIDENT_TRAIL_PARTICLE = PARTICLE_TYPES.register("generic_trident_trail_particle", () -> new ParticleType<>(false, GenericTridentTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<GenericTridentTrail.OrbData> codec() {
            return GenericTridentTrail.OrbData.CODEC(GENERIC_TRIDENT_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<ThunderboltTrail.OrbData>> THUNDERBOLT_TRAIL_PARTICLE = PARTICLE_TYPES.register("thunderbolt_trail_particle", () -> new ParticleType<>(false, ThunderboltTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<ThunderboltTrail.OrbData> codec() {
            return ThunderboltTrail.OrbData.CODEC(THUNDERBOLT_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<SwordBeamTrail.OrbData>> SWORD_BEAM_TRAIL_PARTICLE = PARTICLE_TYPES.register("sword_beam_trail_particle", () -> new ParticleType<>(false, SwordBeamTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<SwordBeamTrail.OrbData> codec() {
            return SwordBeamTrail.OrbData.CODEC(SWORD_BEAM_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<PlanetTrail.OrbData>> PLANET_TRAIL_PARTICLE = PARTICLE_TYPES.register("planet_trail_particle", () -> new ParticleType<>(false, PlanetTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<PlanetTrail.OrbData> codec() {
            return PlanetTrail.OrbData.CODEC(PLANET_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<GenericParticleWithData.ParticleData>> GENERIC_PARTICLE_WITH_DATA =
            PARTICLE_TYPES.register("generic_particle_with_data",
                    () -> new ParticleType<>(false, GenericParticleWithData.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<GenericParticleWithData.ParticleData> codec() {
                            return GenericParticleWithData.ParticleData.CODEC(GENERIC_PARTICLE_WITH_DATA.get());
                        }
                    });

    public static final RegistryObject<ParticleType<ExplosionAmbientParticle.ParticleData>> EXPLOSION_AMBIENT_PARTICLE =
            PARTICLE_TYPES.register("explosion_ambient_particle",
                    () -> new ParticleType<>(false, ExplosionAmbientParticle.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<ExplosionAmbientParticle.ParticleData> codec() {
                            return ExplosionAmbientParticle.ParticleData.CODEC(EXPLOSION_AMBIENT_PARTICLE.get());
                        }
                    });

    public static final RegistryObject<ParticleType<FlatParticleWithData.ParticleData>> FLAT_PARTICLE_WITH_DATA =
            PARTICLE_TYPES.register("generic_flat_particle_with_data",
                    () -> new ParticleType<>(false, FlatParticleWithData.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<FlatParticleWithData.ParticleData> codec() {
                            return FlatParticleWithData.ParticleData.CODEC(FLAT_PARTICLE_WITH_DATA.get());
                        }
                    });

    public static final RegistryObject<ParticleType<GenericMagicCircleParticle.ParticleData>> GENERIC_MAGIC_CIRCLE_PARTICLE =
            PARTICLE_TYPES.register("generic_magic_circle_particle",
                    () -> new ParticleType<>(false, GenericMagicCircleParticle.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<GenericMagicCircleParticle.ParticleData> codec() {
                            return GenericMagicCircleParticle.ParticleData.CODEC(GENERIC_MAGIC_CIRCLE_PARTICLE.get());
                        }
                    });

    public static final RegistryObject<ParticleType<ExplosionMagicCircleParticle.ParticleData>> EXPLOSION_MAGIC_CIRCLE_PARTICLE =
            PARTICLE_TYPES.register("explosion_magic_circle_particle",
                    () -> new ParticleType<>(false, ExplosionMagicCircleParticle.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<ExplosionMagicCircleParticle.ParticleData> codec() {
                            return ExplosionMagicCircleParticle.ParticleData.CODEC(EXPLOSION_MAGIC_CIRCLE_PARTICLE.get());
                        }
                    });

    public static final RegistryObject<ParticleType<MurasamaSlash.ParticleData>> MURASAMA_SLASH_PARTICLE =
            PARTICLE_TYPES.register("murasama_slash_particle",
                    () -> new ParticleType<>(false, MurasamaSlash.ParticleData.DESERIALIZER) {
                        @Override
                        public Codec<MurasamaSlash.ParticleData> codec() {
                            return MurasamaSlash.ParticleData.CODEC(MURASAMA_SLASH_PARTICLE.get());
                        }
                    });


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
