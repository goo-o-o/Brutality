package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.ModUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SupernovaAsteroid extends ThrowableProjectile implements BrutalityGeoEntity {
    public static final EntityDataAccessor<Integer> ANGLE_OFFSET = SynchedEntityData.defineId(SupernovaAsteroid.class, EntityDataSerializers.INT);

    public SupernovaAsteroid(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel, int angleOffset) {
        super(pEntityType, pLevel);
        this.entityData.set(ANGLE_OFFSET, angleOffset);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ANGLE_OFFSET, 0);
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        if (!level().isClientSide) {

            if (pResult.getType() == HitResult.Type.ENTITY) {
                Entity target = ((EntityHitResult) pResult).getEntity();

                if (target != this.getOwner() || target instanceof SupernovaAsteroid) {
                    target.hurt(target.damageSources().playerAttack((Player) this.getOwner()), 7.5F);
                    explode();

                }
            }

            if (pResult.getType() == HitResult.Type.BLOCK) {
                explode();
            }
        }

    }

    float ORBIT_RADIUS = 6F;
    double FOLLOW_SPEED = 0.3; // Adjust for responsiveness
    boolean trailSpawned = false;
    float orbitSpeed = 0.1f;

    @Override
    public void tick() {

        super.tick();
        if (!this.trailSpawned) {
//            this.level().addParticle((new AbstractWorldAlignedTrailParticle.OrbData(0.35F, 0, 0.5F, this.getBbWidth() / 2, this.getId(), 0, 0, 0, "circle", 10)), this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
            this.trailSpawned = true;
        }

        if (getOwner() != null) {
            // Orbit parameters
            float heightOffset = getOwner().getBbHeight() / 2;

            // Calculate target orbit position
            double targetX = getOwner().getX() + Mth.cos(this.tickCount * orbitSpeed + this.entityData.get(ANGLE_OFFSET)) * ORBIT_RADIUS;
            double targetZ = getOwner().getZ() + Mth.sin(this.tickCount * orbitSpeed + this.entityData.get(ANGLE_OFFSET)) * ORBIT_RADIUS;
            double targetY = getOwner().getY() + heightOffset;

            // Calculate movement vector (target - current position)
            Vec3 movement = new Vec3(targetX, targetY, targetZ).subtract(this.position());

            // Apply scaled movement for smooth following
            this.setDeltaMovement(movement.scale(FOLLOW_SPEED));

            List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, this.getBoundingBox())
                    .stream()
                    .filter(projectile -> projectile.getOwner() != getOwner())
                    .toList();

            if (!projectiles.isEmpty()) {
                for (Projectile projectile : projectiles) {
                    projectile.remove(RemovalReason.DISCARDED);
                    explode(); // Custom method to handle explosion
                }
            }

        } else {
            this.discard();
        }
    }


    public void explode() {
        // Handle explosion logic (e.g., damage entities, play effects)
        if (level() instanceof ServerLevel serverLevel) {
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1F, 1F);

            for (int i = 0; i < serverLevel.random.nextInt(50); i++) {
                serverLevel.sendParticles(ModUtils.getRandomParticle(BrutalityModParticles.COSMIC_PARTICLE), this.getX(),
                        this.getY() + this.getBbHeight() / 2, this.getZ(), 1,
                        0, 0, 0, 100);
            }
        }

        discard(); // Remove the asteroid after exploding

    }
}
