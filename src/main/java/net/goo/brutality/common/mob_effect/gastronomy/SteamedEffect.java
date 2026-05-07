package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

public class SteamedEffect extends GastronomyParticleEffect {


    public SteamedEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult, Supplier<? extends ParticleOptions> particleSupplier) {
        super(pCategory, type, baseMult, perLevelMult, particleSupplier);
    }

    public static void pauseTickdown(LivingEntity livingEntity, MobEffectInstance mobEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.hasEffect(BrutalityEffects.STEAMED.get()) && mobEffectInstance.getEffect() != BrutalityEffects.STEAMED.get())
            if (mobEffectInstance.getEffect() instanceof GastronomyEffect)
                cir.setReturnValue(mobEffectInstance.getDuration() > 0);
    }

}

