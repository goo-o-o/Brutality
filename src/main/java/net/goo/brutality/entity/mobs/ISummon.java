package net.goo.brutality.entity.mobs;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface ISummon {

    LivingEntity getSummoner();

    void onUnSummon();

    default boolean shouldIgnoreDamage(DamageSource damageSource) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (damageSource.getEntity() != null)
                return !(getSummoner() == null || damageSource.getEntity() == null || (!damageSource.getEntity().equals(getSummoner()) && !getSummoner().isAlliedTo(damageSource.getEntity())));
        }
        return false;
    }

    default boolean isAlliedHelper(Entity entity) {
        if (getSummoner() == null)
            return false;
        boolean isFellowSummon = entity == getSummoner() || entity.isAlliedTo(getSummoner());
        boolean hasCommonOwner = entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() == getSummoner();
        return isFellowSummon || hasCommonOwner;
    }

    default void onDeathHelper() {
        if (this instanceof LivingEntity entity) {
            Level level = entity.level();
            var deathMessage = entity.getCombatTracker().getDeathMessage();

            if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && getSummoner() instanceof ServerPlayer player) {
                player.sendSystemMessage(deathMessage);
            }
        }
    }

//    default void onRemovedHelper(Entity entity, DeferredHolder<MobEffect, SummonTimer> holder) {
//        /*
//        Decreases player's summon timer amplifier to keep track of how many of their summons remain.
//        */
//        var reason = entity.getRemovalReason();
//        if (reason != null && getSummoner() instanceof ServerPlayer player && reason.shouldDestroy()) {
//            var effect = player.getEffect(holder);
//            if (effect != null) {
//                var decrement = new MobEffectInstance(holder, effect.getDuration(), effect.getAmplifier() - 1, false, false, true);
//                if (decrement.getAmplifier() >= 0) {
//                    player.getActiveEffectsMap().put(holder, decrement);
//                    player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), decrement, false));
//                } else {
//                    player.removeEffect(holder);
//                }
//            }
//            if (reason.equals(Entity.RemovalReason.DISCARDED))
//                player.sendSystemMessage(Component.translatable("ui.irons_spellbooks.summon_despawn_message", ((Entity) this).getDisplayName()));
//
//        }
//    }
}