package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SpectralMawEntity extends ThrowableProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(SpectralMawEntity.class, EntityDataSerializers.INT);

    public SpectralMawEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SpectralMawEntity(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel, int lifespan) {
        super(pEntityType, pShooter, pLevel);
        this.setLifespan(lifespan);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenPlayAndHold("spawn")))
        );
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(this, true);
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFESPAN, 20);
    }

    public int getLifespan() {
        return this.entityData.get(LIFESPAN);
    }

    public void setLifespan(int lifespan) {
        this.entityData.set(LIFESPAN, lifespan);
    }

    @Override
    public void onAddedToWorld() {
        this.level().playSound(this, this.getOnPos(), BrutalitySounds.SPECTRAL_MAW.get(), SoundSource.HOSTILE, 10, Mth.nextFloat(level().random, 0.5F, 1.5F));
        super.onAddedToWorld();
    }

    @Override
    public void tick() {

//        if (!trailSpawned && this.level().isClientSide()) {
//            for (int i = 0; i < 4; i++)
//                this.level().addParticle(new TrailParticleData(BrutalityModParticles.RUINED_PARTICLE.get(), 0.18F, 0.47F, 0.44F, 1,
//                                this.getBbHeight() * 0.75F, this.getId(), 10), this.getRandomX(2),
//                        this.getRandomY() - this.getBbHeight() / 2, this.getRandomZ(2), 0, 0, 0);
//            trailSpawned = true;
//
//        }

        if (this.tickCount > this.getLifespan()) {
            this.discard();
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
//        if (pResult.getEntity() instanceof LivingEntity entity && level() instanceof ServerLevel serverLevel) {
//            entity.addEffect(new MobEffectInstance(BrutalityModMobEffects.STUNNED.get(), 3, 0));
//                serverLevel.sendParticles(new TrailParticleData(BrutalityModParticles.RUINED_PARTICLE.get(), 0.18F, 0.47F, 0.44F, 1,
//                                this.getBbHeight() * 0.75F, this.getId(), 10),
//                        entity.getX(), entity.getY(), entity.getZ(), 10,
//                        0, 0, 0, 0);
//        }

        this.level().playSound(this, this.getOnPos(), SoundEvents.SOUL_ESCAPE, SoundSource.HOSTILE, 10, Mth.nextFloat(level().random, 0.5F, 1.5F));


        super.onHitEntity(pResult);
        this.discard();
    }


}
