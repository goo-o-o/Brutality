package net.goo.armament.entity.base;

import net.goo.armament.particle.custom.SwordBeamTrail;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.goo.armament.util.ModUtils.nextFloatBetweenInclusive;

public class SwordBeam extends ThrowableProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean shouldRandomRoll;
    private int randomRoll, targetsHit = 0, pierceCap = 3, lifespan;
    private String identifier;
    private static final EntityDataAccessor<Integer> CURRENT_FRAME = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_PIERCE_CAP = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.INT);
    private int ticksPerFrame;
    private int totalFrames;
    private float renderScale, damage = 7.5F;

    public SwordBeam(EntityType<SwordBeam> entityType, Level level, String identifier, int lifespan, boolean shouldRandomRoll, int pierceCap, float damage, int ticksPerFrame, int totalFrames, float renderScale) {
        super(entityType, level);
        this.identifier = identifier;
        this.lifespan = lifespan;
        this.shouldRandomRoll = shouldRandomRoll;
        this.ticksPerFrame = ticksPerFrame;
        this.totalFrames = totalFrames;
        this.renderScale = renderScale;
        initializeRoll(level);
        this.entityData.set(DATA_DAMAGE, damage);
        this.entityData.set(DATA_PIERCE_CAP, pierceCap);
        this.damage = damage;
        this.pierceCap = pierceCap;

            Minecraft.getInstance().player.sendSystemMessage(Component.literal(
                    + this.damage + ", pierceCap: " + this.pierceCap));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(this.lifespan + ""));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("" + this));

    }

    public SwordBeam(EntityType<? extends SwordBeam> entityType, Level level) {
        super(entityType, level);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getCurrentFrame() {
        return entityData.get(CURRENT_FRAME);
    }


    public float getRenderScale() { return this.renderScale; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    private void initializeRoll(Level level) {
        this.randomRoll = level.random.nextInt(361);
    }

    public int getRandomRoll() {
        return randomRoll;
    }

    public float getDamage() {
        return this.entityData.get(DATA_DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DATA_DAMAGE, damage);
    }

    public int getPierceCap() {
        return this.entityData.get(DATA_PIERCE_CAP);
    }

    public void setPierceCap(int pierceCap) {
        this.entityData.set(DATA_PIERCE_CAP, pierceCap);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public float getPickRadius() {
        return 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level().isClientSide) { // Ensure this logic runs only on the server
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();

            // Debug message to confirm server-side execution
            if (owner != null) {
                ((Player) owner).sendSystemMessage(Component.literal("Server: Hit entity with damage " + this.entityData.get(DATA_DAMAGE) + " and pierce cap " + this.entityData.get(DATA_PIERCE_CAP)));
            }

            // Check if the target is valid and not the owner or another SwordBeam
            if (target != owner && !(target instanceof SwordBeam)) {
                // Apply damage to the target
                target.hurt(damageSources().magic(), this.entityData.get(DATA_DAMAGE));

                // Increment pierce cap and check if the beam should be discarded
                targetsHit++;
                if (this.targetsHit > this.getPierceCap()) {
                    this.discard(); // Discard the beam if pierce cap is reached
                }
            }
        }
    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        super.shootFromRotation(pShooter, pShooter.getXRot(), pShooter.getYRot(), 0.0F, pVelocity, 0.0F);
    }

    @Override
    public void setSilent(boolean pIsSilent) {
        super.setSilent(true);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_DAMAGE, 1.6F);
        this.entityData.define(DATA_PIERCE_CAP, 2);
        this.entityData.define(CURRENT_FRAME, 8);
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
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
            float b = nextFloatBetweenInclusive(random, 0.25F, 0.5F);
            this.level().addParticle((new SwordBeamTrail.OrbData(0, g, b,random.nextFloat() * 0.3f,random.nextFloat() * 0.3f, this.getId(), getRandomRoll())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            this.setPos(d0, d1, d2);

            if (tickCount >= lifespan || this.getDeltaMovement().length() < 0.1) {
                this.discard();
            }

        }

    }

    protected float getInertia() {
        return 0.925F;
    }

}