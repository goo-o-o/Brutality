package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.hands.PerfectCell;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundUpdateAbilityCooldownsPacket;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.CommonConstants;
import net.mcreator.terramity.potion.ArmorSetAbilityCooldownMobEffect;
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
                        CommonConstants.COOLDOWN_BY_CLASS.get(effect.getClass()), instance.getDuration(), player.getItemBySlot(EquipmentSlot.CHEST)), player);
            }


        }

    }

    @SubscribeEvent
    public static void onTryAddEffect(MobEffectEvent.Applicable event) {
        @NotNull MobEffectInstance effectInstance = event.getEffectInstance();
        MobEffect effect = effectInstance.getEffect();
        LivingEntity entity = event.getEntity();
        if (effect == BrutalityEffects.RADIATION.get()) {
            if (entity.isHolding(BrutalityItems.ATOMIC_JUDGEMENT_HAMMER.get())) {
                event.setResult(Event.Result.DENY);
            }
        }
        PerfectCell.denyEffect(event);

    }

}
