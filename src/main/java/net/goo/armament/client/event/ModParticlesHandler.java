package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.particle.base.sword_waves.SwordWaveParticle;
import net.goo.armament.particle.custom.*;
import net.goo.armament.particle.custom.pokerchip.PokerChipBlueParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipGreenParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipRedParticle;
import net.goo.armament.particle.custom.pokerchip.PokerChipYellowParticle;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.commons.lang3.tuple.Pair;

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
                Pair.of(ModParticles.ZAP_PARTICLE.get(), ZapParticle.Provider::new),
                Pair.of(ModParticles.RADIATION_PARTICLE.get(), RadiationParticle.Provider::new),
                Pair.of(ModParticles.STARBURST_PARTICLE.get(), StarburstParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_RED_PARTICLE.get(), PokerChipRedParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_GREEN_PARTICLE.get(), PokerChipGreenParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_YELLOW_PARTICLE.get(), PokerChipYellowParticle.Provider::new),
                Pair.of(ModParticles.POKER_CHIP_BLUE_PARTICLE.get(), PokerChipBlueParticle.Provider::new),
                Pair.of(ModParticles.TERRA_PARTICLE.get(), TerraParticle.Provider::new),
                Pair.of(ModParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Provider::new),
                Pair.of(ModParticles.SWORD_WAVE_PARTICLE.get(), SwordWaveParticle.Provider::new),
                Pair.of(ModParticles.FROSTMOURNE_WAVE_PARTICLE.get(), FrostmourneWave.Provider::new),
                Pair.of(ModParticles.NUCLEAR_EXPLOSION_PARTICLE.get(), NuclearExplosionParticle.Provider::new)
        );

        event.registerSpecial(ModParticles.NUCLEAR_EXPLOSION_EMITTER.get(), new NuclearExplosionSeedParticle.Provider());
        event.registerSpecial(ModParticles.THUNDERBOLT_TRAIL_PARTICLE.get(), new TridentTrail.OrbFactory());
        event.registerSpecial(ModParticles.SWORD_BEAM_TRAIL_PARTICLE.get(), new SwordBeamTrail.OrbFactory());
        event.registerSpecial(ModParticles.PLANET_TRAIL_PARTICLE.get(), new PlanetTrail.OrbFactory());

    }

    // Helper method to register sprite-based particles
    private static void registerSpriteParticles(RegisterParticleProvidersEvent event, Pair<SimpleParticleType, ParticleEngine.SpriteParticleRegistration<SimpleParticleType>>... particles) {
        for (Pair<SimpleParticleType, ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> pair : particles) {
            event.registerSpriteSet(pair.getLeft(), pair.getRight());
        }
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }


}


