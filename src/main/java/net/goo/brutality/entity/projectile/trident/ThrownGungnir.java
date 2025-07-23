package net.goo.brutality.entity.projectile.trident;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.particle.base.GenericTridentTrailParticle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownGungnir extends BrutalityAbstractTrident implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(ThrownGungnir.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HIT_COUNT = SynchedEntityData.defineId(ThrownGungnir.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HOMING_COOLDOWN = SynchedEntityData.defineId(ThrownGungnir.class, EntityDataSerializers.INT);

    public ThrownGungnir(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
        this.entityData.set(HOMING_TARGET_ID, -1);
        this.entityData.set(HIT_COUNT, 0);
        this.entityData.set(HOMING_COOLDOWN, 0);
    }

    public ThrownGungnir(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOMING_TARGET_ID, -1);
        this.entityData.define(HIT_COUNT, 0);
        this.entityData.define(HOMING_COOLDOWN, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("HomingTargetId", this.entityData.get(HOMING_TARGET_ID));
        pCompound.putInt("HitCount", this.entityData.get(HIT_COUNT));
        pCompound.putInt("HomingCooldown", this.entityData.get(HOMING_COOLDOWN));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("HomingTargetId", Tag.TAG_INT)) {
            this.entityData.set(HOMING_TARGET_ID, pCompound.getInt("HomingTargetId"));
        }
        if (pCompound.contains("HitCount", Tag.TAG_INT)) {
            this.entityData.set(HIT_COUNT, pCompound.getInt("HitCount"));
        }
        if (pCompound.contains("HitCount", Tag.TAG_INT)) {
            this.entityData.set(HOMING_COOLDOWN, pCompound.getInt("HomingCooldown"));
        }
    }


    @Override
    protected Vec3 getTridentBounceStrength() {
        return new Vec3(-0.01D, -1D, -0.01D);
    }


    boolean trailSpawned;

    @Override
    public void tick() {
        if (!trailSpawned && level().isClientSide()) {
            this.level().addParticle((new GenericTridentTrailParticle.OrbData(1F, 0F, 0F, this.getBbHeight() * 0.75F, this.getId(), 0, 0, 0, "circle", 10)), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
            trailSpawned = true;
        }

        super.tick();

        if (this.entityData.get(HIT_COUNT) >= getHitQuota()) {
            this.dealtDamage = true; // <- Set this if necessary for vanilla behavior
            return;
        }

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
    protected int getHitQuota() {
        return BrutalityCommonConfig.GUNGNIR_HIT_QUOTA.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        int hitCount = this.entityData.get(HIT_COUNT);
        this.entityData.set(HIT_COUNT, hitCount + 1);

        this.entityData.set(HOMING_COOLDOWN, 20);

        if (hitCount >= getHitQuota()) {
            this.dealtDamage = true;
        }
    }
}
