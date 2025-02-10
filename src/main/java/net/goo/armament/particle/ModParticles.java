package net.goo.armament.particle;

import net.goo.armament.Armament;
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




    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
