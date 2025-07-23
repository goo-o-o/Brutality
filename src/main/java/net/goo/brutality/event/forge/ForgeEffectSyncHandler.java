package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class ForgeEffectSyncHandler {
    @SubscribeEvent
    public static void onAddEffect(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();

        if (entity.level() instanceof ServerLevel serverLevel) {
            if (event.getEffectInstance().getEffect() == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
                    cap.setMiracleBlighted(true);
                    PacketHandler.sendToNearbyClients((serverLevel),
                            entity.getX(), entity.getY(), entity.getZ(),
                            96, new s2cSyncCapabilitiesPacket(entity.getId(), entity));
                });
            }
        }
    }
    @SubscribeEvent
    public static void onRemoveEffect(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
//        System.out.println(event.getEffect() + " REMOVED");
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (event.getEffect() == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
//                    System.out.println("miracle blighted = false from removed");


                    cap.setMiracleBlighted(false);
                    PacketHandler.sendToNearbyClients((serverLevel),
                            entity.getX(), entity.getY(), entity.getZ(),
                            96, new s2cSyncCapabilitiesPacket(entity.getId(), entity));
                });
            }
        }
    }
    @SubscribeEvent
    public static void onExpiredEffect(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
//        System.out.println(event.getEffectInstance().getEffect() + " EXPIRED");

        if (entity.level() instanceof ServerLevel serverLevel) {
            if (event.getEffectInstance().getEffect() == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
                entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {

//                    System.out.println("miracle blighted = false from expired");
                    cap.setMiracleBlighted(false);
                    PacketHandler.sendToNearbyClients((serverLevel),
                            entity.getX(), entity.getY(), entity.getZ(),
                            96, new s2cSyncCapabilitiesPacket(entity.getId(), entity));
                });
            }
        }
    }



}
