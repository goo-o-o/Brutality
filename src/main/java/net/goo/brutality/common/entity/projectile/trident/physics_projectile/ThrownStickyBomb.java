package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ThrownStickyBomb extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public ThrownStickyBomb(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
        this.damage = 0F;
    }

    public ThrownStickyBomb(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
        this.damage = 0F;
    }

    @Override
    public int getInGroundLifespan() {
        return 800;
    }


    @Override
    public float getModelHeight() {
        return 2;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalitySounds.TARGET_FOUND.get();
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalitySounds.TARGET_FOUND.get();
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        Entity entity = pResult.getEntity();
        if (getOwner() instanceof Player owner) {
            entity.getCapability(BrutalityCapabilities.STICKY_BOMB).ifPresent(cap -> cap.addBomb(owner.getUUID()));
            BrutalityCapabilities.syncToAllTracking(entity, BrutalityCapabilities.STICKY_BOMB);
            discard();
        }
    }

    // TODO: Very ambitious but implement Jolt JNI physics engine instead of our rudimentary physics engine, similar to how Velthoric mod does it
//    @Override
//    protected void onHitBlock(BlockHitResult hitResult) {
//        super.onHitBlock(hitResult);
//
//        Direction face = hitResult.getDirection();
//        Vec3 normal = Vec3.atLowerCornerOf(face.getNormal());
//
//        // 1. Basic Vector Math
//        double horizontalDistance = Math.sqrt(normal.x * normal.x + normal.z * normal.z);
//        float baseYaw = (float) (Math.atan2(normal.x, normal.z) * (180D / Math.PI));
//        float basePitch = (float) (Math.atan2(-normal.y, horizontalDistance) * (180D / Math.PI));
//
//        // 2. The Universal Orientator
//        // If we are on a wall (Horizontal face), we need to flip the rotation
//        // so the 'back' stays against the wall and 'front' points out.
//        if (face.getAxis().isHorizontal()) {
//            this.yaw = baseYaw + 180.0f; // Flip to face outward
//            this.pitch = 0.0f;           // Walls don't need the +90 pitch offset
//            this.roll = 0.0f;            // Reset roll so 'up' is 'up'
//        } else {
//            this.pitch = basePitch + 90.0f;
//            this.roll = 0.0f;
//        }
//
//        // 3. Prevent Animation "Glitching"
//        // Set previous values to current so it doesn't spin wildly for one frame
//        this.prevYaw = this.yaw;
//        this.prevPitch = this.pitch;
//        this.prevRoll = this.roll;
//    }
}
