package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.particle.custom.StarburstParticle;
import net.goo.armament.particle.custom.SupernovaSwordParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> supernova_sword_particle_factory = SupernovaSwordParticle.Provider::new;
        event.registerSpriteSet(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(), supernova_sword_particle_factory);

        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> supernova_explosion_particle_factory = StarburstParticle.Provider::new;
        event.registerSpriteSet(ModParticles.STARBURST_PARTICLE.get(), supernova_explosion_particle_factory);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();

        });
    }


}


