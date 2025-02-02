package net.goo.armament.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class TerraBeamEntity extends AbstractHurtingProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final int lifespan = 60;
    private double randomRoll;
    private boolean isRollInitialized;
    private int pierceCap;

    public TerraBeamEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        initializeRoll(level);
        this.pierceCap = 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    private PlayState predicate(AnimationState animationState) {
        return null;
    }


    private void initializeRoll(Level level) {
        this.randomRoll = level.random.nextInt(360);
        this.isRollInitialized = true;
    }

    public double getRandomRoll() {
        return randomRoll;
    }

    public boolean isRollInitialized() {
        return isRollInitialized;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
        if (this.getOwner() != null) {
            Entity target = pResult.getEntity();
            if (pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof TerraBeamEntity)) {
                target.hurt(damageSources().magic(), 7.5F);
                pierceCap++;
                if (pierceCap >= 3) {
                    this.discard();
                }
            }
        }
    }

    @Override
    public void setSilent(boolean pIsSilent) {
        super.setSilent(pIsSilent);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
//            updateAnimation();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
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

            this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        }
        if (tickCount >= lifespan || this.getDeltaMovement().length() < 0.1) {
            this.discard();
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.COMPOSTER;
    }

    @Override
    protected float getInertia() {
        return 0.925F;
    }

}
