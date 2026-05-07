package net.goo.brutality.common.mob_effect;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ResilienceEffect extends MobEffect {
    public ResilienceEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static void handleHurt(LivingEntity livingEntity) {
        if (livingEntity.hasEffect(BrutalityEffects.RESILIENCE.get())) {
            livingEntity.invulnerableTime += livingEntity.getEffect(BrutalityEffects.RESILIENCE.get()).getAmplifier() + 1;
        }
    }

}
