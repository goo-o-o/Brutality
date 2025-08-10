package net.goo.brutality.entity.projectile.beam;

import net.goo.brutality.entity.base.SwordBeam;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class TerraBeam extends SwordBeam {
    public TerraBeam(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level, -15, 15);
        this.shouldNoClip = true;
    }

    @Override
    public float getDamage() {
        return 7.5F;
    }

    @Override
    public float getInertia() {
        return 0.875F;
    }

    @Override
    public int getPierceCap() {
        return 3;
    }

    @Override
    public SimpleParticleType getHitParticle() {
        return BrutalityModParticles.TERRA_PARTICLE.get();
    }

    private boolean trailSpawned = false;

    @Override
    public void tick() {
            if (!trailSpawned && level().isClientSide()) {
                Vec3 moveVec = this.getDeltaMovement();

                moveVec = moveVec.normalize();

                float yaw = (float) Math.atan2(-moveVec.z, moveVec.x);
                float pitch = (float) Math.asin(moveVec.y);

//                this.level().addParticle((new AbstractWorldAlignedTrailParticle.OrbData(0, 0.8F, 0.6F, this.getBbHeight(), this.getId(), pitch, yaw, getRandomRollRadians(), "sword", 5)), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
                trailSpawned = true;
            }


            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            if (this.getDeltaMovement().length() < 0.1) {
                this.discard();
            }

            super.tick();
        }
}
