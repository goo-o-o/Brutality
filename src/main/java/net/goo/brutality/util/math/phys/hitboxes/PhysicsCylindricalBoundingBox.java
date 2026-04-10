package net.goo.brutality.util.math.phys.hitboxes;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

/**
 * CylindricalBoundingBox with physics properties for realistic collision response.
 */
public class PhysicsCylindricalBoundingBox extends CylindricalBoundingBox implements PhysicsBoundingBox {
    
    private float restitution = 0.6f;
    private float friction = 0.3f;
    
    public PhysicsCylindricalBoundingBox(Vec3 center, float height, float radius, float innerRadius) {
        super(center, height, radius, innerRadius);
    }
    
    public PhysicsCylindricalBoundingBox setRestitution(float restitution) {
        this.restitution = Math.max(0f, Math.min(1f, restitution));
        return this;
    }
    
    public PhysicsCylindricalBoundingBox setFriction(float friction) {
        this.friction = Math.max(0f, Math.min(1f, friction));
        return this;
    }
    
    @Override
    public float getRestitution() {
        return restitution;
    }
    
    @Override
    public float getFriction() {
        return friction;
    }
    
    @Override
    public Vec3 getCollisionNormal(Vec3 impactPoint, Vec3 entityCenter) {
        // Transform to local space
        Vector3f localImpact = impactPoint.subtract(center).toVector3f();
        Matrix3f invRot = new Matrix3f(rotation).invert();
        invRot.transform(localImpact);
        
        // Check if hit top or bottom cap
        float distanceFromMidPlane = Math.abs(localImpact.y);
        float radialDistSq = localImpact.x * localImpact.x + localImpact.z * localImpact.z;
        
        Vector3f localNormal;
        
        // If we're near the top/bottom edge, return vertical normal
        if (distanceFromMidPlane > halfHeight * 0.9f) {
            localNormal = new Vector3f(0, Math.signum(localImpact.y), 0);
        } else {
            // Radial collision - return direction from axis
            float radialDist = (float)Math.sqrt(radialDistSq);
            if (radialDist < 0.01f) {
                // Very close to center - use entity direction
                Vector3f entityLocal = entityCenter.subtract(center).toVector3f();
                invRot.transform(entityLocal);
                localNormal = new Vector3f(entityLocal.x, 0, entityLocal.z).normalize();
            } else {
                localNormal = new Vector3f(localImpact.x / radialDist, 0, localImpact.z / radialDist);
            }
        }
        
        // Transform normal back to world space
        rotation.transform(localNormal);
        return new Vec3(localNormal.x, localNormal.y, localNormal.z).normalize();
    }
    
    @Override
    public BaseBoundingBox copy() {
        PhysicsCylindricalBoundingBox copy = new PhysicsCylindricalBoundingBox(center, height, radius, innerRadius);
        copy.rotation.set(this.rotation);
        copy.restitution = this.restitution;
        copy.friction = this.friction;
        return copy;
    }
}