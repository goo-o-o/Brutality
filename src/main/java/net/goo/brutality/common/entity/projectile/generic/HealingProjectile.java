package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.client.particle.providers.EntityIdParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HealingProjectile extends ThrowableProjectile {

    public HealingProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
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
            if (getOwner() != null && level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new EntityIdParticleData<>(BrutalityParticles.HEALING_TRAIL_PARTICLE.get(), getId()), getX(), getY(0.5), getZ(), 1, 0, 0, 0, 0);
            }


        super.tick();

        LivingEntity owner = getOwner() instanceof LivingEntity living ? living : null;
        if (owner == null) {
            discard();
            return;
        }

        Vec3 toOwner = owner.getEyePosition().subtract(this.position());
        double dist = toOwner.length();

        if (dist < 0.5) {  // Close enough
            owner.heal(1);
            discard();
            return;
        }

        double turnSpeed = 0.5;
        double maxSpeed = 0.5;
        double accel = 0.05;

        Vec3 currentVel = getDeltaMovement();
        Vec3 desiredDir = toOwner.normalize();

        Vec3 newVel = currentVel.add(desiredDir.scale(turnSpeed)).normalize();

        double speed = currentVel.length() + accel;
        if (speed > maxSpeed) speed = maxSpeed;

        setDeltaMovement(newVel.scale(speed));
    }

}
