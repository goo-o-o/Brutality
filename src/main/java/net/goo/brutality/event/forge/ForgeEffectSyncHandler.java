package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class ForgeEffectSyncHandler {
    @SubscribeEvent
    public static void onAddEffect(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();
        if (entity.level() instanceof ServerLevel) {
            entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
                if (effect == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                    cap.setMiracleBlighted(true);
                } else if (effect == BrutalityModMobEffects.ENRAGED.get()) {
                    cap.setRage(true);
                } else if (effect == BrutalityModMobEffects.THE_VOID.get()) {
                    cap.setTheVoid(true);
                } else if (effect == BrutalityModMobEffects.LIGHT_BOUND.get()) {
                    cap.setLightBound(true);
                }
                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
            });

        }
    }

    @SubscribeEvent
    public static void onTryAddEffect(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == BrutalityModMobEffects.RADIATION.get()) {
            if (event.getEntity().isHolding(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get())) {
                event.setResult(Event.Result.DENY);
            }
        }
    }


    @SubscribeEvent
    public static void onRemoveEffect(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) return;
        ;
        MobEffect effect = instance.getEffect();

        if (entity.level() instanceof ServerLevel) {
            entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
                if (effect == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                    cap.setMiracleBlighted(false);
                } else if (effect == BrutalityModMobEffects.ENRAGED.get()) {
                    cap.setRage(false);
                } else if (effect == BrutalityModMobEffects.THE_VOID.get()) {
                    cap.setTheVoid(false);
                } else if (effect == BrutalityModMobEffects.LIGHT_BOUND.get()) {
                    cap.setLightBound(false);
                }
                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
            });
        }
    }

    @SubscribeEvent
    public static void onExpiredEffect(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) return;
        MobEffect effect = instance.getEffect();

        if (entity.level() instanceof ServerLevel) {
            entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
                if (effect == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                    cap.setMiracleBlighted(false);
                } else if (effect == BrutalityModMobEffects.ENRAGED.get()) {
                    cap.setRage(false);
                } else if (effect == BrutalityModMobEffects.THE_VOID.get()) {
                    cap.setTheVoid(false);
                } else if (effect == BrutalityModMobEffects.LIGHT_BOUND.get()) {
                    cap.setLightBound(false);
                }
                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
            });
        }
    }


}
