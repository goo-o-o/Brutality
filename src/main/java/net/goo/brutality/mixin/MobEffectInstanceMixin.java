package net.goo.brutality.mixin;

import net.goo.brutality.mob_effect.gastronomy.IGastronomyEffect;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void pauseEffectWhenSteamed(LivingEntity pEntity, Runnable pOnExpirationRunnable, CallbackInfoReturnable<Boolean> cir) {
        MobEffectInstance self = (MobEffectInstance) (Object) this;

        if (pEntity.hasEffect(BrutalityModMobEffects.STEAMED.get())
                && self.getEffect() != BrutalityModMobEffects.STEAMED.get()) {
            if (self.getEffect() instanceof IGastronomyEffect)
                cir.setReturnValue(self.getDuration() > 0);
        }
    }
}
