package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FriedEffect extends GastronomyEffect {

    public FriedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
    }

    public static float processHurt(LivingEntity victim, float amount) {
        if (victim.hasEffect(BrutalityEffects.OILED.get()) && victim.hasEffect(BrutalityEffects.FRIED.get())) {
            return amount * 1.1F;
        }
        return amount;
    }
}
