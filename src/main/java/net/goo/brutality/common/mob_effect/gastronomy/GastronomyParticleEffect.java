package net.goo.brutality.common.mob_effect.gastronomy;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public class GastronomyParticleEffect extends GastronomyEffect {
    private final Supplier<? extends ParticleOptions> particleSupplier;

    public GastronomyParticleEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult, Supplier<? extends ParticleOptions> particleSupplier) {
        super(pCategory, type, baseMult, perLevelMult);
        this.particleSupplier = particleSupplier;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleSupplier.get(),
                    pLivingEntity.getX(), pLivingEntity.getY() + pLivingEntity.getBbHeight() / 2, pLivingEntity.getZ(), 1,
                    0.5, 0.5, 0.5, 0);
        }
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 4 == 0;
    }
}
