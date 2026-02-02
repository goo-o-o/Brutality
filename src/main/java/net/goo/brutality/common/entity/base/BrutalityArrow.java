package net.goo.brutality.common.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public abstract class BrutalityArrow extends AbstractArrow implements BrutalityGeoEntity {
    protected BrutalityArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected BrutalityArrow(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    protected BrutalityArrow(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    public SimpleParticleType getCritParticle() {
        return ParticleTypes.CRIT;
    }

    public float getGravity() {
        return 0.05F;
    }


}
