package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.entity.base.BrutalityAbstractArrow;
import net.goo.brutality.common.entity.base.BrutalityArrow;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

}

