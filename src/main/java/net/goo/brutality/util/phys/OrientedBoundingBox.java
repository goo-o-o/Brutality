package net.goo.brutality.util.phys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public final class OrientedBoundingBox extends Hitbox {
    public Vec3 halfExtents;

    // ────────────────── Constructors ──────────────────
    public OrientedBoundingBox(Vec3 center, Vec3 halfExtents, Matrix3f rotation) {
        super(); // Hitbox already creates identity rotation
        this.center = center;
        this.halfExtents = halfExtents;
        if (rotation != null) this.rotation.set(rotation);
    }

    public OrientedBoundingBox(Vec3 center, Vec3 halfExtents, float pitch, float yaw, float roll) {
        this(center, halfExtents, new Matrix3f().rotationXYZ(
                (float) Math.toRadians(pitch),
                (float) Math.toRadians(yaw),
                (float) Math.toRadians(roll)));
    }

    public OrientedBoundingBox(Vec3 center, float hx, float hy, float hz) {
        this(center, new Vec3(hx, hy, hz), null);
    }

    public static OrientedBoundingBox fromAABB(AABB aabb) {
        return new OrientedBoundingBox(
                aabb.getCenter(),
                new Vec3((aabb.maxX - aabb.minX) * 0.5,
                        (aabb.maxY - aabb.minY) * 0.5,
                        (aabb.maxZ - aabb.minZ) * 0.5),
                null
        );
    }

    @Override
    public boolean intersectsAABB(AABB aabb) {
        OrientedBoundingBox temp = fromAABB(aabb);
        return obbVsObb(this, temp);
    }

    @Override
    public AABB getAABB() {
        float ex = (float) (Math.abs(rotation.m00()) * halfExtents.x +
                Math.abs(rotation.m01()) * halfExtents.y +
                Math.abs(rotation.m02()) * halfExtents.z);

        float ey = (float) (Math.abs(rotation.m10()) * halfExtents.x +
                Math.abs(rotation.m11()) * halfExtents.y +
                Math.abs(rotation.m12()) * halfExtents.z);

        float ez = (float) (Math.abs(rotation.m20()) * halfExtents.x +
                Math.abs(rotation.m21()) * halfExtents.y +
                Math.abs(rotation.m22()) * halfExtents.z);

        Vec3 he = new Vec3(ex, ey, ez);
        return new AABB(center.subtract(he), center.add(he));
    }

    @Override
    public Hitbox copy() {
        return new OrientedBoundingBox(center, halfExtents, new Matrix3f(rotation));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        // Get camera position ONCE
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cam = camera.getPosition();

        // Compute corners in world space
        Vec3 c = center;
        Vec3 hx = getLocalAxis(0).scale(halfExtents.x);
        Vec3 hy = getLocalAxis(1).scale(halfExtents.y);
        Vec3 hz = getLocalAxis(2).scale(halfExtents.z);

        Vec3 v1 = c.subtract(hx).subtract(hy).subtract(hz);
        Vec3 v2 = c.add(hx).subtract(hy).subtract(hz);
        Vec3 v3 = c.add(hx).add(hy).subtract(hz);
        Vec3 v4 = c.subtract(hx).add(hy).subtract(hz);
        Vec3 v5 = c.subtract(hx).subtract(hy).add(hz);
        Vec3 v6 = c.add(hx).subtract(hy).add(hz);
        Vec3 v7 = c.add(hx).add(hy).add(hz);
        Vec3 v8 = c.subtract(hx).add(hy).add(hz);

        Vec3[][] edges = {
                {v1, v2}, {v2, v3}, {v3, v4}, {v4, v1},
                {v5, v6}, {v6, v7}, {v7, v8}, {v8, v5},
                {v1, v5}, {v2, v6}, {v3, v7}, {v4, v8}
        };

        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        float r = 0.0f, g = 1.0f, b = 0.0f, a = 0.8f;

        for (Vec3[] edge : edges) {
            Vec3 first = edge[0].subtract(cam);  // <-- THIS LINE FIXES EVERYTHING
            Vec3 second = edge[1].subtract(cam);  // <-- THIS LINE FIXES EVERYTHING

            consumer.vertex(matrix, (float) first.x, (float) first.y, (float) first.z)
                    .color(r, g, b, a).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
            consumer.vertex(matrix, (float) second.x, (float) second.y, (float) second.z)
                    .color(r, g, b, a).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normal, 0, 1, 0).endVertex();
        }
    }


    public Vec3 getLocalAxis(int i) {
        return new Vec3(
                rotation.get(i, 0),
                rotation.get(i, 1),
                rotation.get(i, 2)
        );
    }


    @Override
    public Hitbox inflate(float f) {
        halfExtents = halfExtents.scale(f);
        return this;
    }

    private record Proj(float min, float max) {
        boolean diverges(Proj o) {
            return max < o.min || o.max < min;
        }
    }

    private Vector3f rotationX() {
        return new Vector3f(rotation.m00(), rotation.m01(), rotation.m02());
    }

    private Vector3f rotationY() {
        return new Vector3f(rotation.m10(), rotation.m11(), rotation.m12());
    }

    private Vector3f rotationZ() {
        return new Vector3f(rotation.m20(), rotation.m21(), rotation.m22());
    }

    private Proj project(Vector3f axis) {
        Vector3f c = center.toVector3f();
        float r = (float) (halfExtents.x * Math.abs(axis.dot(rotationX())) +
                halfExtents.y * Math.abs(axis.dot(rotationY())) +
                halfExtents.z * Math.abs(axis.dot(rotationZ())));
        float p = axis.dot(c);
        return new Proj(p - r, p + r);
    }

    private Vector3f centerVector3f() {
        return new Vector3f((float) center.x, (float) center.y, (float) center.z);
    }

    private static boolean obbVsObb(OrientedBoundingBox a, OrientedBoundingBox b) {
        Vector3f[] axes = new Vector3f[15];
        int i = 0;

        // A axes
        axes[i++] = a.rotationX();
        axes[i++] = a.rotationY();
        axes[i++] = a.rotationZ();
        // B axes
        axes[i++] = b.rotationX();
        axes[i++] = b.rotationY();
        axes[i++] = b.rotationZ();

        // Cross products
        for (int j = 0; j < 3; j++) {
            for (int k = 3; k < 6; k++) {
                axes[i++] = new Vector3f().cross(axes[j], axes[k]);
            }
        }

        for (Vector3f axis : axes) {
            if (axis.lengthSquared() < 1e-6f) continue;
            axis.normalize();
            if (a.project(axis).diverges(b.project(axis))) return false;
        }
        return true;
    }

    @Override
    public <T extends Entity> List<T> findEntitiesHit(Player player, Class<T> clazz, boolean needsLos, Predicate<? super T> filter) {
        List<T> candidates = player.level().getEntitiesOfClass(clazz, getAABB(), e -> e.isAlive() && e.isPickable() && e != player && e != player.getVehicle());

        return filter(player, candidates, needsLos);
    }


}
