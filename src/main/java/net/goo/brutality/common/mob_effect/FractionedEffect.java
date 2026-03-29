package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.EffectUtils;
import net.goo.brutality.util.ModUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class FractionedEffect extends MobEffect {

    public FractionedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level() instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(BrutalityParticles.MATH_PARTICLE.get(), pLivingEntity.getX(), pLivingEntity.getY(0.5), pLivingEntity.getZ(), 3, 1,1,1,0);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 5 == 0;
    }

    public static void onExpire(LivingEntity entity, MobEffectInstance effectInstance) {
        LivingEntity nearestEntity = entity.level().getNearestEntity(LivingEntity.class,
                TargetingConditions.DEFAULT, entity, entity.getX(), entity.getY(0.5), entity.getZ(), entity.getBoundingBox().inflate(3));

        if (nearestEntity != null) {
            int hpA = (int) entity.getHealth();
            int hpB = (int) nearestEntity.getHealth();

//            System.out.println("hpA: " + hpA + " | hpB: " + hpB);
            if (hpA > 0 && hpB > 0 && (hpA % hpB == 0 || hpB % hpA == 0)) {
                DamageSource damageSource = entity.damageSources().generic();
                Entity source = EffectUtils.getEffectSource(entity.level(), effectInstance);
//                System.out.println(source);
                if (source instanceof Player player) {
                    damageSource = entity.damageSources().playerAttack(player);
                } else if (source instanceof LivingEntity livingEntity) {
                    damageSource = entity.damageSources().mobAttack(livingEntity);
                }
//                System.out.println(damageSource);

                int damage = ModUtils.gcd(hpA, hpB);
                entity.hurt(damageSource, damage);
                nearestEntity.hurt(damageSource, damage);
                nearestEntity.removeEffect(BrutalityEffects.FRACTIONED.get());

                if (entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(BrutalityParticles.MATH_PARTICLE.get(), entity.getX(), entity.getY(0.5), entity.getZ(), 10, 1,1,1,0);
                    serverLevel.sendParticles(BrutalityParticles.MATH_PARTICLE.get(), nearestEntity.getX(), nearestEntity.getY(0.5), nearestEntity.getZ(), 10, 1,1,1,0);
                }

            }
        }
    }
}
