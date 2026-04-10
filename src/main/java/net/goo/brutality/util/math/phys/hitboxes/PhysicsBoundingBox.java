package net.goo.brutality.util.math.phys.hitboxes;

import net.minecraft.world.phys.Vec3;

/**
 * Extension interface for bounding boxes that support physics simulation.
 * Provides bounce coefficients, friction, and collision response properties.
 */
public interface PhysicsBoundingBox {
    
    /**
     * Get the bounce coefficient (restitution) for this surface.
     * 0.0 = perfectly inelastic (no bounce)
     * 1.0 = perfectly elastic (full bounce)
     * Typical values: 0.3-0.9
     */
    float getRestitution();
    
    /**
     * Get the friction coefficient for this surface.
     * 0.0 = frictionless
     * 1.0 = high friction
     * Typical values: 0.1-0.8
     */
    float getFriction();
    
    /**
     * Calculate the collision normal at the point of impact.
     * For OBB: returns the face normal
     * For Cylinder: returns the radial direction or top/bottom normal
     * 
     * @param impactPoint The world-space point where collision occurred
     * @param entityCenter The center of the colliding entity
     * @return Normalized collision normal vector
     */
    Vec3 getCollisionNormal(Vec3 impactPoint, Vec3 entityCenter);
    
    /**
     * Calculate torque axis for rotational response to collision.
     * Returns the axis perpendicular to both velocity and collision normal.
     * 
     * @param velocity The velocity of the colliding entity
     * @param normal The collision normal
     * @return Normalized torque axis (or Vec3.ZERO if parallel)
     */
    default Vec3 getTorqueAxis(Vec3 velocity, Vec3 normal) {
        Vec3 cross = velocity.cross(normal);
        double lengthSq = cross.lengthSqr();
        return lengthSq < 1e-6 ? Vec3.ZERO : cross.normalize();
    }
}