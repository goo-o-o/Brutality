package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
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
        super.tick();  // Important: call first (handles position update)

        if (level() instanceof ClientLevel clientLevel) {
            Vec3 prev = new Vec3(xo, yo, zo);        // last tick
            Vec3 curr = position();                 // this tick
            Vec3 prevMotion = getDeltaMovement().scale(0.5);  // approx previous direction

            // Estimate control points for quadratic Bezier (smooth curve)
            Vec3 p1 = prev.add(prevMotion);          // pull curve forward from start

            double dist = prev.distanceTo(curr);
            if (dist < 0.01) return;

            int particles = (int) Math.ceil(dist * 8);  // ~12 per block, adjust for density

            for (int i = 1; i <= particles; i++) {  // start i=1 to skip exact prev pos
                float t = i / (float) particles;

                // Quadratic Bezier: (1-t)^2 * p0 + 2*(1-t)*t * p1 + t^2 * p2
                double oneMinusT = 1.0 - t;
                Vec3 pos = prev.scale(oneMinusT * oneMinusT)
                        .add(p1.scale(2 * oneMinusT * t))
                        .add(curr.scale(t * t));

                clientLevel.addParticle(
                        BrutalityModParticles.HEALING_PARTICLE.get(),
                        pos.x, pos.y, pos.z,
                        0, 0, 0
                );
            }
        }

        LivingEntity owner = getOwner() instanceof LivingEntity living ? living : null;
        if (owner == null) {
            discard();
            return;
        }

        Vec3 toOwner = owner.getEyePosition().subtract(this.position());
        double dist = toOwner.length();

        if (dist < 0.5) {  // Close enough
            owner.heal(1.0F);
            discard();
            return;
        }

        double turnSpeed = 0.5;
        double maxSpeed   = 0.75;
        double accel      = 0.05;

        Vec3 currentVel = getDeltaMovement();
        Vec3 desiredDir = toOwner.normalize();

        Vec3 newVel = currentVel.add(desiredDir.scale(turnSpeed)).normalize();

        double speed = currentVel.length() + accel;
        if (speed > maxSpeed) speed = maxSpeed;

        setDeltaMovement(newVel.scale(speed));
    }

}
