package net.goo.brutality.entity.explosion;

import net.goo.brutality.entity.spells.brimwielder.AnnihilationSpellEntity;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BloodExplosion extends BrutalityExplosion {

    public BloodExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
    }

    public BloodExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, BlockInteraction pBlockInteraction) {
        super(pLevel, pSource, pDamageSource, pDamageCalculator, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
    }

    @Override
    protected SimpleParticleType getParticle() {
        return BrutalityModParticles.BLOOD_EXPLOSION_PARTICLE.get();
    }

    @Override
    protected SimpleParticleType getParticleEmitter() {
        return BrutalityModParticles.BLOOD_EXPLOSION_EMITTER.get();
    }

    @Override
    protected SoundEvent getExplosionSound() {
        return BrutalityModSounds.BLOOD_SPLATTER.get();
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

    @Override
    public Predicate<Entity> getEntityFilter() {
        return entity -> (!(entity instanceof AnnihilationSpellEntity));
    }

}
