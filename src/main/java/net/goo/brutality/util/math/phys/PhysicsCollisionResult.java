package net.goo.brutality.util.math.phys;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

/**
 * Result of a physics collision calculation.
 * Contains all data needed to apply realistic bounce and rotation.
 */
public class PhysicsCollisionResult {
    
    // Collision data
    public final Vec3 collisionNormal;
    public final Vec3 impactPoint;
    public final float restitution;
    public final float friction;
    
    // Response velocities
    public final Vec3 newVelocity;
    public final Vec3 angularVelocity;
    
    // Rotation change
    public final Matrix3f rotationDelta;
    
    // Metrics
    public final float impactSpeed;
    public final float energyLoss;
    
    public PhysicsCollisionResult(
            Vec3 collisionNormal,
            Vec3 impactPoint,
            float restitution,
            float friction,
            Vec3 newVelocity,
            Vec3 angularVelocity,
            Matrix3f rotationDelta,
            float impactSpeed,
            float energyLoss
    ) {
        this.collisionNormal = collisionNormal;
        this.impactPoint = impactPoint;
        this.restitution = restitution;
        this.friction = friction;
        this.newVelocity = newVelocity;
        this.angularVelocity = angularVelocity;
        this.rotationDelta = rotationDelta;
        this.impactSpeed = impactSpeed;
        this.energyLoss = energyLoss;
    }
    
    public boolean hasCollision() {
        return collisionNormal != null;
    }
}