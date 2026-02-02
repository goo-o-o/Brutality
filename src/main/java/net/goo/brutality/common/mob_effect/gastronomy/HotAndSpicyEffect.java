package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class HotAndSpicyEffect extends MobEffect implements IGastronomyEffect {

    public HotAndSpicyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public Type getType() {
        return Type.WET;
    }

    @Override
    public boolean scalesWithLevel() {
        return false;
    }

    @Override
    public boolean modifiesDamage() {
        return false;
    }

    @Override
    public float baseMultiplier() {
        return 0.05F;
    }

    @Override
    public float multiplierPerLevel() {
        return 0;
    }

    @Override
    public void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {
        int amplifier = attacker.getEffect(BrutalityEffects.HOT_AND_SPICY.get()).getAmplifier() + 1;
        victim.setSecondsOnFire(amplifier * 2);
    }
}
