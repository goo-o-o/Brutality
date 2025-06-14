package net.goo.brutality.mixin;

import net.goo.brutality.entity.base.BrutalityArrow;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

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
}