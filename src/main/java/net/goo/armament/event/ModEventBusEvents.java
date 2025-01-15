package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.particle.custom.SupernovaSwordParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {


    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        ParticleEngine.SpriteParticleRegistration<SimpleParticleType> factory = SupernovaSwordParticle.Provider::new;
        event.registerSpriteSet(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(), factory);
    }

}
