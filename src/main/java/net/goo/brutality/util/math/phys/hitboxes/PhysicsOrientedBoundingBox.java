package net.goo.brutality.util.math.phys.hitboxes;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

/**
 * OrientedBoundingBox with physics properties for realistic collision response.
 */
public class PhysicsOrientedBoundingBox extends OrientedBoundingBox implements PhysicsBoundingBox {
    
    private float restitution = 0.6f; // Default bounce
    private float friction = 0.3f;     // Default friction
    
    public PhysicsOrientedBoundingBox(Vec3 center, Vec3 halfExtents, Matrix3f rotation) {
        super(center, halfExtents, rotation);
    }
    
    public PhysicsOrientedBoundingBox(Vec3 center, Vec3 halfExtents, float pitch, float yaw, float roll) {
        super(center, halfExtents, pitch, yaw, roll);
    }
    
    public PhysicsOrientedBoundingBox(Vec3 center, float hx, float hy, float hz) {
        super(center, hx, hy, hz);
    }
    
    public PhysicsOrientedBoundingBox setRestitution(float restitution) {
        this.restitution = Math.max(0f, Math.min(1f, restitution));
        return this;
    }
    
    public PhysicsOrientedBoundingBox setFriction(float friction) {
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
        // Transform impact point to local space
        Vector3f localImpact = impactPoint.subtract(center).toVector3f();
        Matrix3f invRot = new Matrix3f(rotation).invert();
        invRot.transform(localImpact);
        
        // Find which face is closest
        float absX = Math.abs(localImpact.x / (float)halfExtents.x);
        float absY = Math.abs(localImpact.y / (float)halfExtents.y);
        float absZ = Math.abs(localImpact.z / (float)halfExtents.z);
        
        Vector3f localNormal;
        if (absX >= absY && absX >= absZ) {
            // X face
            localNormal = new Vector3f(Math.signum(localImpact.x), 0, 0);
        } else if (absY >= absX && absY >= absZ) {
            // Y face
            localNormal = new Vector3f(0, Math.signum(localImpact.y), 0);
        } else {
            // Z face
            localNormal = new Vector3f(0, 0, Math.signum(localImpact.z));
        }
        
        // Transform normal back to world space
        rotation.transform(localNormal);
        return new Vec3(localNormal.x, localNormal.y, localNormal.z).normalize();
    }
    
    @Override
    public BaseBoundingBox copy() {
        PhysicsOrientedBoundingBox copy = new PhysicsOrientedBoundingBox(center, halfExtents, new Matrix3f(rotation));
        copy.restitution = this.restitution;
        copy.friction = this.friction;
        return copy;
    }
}