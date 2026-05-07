package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BarkEffect extends GastronomyEffect {

    public BarkEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
    }

    public static float processHurt(LivingEntity victim, float amount) {
        if (victim.hasEffect(BrutalityEffects.BARK.get())) {
            if (victim.hasEffect(BrutalityEffects.SALTED.get())) {
                amount *= 1F + (0.025F * (victim.getEffect(BrutalityEffects.SALTED.get()).getAmplifier() + 1));
            }
            if (victim.hasEffect(BrutalityEffects.PEPPERED.get())) {
                amount *= 1F + (0.025F * (victim.getEffect(BrutalityEffects.PEPPERED.get()).getAmplifier() + 1));
            }
        }
        return amount;
    }
}
