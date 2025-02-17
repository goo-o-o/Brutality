package net.goo.armament.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TerraBeam extends ThrowableProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int lifespan = 60;
    private double randomRoll;
    private int pierceCap;

    public TerraBeam(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
        initializeRoll(level);
        this.pierceCap = 0;
    }

    public TerraBeam(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public TerraBeam(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
        this.noCulling = true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    private PlayState predicate(AnimationState animationState) {
        return null;
    }


    private void initializeRoll(Level level) {
        this.randomRoll = level.random.nextInt(360);
        boolean isRollInitialized = true;
    }

    public double getRandomRoll() {
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
        if (this.getOwner() != null) {
            Entity target = pResult.getEntity();
            if (pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof TerraBeam)) {
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
        super.setSilent(true);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
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

            Vec3 offsetVector = motion.normalize().scale(6);
            double xx = random.nextGaussian() * 0.15F;
            double yy = random.nextGaussian() * 0.15F;
            double zz = random.nextGaussian() * 0.15F;
            Vec3 direction = position();
            this.level().addParticle(this.getTrailParticle(), true, direction.x() + offsetVector.x(), direction.y() + offsetVector.y() + 0.5, direction.z() + offsetVector.z(), xx, yy, zz);
            this.setPos(d0, d1, d2);

        }
        if (tickCount >= lifespan || this.getDeltaMovement().length() < 0.1) {
            this.discard();
        }
    }


    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.COMPOSTER;
    }


    protected float getInertia() {
        return 0.925F;
    }

}
