package net.goo.brutality.common.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BrutalityAbstractPhysicsThrowingProjectile extends BrutalityAbstractThrowingProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> BOUNCE_COUNT = SynchedEntityData.defineId(BrutalityAbstractPhysicsThrowingProjectile.class, EntityDataSerializers.INT);
    protected int bounceCount = 0;
    public float prevRoll;
    public float roll;
    public float prevYaw;
    public float yaw;
    public float prevPitch;
    public float pitch;

    public BrutalityAbstractPhysicsThrowingProjectile(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public BrutalityAbstractPhysicsThrowingProjectile(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOUNCE_COUNT, 0);
    }

    public int getBounceCount() {
        return this.entityData.get(BOUNCE_COUNT);
    }

    public void setBounceCount(int count) {
        this.entityData.set(BOUNCE_COUNT, count, true);
        this.bounceCount = count;
    }

    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        if (source.is(DamageTypes.PLAYER_ATTACK) && source.getEntity() instanceof Player player) {
            Vec3 look = player.getLookAngle().scale(0.5 + player.getAttributeValue(Attributes.ATTACK_KNOCKBACK));
            this.markHurt();
            this.inGround = false;
            setDeltaMovement(look);
            return false;
        }
        return super.hurt(source, pAmount);
    }

    protected Vec3 getEntityBounceStrength() {
        return new Vec3(-0.01D, -0.1D, -0.01D);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!inGround)
            this.setDeltaMovement(getDeltaMovement().multiply(getEntityBounceStrength()));
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
        if (speed > 0.25 && !inGround) {
            if (!lockRoll())
                this.roll += speed * getRotationSpeed(); // Adjust multiplier for rotation speed
            if (!lockYaw()) {
                this.yaw = (float) Math.atan2(motion.z, motion.x) * Mth.RAD_TO_DEG - 90.0f;
                this.yaw = Mth.wrapDegrees(this.yaw);
            }
            if (!lockPitch()) {
                this.pitch = (float) Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)) * Mth.RAD_TO_DEG;
                this.pitch = Mth.wrapDegrees(this.pitch);
            }
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

    protected int getBounceQuota() {
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
        this.playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.shakeTime = 7;


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
    protected void onHit(@NotNull HitResult hitResult) {
        if (inGround) return;
        setBounceCount(getBounceCount() + 1);

        if (getBounceCount() >= getBounceQuota()) {
            inGround = true;
            onFinalBounce(hitResult);
            if (shouldDiscardAfterBounce() && !level().isClientSide()) {
                DelayedTaskScheduler.queueServerWork(level(), 2, this::discard);
            }
        }

        super.onHit(hitResult);

    }


    protected void onFinalBounce(HitResult result) {

    }

    protected boolean shouldDiscardAfterBounce() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public float getModelHeight() {
        return 1;
    }
}
