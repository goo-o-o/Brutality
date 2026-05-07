package net.goo.brutality.util.math.phys;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

/**
 * Result of a physics collision calculation.
 * Contains all data needed to apply realistic bounce and rotation.
 *
 * @param collisionNormal Collision data
 * @param newVelocity     Response velocities
 * @param rotationDelta   Rotation change
 * @param impactSpeed     Metrics
 */
public record PhysicsCollisionResult(Vec3 collisionNormal, Vec3 impactPoint, float restitution, float friction,
                                     Vec3 newVelocity, Vec3 angularVelocity, Matrix3f rotationDelta, float impactSpeed,
                                     float energyLoss) {

    public boolean hasCollision() {
        return collisionNormal != null;
    }
}