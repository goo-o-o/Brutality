package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.client.particle.providers.EntityIdParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockchainedProjectile extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> TARGET_ID_DATA = SynchedEntityData.defineId(BlockchainedProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE_DATA = SynchedEntityData.defineId(BlockchainedProjectile.class, EntityDataSerializers.FLOAT);
    private static final String TARGET_ID = "target_id", DAMAGE = "damage";

    public BlockchainedProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static BlockchainedProjectile create(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel, LivingEntity owner, LivingEntity target, float damage) {
        BlockchainedProjectile projectile = new BlockchainedProjectile(pEntityType, pLevel);
        projectile.entityData.set(TARGET_ID_DATA, target.getId());
        projectile.entityData.set(DAMAGE_DATA, damage);
        projectile.setOwner(owner);
        return projectile;
    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(TARGET_ID_DATA, 1);
        this.entityData.define(DAMAGE_DATA, 1F);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TARGET_ID)) {
            this.entityData.set(TARGET_ID_DATA, tag.getInt(TARGET_ID));
        }
        if (tag.contains(DAMAGE)) {
            this.entityData.set(DAMAGE_DATA, tag.getFloat(DAMAGE));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TARGET_ID, this.entityData.get(TARGET_ID_DATA));
        tag.putFloat(DAMAGE, this.entityData.get(DAMAGE_DATA));
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isColliding(BlockPos pPos, BlockState pState) {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }


    @Override
    public void tick() {

        if (firstTick)
            if (level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new EntityIdParticleData<>(BrutalityParticles.BLOCKCHAINED_TRAIL_PARTICLE.get(), getId()), getX(), getY(0.5), getZ(), 1, 0, 0, 0, 0);
            }


        super.tick();

        if (!(level().getEntity(this.entityData.get(TARGET_ID_DATA)) instanceof LivingEntity target)) return;

        Vec3 toTarget = target.getEyePosition().subtract(this.position());
        double dist = toTarget.length();

        if (dist < 0.5) {  // Close enough
            float damage = this.entityData.get(DAMAGE_DATA);
            target.invulnerableTime = 0;
            if (getOwner() instanceof LivingEntity livingOwner)
                target.hurt(target.damageSources().mobAttack(livingOwner), damage);
            else target.hurt(target.damageSources().generic(), damage);
            discard();
            return;
        }

        double turnSpeed = 0.5;
        double maxSpeed = 0.5;
        double accel = 0.05;

        Vec3 currentVel = getDeltaMovement();
        Vec3 desiredDir = toTarget.normalize();

        Vec3 newVel = currentVel.add(desiredDir.scale(turnSpeed)).normalize();

        double speed = currentVel.length() + accel;
        if (speed > maxSpeed) speed = maxSpeed;

        setDeltaMovement(newVel.scale(speed));
    }

}
