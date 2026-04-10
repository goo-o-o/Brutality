package net.goo.brutality.common.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.util.math.phys.CollisionResolver;
import net.goo.brutality.util.math.phys.PhysicsComponent;
import net.goo.brutality.util.math.phys.hitboxes.BaseBoundingBox;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Physics-enabled projectile with realistic collision and rotation.
 * Uses a proper physics simulation instead of Minecraft's default projectile behavior.
 */
public abstract class BrutalityAbstractPhysicsProjectile extends Projectile implements BrutalityGeoEntity {

    // Physics system
    protected PhysicsComponent physics;
    protected BaseBoundingBox physicsHitbox;

    // Rotation tracking for rendering (interpolated)
    public float prevRoll;
    public float roll;
    public float prevYaw;
    public float yaw;
    public float prevPitch;
    public float pitch;

    // Lifecycle
    protected int ticksExisted = 0;
    protected boolean hasHitGround = false;
    protected int groundTicks = 0;

    public BrutalityAbstractPhysicsProjectile(EntityType<? extends Projectile> type, LivingEntity shooter, Level level) {
        super(type, level);
        this.setOwner(shooter);
        initPhysics();
    }

    protected BrutalityAbstractPhysicsProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        initPhysics();
    }

    /**
     * Initialize physics component with default values.
     * Override to customize physics properties.
     */
    protected void initPhysics() {
        physics = new PhysicsComponent(
                getMass(),
                getAirDrag(),
                getRestitution()
        );
        physics.setFriction(getFriction());
        physics.setEnableGravity(shouldApplyGravity());
        physics.setMaxSpeed(getMaxSpeed());

        // Create physics hitbox (default is oriented bounding box)
        physicsHitbox = createPhysicsHitbox();
    }

    /**
     * Create the physics hitbox. Override for custom shapes.
     */
    protected BaseBoundingBox createPhysicsHitbox() {
        return new OrientedBoundingBox(
                position(),
                getHitboxHalfExtents(),
                pitch, yaw, roll
        );
    }

    /**
     * Shoot the projectile with an initial velocity
     */
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 direction = new Vec3(x, y, z).normalize();

        // Add inaccuracy
        direction = direction.add(
                this.random.triangle(0.0, 0.0172275 * inaccuracy),
                this.random.triangle(0.0, 0.0172275 * inaccuracy),
                this.random.triangle(0.0, 0.0172275 * inaccuracy)
        ).normalize();

        // Set initial velocity
        physics.setVelocity(direction.scale(velocity));

        // Set initial rotation to match direction
        this.yaw = (float) (Mth.atan2(direction.x, direction.z) * Mth.RAD_TO_DEG);
        this.pitch = (float) (Mth.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)) * Mth.RAD_TO_DEG);

        // Add initial spin based on launch velocity
        if (shouldSpinOnLaunch()) {
            Vec3 spinAxis = direction.cross(new Vec3(0, 1, 0)).normalize();
            physics.setAngularVelocity(spinAxis.scale(velocity * 0.1));
        }
    }

    @Override
    public void tick() {
        super.tick();
        ticksExisted++;

        // Store previous rotation for interpolation
        this.prevRoll = this.roll;
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;

        if (!level().isClientSide) {
            // Server-side physics
            tickPhysics();
        } else {
            // Client-side: sync rotation from velocity
            syncRotationFromVelocity();
        }

        // Check for entity collisions
        checkEntityCollisions();

        // Despawn check
        if (shouldDespawn()) {
            this.discard();
        }
    }

    /**
     * Main physics update
     */
    protected void tickPhysics() {
        // Apply gravity
        physics.applyGravity();

        // Apply custom forces (wind, drag, etc.)
        applyCustomForces();

        // Integrate physics to get movement delta
        Vec3 movement = physics.integrate();

        // Update hitbox position and rotation
        updatePhysicsHitbox();

        // Resolve collisions and apply movement
        Vec3 finalMovement = CollisionResolver.resolveCollisions(
                level(),
                physicsHitbox,
                movement,
                physics
        );

        // Apply final position
        this.setPos(position().add(finalMovement));

        // Update hitbox to final position
        physicsHitbox.setCenter(position());

        // Check if grounded
        boolean isGrounded = CollisionResolver.isOnGround(level(), physicsHitbox);
        if (isGrounded) {
            onGroundHit();
        } else {
            groundTicks = 0;
            hasHitGround = false;
        }

        // Update rotation from angular velocity
        updateRotationFromAngularVelocity();

        // Sync velocity to Minecraft entity system (for replication)
        this.setDeltaMovement(physics.getVelocity());
    }

    /**
     * Update rotation based on angular velocity
     */
    protected void updateRotationFromAngularVelocity() {
        Vec3 angVel = physics.getAngularVelocity();

        this.pitch += (float) (angVel.x * Mth.RAD_TO_DEG);
        this.yaw += (float) (angVel.y * Mth.RAD_TO_DEG);
        this.roll += (float) (angVel.z * Mth.RAD_TO_DEG);

        // Keep angles in reasonable range
        this.pitch = Mth.wrapDegrees(this.pitch);
        this.yaw = Mth.wrapDegrees(this.yaw);
        this.roll = Mth.wrapDegrees(this.roll);
    }

    /**
     * Sync rotation from velocity (for visual consistency)
     */
    protected void syncRotationFromVelocity() {
        Vec3 vel = physics.getVelocity();
        double speed = vel.length();

        if (speed > 0.1 && !hasHitGround) {
            // Update yaw and pitch to face direction of movement

            if (!lockYaw())
                this.yaw = (float) (Mth.atan2(vel.z, vel.x) * Mth.RAD_TO_DEG) - 90.0f;

            if (!lockPitch())
                this.pitch = (float) (Mth.atan2(vel.y, Math.sqrt(vel.x * vel.x + vel.z * vel.z)) * Mth.RAD_TO_DEG);

            if (!lockRoll()) {
                this.roll += speed * getSpinSpeed();
            }
        }
    }

    /**
     * Update physics hitbox transform
     */
    protected void updatePhysicsHitbox() {
        if (physicsHitbox instanceof OrientedBoundingBox obb) {
            obb.setCenter(position());
            obb.setRotation(pitch, yaw, roll);
        } else {
            physicsHitbox.setCenter(position());
        }
    }

    /**
     * Check for collisions with entities
     */
    protected void checkEntityCollisions() {
        if (hasHitGround) return;

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level(),
                this,
                position(),
                position().add(physics.getVelocity()),
                physicsHitbox.getAABB().inflate(1.0),
                this::canHitEntity
        );

        if (entityHit != null && entityHit.getType() == HitResult.Type.ENTITY) {
            onHitEntity(entityHit);
        }
    }

    /**
     * Called when projectile hits the ground
     */
    protected void onGroundHit() {
        if (!hasHitGround) {
            hasHitGround = true;
            onFirstGroundHit();
        }

        groundTicks++;

        // Gradually stop movement
        physics.setVelocity(physics.getVelocity().scale(0.8));
        physics.setAngularVelocity(physics.getAngularVelocity().scale(0.8));

        if (groundTicks > 10 && physics.getVelocity().lengthSqr() < 0.001) {
            physics.stop();
        }
    }

    /**
     * Called on first ground contact
     */
    protected void onFirstGroundHit() {
//        playSound(getHitGroundSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();

        // Calculate impact force
        double impactSpeed = physics.getVelocity().length();
        float damage = (float) (impactSpeed * getDamageMultiplier());

        // Apply damage
        target.hurt(damageSources().thrown(this, getOwner()), damage);

        // Apply knockback based on physics
        if (target instanceof LivingEntity living) {
            Vec3 knockback = physics.getVelocity().normalize().scale(impactSpeed * 0.3);
            living.knockback(knockback.length(), -knockback.x, -knockback.z);
        }

        // Bounce or stick behavior
        if (shouldBounceOffEntities()) {
            Vec3 normal = position().subtract(target.position()).normalize();
            physics.handleCollision(normal, physics.getVelocity(), null);
        } else {
            physics.stop();
            hasHitGround = true;
        }

        onEntityHit(target);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            // Deflect based on attacker's knockback
            Vec3 deflection = attacker.getLookAngle().scale(amount * 0.5);
            physics.addImpulse(deflection);
            return true;
        }
        return super.hurt(source, amount);
    }

    // NBT Serialization

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        CompoundTag physicsTag = new CompoundTag();
        physics.save(physicsTag);
        tag.put("Physics", physicsTag);

        tag.putFloat("Pitch", pitch);
        tag.putFloat("Yaw", yaw);
        tag.putFloat("Roll", roll);
        tag.putInt("TicksExisted", ticksExisted);
        tag.putBoolean("HasHitGround", hasHitGround);
        tag.putInt("GroundTicks", groundTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Physics")) {
            physics.load(tag.getCompound("Physics"));
        }

        pitch = tag.getFloat("Pitch");
        yaw = tag.getFloat("Yaw");
        roll = tag.getFloat("Roll");
        ticksExisted = tag.getInt("TicksExisted");
        hasHitGround = tag.getBoolean("HasHitGround");
        groundTicks = tag.getInt("GroundTicks");

        prevPitch = pitch;
        prevYaw = yaw;
        prevRoll = roll;
    }

    // Override these methods to customize behavior

    /**
     * Mass of the projectile (affects momentum and forces)
     */
    protected float getMass() {
        return 1.0f;
    }

    /**
     * Air drag coefficient (0 = no drag, 1 = instant stop)
     */
    protected float getAirDrag() {
        return 0.99f;
    }

    /**
     * Bounciness (0 = no bounce, 1 = perfect bounce)
     */
    protected float getRestitution() {
        return 0.3f;
    }

    /**
     * Surface friction (0 = ice, 1 = velcro)
     */
    protected float getFriction() {
        return 0.8f;
    }

    /**
     * Maximum speed cap
     */
    protected float getMaxSpeed() {
        return 5.0f;
    }

    /**
     * Should gravity be applied
     */
    protected boolean shouldApplyGravity() {
        return true;
    }

    /**
     * Half-extents of the hitbox (x, y, z)
     */
    protected Vec3 getHitboxHalfExtents() {
        return new Vec3(0.25, 0.25, 0.25);
    }

    /**
     * Damage multiplier from impact speed
     */
    protected float getDamageMultiplier() {
        return 2.0f;
    }

    /**
     * Should the projectile bounce off entities
     */
    protected boolean shouldBounceOffEntities() {
        return false;
    }

    /**
     * Should the projectile spin when launched
     */
    protected boolean shouldSpinOnLaunch() {
        return true;
    }

    /**
     * Spin speed multiplier for visual roll
     */
    protected float getSpinSpeed() {
        return 20.0f;
    }

    /**
     * Lock roll rotation
     */
    protected boolean lockRoll() {
        return false;
    }

    protected boolean lockPitch() {
        return false;
    }

    protected boolean lockYaw() {
        return false;
    }

    /**
     * Apply custom forces each tick (wind, homing, etc.)
     */
    protected void applyCustomForces() {
        // Override to add custom forces
    }

    /**
     * Called when entity is hit
     */
    protected void onEntityHit(Entity target) {
        // Override for custom behavior
    }

    /**
     * Should the projectile despawn
     */
    protected boolean shouldDespawn() {
        return ticksExisted > 6000 || (hasHitGround && groundTicks > 200);
    }

    @Override
    protected void defineSynchedData() {
        // No extra synced data needed
    }


    // Static registration helper

    public static <T extends BrutalityAbstractPhysicsProjectile> RegistryObject<EntityType<T>> register(
            String name, EntityType.EntityFactory<T> factory, DeferredRegister<EntityType<?>> registry, float width, float height) {
        return registry.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(width, height)
                .clientTrackingRange(64)
                .updateInterval(1) // Update every tick for smooth physics
                .build(name));
    }

    // GeckoLib Animation

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Override to add animation controllers
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public float getModelHeight() {
        return 1.0f;
    }

    // Accessor methods

    public PhysicsComponent getPhysics() {
        return physics;
    }

    public BaseBoundingBox getPhysicsHitbox() {
        return physicsHitbox;
    }
}