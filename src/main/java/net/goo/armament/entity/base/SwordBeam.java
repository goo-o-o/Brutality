package net.goo.armament.entity.base;

import net.goo.armament.particle.custom.SwordBeamTrail;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.goo.armament.util.ModUtils.nextFloatBetweenInclusive;

public class SwordBeam extends ThrowableProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean shouldRandomRoll;
    private int randomRoll;
    private static int pierceCap, lifespan;
    private static float damage;
    private static String identifier;
    private static final EntityDataAccessor<Integer> CURRENT_FRAME = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PIERCE_CAP = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> IDENTIFIER = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(SwordBeam.class, EntityDataSerializers.FLOAT);
    private int ticksPerFrame;
    private int totalFrames;
    private float renderScale;
    private int targetsHit;

    public SwordBeam(EntityType<SwordBeam> entityType, Level level, String identifier, int lifespan, boolean shouldRandomRoll, int pierceCap, float damage, int ticksPerFrame, int totalFrames, float renderScale) {
        super(entityType, level);
        setIdentifier(identifier);
        setLifespan(lifespan);
        setPierceCap(pierceCap);
        setDamage(damage);
        this.shouldRandomRoll = shouldRandomRoll;
        this.ticksPerFrame = ticksPerFrame;
        this.totalFrames = totalFrames;
        this.renderScale = renderScale;
        initializeRoll(level);
    }

    public SwordBeam(@NotNull EntityType<SwordBeam> entityType, Level level) {
        super(entityType, level);
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int lifespan) {
        this.entityData.set(LIFESPAN, lifespan);
    }

    public int getPierceCap() {
        return this.entityData.get(PIERCE_CAP);
    }

    public void setPierceCap(int pierceCap) {
        this.entityData.set(PIERCE_CAP, pierceCap);
    }

    public String getIdentifier() {
        return this.entityData.get(IDENTIFIER);
    }

    public void setIdentifier(String identifier) {
        this.entityData.set(IDENTIFIER, identifier);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public int getCurrentFrame() {
        return entityData.get(CURRENT_FRAME);
    }

    public float getRenderScale() { return this.renderScale; }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    private void initializeRoll(Level level) {
        if (this.shouldRandomRoll) {this.randomRoll = level.random.nextInt(361); }
    }

    public int getRandomRoll() {
        return randomRoll;
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
        if (this.getOwner() != null && !this.level().isClientSide) {
            Entity target = pResult.getEntity();
            if (pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof SwordBeam)) {
                target.hurt(target.damageSources().playerAttack((Player) this.getOwner()), getDamage());
                targetsHit++;
                if (targetsHit >= getPierceCap()) {
                    this.discard();
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
        this.entityData.define(CURRENT_FRAME, 8);
        this.entityData.define(LIFESPAN, 60);
        this.entityData.define(DAMAGE, getDamage());
        this.entityData.define(PIERCE_CAP, getPierceCap());
        this.entityData.define(IDENTIFIER, getIdentifier());
    }

    @Override
    public void tick() {
//        if (this.level().isClientSide) { Minecraft.getInstance().player.sendSystemMessage(Component.literal("this.damage TICK: " + this.damage + ", this.pierceCap TICK: " + this.pierceCap)); }
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
            float b = nextFloatBetweenInclusive(random, 0.25F, 0.5F);
            this.level().addParticle((new SwordBeamTrail.OrbData(0, g, b,random.nextFloat() * 0.3f,random.nextFloat() * 0.3f, this.getId(), getRandomRoll())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);
            this.setPos(d0, d1, d2);

            if (this.tickCount >= getLifespan() || this.getDeltaMovement().length() < 0.1) {
                this.discard();
            }

        }
    }


    protected float getInertia() {
        return 0.925F;
    }

}
