package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import static net.goo.brutality.util.ModResources.RAINBOW_TRAIL_FX;

public class BiomechReactor extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(BiomechReactor.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HOMING_COOLDOWN = SynchedEntityData.defineId(BiomechReactor.class, EntityDataSerializers.INT);

    public BiomechReactor(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public BiomechReactor(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }


    @Override
    public float getGravity() {
        return 0;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("HomingTargetId", Tag.TAG_INT)) {
            this.entityData.set(HOMING_TARGET_ID, pCompound.getInt("HomingTargetId"));
        }
        if (pCompound.contains("HitCount", Tag.TAG_INT)) {
            this.entityData.set(HOMING_COOLDOWN, pCompound.getInt("HomingCooldown"));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOMING_TARGET_ID, -1);
        this.entityData.define(HOMING_COOLDOWN, 25);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("HomingTargetId", this.entityData.get(HOMING_TARGET_ID));
        pCompound.putInt("HomingCooldown", this.entityData.get(HOMING_COOLDOWN));
    }

    @Override
    protected int getLifespan() {
        return 200;
    }

    @Override
    public void tick() {
        if (firstTick && !(level() instanceof ServerLevel)) {
            EntityEffect rainbowTrail = new EntityEffect(RAINBOW_TRAIL_FX.get(), this.level(), this, EntityEffect.AutoRotate.NONE);
            rainbowTrail.start();
        }


        int cooldown = this.entityData.get(HOMING_COOLDOWN);
        if (cooldown > 0) {
            this.entityData.set(HOMING_COOLDOWN, cooldown - 1);
        } else if (!this.inGround && !this.dealtDamage) {
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
                        new AABB(this.blockPosition()).inflate(25)
                );

                if (nearestMob != null) {
                    this.entityData.set(HOMING_TARGET_ID, nearestMob.getId());
                    this.level().playLocalSound(this.getOnPos(), BrutalitySounds.TARGET_FOUND.get(), SoundSource.AMBIENT, 4F, Mth.nextFloat(level().random, 0.8F, 1.2F), false);
                }

            } else {

                Entity target = level().getEntity(this.entityData.get(HOMING_TARGET_ID));

                if (target != null) {
                    this.collideWithBlocks = false;
                    Vec3 toVec = target.getPosition(1F).subtract(this.getPosition(1F));
                    double distance = toVec.length();

                    if (distance > 0.01) {
//                        double scale = 0.05 * (distance + 0.1);
                        double scale = 2.5 / (distance * 2 + 0.1) + 0.25;
                        Vec3 motion = toVec.normalize().scale(scale);
                        this.addDeltaMovement(motion);

                        if (this.getDeltaMovement().length() > 1) {
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
                        }
                    }
                } else {
                    this.collideWithBlocks = true;
                }
            }
        }

        super.tick();


    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }


    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (this.getOwner() instanceof LivingEntity owner) {
            for (LivingEntity livingEntity : level().getNearbyEntities(LivingEntity.class,
                    TargetingConditions.DEFAULT.ignoreInvisibilityTesting().ignoreLineOfSight(), owner, this.getBoundingBox().inflate(10))) {
                livingEntity.hurt(livingEntity.damageSources().explosion(owner, livingEntity), BrutalityCommonConfig.BIOMECH_REACTOR_DAMAGE.get());
                livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.MIRACLE_BLIGHT.get(), 40, 0, false, false));
            }

        }

        this.playSound(BrutalitySounds.BIOMECH_REACTOR_BOOM.get(), 5F, Mth.nextFloat(this.random, 0.8F, 1.2F));
        this.discard();
    }


    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        Vec3 location = hitResult.getLocation();
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityParticles.BIOMECH_REACTOR_PARTICLE.get(),
                    location.x, location.y, location.z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        Entity hitEntity = hitResult.getEntity();
        Vec3 location = hitResult.getLocation();

        if (hitEntity instanceof LivingEntity livingEntity && livingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityParticles.BIOMECH_REACTOR_PARTICLE.get(),
                    location.x, location.y + hitEntity.getBbHeight() / 2, location.z, 1, 0, 0, 0, 0);

        }
    }


    @Override
    protected float getBounciness() {
        return 0.25F;
    }

    @Override
    public float getModelHeight() {
        return 16;
    }
}
