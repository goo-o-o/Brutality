package net.goo.brutality.util.math.phys;

import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class PhysicsProcessor {
    public Vector3f angularVelocity = new Vector3f(0, 0, 0);
    public float mass = 1.0f;
    public float elasticity = 0.3f; // Bounciness
    public float friction = 0.4f;   // Surface grip

    public void solvePhysics(Entity entity, OrientedBoundingBox obb) {
        Level level = entity.level();
        Vec3 velocity = entity.getDeltaMovement();
        Matrix3f worldInertiaInv = obb.getInertiaTensor(mass).invert();

        for (Vec3 vertex : obb.getTransformedVertices()) {
            BlockPos pos = BlockPos.containing(vertex);
            if (level.getBlockState(pos).isSolidRender(level, pos)) {

                // 1. Calculate relative velocity at the contact point
                Vector3f r = new Vector3f((float)(vertex.x - obb.center.x), (float)(vertex.y - obb.center.y), (float)(vertex.z - obb.center.z));
                Vector3f tangentialVel = new Vector3f(angularVelocity).cross(r);
                Vector3f pointVelocity = new Vector3f((float)velocity.x, (float)velocity.y, (float)velocity.z).add(tangentialVel);

                // 2. Collision Normal (Assume Up for floor, but can be improved)
                Vector3f normal = new Vector3f(0, 1, 0);
                float velAlongNormal = pointVelocity.dot(normal);

                // 3. Only resolve if moving INTO the block
                if (velAlongNormal < 0) {
                    float j = -(1 + elasticity) * velAlongNormal;

                    // The "Impulse Equation" denominator
                    Vector3f cross = new Vector3f(r).cross(normal);
                    worldInertiaInv.transform(cross);
                    float denom = (1f / mass) + normal.dot(new Vector3f(cross).cross(r));

                    j /= denom;

                    // 4. Apply Impulse
                    Vector3f impulse = new Vector3f(normal).mul(j);

                    // Linear change
                    velocity = velocity.add(impulse.x / mass, impulse.y / mass, impulse.z / mass);

                    // Angular change: I^-1 * (r x J)
                    Vector3f angImpulse = new Vector3f(r).cross(impulse);
                    worldInertiaInv.transform(angImpulse);
                    angularVelocity.add(angImpulse);

                    // 5. Apply Friction (very simplified)
                    velocity = velocity.multiply(0.95, 1.0, 0.95);
                    angularVelocity.mul(0.95f);
                }
            }
        }

        // Final updates
        entity.setDeltaMovement(velocity);
        if (angularVelocity.lengthSquared() > 1e-6f) {
            obb.rotation.rotateXYZ(angularVelocity.x, angularVelocity.y, angularVelocity.z);
            // Orthormalize the matrix to prevent scaling drift over time
            obb.rotation.transpose().invert();
        }
    }
}