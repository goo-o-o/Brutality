package net.goo.brutality.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class BrutalityAbstractPhysicsProjectile extends BrutalityAbstractTrident implements BrutalityGeoEntity {

    protected int bounceCount = 0;
    public float prevRoll;
    public float roll;
    public float prevYaw;
    public float yaw;
    public float prevPitch;
    public float pitch;

    public BrutalityAbstractPhysicsProjectile(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
    }

    public BrutalityAbstractPhysicsProjectile(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    public void tick() {
        super.tick();

        // Store previous values
        this.prevRoll = this.roll;
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;

        // Calculate current rotation values
        Vec3 motion = this.getDeltaMovement();
        float speed = (float) motion.length();
        if (speed > 0.1 && !inGround) {
            if (!lockRoll())
                this.roll += speed * getRotationSpeed(); // Adjust multiplier for rotation speed
            if (!lockYaw())
                this.yaw = (float) Math.atan2(motion.z, motion.x) * Mth.RAD_TO_DEG - 90.0f;
            if (!lockPitch())
                this.pitch = (float) Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)) * Mth.RAD_TO_DEG;
        }
    }

    protected float getRotationSpeed() {
        return 30;
    }

    protected boolean lockRoll() {
        return false;
    }

    protected boolean lockYaw() {
        return false;
    }

    protected boolean lockPitch() {
        return false;
    }

    protected int getBounceCount() {
        return 5;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        // FIRST check if we should stop bouncing
        if (this.inGround) {
            super.onHitBlock(hitResult); // Handle vanilla ground sticking
            return;
        }

        // Play impact effects
        this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.shakeTime = 7;

        // Check bounce limit BEFORE physics
        this.bounceCount++;
        if (this.bounceCount > getBounceCount()) {
            this.inGround = true;
            super.onHitBlock(hitResult); // Final stick to ground
            return;
        }

        if (this.inGround) return;
        // Bounce physics
        Vec3 normal = Vec3.atLowerCornerOf(hitResult.getDirection().getNormal());
        Vec3 incoming = this.getDeltaMovement();
        Vec3 reflected = incoming.subtract(normal.scale(2 * incoming.dot(normal)));

        this.setDeltaMovement(reflected.scale(getBounciness()));

        // Position adjustment
        Vec3 newPos = hitResult.getLocation().add(reflected.normalize().scale(0.1));
        this.setPos(newPos.x, newPos.y, newPos.z);

        // Reduce piercing
        if (this.getPierceLevel() > 0) {
            this.setPierceLevel((byte) (this.getPierceLevel() - 1));
        }
    }

    protected float getBounciness() {
        return 0.175F;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (this.inGround) return; // Critical: Skip if already grounded

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) hitResult);
        } else {
            super.onHit(hitResult);
        }
    }

    protected boolean shouldDiscard() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    public float getModelHeight() {
        return 1;
    }
}
