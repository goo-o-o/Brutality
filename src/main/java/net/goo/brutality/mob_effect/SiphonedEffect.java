package net.goo.brutality.mob_effect;

import net.goo.brutality.entity.projectile.generic.HealingProjectile;
import net.goo.brutality.mixin.accessors.MobEffectInstanceSourceAccessor;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SiphonedEffect extends MobEffect {

    public SiphonedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // In applyEffectTick
    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        if (target.level().isClientSide()) return;

        MobEffectInstance inst = target.getEffect(BrutalityModMobEffects.SIPHONED.get());

        if (inst != null) {
            Integer sourceID = ((MobEffectInstanceSourceAccessor) inst).brutality$getSourceID();
            if (sourceID != null && target.level().getEntity(sourceID) instanceof LivingEntity source) {

                HealingProjectile healingProjectile = new HealingProjectile(BrutalityModEntities.HEALING_PROJECTILE.get(), target.level());
                healingProjectile.setOwner(source);
                healingProjectile.setPos(target.getX(), target.getY(0.5), target.getZ());
                target.level().addFreshEntity(healingProjectile);

                target.hurt(target.damageSources().indirectMagic(source, source), amplifier + 1);
                return;
            }
        }

        target.hurt(target.damageSources().magic(), amplifier + 1);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}

