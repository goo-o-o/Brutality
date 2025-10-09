package net.goo.brutality.mixin;

import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

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

    @ModifyVariable(method = "setRemainingFireTicks", at = @At("HEAD"), argsOnly = true)
    private int modifyFireTime(int value) {
        Entity entity = (((Entity) (Object) this));
        if (entity instanceof LivingEntity livingEntity) {
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(livingEntity).orElse(null);
            if (handler != null) {
                if (handler.isEquipped(BrutalityModItems.FIRE_EXTINGUISHER.get())) {
                    System.out.println("original " + value);
                    System.out.println("after " + value * 0.5F);
                    return (int) (value * 0.5);
                }
            }
        }
        return value;
    }
}
