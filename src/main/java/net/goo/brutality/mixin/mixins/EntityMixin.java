package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
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
                if (livingEntity.hasEffect(BrutalityEffects.OILED.get())) {
                    instance.invulnerableTime = 0;
                    int amplifier = livingEntity.getEffect(BrutalityEffects.OILED.get()).getAmplifier();
                    return instance.hurt(pSource, amplifier + 1);
                }
            }

        }

        return instance.hurt(pSource, pAmount);
    }

    @ModifyArg(method = "setSecondsOnFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setRemainingFireTicks(I)V"), index = 0)
    private int halveFireTicks(int originalTime) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {

            return CuriosApi.getCuriosInventory(livingEntity)
                    .filter(handler -> handler.isEquipped(BrutalityItems.FIRE_EXTINGUISHER.get()))
                    .map(handler -> Math.max(1, ((int) (originalTime * 0.5))))
                    .orElse(originalTime);
        }
        return originalTime;
    }

}
