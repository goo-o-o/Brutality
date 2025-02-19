package net.goo.armament.registry;

import com.mojang.serialization.Codec;
import net.goo.armament.Armament;
import net.goo.armament.particle.custom.SwordBeamTrail;
import net.goo.armament.particle.custom.ThunderboltTrail;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles{
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Armament.MOD_ID);

    public static final RegistryObject<SimpleParticleType> SUPERNOVA_SWORD_PARTICLE =
            PARTICLE_TYPES.register("supernova_sword_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLACK_HOLE_PARTICLE =
            PARTICLE_TYPES.register("black_hole_entity_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> VOID_SWEEP_PARTICLE =
            PARTICLE_TYPES.register("void_sweep_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ZAP_PARTICLE =
            PARTICLE_TYPES.register("zap_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> STARBURST_PARTICLE =
            PARTICLE_TYPES.register("starburst_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> POKER_CHIP_RED_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_red_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_BLUE_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_blue_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_GREEN_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_green_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POKER_CHIP_YELLOW_PARTICLE =
            PARTICLE_TYPES.register("poker_chip_yellow_particle", () -> new SimpleParticleType(true));

    public static final RegistryObject<ParticleType<ThunderboltTrail.OrbData>> THUNDERBOLT_TRAIL_PARTICLE = PARTICLE_TYPES.register("thunderbolt_trail_particle", () -> new ParticleType<ThunderboltTrail.OrbData>(false, ThunderboltTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<ThunderboltTrail.OrbData> codec() {
            return ThunderboltTrail.OrbData.CODEC(THUNDERBOLT_TRAIL_PARTICLE.get());
        }
    });

    public static final RegistryObject<ParticleType<SwordBeamTrail.OrbData>> SWORD_BEAM_TRAIL_PARTICLE = PARTICLE_TYPES.register("sword_beam_trail_particle", () -> new ParticleType<SwordBeamTrail.OrbData>(false, SwordBeamTrail.OrbData.DESERIALIZER) {
        @Override
        public Codec<SwordBeamTrail.OrbData> codec() {
            return SwordBeamTrail.OrbData.CODEC(SWORD_BEAM_TRAIL_PARTICLE.get());
        }
    });


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
