package net.goo.brutality.common.mob_effect.gastronomy.wet;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CaramelizedEffect extends MobEffect implements IGastronomyEffect {

    public CaramelizedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }


    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
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
        return true;
    }

    @Override
    public float baseMultiplier() {
        return 0.125F;
    }

    @Override
    public float multiplierPerLevel() {
        return 0;
    }

}

