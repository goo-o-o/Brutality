package net.goo.brutality.common.entity.projectile.trident;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import javax.annotation.Nullable;

import static net.goo.brutality.util.ModResources.RAINBOW_TRAIL_FX;

public class ExobladeBeam extends BrutalityAbstractTrident implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(ExobladeBeam.class, EntityDataSerializers.INT);

    public ExobladeBeam(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.entityData.set(HOMING_TARGET_ID, -1);
        this.collideWithBlocks = false;
    }

    public ExobladeBeam(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOMING_TARGET_ID, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("HomingTargetId", this.entityData.get(HOMING_TARGET_ID));
    }

    @Override
    public float getGravity() {
        return 0;
    }

    public float getDamage(@Nullable LivingEntity livingEntity) {
        return 12F;
    }

    @Override
    public int getInGroundLifespan() {
        return 60;
    }


    public void tick() {
        if (firstTick && !(level() instanceof ServerLevel)) {
//            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
                EntityEffect rainbowTrail = new EntityEffect(RAINBOW_TRAIL_FX, this.level(), this, EntityEffect.AutoRotate.NONE);
                rainbowTrail.start();
//            });
        }
        super.tick();

        tickDespawn();

        if (!this.inGround && !this.dealtDamage) {
            if (this.entityData.get(HOMING_TARGET_ID) == -1) {
                LivingEntity nearestMob = this.level().getNearestEntity(
                        LivingEntity.class,
                        TargetingConditions.forCombat()
                                .ignoreLineOfSight()
                                .ignoreInvisibilityTesting()
                                .selector(mob -> {
                                    if (mob == this.getOwner()) return false;
                                    if (mob instanceof Player player)
                                        return !player.isCreative() && !player.isSpectator();
                                    return true;
                                }),
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        new AABB(this.blockPosition()).inflate(5)
                );

                if (nearestMob != null) {
                    this.entityData.set(HOMING_TARGET_ID, nearestMob.getId());
                }

            } else {

                Entity target = level().getEntity(this.entityData.get(HOMING_TARGET_ID));

                if (target != null) {
                    this.collideWithBlocks = false;
                    Vec3 toVec = target.getPosition(1F).subtract(this.getPosition(1F));
                    double distance = toVec.length();

                    if (distance > 0.01) {
//                        double scale = 0.05 * (distance + 0.1);
                        double scale = 7.5 / (distance * 2 + 0.1) + 0.25;
                        Vec3 motion = toVec.normalize().scale(scale);
                        this.addDeltaMovement(motion);

                        if (this.getDeltaMovement().length() > 2) {
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
                        }

                    }
                } else {
                    this.collideWithBlocks = true;
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.level() instanceof ServerLevel serverLevel && serverLevel.random.nextBoolean()) {
            livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.MIRACLE_BLIGHT.get(), 80, 0, false, false));
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        this.discard();
        super.onHit(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}