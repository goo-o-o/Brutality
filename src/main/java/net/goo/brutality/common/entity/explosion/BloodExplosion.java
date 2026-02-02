package net.goo.brutality.common.entity.explosion;

import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodExplosion extends BrutalityExplosion {


    public BloodExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, List<BlockPos> pPositions) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pPositions);
    }

    public BloodExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Level.ExplosionInteraction explosionInteraction, List<BlockPos> pPositions) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, explosionInteraction, pPositions);
    }

    public BloodExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Level.ExplosionInteraction explosionInteraction) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, explosionInteraction);
    }

    public BloodExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Level.ExplosionInteraction explosionInteraction) {
        super(pLevel, pSource, pDamageSource, pDamageCalculator, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, explosionInteraction);
    }

    @Override
    protected SimpleParticleType getParticle() {
        return BrutalityParticles.BLOOD_EXPLOSION_PARTICLE.get();
    }

    @Override
    protected SimpleParticleType getParticleEmitter() {
        return BrutalityParticles.BLOOD_EXPLOSION_EMITTER.get();
    }

    @Override
    protected SoundEvent getExplosionSound() {
        return BrutalitySounds.BLOOD_SPLATTER.get();
    }

    @Override
    protected boolean needsInteractWithBlocksForEmitter() {
        return false;
    }

    @Override
    public void onHit(Entity entity, double impactFactor) {
//        if (entity instanceof LivingEntity livingEntity && livingEntity != getIndirectSourceEntity()) {
//            if (!livingEntity.level().isClientSide()) {
//                livingEntity.addEffect(new MobEffectInstance(TerramityModMobEffects.HEXED.get(), 60, 0));
//            }
//        }
    }
}
