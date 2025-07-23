package net.goo.brutality.mob_effect.gastronomy.wet;

import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CaramelizedEffect extends MobEffect implements IGastronomyEffect {

    public CaramelizedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

//        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
//            serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
//                    pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
//                    0.5, 0.5, 0.5
//                    ,0);
//
//        }
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
        return 0.05F;
    }

}

