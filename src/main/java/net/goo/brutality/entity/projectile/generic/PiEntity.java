package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.particle.base.AbstractWorldAlignedTrailParticle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PiEntity extends ThrowableProjectile implements BrutalityGeoEntity {
    public static final EntityDataAccessor<Integer> ANGLE_OFFSET = SynchedEntityData.defineId(PiEntity.class, EntityDataSerializers.INT);

    public PiEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel, int angleOffset) {
        super(pEntityType, pLevel);
        this.entityData.set(ANGLE_OFFSET, angleOffset);
    }



    @Override
    protected void defineSynchedData() {
        this.entityData.define(ANGLE_OFFSET, 0);
    }

    float ORBIT_RADIUS = 3.14F;
    double FOLLOW_SPEED = 0.3; // Adjust for responsiveness
    boolean trailSpawned = false;
    double CIRCLES_PER_SECOND = 3;
    double RADIANS_PER_SECOND = 3.14 * CIRCLES_PER_SECOND;
    double TICKS_PER_SECOND = 20.0;
    double ORBIT_SPEED = RADIANS_PER_SECOND / TICKS_PER_SECOND;

    @Override
    public void tick() {

        super.tick();
        if (!this.trailSpawned && level().isClientSide()) {
            this.level().addParticle((new AbstractWorldAlignedTrailParticle.OrbData(0, 0, 0, this.getBbHeight(), this.getId(), 0, 0, 0, "circle", 10)), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
            this.trailSpawned = true;
        }

        if (getOwner() != null) {
            // Orbit parameters
            float heightOffset = getOwner().getBbHeight() / 2;
            // Calculate target orbit position
            double targetX = getOwner().getX() + Mth.cos((float) (this.tickCount * ORBIT_SPEED + this.entityData.get(ANGLE_OFFSET))) * ORBIT_RADIUS;
            double targetZ = getOwner().getZ() + Mth.sin((float) (this.tickCount * ORBIT_SPEED + this.entityData.get(ANGLE_OFFSET))) * ORBIT_RADIUS;
            double targetY = getOwner().getY() + heightOffset;

            // Calculate movement vector (target - current position)
            Vec3 movement = new Vec3(targetX, targetY, targetZ).subtract(this.position());

            // Apply scaled movement for smooth following
            this.setDeltaMovement(movement.scale(FOLLOW_SPEED));

        } else {
            this.discard();
        }
    }



    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (this.getOwner() instanceof Player owner && pResult.getEntity() != owner) {
            pResult.getEntity().invulnerableTime = 0;
            pResult.getEntity().hurt(owner.damageSources().indirectMagic(owner, this), 3.14F);
        }
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
