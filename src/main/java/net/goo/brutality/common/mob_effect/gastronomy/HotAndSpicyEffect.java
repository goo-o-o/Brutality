package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HotAndSpicyEffect extends GastronomyEffect {
    public HotAndSpicyEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, type, baseMult, perLevelMult);
    }

    @Override
    public void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {
        int amplifier = attacker.getEffect(BrutalityEffects.HOT_AND_SPICY.get()).getAmplifier() + 1;
        victim.setSecondsOnFire(amplifier * 2);
    }
}
