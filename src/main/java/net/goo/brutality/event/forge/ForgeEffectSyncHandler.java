package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.curios.hands.PerfectCell;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.ClientboundUpdateAbilityCooldownsPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.Constants;
import net.mcreator.terramity.potion.ArmorSetAbilityCooldownMobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class ForgeEffectSyncHandler {
    @SubscribeEvent
    public static void onAddEffect(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        MobEffect effect = instance.getEffect();
//        if (entity.level() instanceof ServerLevel) {
//            entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).ifPresent(cap -> {
//                if (effect == BrutalityModMobEffects.MIRACLE_BLIGHT.get()) {
//                    cap.setMiracleBlighted(true);
//                } else if (effect == BrutalityModMobEffects.ENRAGED.get()) {
//                    cap.setRage(true);
//                } else if (effect == BrutalityModMobEffects.THE_VOID.get()) {
//                    cap.setTheVoid(true);
//                } else if (effect == BrutalityModMobEffects.LIGHT_BOUND.get()) {
//                    cap.setLightBound(true);
//                }
//                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
//            });
//        }

        if (entity instanceof ServerPlayer player) {
            if (effect instanceof ArmorSetAbilityCooldownMobEffect) {
                PacketHandler.sendToPlayerClient(new ClientboundUpdateAbilityCooldownsPacket(
                        Constants.COOLDOWN_BY_CLASS.get(effect.getClass()), instance.getDuration(), player.getItemBySlot(EquipmentSlot.CHEST)), player);
            }


        }

    }

    @SubscribeEvent
    public static void onTryAddEffect(MobEffectEvent.Applicable event) {
        @NotNull MobEffectInstance effectInstance = event.getEffectInstance();
        MobEffect effect = effectInstance.getEffect();
        LivingEntity entity = event.getEntity();
        if (effect == BrutalityModMobEffects.RADIATION.get()) {
            if (entity.isHolding(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get())) {
                event.setResult(Event.Result.DENY);
            }
        }

        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityModItems.PERFECT_CELL.get())) {
                if (PerfectCell.DENIED_EFFECTS.contains(effect)) {
                    event.setResult(Event.Result.DENY);
                }

            }
        });

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
