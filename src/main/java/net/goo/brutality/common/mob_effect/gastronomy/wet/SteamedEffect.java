package net.goo.brutality.common.mob_effect.gastronomy.wet;

import net.goo.brutality.common.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class SteamedEffect extends MobEffect implements IGastronomyEffect {

    public SteamedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
            if (pLivingEntity.tickCount % 4 == 0)
                serverLevel.sendParticles(BrutalityParticles.STEAM_PARTICLE.get(),
                        pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
                        0.5, 0.5, 0.5
                        , 0);
        }

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
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
        return 0.15F;
    }

    @Override
    public float multiplierPerLevel() {
        return 0;
    }

    public static void pauseTickdown(LivingEntity livingEntity, MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.hasEffect(BrutalityEffects.STEAMED.get()) && mobEffectInstance.getEffect() != BrutalityEffects.STEAMED.get())
            if (mobEffectInstance.getEffect() instanceof IGastronomyEffect)
                cir.setReturnValue(mobEffectInstance.getDuration() > 0);
    }

}

