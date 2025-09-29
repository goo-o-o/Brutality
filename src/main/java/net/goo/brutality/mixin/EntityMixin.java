package net.goo.brutality.mixin;

import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Entity.class)
public abstract class EntityMixin {


    @Redirect(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
    )
    private boolean redirectHurt(Entity instance, DamageSource pSource, float pAmount) {
        if (instance instanceof LivingEntity livingEntity) {

            if (pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.IN_FIRE) || pSource.is(DamageTypes.LAVA)) {
                if (livingEntity.hasEffect(BrutalityModMobEffects.OILED.get())) {
                    instance.invulnerableTime = 0;
                    int amplifier = livingEntity.getEffect(BrutalityModMobEffects.OILED.get()).getAmplifier();
                    return instance.hurt(pSource, amplifier + 1);
                }
            }

        }

        return instance.hurt(pSource, pAmount);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setRemainingFireTicks(I)V", shift = At.Shift.AFTER))
    private void modifyFireTickReduction(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getRemainingFireTicks() > 0 && !livingEntity.fireImmune()) {
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                    if (handler.isEquipped(BrutalityModItems.FIRE_EXTINGUISHER.get())) {
                        livingEntity.setRemainingFireTicks(livingEntity.getRemainingFireTicks() - 1); // Additional -1 for -2 per tick
                    }
                });
            }
        }
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    private void adjustFireDamageTiming(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getRemainingFireTicks() > 0 && !livingEntity.fireImmune() && !livingEntity.isInLava()) {
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                    if (handler.isEquipped(BrutalityModItems.FIRE_EXTINGUISHER.get())) {
                        if (livingEntity.getRemainingFireTicks() % 40 == 0) {
                            livingEntity.hurt(livingEntity.damageSources().onFire(), 1.0F);
                        }
                        ci.cancel();
                    }
                });
            }
        }
    }

}
