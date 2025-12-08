package net.goo.brutality.util.phys;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class OrientedBoundingBox {
    public Vec3 center;
    public Vec3 halfExtents;
    public final Matrix3f rotation;

    private record Projection(float min, float max) {
        boolean overlaps(Projection o) {
            return !(max < o.min || o.max < min);
        }
    }

    private Projection project(Vector3f axis) {
        Vector3f c = new Vector3f((float) center.x, (float) center.y, (float) center.z);

        float r = (float) (halfExtents.x * Math.abs(axis.dot(new Vector3f(rotation.m00(), rotation.m01(), rotation.m02()))) +
                halfExtents.y * Math.abs(axis.dot(new Vector3f(rotation.m10(), rotation.m11(), rotation.m12()))) +
                halfExtents.z * Math.abs(axis.dot(new Vector3f(rotation.m20(), rotation.m21(), rotation.m22()))));

        float centerProj = axis.dot(c);
        return new Projection(centerProj - r, centerProj + r);
    }

    public OrientedBoundingBox(Vec3 center, Vec3 halfExtents, Matrix3f rotation) {
        this.center = center;
        this.halfExtents = halfExtents;
        this.rotation = rotation;
    }

    public OrientedBoundingBox(Vec3 center, Vec3 halfExtents, float pitch, float yaw, float roll) {
        this(center, halfExtents, new Matrix3f().rotationXYZ(
                (float) Math.toRadians(pitch),
                (float) Math.toRadians(yaw),
                (float) Math.toRadians(roll)));
    }

    public OrientedBoundingBox(Vec3 center, float halfX, float halfY, float halfZ, float pitch, float yaw, float roll) {
        this(center, new Vec3(halfX, halfY, halfZ), new Matrix3f().rotationXYZ(
                (float) Math.toRadians(pitch),
                (float) Math.toRadians(yaw),
                (float) Math.toRadians(roll)));
    }

    public static OrientedBoundingBox create(Vec3 center, Vec3 fullExtents, float pitch, float yaw, float roll) {
        return new OrientedBoundingBox(center, fullExtents.scale(0.5F), pitch, yaw, roll);
    }

    public static OrientedBoundingBox create(Vec3 center, float fullX, float fullY, float fullZ, float pitch, float yaw, float roll) {
        return new OrientedBoundingBox(center, new Vec3(fullX, fullY, fullZ).scale(0.5F), pitch, yaw, roll);
    }

    public static OrientedBoundingBox create(AABB aabb) {
        return new OrientedBoundingBox(
                aabb.getCenter(),
                new Vec3(
                        (aabb.maxX - aabb.minX) / 2,
                        (aabb.maxY - aabb.minY) / 2,
                        (aabb.maxZ - aabb.minZ) / 2
                ),
                new Matrix3f()
        );
    }

    public Vec3 getLocalAxis(int i) {
        return new Vec3(
                rotation.get(i, 0),
                rotation.get(i, 1),
                rotation.get(i, 2)
        );
    }

    public OrientedBoundingBox setRotation(Matrix3f rotation) {
        rotation.set(rotation);
        return this;
    }

    public OrientedBoundingBox setRotationEuler(float pitch, float yaw, float roll) {
        rotation.identity()
                .rotateY((float) Math.toRadians(Mth.wrapDegrees(-yaw)))      // Minecraft yaw is CCW, JOML is CW → negate
                .rotateX((float) Math.toRadians(Mth.wrapDegrees(pitch)))
                .rotateZ((float) Math.toRadians(Mth.wrapDegrees(roll)));
        return this;
    }

    public OrientedBoundingBox inflate(float amount) {
        return scale(amount, amount, amount);
    }

    public OrientedBoundingBox scale(float x, float y, float z) {
        halfExtents.multiply(x, y, z);
        return this;
    }

    public OrientedBoundingBox extend(float x, float y, float z) {
        halfExtents = halfExtents.add(x, y, z);
        return this;
    }

    public OrientedBoundingBox rotateLocalX(float deg) {
        rotation.rotateX((float) Math.toRadians(deg));
        return this;
    }

    public OrientedBoundingBox rotateLocalY(float deg) {
        rotation.rotateY((float) Math.toRadians(deg));
        return this;
    }

    public OrientedBoundingBox rotateLocalZ(float deg) {
        rotation.rotateZ((float) Math.toRadians(deg));
        return this;
    }

    public OrientedBoundingBox rotateEuler(float yawDeg, float pitchDeg, float rollDeg) {
        Matrix3f extra = new Matrix3f()
                .rotateY((float) Math.toRadians(-yawDeg))
                .rotateX((float) Math.toRadians(pitchDeg)) // +pitch = look down
                .rotateZ((float) Math.toRadians(Mth.wrapDegrees(rollDeg)));

        // newRotation = extra * oldRotation
        rotation.mul(extra);
        return this;
    }

    public OrientedBoundingBox lookAt(Vec3 targetDir) {
        Vector3f dir = new Vector3f((float) targetDir.x, (float) targetDir.y, (float) targetDir.z).normalize();
        rotation.set(new Quaternionf().lookAlong(dir, new Vector3f(0, 1, 0))).get(rotation);
        return this;
    }

    public OrientedBoundingBox translateWorld(Vec3 translation) {
        center = center.add(translation);
        return this;
    }

    public OrientedBoundingBox translateLocal(Vec3 localOffset) {
        Vector3f temp = localOffset.toVector3f();
        rotation.transform(temp);  // local → world
        center = center.add(temp.x, temp.y, temp.z);
        return this;
    }

    public boolean intersects(OrientedBoundingBox other) {
        Vector3f[] axes = getSeparatingAxes(other);

        for (Vector3f axis : axes) {
            if (axis.lengthSquared() < 1e-6f) continue;

            axis.normalize();
            Projection p1 = project(axis);
            Projection p2 = other.project(axis);

            if (!p1.overlaps(p2)) return false;
        }
        return true;
    }

    public static boolean intersects(OrientedBoundingBox first, OrientedBoundingBox other) {
        Vector3f[] axes = first.getSeparatingAxes(other);

        for (Vector3f axis : axes) {
            if (axis.lengthSquared() < 1e-6f) continue;

            axis.normalize();
            Projection p1 = first.project(axis);
            Projection p2 = other.project(axis);

            if (!p1.overlaps(p2)) return false;
        }
        return true;
    }

    // Replace getSeparatingAxes with this (only change: read columns, not rows)
    private Vector3f[] getSeparatingAxes(OrientedBoundingBox b) {
        Vector3f[] axes = new Vector3f[15];
        int i = 0;

        // A's local axes (now correctly reading COLUMNS)
        axes[i++] = new Vector3f(rotation.m00(), rotation.m01(), rotation.m02()); // X axis
        axes[i++] = new Vector3f(rotation.m10(), rotation.m11(), rotation.m12()); // Y axis
        axes[i++] = new Vector3f(rotation.m20(), rotation.m21(), rotation.m22()); // Z axis

        // B's local axes
        axes[i++] = new Vector3f(b.rotation.m00(), b.rotation.m01(), b.rotation.m02());
        axes[i++] = new Vector3f(b.rotation.m10(), b.rotation.m11(), b.rotation.m12());
        axes[i++] = new Vector3f(b.rotation.m20(), b.rotation.m21(), b.rotation.m22());

        // Cross products
        for (int a = 0; a < 3; a++) {
            Vector3f axisA = axes[a];
            for (int bb = 3; bb < 6; bb++) {
                axes[i++] = new Vector3f().cross(axisA, axes[bb]);
            }
        }
        return axes;
    }


    public static <T extends Entity> List<T> filterMultiple(
            Player player,
            List<OrientedBoundingBox> hitboxes,
            List<T> entities
    ) {
        return entities.stream()
                .filter(entity -> {
                    AABB entityBox = entity.getBoundingBox();
                    OrientedBoundingBox entityOBB = OrientedBoundingBox.create(entityBox);
                    return hitboxes.stream().anyMatch(obb -> obb.intersects(entityOBB)) &&
                            player.hasLineOfSight(entity);
                })
                .toList();
    }

    public <T extends Entity> List<T> filter(Player player, List<T> entities) {
        return entities.stream()
                .filter(entity ->
                        this.intersects(OrientedBoundingBox.create(entity.getBoundingBox())) &&
                                player.hasLineOfSight(entity)
                )
                .toList();
    }

    public AABB getAABB() {

        float eX = (float) (Math.abs(rotation.m00()) * halfExtents.x +
                Mth.abs(rotation.m01()) * halfExtents.y +
                Math.abs(rotation.m02()) * halfExtents.z);

        float eY = (float) (Math.abs(rotation.m10()) * halfExtents.x +
                Mth.abs(rotation.m11()) * halfExtents.y +
                Math.abs(rotation.m12()) * halfExtents.z);

        float eZ = (float) (Math.abs(rotation.m20()) * halfExtents.x +
                Mth.abs(rotation.m21()) * halfExtents.y +
                Math.abs(rotation.m22()) * halfExtents.z);

        Vec3 newHalf = new Vec3(eX, eY, eZ);

        return new AABB(center.subtract(newHalf), center.add(newHalf));
    }

    public OrientedBoundingBox copy() {
        return new OrientedBoundingBox(center, halfExtents, new Matrix3f(rotation));
    }

    public static Vec3 getShoulderPosition(Player player) {
        double shoulderHeight = (double) player.getBbHeight() * 0.15 * (double) player.getScale();
        return player.getEyePosition().add(0F, -shoulderHeight, 0F);
    }


    public static <T extends Entity> TargetResult<T> findAttackTargetResult(
            Player player,
            Class<T> clazz,
            List<OrientedBoundingBox> hitboxes,
            Vec3 localOffset,
            float pitch, float yaw, float roll,
            boolean clipToBlocks
    ) {
        if (hitboxes.isEmpty()) return null;

        Vec3 origin = getShoulderPosition(player);
        Vec3 look = player.getLookAngle();
        float halfWidth = player.getBbWidth() * 0.5F;
        // Transform all hitboxes to world space once
        List<OrientedBoundingBox> worldHitboxes = hitboxes.stream()
                .map(obb -> obb.copy()
                        .translateWorld(origin)
                        .rotateEuler(yaw, pitch, roll)
                        .translateLocal(new Vec3(0, 0, obb.halfExtents.z))
                        .translateLocal(localOffset.add(0, 0, halfWidth))
                )
                .toList();

        // Fast culling AABB covering all hitboxes
        AABB cull = null;
        for (var obb : worldHitboxes) {
            AABB a = obb.getAABB();
            cull = cull == null ? a : cull.minmax(a);
        }

        assert cull != null;
        cull = cull.inflate(2.0); // safety

        List<T> candidates = player.level().getEntitiesOfClass(clazz, cull, e ->
                e.isAlive() && e.isPickable() && e != player && e != player.getVehicle());

        // Final SAT test
        List<T> hits = candidates.stream()
                .filter(e -> worldHitboxes.stream().anyMatch(obb -> obb.intersects(create(e.getBoundingBox())))
                        && player.hasLineOfSight(e)).toList();

        Vec3 rayEnd = origin.add(look.scale(200));
        float maxReach = 200.0F;
        if (clipToBlocks) {
            BlockHitResult blockHit = player.level().clip(new ClipContext(
                    origin, rayEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (blockHit.getType() == HitResult.Type.BLOCK) {
                maxReach = (float) blockHit.getLocation().distanceTo(origin);
                rayEnd = blockHit.getLocation();
            }

            OrientedBoundingBox main = worldHitboxes.get(0);

            // Apply to ALL world hitboxes (important for multi-box weapons like Schism)
            double currentLength = main.halfExtents.z * 2.0; // full forward length
            if (maxReach < currentLength) {
                // Shrink forward extent and pull box back so end touches wall
                double newHalfZ = (maxReach - localOffset.z - halfWidth) * 0.5;
                newHalfZ = Math.max(newHalfZ, 0);
                main.halfExtents = new Vec3(main.halfExtents.x, main.halfExtents.y, newHalfZ);
                // Move box backward by the amount we shrunk
                Vec3 forward = main.getLocalAxis(2); // forward axis
                main.center = main.center.subtract(forward.scale(currentLength * 0.5 - newHalfZ));
            }
        }

        return new TargetResult<>(hits, worldHitboxes, maxReach, rayEnd);
    }


    public static <T extends Entity> TargetResult<T> findAttackTargetResult(Player p, Class<T> c, List<OrientedBoundingBox> h, Vec3 off, boolean clip) {
        return findAttackTargetResult(p, c, h, off, p.getViewXRot(0), p.getViewYRot(0), 0, clip);
    }

    public static <T extends Entity> TargetResult<T> findAttackTargetResult(Player p, Class<T> c, OrientedBoundingBox h, Vec3 off, boolean clip) {
        return findAttackTargetResult(p, c, List.of(h), off, p.getViewXRot(0), p.getViewYRot(0), 0, clip);
    }

    public static <T extends Entity> TargetResult<T> findAttackTargetResult(Player p, Class<T> c, OrientedBoundingBox h, Vec3 off, float pitch, float yaw, float roll, boolean clip) {
        return findAttackTargetResult(p, c, List.of(h), off, pitch, yaw, roll, clip);
    }

    public static class TargetResult<T extends Entity> {
        public List<T> entities;
        public List<OrientedBoundingBox> hitboxes;
        public float distance;
        public Vec3 endPos;

        public TargetResult(List<T> entities, OrientedBoundingBox hitbox, float distance, Vec3 endPos) {
            this.entities = entities;
            this.hitboxes = List.of(hitbox);
            this.distance = distance;
            this.endPos = endPos;
        }

        public TargetResult(List<T> entities, List<OrientedBoundingBox> hitboxes, float distance, Vec3 endPos) {
            this.entities = entities;
            this.hitboxes = hitboxes;
            this.distance = distance;
            this.endPos = endPos;
        }
    }

}
