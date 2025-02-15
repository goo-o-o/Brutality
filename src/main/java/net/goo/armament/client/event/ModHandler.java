package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
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

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModHandler {


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }



    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> supernova_sword_particle_factory = SupernovaSwordParticle.Provider::new;
        event.registerSpriteSet(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(), supernova_sword_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> black_hole_entity_particle_factory = BlackholeEntityParticle.Provider::new;
        event.registerSpriteSet(ModParticles.BLACK_HOLE_PARTICLE.get(), black_hole_entity_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> void_sweep_particle_factory = VoidSweepParticle.Provider::new;
        event.registerSpriteSet(ModParticles.VOID_SWEEP_PARTICLE.get(), void_sweep_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> zap_particle_factory = ZapParticle.Provider::new;
        event.registerSpriteSet(ModParticles.ZAP_PARTICLE.get(), zap_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> supernova_explosion_particle_factory = StarburstParticle.Provider::new;
        event.registerSpriteSet(ModParticles.STARBURST_PARTICLE.get(), supernova_explosion_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> poker_chip_red_particle_factory = PokerChipRedParticle.Provider::new;
        event.registerSpriteSet(ModParticles.POKER_CHIP_RED_PARTICLE.get(), poker_chip_red_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> poker_chip_green_particle_factory = PokerChipGreenParticle.Provider::new;
        event.registerSpriteSet(ModParticles.POKER_CHIP_GREEN_PARTICLE.get(), poker_chip_green_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> poker_chip_yellow_particle_factory = PokerChipYellowParticle.Provider::new;
        event.registerSpriteSet(ModParticles.POKER_CHIP_YELLOW_PARTICLE.get(), poker_chip_yellow_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> poker_chip_blue_particle_factory = PokerChipBlueParticle.Provider::new;
        event.registerSpriteSet(ModParticles.POKER_CHIP_BLUE_PARTICLE.get(), poker_chip_blue_particle_factory);

        event.registerSpecial(ModParticles.THUNDERBOLT_TRAIL_PARTICLE.get(), new ThunderboltTrailParticle.OrbFactory());

    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }


}


