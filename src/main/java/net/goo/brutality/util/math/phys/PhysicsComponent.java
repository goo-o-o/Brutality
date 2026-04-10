package net.goo.brutality.util.math.phys;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

/**
 * Core physics component for entities with realistic physics simulation.
 * Handles velocity, acceleration, forces, and integration.
 */
public class PhysicsComponent {
    
    // Movement state
    private Vec3 velocity = Vec3.ZERO;
    private Vec3 angularVelocity = Vec3.ZERO; // radians per tick (pitch, yaw, roll)
    
    // Physical properties
    private float mass = 1.0f;
    private float drag = 0.99f; // Air resistance multiplier per tick
    private float angularDrag = 0.98f;
    private float restitution = 0.3f; // Bounciness (0 = no bounce, 1 = perfect bounce)
    private float friction = 0.8f; // Surface friction on collision
    
    // Constraints
    private float maxSpeed = 5.0f;
    private float maxAngularSpeed = (float) Math.toRadians(720); // 720 degrees per tick max
    
    // Accumulated forces (cleared each tick)
    private Vec3 forceAccumulator = Vec3.ZERO;
    private Vec3 torqueAccumulator = Vec3.ZERO;
    
    // Collision response
    private boolean enableGravity = true;
    private boolean enableCollisions = true;
    
    public PhysicsComponent() {}
    
    public PhysicsComponent(float mass, float drag, float restitution) {
        this.mass = mass;
        this.drag = drag;
        this.restitution = restitution;
    }
    
    /**
     * Add a force to be applied this tick (in blocks/tick²)
     */
    public void addForce(Vec3 force) {
        this.forceAccumulator = this.forceAccumulator.add(force);
    }
    
    /**
     * Add an impulse (immediate velocity change)
     */
    public void addImpulse(Vec3 impulse) {
        this.velocity = this.velocity.add(impulse.scale(1.0 / mass));
        clampVelocity();
    }
    
    /**
     * Add torque to be applied this tick (in radians/tick²)
     */
    public void addTorque(Vec3 torque) {
        this.torqueAccumulator = this.torqueAccumulator.add(torque);
    }
    
    /**
     * Add angular impulse (immediate angular velocity change)
     */
    public void addAngularImpulse(Vec3 angularImpulse) {
        this.angularVelocity = this.angularVelocity.add(angularImpulse.scale(1.0 / mass));
        clampAngularVelocity();
    }
    
    /**
     * Apply gravity force
     */
    public void applyGravity() {
        if (enableGravity) {
            addForce(new Vec3(0, -0.04 * mass, 0)); // Minecraft gravity ≈ 0.08 blocks/tick²
        }
    }
    
    /**
     * Integrate physics for one tick
     * @return new position delta to apply
     */
    public Vec3 integrate() {
        // Apply accumulated forces: F = ma -> a = F/m
        Vec3 acceleration = forceAccumulator.scale(1.0 / mass);
        velocity = velocity.add(acceleration);
        
        // Apply accumulated torques
        Vec3 angularAcceleration = torqueAccumulator.scale(1.0 / mass);
        angularVelocity = angularVelocity.add(angularAcceleration);
        
        // Apply drag
        velocity = velocity.scale(drag);
        angularVelocity = angularVelocity.scale(angularDrag);
        
        // Clamp velocities to prevent instability
        clampVelocity();
        clampAngularVelocity();
        
        // Clear accumulators
        forceAccumulator = Vec3.ZERO;
        torqueAccumulator = Vec3.ZERO;
        
        // Return position delta
        return velocity;
    }
    
    /**
     * Handle collision with a surface
     * @param normal Surface normal (should be normalized)
     * @param relativeVelocity Velocity relative to the surface
     * @param tangent Optional tangent direction for friction
     */
    public void handleCollision(Vec3 normal, Vec3 relativeVelocity, Vec3 tangent) {
        if (!enableCollisions) return;
        
        // Decompose velocity into normal and tangent components
        double normalSpeed = relativeVelocity.dot(normal);
        
        // Only respond if moving into the surface
        if (normalSpeed < 0) {
            Vec3 normalVelocity = normal.scale(normalSpeed);
            Vec3 tangentVelocity = relativeVelocity.subtract(normalVelocity);
            
            // Apply restitution (bounce)
            Vec3 reflectedNormal = normal.scale(-normalSpeed * restitution);
            
            // Apply friction to tangent velocity
            Vec3 frictionedTangent = tangentVelocity.scale(friction);
            
            // Set new velocity
            velocity = reflectedNormal.add(frictionedTangent);
            
            // Add slight random spin on collision for realism
            if (tangent != null && tangent.lengthSqr() > 0.001) {
                Vec3 spinAxis = tangent.normalize();
                double spinAmount = Math.abs(normalSpeed) * 0.1;
                addAngularImpulse(spinAxis.scale(spinAmount));
            }
        }
    }
    
    /**
     * Apply a collision impulse (for entity-entity collisions)
     */
    public void applyCollisionImpulse(Vec3 impulse, Vec3 contactPoint, Vec3 centerOfMass) {
        // Linear impulse
        addImpulse(impulse);
        
        // Angular impulse from torque: τ = r × F
        Vec3 r = contactPoint.subtract(centerOfMass);
        Vec3 torque = new Vec3(
            r.y * impulse.z - r.z * impulse.y,
            r.z * impulse.x - r.x * impulse.z,
            r.x * impulse.y - r.y * impulse.x
        );
        addAngularImpulse(torque.scale(0.5)); // Scale down for stability
    }
    
    private void clampVelocity() {
        double speed = velocity.length();
        if (speed > maxSpeed) {
            velocity = velocity.scale(maxSpeed / speed);
        }
    }
    
    private void clampAngularVelocity() {
        double angularSpeed = angularVelocity.length();
        if (angularSpeed > maxAngularSpeed) {
            angularVelocity = angularVelocity.scale(maxAngularSpeed / angularSpeed);
        }
    }
    
    // Getters and Setters
    
    public Vec3 getVelocity() {
        return velocity;
    }
    
    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
        clampVelocity();
    }
    
    public Vec3 getAngularVelocity() {
        return angularVelocity;
    }
    
    public void setAngularVelocity(Vec3 angularVelocity) {
        this.angularVelocity = angularVelocity;
        clampAngularVelocity();
    }
    
    public float getMass() {
        return mass;
    }
    
    public void setMass(float mass) {
        this.mass = Math.max(0.1f, mass); // Prevent zero/negative mass
    }
    
    public float getDrag() {
        return drag;
    }
    
    public void setDrag(float drag) {
        this.drag = Math.max(0, Math.min(1, drag));
    }
    
    public float getAngularDrag() {
        return angularDrag;
    }
    
    public void setAngularDrag(float angularDrag) {
        this.angularDrag = Math.max(0, Math.min(1, angularDrag));
    }
    
    public float getRestitution() {
        return restitution;
    }
    
    public void setRestitution(float restitution) {
        this.restitution = Math.max(0, Math.min(1, restitution));
    }
    
    public float getFriction() {
        return friction;
    }
    
    public void setFriction(float friction) {
        this.friction = Math.max(0, Math.min(1, friction));
    }
    
    public float getMaxSpeed() {
        return maxSpeed;
    }
    
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    public boolean isEnableGravity() {
        return enableGravity;
    }
    
    public void setEnableGravity(boolean enableGravity) {
        this.enableGravity = enableGravity;
    }
    
    public boolean isEnableCollisions() {
        return enableCollisions;
    }
    
    public void setEnableCollisions(boolean enableCollisions) {
        this.enableCollisions = enableCollisions;
    }
    
    // NBT Serialization
    
    public void save(CompoundTag tag) {
        tag.putDouble("vx", velocity.x);
        tag.putDouble("vy", velocity.y);
        tag.putDouble("vz", velocity.z);
        
        tag.putDouble("avx", angularVelocity.x);
        tag.putDouble("avy", angularVelocity.y);
        tag.putDouble("avz", angularVelocity.z);
        
        tag.putFloat("mass", mass);
        tag.putFloat("drag", drag);
        tag.putFloat("angularDrag", angularDrag);
        tag.putFloat("restitution", restitution);
        tag.putFloat("friction", friction);
        tag.putFloat("maxSpeed", maxSpeed);
        
        tag.putBoolean("gravity", enableGravity);
        tag.putBoolean("collisions", enableCollisions);
    }
    
    public void load(CompoundTag tag) {
        velocity = new Vec3(
            tag.getDouble("vx"),
            tag.getDouble("vy"),
            tag.getDouble("vz")
        );
        
        angularVelocity = new Vec3(
            tag.getDouble("avx"),
            tag.getDouble("avy"),
            tag.getDouble("avz")
        );
        
        mass = tag.getFloat("mass");
        drag = tag.getFloat("drag");
        angularDrag = tag.getFloat("angularDrag");
        restitution = tag.getFloat("restitution");
        friction = tag.getFloat("friction");
        maxSpeed = tag.getFloat("maxSpeed");
        
        enableGravity = tag.getBoolean("gravity");
        enableCollisions = tag.getBoolean("collisions");
    }
    
    /**
     * Get current kinetic energy (for debugging/effects)
     */
    public double getKineticEnergy() {
        return 0.5 * mass * velocity.lengthSqr();
    }
    
    /**
     * Stop all movement
     */
    public void stop() {
        velocity = Vec3.ZERO;
        angularVelocity = Vec3.ZERO;
        forceAccumulator = Vec3.ZERO;
        torqueAccumulator = Vec3.ZERO;
    }
}