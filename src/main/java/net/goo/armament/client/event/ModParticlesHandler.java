package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.weapon.custom.FallenScythe;
import net.goo.armament.item.weapon.custom.MurasamaSword;
import net.goo.armament.item.noir.ShadowstepSword;
import net.goo.armament.particle.base.FlatParticle;
import net.goo.armament.particle.base.GenericMagicCircleParticle;
import net.goo.armament.particle.custom.*;
import net.goo.armament.particle.custom.flat.ExplosionMagicCircleParticle;
import net.goo.armament.particle.custom.flat.MurasamaSlash;
import net.goo.armament.particle.custom.flat.waves.FrostmourneWave;
import net.goo.armament.particle.custom.flat.waves.HealWave;
import net.goo.armament.particle.custom.pokerchip.PokerChipBlueParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipGreenParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipRedParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipYellowParticle;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticlesHandler {


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        // Register sprite-based particles
        registerSpriteParticles(event,
                Pair.of(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(), SupernovaSwordParticle.Provider::new),
                Pair.of(ModParticles.BLACK_HOLE_PARTICLE.get(), BlackholeEntityParticle.Provider::new),
                Pair.of(ModParticles.SHADOW_SWEEP_PARTICLE.get(), VoidSweepParticle.Provider::new),
                Pair.of(ModParticles.SOUL_SWEEP_PARTICLE.get(), SoulSweepParticle.Provider::new),
                Pair.of(ModParticles.MURASAMA_SWEEP_PARTICLE.get(), MurasamaSweepParticle.Provider::new),
                Pair.of(ModParticles.ZAP_PARTICLE.get(), ZapParticle.Provider::new),
                Pair.of(ModParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new),
                Pair.of(ModParticles.STARBURST_PARTICLE.get(), StarburstParticle.Provider::new),
                Pair.of(ModParticles.MURASAMA_PARTICLE.get(), MurasamaParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_RED_PARTICLE.get(), PokerChipRedParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_GREEN_PARTICLE.get(), PokerChipGreenParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_YELLOW_PARTICLE.get(), PokerChipYellowParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_BLUE_PARTICLE.get(), PokerChipBlueParticle.Provider::new),
                Pair.of(ModParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new),
                Pair.of(ModParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new),
                Pair.of(ModParticles.FLAT_PARTICLE.get(), FlatParticle.Provider::new),
                Pair.of(ModParticles.FROSTMOURNE_WAVE_PARTICLE.get(), FrostmourneWave.Provider::new),
                Pair.of(ModParticles.HEAL_WAVE_PARTICLE.get(), HealWave.Provider::new),
                Pair.of(ModParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new)
        );

        event.registerSpecial(ModParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionSeedParticle.Provider());
        event.registerSpecial(ModParticles.GENERIC_TRIDENT_TRAIL_PARTICLE.get(), new GenericTridentTrail.OrbFactory());
        event.registerSpecial(ModParticles.THUNDERBOLT_TRAIL_PARTICLE.get(), new ThunderboltTrail.OrbFactory());
        event.registerSpecial(ModParticles.SWORD_BEAM_TRAIL_PARTICLE.get(), new SwordBeamTrail.OrbFactory());
        event.registerSpecial(ModParticles.PLANET_TRAIL_PARTICLE.get(), new PlanetTrail.OrbFactory());
        event.registerSpriteSet(ModParticles.GENERIC_MAGIC_CIRCLE_PARTICLE.get(), GenericMagicCircleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.EXPLOSION_MAGIC_CIRCLE_PARTICLE.get(), ExplosionMagicCircleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.EXPLOSION_AMBIENT_PARTICLE.get(), ExplosionAmbientParticle.Provider::new);
        event.registerSpriteSet(ModParticles.MURASAMA_SLASH_PARTICLE.get(), MurasamaSlash.Provider::new);
    }

    // For standard SimpleParticleType particles
    private static void registerSpriteParticles(RegisterParticleProvidersEvent event,
                                                Pair<SimpleParticleType, ParticleEngine.SpriteParticleRegistration<SimpleParticleType>>... particles) {
        for (var pair : particles) {
            event.registerSpriteSet(pair.getLeft(), pair.getRight());
        }
    }




    private static Map<Class<? extends Item>, Supplier<ParticleOptions>> PARTICLE_SUPPLIERS;

    public static void init() {
        PARTICLE_SUPPLIERS = Map.of(
                FallenScythe.class, ModParticles.SOUL_SWEEP_PARTICLE::get,
                ShadowstepSword.class, ModParticles.SHADOW_SWEEP_PARTICLE::get,
                MurasamaSword.class, ModParticles.MURASAMA_SWEEP_PARTICLE::get
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


