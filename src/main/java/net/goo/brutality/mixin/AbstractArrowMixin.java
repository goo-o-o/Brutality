package net.goo.brutality.mixin;

import net.goo.brutality.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow protected abstract float getWaterInertia();

    @Redirect(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
                    ordinal = 0
            )
    )
    private void redirectCritParticle(Level instance, ParticleOptions pParticleData, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ParticleOptions particle = arrow instanceof BrutalityArrow
                ? ((BrutalityArrow) arrow).getCritParticle()
                : pParticleData;

        instance.addParticle(particle, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
    }

    @Redirect(method = "tick()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V", ordinal = 0))
    private void changeGravity(AbstractArrow arrow, double x, double y, double z) {
        if (arrow instanceof BrutalityArrow brutalityArrow) {
            arrow.setDeltaMovement(x, y - brutalityArrow.getGravity(), z);
        } else {
            arrow.setDeltaMovement(x, y - 0.05, z);
        }
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 modifyDrag(Vec3 original, double scale) {
        AbstractArrow arrow = (AbstractArrow)(Object)this;

        if (arrow instanceof BrutalityAbstractArrow brutalityAbstractArrow) {
        double newScale = arrow.isInWater() ? getWaterInertia() : brutalityAbstractArrow.getAirDrag(); // 0.95 instead of 0.99
            return original.scale(newScale);
        }

        return original;
    }

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", ordinal = 0),
            cancellable = true
    )
    private void redirectHitEntitySound(EntityHitResult pResult, CallbackInfo ci) {
        AbstractArrow arrow = (((AbstractArrow) (Object) this));

        if (arrow instanceof BrutalityAbstractArrow brutalityAbstractArrow) {
            arrow.playSound(brutalityAbstractArrow.getHitEntitySound(), 1.0F, 1.2F / (arrow.level().random.nextFloat() * 0.2F + 0.9F));
            ci.cancel();
        }
    }

}

