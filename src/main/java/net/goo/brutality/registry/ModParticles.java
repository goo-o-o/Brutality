package net.goo.brutality.registry;

import com.mojang.serialization.Codec;
import net.goo.brutality.Brutality;
import net.goo.brutality.particle.base.*;
import net.goo.brutality.particle.custom.CreaseOfCreationParticle;
import net.goo.brutality.particle.custom.ExplosionAmbientParticle;
import net.goo.brutality.particle.custom.flat.ExplosionMagicCircleParticle;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Brutality.MOD_ID);

    private static RegistryObject<SimpleParticleType> registerSimpleParticle(String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(true));
    }
    public static final List<RegistryObject<SimpleParticleType>> SUPERNOVA_PARTICLE = List.of(
            registerSimpleParticle("supernova_particle_purple"),
            registerSimpleParticle("supernova_particle_yellow")
    );

    public static final List<RegistryObject<SimpleParticleType>> MURASAMA_PARTICLE = List.of(
            registerSimpleParticle("murasama_particle_a"),
            registerSimpleParticle("murasama_particle_b"),
            registerSimpleParticle("murasama_particle_c")
    );

    public static final RegistryObject<SimpleParticleType> CABBAGE_PARTICLE =
            PARTICLE_TYPES.register("cabbage_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WINTERMELON_PARTICLE =
            PARTICLE_TYPES.register("wintermelon_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> BLACK_HOLE_PARTICLE =
            PARTICLE_TYPES.register("black_hole_entity_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> ZAP_PARTICLE =
            PARTICLE_TYPES.register("zap_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RADIATION_PARTICLE =
            PARTICLE_TYPES.register("radiation_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> STARBURST_PARTICLE =
            PARTICLE_TYPES.register("starburst_particle", () -> new SimpleParticleType(true));

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
    public static final RegistryObject<SimpleParticleType> SUPERNOVA_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("supernova_sweep_particle", () -> new SimpleParticleType(true));


    public static final RegistryObject<ParticleType<AbstractWorldAlignedTrailParticle.OrbData>> GENERIC_WORLD_ALIGNED_TRAIL_PARTICLE =
            PARTICLE_TYPES.register("generic_world_aligned_trail_particle", ()
                    -> new ParticleType<>(false, AbstractWorldAlignedTrailParticle.OrbData.DESERIALIZER) {
        @Override
        public Codec<AbstractWorldAlignedTrailParticle.OrbData> codec() {
            return AbstractWorldAlignedTrailParticle.OrbData.CODEC(GENERIC_WORLD_ALIGNED_TRAIL_PARTICLE.get());
        }
    });
    public static final RegistryObject<ParticleType<AbstractCameraAlignedTrailParticle.OrbData>> GENERIC_CAMERA_ALIGNED_TRAIL_PARTICLE =
            PARTICLE_TYPES.register("generic_camera_aligned_trail_particle", ()
                    -> new ParticleType<>(false, AbstractCameraAlignedTrailParticle.OrbData.DESERIALIZER) {
        @Override
        public Codec<AbstractCameraAlignedTrailParticle.OrbData> codec() {
            return AbstractCameraAlignedTrailParticle.OrbData.CODEC(GENERIC_CAMERA_ALIGNED_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<CreaseOfCreationParticle.OrbData>> CREASE_OF_CREATION_PARTICLE =
            PARTICLE_TYPES.register("crease_of_creation_particle", ()
                    -> new ParticleType<>(false, CreaseOfCreationParticle.OrbData.DESERIALIZER) {
        @Override
        public Codec<CreaseOfCreationParticle.OrbData> codec() {
            return CreaseOfCreationParticle.OrbData.CODEC(CREASE_OF_CREATION_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<GenericTridentTrailParticle.OrbData>> GENERIC_TRIDENT_TRAIL_PARTICLE =
            PARTICLE_TYPES.register("trident_trail_particle", () ->
                    new ParticleType<>(false, GenericTridentTrailParticle.OrbData.DESERIALIZER) {

        @Override
        public Codec<GenericTridentTrailParticle.OrbData> codec() {
            return GenericTridentTrailParticle.OrbData.CODEC(GENERIC_TRIDENT_TRAIL_PARTICLE.get());
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
