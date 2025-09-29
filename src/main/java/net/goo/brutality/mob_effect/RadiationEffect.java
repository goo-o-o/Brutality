package net.goo.brutality.mob_effect;

import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RadiationEffect extends MobEffect {
    public RadiationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();

        pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.0F);

        level.addParticle(BrutalityModParticles.RADIATION_PARTICLE.get(), pLivingEntity.getX(), pLivingEntity.getY(0.5), pLivingEntity.getZ(), 0, 0, 0);

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int divisor = Math.max(1, 20 / Math.max(1, Math.min(pAmplifier + 1, 255))); // Ensure divisor is at least 1
        return pDuration % divisor == 0;
    }
}
