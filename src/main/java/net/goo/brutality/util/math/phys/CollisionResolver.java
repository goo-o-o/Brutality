package net.goo.brutality.util.math.phys;

import net.goo.brutality.util.math.phys.hitboxes.BaseBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision detection and response for physics-enabled entities.
 * Uses continuous collision detection (CCD) to prevent tunneling at high speeds.
 */
public class CollisionResolver {
    
    private static final int MAX_COLLISION_ITERATIONS = 4;
    private static final double MIN_MOVEMENT_THRESHOLD = 0.001;
    private static final double SKIN_WIDTH = 0.001; // Small offset to prevent sinking into surfaces
    
    /**
     * Result of a collision detection query
     */
    public static class CollisionResult {
        public boolean hasCollision = false;
        public Vec3 normal = Vec3.ZERO;
        public Vec3 contactPoint = Vec3.ZERO;
        public double penetrationDepth = 0;
        public BlockPos blockPos = null;
        public BlockState blockState = null;
        
        public void reset() {
            hasCollision = false;
            normal = Vec3.ZERO;
            contactPoint = Vec3.ZERO;
            penetrationDepth = 0;
            blockPos = null;
            blockState = null;
        }
    }
    
    /**
     * Sweep test: move the bounding box along a path and detect first collision
     * Uses continuous collision detection to prevent tunneling
     */
    public static CollisionResult sweepTest(Level level, BaseBoundingBox boundingBox, Vec3 movement) {
        CollisionResult result = new CollisionResult();
        
        if (movement.lengthSqr() < MIN_MOVEMENT_THRESHOLD * MIN_MOVEMENT_THRESHOLD) {
            return result;
        }
        
        // Get potential collision blocks using expanded AABB
        AABB sweepAABB = boundingBox.getAABB().expandTowards(movement).inflate(0.5);
        List<BlockCollision> blockCollisions = getBlockCollisions(level, sweepAABB);
        
        if (blockCollisions.isEmpty()) {
            return result;
        }
        
        // Perform swept collision detection
        double closestTime = 1.0;
        Vec3 closestNormal = null;
        Vec3 closestContact = null;
        BlockPos closestBlock = null;
        BlockState closestState = null;
        
        for (BlockCollision blockCol : blockCollisions) {
            // Perform AABB sweep test (simplified GJK for AABBs)
            SweepResult sweep = sweepAABBvsAABB(boundingBox.getAABB(), blockCol.aabb, movement);
            
            if (sweep.hit && sweep.time < closestTime && sweep.time >= 0) {
                closestTime = sweep.time;
                closestNormal = sweep.normal;
                closestContact = sweep.contactPoint;
                closestBlock = blockCol.pos;
                closestState = blockCol.state;
            }
        }
        
        if (closestNormal != null) {
            result.hasCollision = true;
            result.normal = closestNormal;
            result.contactPoint = closestContact;
            result.penetrationDepth = movement.length() * (1.0 - closestTime);
            result.blockPos = closestBlock;
            result.blockState = closestState;
        }
        
        return result;
    }
    
    /**
     * Resolve collisions and compute final movement with sliding
     */
    public static Vec3 resolveCollisions(Level level, BaseBoundingBox boundingBox, Vec3 movement, PhysicsComponent physics) {
        Vec3 remainingMovement = movement;
        Vec3 finalPosition = Vec3.ZERO;
        
        for (int iteration = 0; iteration < MAX_COLLISION_ITERATIONS; iteration++) {
            if (remainingMovement.lengthSqr() < MIN_MOVEMENT_THRESHOLD * MIN_MOVEMENT_THRESHOLD) {
                break;
            }
            
            CollisionResult collision = sweepTest(level, boundingBox, remainingMovement);
            
            if (!collision.hasCollision) {
                // No collision, apply full movement
                finalPosition = finalPosition.add(remainingMovement);
                break;
            }
            
            // Move to collision point with skin width
            double safeTime = Math.max(0, collision.penetrationDepth / remainingMovement.length() - SKIN_WIDTH);
            Vec3 safeMovement = remainingMovement.scale(safeTime);
            finalPosition = finalPosition.add(safeMovement);
            
            // Update bounding box position for next iteration
            boundingBox.setCenter(boundingBox.getCenter().add(safeMovement));
            
            // Apply collision response to physics
            if (physics != null) {
                Vec3 tangent = getTangent(collision.normal, remainingMovement);
                physics.handleCollision(collision.normal, remainingMovement, tangent);
            }
            
            // Slide along surface
            remainingMovement = slideAlongSurface(remainingMovement, collision.normal);
        }
        
        return finalPosition;
    }
    
    /**
     * Simplified AABB vs AABB sweep test
     */
    private static SweepResult sweepAABBvsAABB(AABB moving, AABB stationary, Vec3 velocity) {
        SweepResult result = new SweepResult();
        
        // Expand stationary AABB by moving AABB's size (Minkowski difference)
        Vec3 movingSize = new Vec3(
            (moving.maxX - moving.minX) * 0.5,
            (moving.maxY - moving.minY) * 0.5,
            (moving.maxZ - moving.minZ) * 0.5
        );
        
        AABB expandedStatic = new AABB(
            stationary.minX - movingSize.x,
            stationary.minY - movingSize.y,
            stationary.minZ - movingSize.z,
            stationary.maxX + movingSize.x,
            stationary.maxY + movingSize.y,
            stationary.maxZ + movingSize.z
        );
        
        Vec3 movingCenter = moving.getCenter();
        
        // Ray vs AABB intersection
        double[] tNear = {Double.NEGATIVE_INFINITY};
        double[] tFar = {Double.POSITIVE_INFINITY};
        Vec3 normal = Vec3.ZERO;
        
        // X axis
        if (Math.abs(velocity.x) > 1e-6) {
            double t1 = (expandedStatic.minX - movingCenter.x) / velocity.x;
            double t2 = (expandedStatic.maxX - movingCenter.x) / velocity.x;
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }
            if (t1 > tNear[0]) {
                tNear[0] = t1;
                normal = velocity.x > 0 ? new Vec3(-1, 0, 0) : new Vec3(1, 0, 0);
            }
            tFar[0] = Math.min(tFar[0], t2);
        } else if (movingCenter.x < expandedStatic.minX || movingCenter.x > expandedStatic.maxX) {
            return result; // No intersection
        }
        
        // Y axis
        if (Math.abs(velocity.y) > 1e-6) {
            double t1 = (expandedStatic.minY - movingCenter.y) / velocity.y;
            double t2 = (expandedStatic.maxY - movingCenter.y) / velocity.y;
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }
            if (t1 > tNear[0]) {
                tNear[0] = t1;
                normal = velocity.y > 0 ? new Vec3(0, -1, 0) : new Vec3(0, 1, 0);
            }
            tFar[0] = Math.min(tFar[0], t2);
        } else if (movingCenter.y < expandedStatic.minY || movingCenter.y > expandedStatic.maxY) {
            return result;
        }
        
        // Z axis
        if (Math.abs(velocity.z) > 1e-6) {
            double t1 = (expandedStatic.minZ - movingCenter.z) / velocity.z;
            double t2 = (expandedStatic.maxZ - movingCenter.z) / velocity.z;
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }
            if (t1 > tNear[0]) {
                tNear[0] = t1;
                normal = velocity.z > 0 ? new Vec3(0, 0, -1) : new Vec3(0, 0, 1);
            }
            tFar[0] = Math.min(tFar[0], t2);
        } else if (movingCenter.z < expandedStatic.minZ || movingCenter.z > expandedStatic.maxZ) {
            return result;
        }
        
        // Check for valid intersection
        if (tNear[0] > tFar[0] || tFar[0] < 0) {
            return result;
        }
        
        if (tNear[0] >= 0 && tNear[0] <= 1) {
            result.hit = true;
            result.time = tNear[0];
            result.normal = normal;
            result.contactPoint = movingCenter.add(velocity.scale(tNear[0]));
        }
        
        return result;
    }
    
    /**
     * Get tangent direction for sliding along a surface
     */
    private static Vec3 getTangent(Vec3 normal, Vec3 velocity) {
        Vec3 tangent = velocity.subtract(normal.scale(velocity.dot(normal)));
        double length = tangent.length();
        return length > 1e-6 ? tangent.scale(1.0 / length) : Vec3.ZERO;
    }
    
    /**
     * Project velocity onto surface plane (for sliding)
     */
    private static Vec3 slideAlongSurface(Vec3 velocity, Vec3 normal) {
        // Remove component of velocity in normal direction
        return velocity.subtract(normal.scale(velocity.dot(normal)));
    }
    
    /**
     * Get all block collisions in the given AABB
     */
    private static List<BlockCollision> getBlockCollisions(Level level, AABB searchAABB) {
        List<BlockCollision> collisions = new ArrayList<>();
        
        int minX = (int) Math.floor(searchAABB.minX);
        int minY = (int) Math.floor(searchAABB.minY);
        int minZ = (int) Math.floor(searchAABB.minZ);
        int maxX = (int) Math.floor(searchAABB.maxX);
        int maxY = (int) Math.floor(searchAABB.maxY);
        int maxZ = (int) Math.floor(searchAABB.maxZ);
        
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    
                    if (!state.isAir()) {
                        VoxelShape shape = state.getCollisionShape(level, pos);
                        if (!shape.isEmpty()) {
                            for (AABB aabb : shape.toAabbs()) {
                                AABB offsetAABB = aabb.move(x, y, z);
                                collisions.add(new BlockCollision(pos.immutable(), state, offsetAABB));
                            }
                        }
                    }
                }
            }
        }
        
        return collisions;
    }
    
    /**
     * Check if entity is on ground
     */
    public static boolean isOnGround(Level level, BaseBoundingBox boundingBox) {
        Vec3 downMovement = new Vec3(0, -0.001, 0);
        CollisionResult result = sweepTest(level, boundingBox, downMovement);
        return result.hasCollision && Math.abs(result.normal.y) > 0.9;
    }
    
    // Helper classes

    private record BlockCollision(BlockPos pos, BlockState state, AABB aabb) {
    }
    
    private static class SweepResult {
        boolean hit = false;
        double time = 1.0;
        Vec3 normal = Vec3.ZERO;
        Vec3 contactPoint = Vec3.ZERO;
    }
}