package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.particle.providers.TrailParticleData;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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

public class ThrownBiomechReactor extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(ThrownBiomechReactor.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HOMING_COOLDOWN = SynchedEntityData.defineId(ThrownBiomechReactor.class, EntityDataSerializers.INT);

    public ThrownBiomechReactor(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.entityData.set(HOMING_TARGET_ID, -1);
        this.entityData.set(HOMING_COOLDOWN, 25);
    }

    public ThrownBiomechReactor(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);

        this.entityData.set(HOMING_TARGET_ID, -1);
        this.entityData.set(HOMING_COOLDOWN, 25);
    }


    @Override
    public float getGravity() {
        return 0;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
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
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("HomingTargetId", this.entityData.get(HOMING_TARGET_ID));
        pCompound.putInt("HomingCooldown", this.entityData.get(HOMING_COOLDOWN));
    }




    @Override
    public void tick() {
        super.tick();
        int cooldown = this.entityData.get(HOMING_COOLDOWN);
        if (cooldown > 0) {
            this.entityData.set(HOMING_COOLDOWN, cooldown - 1);
            return;
        }


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
                        new AABB(this.blockPosition()).inflate(25)
                );

                if (nearestMob != null) {
                    this.entityData.set(HOMING_TARGET_ID, nearestMob.getId());
                    this.level().playLocalSound(this.getOnPos(), BrutalityModSounds.TARGET_FOUND.get(), SoundSource.AMBIENT, 4F, Mth.nextFloat(level().random, 0.8F, 1.2F), false);
                }

            } else {

                if (firstTick && level().isClientSide) {
                    this.level().addParticle((new TrailParticleData(BrutalityModParticles.RAINBOW_TRAIL_PARTICLE.get(),
                            1, 1, 1, 1,this.getBbHeight() * 0.75F, this.getId(), 10)),
                            this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
                }

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
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }


    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.getOwner() instanceof LivingEntity owner) {
            for (LivingEntity livingEntity : level().getNearbyEntities(LivingEntity.class,
                    TargetingConditions.DEFAULT.ignoreInvisibilityTesting().ignoreLineOfSight(), owner, this.getBoundingBox().inflate(10))) {
                livingEntity.hurt(livingEntity.damageSources().explosion(owner, livingEntity), 30);
                livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.MIRACLE_BLIGHT.get(), 40, 0, false, false));
            }

        }

        this.playSound(BrutalityModSounds.BIOMECH_REACTOR_BOOM.get(), 5F, Mth.nextFloat(this.random, 0.8F, 1.2F));
        this.discard();
    }


    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        Vec3 location = hitResult.getLocation();
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.BIOMECH_REACTOR_PARTICLE.get(),
                    location.x, location.y, location.z, 1, 0, 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity hitEntity = hitResult.getEntity();
        Vec3 location = hitResult.getLocation();

        if (hitEntity instanceof LivingEntity livingEntity && livingEntity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(BrutalityModParticles.BIOMECH_REACTOR_PARTICLE.get(),
                    location.x, location.y + hitEntity.getBbHeight() / 2, location.z, 1, 0, 0, 0, 0);

        }
    }

    @Override
    public float getDamage() {
        return 20;
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
