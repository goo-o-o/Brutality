package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.common.entity.base.BrutalitySwordBeam;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class Bloodslash extends BrutalitySwordBeam {
    public Bloodslash(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        setGlowingTag(true);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public int getTeamColor() {
        return FastColor.ARGB32.color(255, 255, 0,0);
    }

    @Override
    public int getPierceCap() {
        return 3;
    }

    @Override
    public void tick() {
        super.tick();
        if (getDeltaMovement().length() < 0.1) {
            level().addParticle(getHitParticle(), getX(), getY(0.5), getZ(), 0, 0, 0);
            discard();
        }
    }


    public void spawnTrailParticle() {
        if (tickCount % 3 == 0)
            level().addParticle(BrutalityParticles.BLOOD_PARTICLE.get(), getRandomX(1), getY(0.5), getRandomZ(1), 0.5, 0.5, 0.5);
    }

    @Override
    public int getLifespan() {
        return 80;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (getOwner() instanceof LivingEntity livingEntity) {
            livingEntity.heal(4);
        }
    }

    @Override
    public SimpleParticleType getHitParticle() {
        return BrutalityParticles.BLOOD_EXPLOSION_PARTICLE.get();
    }

}
