package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SearedEffect extends GastronomyEffect {

    public SearedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
    }

    public static float processHurt(LivingEntity victim, float amount) {
        if (victim.isOnFire()) {
            if (victim.hasEffect(BrutalityEffects.SEARED.get())) {
                return amount * (1 + 0.15F * victim.getEffect(BrutalityEffects.SEARED.get()).getAmplifier() + 1);
            }
        }
        return amount;
    }
}
