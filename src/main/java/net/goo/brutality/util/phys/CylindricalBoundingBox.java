package net.goo.brutality.util.phys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class CylindricalBoundingBox extends Hitbox {
    public float radius;
    public float innerRadius; // For ring shape, 0 for full circle
    public float height;
    public float halfHeight;

    public CylindricalBoundingBox(float height, float radius, float innerRadius) {
        this.height = height;
        this.halfHeight = height * 0.5F;
        this.radius = radius;
        this.innerRadius = innerRadius;
    }

    @Override
    public AABB getAABB() {
        if (center == null) return new AABB(0, 0, 0, 0, 0, 0);

        float r = Math.max(radius, innerRadius);

        float ex = Math.abs(rotation.m00()) * r + Math.abs(rotation.m02()) * r;
        float ey = Math.abs(rotation.m10()) * halfHeight + Math.abs(rotation.m11()) * halfHeight + Math.abs(rotation.m12()) * halfHeight;
        float ez = Math.abs(rotation.m20()) * r + Math.abs(rotation.m22()) * r;

        Vec3 halfExtent = new Vec3(ex, ey, ez);
        return new AABB(center.subtract(halfExtent), center.add(halfExtent));
    }

    @Override
    public Hitbox copy() {
        return null;
    }

    @Override
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {

    }

    @Override
    public boolean intersectsAABB(AABB aabb) {
        if (center == null) return false;

        // Inverse rotation: transform AABB into cylinder's local space
        Matrix3f invRot = new Matrix3f(rotation).invert();

        // 8 corners of the AABB relative to cylinder center
        Vector3f[] corners = new Vector3f[8];
        int i = 0;
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    corners[i++] = new Vector3f(
                            (float) (x == 0 ? aabb.minX : aabb.maxX) - (float) center.x,
                            (float) (y == 0 ? aabb.minY : aabb.maxY) - (float) center.y,
                            (float) (z == 0 ? aabb.minZ : aabb.maxZ) - (float) center.z
                    );
                }
            }
        }

        // Transform corners to local space
        float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
        float minZ = Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for (Vector3f c : corners) {
            invRot.transform(c);
            minX = Math.min(minX, c.x);
            maxX = Math.max(maxX, c.x);
            minY = Math.min(minY, c.y);
            maxY = Math.max(maxY, c.y);
            minZ = Math.min(minZ, c.z);
            maxZ = Math.max(maxZ, c.z);
        }

        // 1. Height check (Y axis)
        if (maxY < -halfHeight || minY > halfHeight) return false;

        // 2. Closest point in XZ plane to cylinder axis (0,0)
        float closestX = Math.max(minX, Math.min(0f, maxX));
        float closestZ = Math.max(minZ, Math.min(0f, maxZ));
        float distSq = closestX * closestX + closestZ * closestZ;

        // 3. Cylinder / ring check
        if (innerRadius > 0) {
            return distSq <= radius * radius && distSq >= innerRadius * innerRadius;
        }
        return distSq <= radius * radius;
    }

    @Override
    public <T extends Entity> List<T> findEntitiesHit(Player player, Class<T> clazz, boolean needsLos, Predicate<? super T> filter) {
        return List.of();
    }
}