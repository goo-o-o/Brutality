package net.goo.armament.entity.custom;

import net.goo.armament.entity.base.SwordBeam;
import net.goo.armament.particle.custom.SwordBeamTrail;
import net.goo.armament.registry.ModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import static net.goo.armament.util.ModUtils.nextFloatBetweenInclusive;

public class TerraBeam extends SwordBeam {
    public TerraBeam(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getOrigin() {
        return 70;
    }

    @Override
    public int getBound() {
        return 110;
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
    public String geoIdentifier() {
        return "terra_beam";
    }

    @Override
    public SimpleParticleType getHitParticle() {
        return ModParticles.TERRA_PARTICLE.get();
    }


    @Override
    public void tick() {
        Entity owner = this.getOwner();
        if (this.level().isClientSide || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            Vec3 motion = this.getDeltaMovement();

            float inertia = this.getInertia();
            this.checkInsideBlocks();
            double d0 = this.getX() + motion.x;
            double d1 = this.getY() + motion.y;
            double d2 = this.getZ() + motion.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            this.setDeltaMovement(motion.scale(inertia));

            float g = nextFloatBetweenInclusive(random, 0.75F, 1F);
            float b = nextFloatBetweenInclusive(random, 0F, 0.15F);
            this.level().addParticle((new SwordBeamTrail.OrbData(0, g, b, 0.3f, 0.3f, this.getId(), getRandomRoll())), this.getX(), this.getY() + 10F, this.getZ(), 0, 0, 0);
            this.setPos(d0, d1, d2);


            if (tickCount >= getLifespan() || this.getDeltaMovement().length() < 0.1) {
                this.discard();
            }

        }
        if (!level().isClientSide()) {
            ((ServerLevel) level()).sendParticles(ModParticles.TERRA_PARTICLE.get(), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 1, 0,0,0,0);
        }
    }
}
