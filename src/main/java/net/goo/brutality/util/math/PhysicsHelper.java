package net.goo.brutality.util.math;

import net.goo.brutality.util.math.phys.PhysicsCollisionResult;
import net.goo.brutality.util.math.phys.hitboxes.PhysicsBoundingBox;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;
/**
 * Utility class for physics calculations including collision response,
 * bouncing, friction, and rotation updates.
 */
public class PhysicsHelper {

    public static Vec3i toVector3i(Vec3 vec3) {
        return new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }

    public static Vector3f toVector3f(Vec3 vec3) {
        return new Vector3f((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }
    public static Vec3 fromVector3f(Vector3f vec3) {
        return new Vec3((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }

    /**
     * Calculate the collision response for an entity bouncing off a physics bounding box.
     *
     * @param velocity Current velocity of the entity
     * @param impactPoint Where the collision occurred
     * @param entityCenter Center of the colliding entity
     * @param boundingBox The physics bounding box being collided with
     * @param entityMass Mass of the entity (affects rotation speed)
     * @param entityMomentScale Moment of inertia scale (0.5-2.5, higher = slower rotation)
     * @return PhysicsCollisionResult with new velocity, rotation, etc.
     */
    public static PhysicsCollisionResult calculateCollision(
            Vec3 velocity,
            Vec3 impactPoint,
            Vec3 entityCenter,
            PhysicsBoundingBox boundingBox,
            float entityMass,
            float entityMomentScale
    ) {
        // Get collision normal from the bounding box
        Vec3 normal = boundingBox.getCollisionNormal(impactPoint, entityCenter);

        // Calculate impact speed
        double impactSpeed = Math.abs(velocity.dot(normal));

        if (impactSpeed < 0.01) {
            // Barely touching - no collision response
            return new PhysicsCollisionResult(
                    normal, impactPoint,
                    boundingBox.getRestitution(), boundingBox.getFriction(),
                    velocity, Vec3.ZERO, new Matrix3f(),
                    0f, 0f
            );
        }

        // Decompose velocity into normal and tangent components
        Vec3 velocityNormal = normal.scale(velocity.dot(normal));
        Vec3 velocityTangent = velocity.subtract(velocityNormal);

        // Apply restitution (bounce)
        float restitution = boundingBox.getRestitution();
        Vec3 reflectedNormal = velocityNormal.scale(-restitution);

        // Apply friction to tangent component
        float friction = boundingBox.getFriction();
        double tangentSpeed = velocityTangent.length();
        Vec3 frictionForce = tangentSpeed > 0.01
                ? velocityTangent.normalize().scale(-friction * impactSpeed)
                : Vec3.ZERO;

        Vec3 newTangent = velocityTangent.add(frictionForce);

        // Clamp tangent to prevent reversing direction
        if (tangentSpeed > 0.01 && newTangent.dot(velocityTangent) < 0) {
            newTangent = Vec3.ZERO;
        }

        // Combined new velocity
        Vec3 newVelocity = reflectedNormal.add(newTangent);

        // Calculate angular velocity (spin from impact)
        Vec3 torqueAxis = boundingBox.getTorqueAxis(velocity, normal);
        double angularSpeed = 0;

        if (torqueAxis.lengthSqr() > 0.001) {
            // Angular speed proportional to impact speed and inversely to moment
            angularSpeed = (impactSpeed * friction) / (entityMass * entityMomentScale);
            angularSpeed = Math.min(angularSpeed, Math.PI); // Cap rotation speed
        }

        Vec3 angularVelocity = torqueAxis.scale(angularSpeed);

        // Create rotation delta for this frame
        Matrix3f rotationDelta = createRotationFromAngularVelocity(angularVelocity, 0.05f); // Assume 1 tick = 0.05s

        // Calculate energy loss
        double initialEnergy = velocity.lengthSqr();
        double finalEnergy = newVelocity.lengthSqr();
        float energyLoss = (float)((initialEnergy - finalEnergy) / Math.max(initialEnergy, 0.001));

        return new PhysicsCollisionResult(
                normal, impactPoint,
                restitution, friction,
                newVelocity, angularVelocity,
                rotationDelta,
                (float)impactSpeed, energyLoss
        );
    }

    /**
     * Create a rotation matrix from angular velocity vector.
     * The direction of the vector is the rotation axis, magnitude is angular speed.
     *
     * @param angularVelocity Angular velocity vector (rad/s)
     * @param deltaTime Time step in seconds
     * @return Rotation matrix for this time step
     */
    public static Matrix3f createRotationFromAngularVelocity(Vec3 angularVelocity, float deltaTime) {
        double angle = angularVelocity.length() * deltaTime;

        if (angle < 0.0001) {
            return new Matrix3f(); // Identity - no rotation
        }

        Vec3 axis = angularVelocity.normalize();
        Vector3f axisVec = new Vector3f((float)axis.x, (float)axis.y, (float)axis.z);

        return new Matrix3f().rotation((float)angle, axisVec);
    }

    /**
     * Apply rotation delta to entity's pitch, yaw, and roll.
     * Extracts Euler angles from the rotation matrix.
     *
     * @param currentPitch Current pitch in degrees
     * @param currentYaw Current yaw in degrees
     * @param currentRoll Current roll in degrees
     * @param rotationDelta Rotation matrix to apply
     * @return float[3] with new {pitch, yaw, roll} in degrees
     */
    public static float[] applyRotationDelta(float currentPitch, float currentYaw, float currentRoll, Matrix3f rotationDelta) {
        // Convert current angles to rotation matrix
        Matrix3f current = new Matrix3f()
                .rotateY((float)Math.toRadians(-currentYaw))   // MC yaw is CCW
                .rotateX((float)Math.toRadians(currentPitch))
                .rotateZ((float)Math.toRadians(currentRoll));

        // Apply delta
        current.mul(rotationDelta);

        // Extract new Euler angles (YXZ order to match Minecraft)
        return extractEulerAngles(current);
    }

    /**
     * Extract Euler angles from a rotation matrix in YXZ order (yaw, pitch, roll).
     *
     * @param matrix Rotation matrix
     * @return float[3] with {pitch, yaw, roll} in degrees
     */
    public static float[] extractEulerAngles(Matrix3f matrix) {
        // Extract pitch (X rotation)
        float pitch = (float)Math.asin(-Math.max(-1f, Math.min(1f, matrix.m21())));

        float yaw, roll;

        // Check for gimbal lock
        if (Math.abs(matrix.m21()) > 0.9999f) {
            // Gimbal lock case
            yaw = (float)Math.atan2(-matrix.m02(), matrix.m00());
            roll = 0;
        } else {
            yaw = (float)Math.atan2(matrix.m20(), matrix.m22());
            roll = (float)Math.atan2(matrix.m01(), matrix.m11());
        }

        // Convert to degrees and negate yaw for MC convention
        return new float[] {
                (float)Math.toDegrees(pitch),
                -(float)Math.toDegrees(yaw),  // MC yaw is CCW
                (float)Math.toDegrees(roll)
        };
    }

    /**
     * Apply air resistance to velocity.
     *
     * @param velocity Current velocity
     * @param dragCoefficient Drag coefficient (0.0-1.0, typical 0.01-0.05)
     * @param deltaTime Time step in seconds
     * @return New velocity after drag
     */
    public static Vec3 applyDrag(Vec3 velocity, float dragCoefficient, float deltaTime) {
        double speed = velocity.length();
        if (speed < 0.001) return velocity;

        // Drag force proportional to speed squared
        double dragForce = dragCoefficient * speed * speed * deltaTime;
        double newSpeed = Math.max(0, speed - dragForce);

        return velocity.normalize().scale(newSpeed);
    }

    /**
     * Calculate the moment of inertia scale for common shapes.
     * Higher values = slower rotation response to impacts.
     *
     * @param shape "sphere", "box", "cylinder"
     * @return Moment scale factor
     */
    public static float getMomentScale(String shape) {
        return switch (shape.toLowerCase()) {
            case "sphere" -> 0.4f;      // I = (2/5)mr²
            case "box" -> 1.67f;         // I = (1/6)m(w²+h²) approximation
            case "cylinder" -> 0.5f;     // I = (1/2)mr² around axis
            default -> 1.0f;
        };
    }
}
