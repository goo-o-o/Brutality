package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.entity.projectile.generic.BlockchainedProjectile;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.EffectUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.List;

public class BlockchainedEffect extends MobEffect {
    public BlockchainedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        MobEffectInstance instance = pLivingEntity.getEffect(BrutalityEffects.BLOCKCHAINED.get());
        if (instance != null && EffectUtils.getEffectSource(pLivingEntity.level(), instance) instanceof LivingEntity source) {
            if (pLivingEntity.distanceToSqr(source) > 100 || !pLivingEntity.hasLineOfSight(source)) {
                // if more than 10 blocks or not visible, clear
                pLivingEntity.removeEffect(BrutalityEffects.BLOCKCHAINED.get());
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 10 == 0;
    }

    public static float handleHurt(LivingEntity originHurt, float damage) {
        // 1. Remove the effect IMMEDIATELY to prevent recursion
        if (!originHurt.hasEffect(BrutalityEffects.BLOCKCHAINED.get())) return damage;
        int amp = originHurt.getEffect(BrutalityEffects.BLOCKCHAINED.get()).getAmplifier();
        originHurt.removeEffect(BrutalityEffects.BLOCKCHAINED.get());

        List<LivingEntity> nearbyEntities = originHurt.level().getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.DEFAULT,
                originHurt,
                originHurt.getBoundingBox().inflate(10)
        );

        // Filter list to only those with the effect
        List<LivingEntity> targets = nearbyEntities.stream()
                .filter(e -> e.hasEffect(BrutalityEffects.BLOCKCHAINED.get()))
                .toList();

        if (targets.isEmpty()) return damage;

        // 2. Calculate damage per node (including origin if desired, or just spread)
        float dmgPer = damage * 0.05F * (amp + 1);

        for (LivingEntity target : targets) {
            BlockchainedProjectile projectile = BlockchainedProjectile.create(
                    BrutalityEntities.BLOCKCHAINED_PROJECTILE.get(),
                    originHurt.level(),
                    originHurt,
                    target,
                    dmgPer
            );
            // Ensure projectile spawns at the center of the origin
            projectile.setPos(originHurt.getX(), originHurt.getY(0.5), originHurt.getZ());
            originHurt.level().addFreshEntity(projectile);
        }

        // 3. Return the reduced damage for the original victim
        return damage;
    }
}
