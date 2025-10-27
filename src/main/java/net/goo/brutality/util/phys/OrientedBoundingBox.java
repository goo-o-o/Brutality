package net.goo.brutality.util.phys;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.List;
import java.util.stream.Collectors;

public class OrientedBoundingBox {
    public Vec3 center;
    public Vec3 extent;
    public Vec3 axisX;
    public Vec3 axisY;
    public Vec3 axisZ;
    public Vec3 scaledAxisX;
    public Vec3 scaledAxisY;
    public Vec3 scaledAxisZ;
    public Matrix3f rotation;
    public Vec3 vertex1;
    public Vec3 vertex2;
    public Vec3 vertex3;
    public Vec3 vertex4;
    public Vec3 vertex5;
    public Vec3 vertex6;
    public Vec3 vertex7;
    public Vec3 vertex8;
    public Vec3[] vertices;

    public OrientedBoundingBox(Vec3 center, double width, double height, double depth, float yaw, float pitch) {
        this.rotation = new Matrix3f();
        this.center = center;
        this.extent = new Vec3(width / (double)2.0F, height / (double)2.0F, depth / (double)2.0F);
        this.axisZ = Vec3.directionFromRotation(yaw, pitch).normalize();
        this.axisY = Vec3.directionFromRotation(yaw + 90.0F, pitch).reverse().normalize();
        this.axisX = this.axisZ.cross(this.axisY);
    }

    public static Vec3 getInitialTracingPoint(Player player) {
        double shoulderHeight = player.getBbHeight() * 0.15 * player.getScale();
        return player.getEyePosition().subtract(0, shoulderHeight, 0);
    }

    public OrientedBoundingBox(Vec3 center, Vec3 size, float yaw, float pitch) {
        this(center, size.x, size.y, size.z, yaw, pitch);
    }

    public OrientedBoundingBox(AABB box) {
        this.rotation = new Matrix3f();
        this.center = new Vec3((box.maxX + box.minX) / (double)2.0F, (box.maxY + box.minY) / (double)2.0F, (box.maxZ + box.minZ) / (double)2.0F);
        this.extent = new Vec3(Math.abs(box.maxX - box.minX) / (double)2.0F, Math.abs(box.maxY - box.minY) / (double)2.0F, Math.abs(box.maxZ - box.minZ) / (double)2.0F);
        this.axisX = new Vec3((double)1.0F, (double)0.0F, (double)0.0F);
        this.axisY = new Vec3((double)0.0F, (double)1.0F, (double)0.0F);
        this.axisZ = new Vec3((double)0.0F, (double)0.0F, (double)1.0F);
    }

    public OrientedBoundingBox(OrientedBoundingBox obb) {
        this.rotation = new Matrix3f();
        this.center = obb.center;
        this.extent = obb.extent;
        this.axisX = obb.axisX;
        this.axisY = obb.axisY;
        this.axisZ = obb.axisZ;
    }

    public List<? extends Entity> filterEntities(List<? extends Entity> entities) {
        return entities.stream()
                .filter(entity -> intersects(entity.getBoundingBox())
                        || contains(entity.getPosition(0).add(0, entity.getBbHeight() / 2F, 0))
                )
                .collect(Collectors.toList());
    }

    public OrientedBoundingBox copy() {
        return new OrientedBoundingBox(this);
    }

    public OrientedBoundingBox offsetAlongAxisX(double offset) {
        this.center = this.center.add(this.axisX.scale(offset));
        return this;
    }

    public OrientedBoundingBox offsetAlongAxisY(double offset) {
        this.center = this.center.add(this.axisY.scale(offset));
        return this;
    }

    public OrientedBoundingBox offsetAlongAxisZ(double offset) {
        this.center = this.center.add(this.axisZ.scale(offset));
        return this;
    }

    public OrientedBoundingBox offset(Vec3 offset) {
        this.center = this.center.add(offset);
        return this;
    }

    public OrientedBoundingBox scale(double scale) {
        this.extent = this.extent.scale(scale);
        return this;
    }

    public OrientedBoundingBox updateVertex() {
        this.rotation.set(0, 0, (float)this.axisX.x);
        this.rotation.set(0, 1, (float)this.axisX.y);
        this.rotation.set(0, 2, (float)this.axisX.z);
        this.rotation.set(1, 0, (float)this.axisY.x);
        this.rotation.set(1, 1, (float)this.axisY.y);
        this.rotation.set(1, 2, (float)this.axisY.z);
        this.rotation.set(2, 0, (float)this.axisZ.x);
        this.rotation.set(2, 1, (float)this.axisZ.y);
        this.rotation.set(2, 2, (float)this.axisZ.z);
        this.scaledAxisX = this.axisX.scale(this.extent.x);
        this.scaledAxisY = this.axisY.scale(this.extent.y);
        this.scaledAxisZ = this.axisZ.scale(this.extent.z);
        this.vertex1 = this.center.subtract(this.scaledAxisZ).subtract(this.scaledAxisX).subtract(this.scaledAxisY);
        this.vertex2 = this.center.subtract(this.scaledAxisZ).add(this.scaledAxisX).subtract(this.scaledAxisY);
        this.vertex3 = this.center.subtract(this.scaledAxisZ).add(this.scaledAxisX).add(this.scaledAxisY);
        this.vertex4 = this.center.subtract(this.scaledAxisZ).subtract(this.scaledAxisX).add(this.scaledAxisY);
        this.vertex5 = this.center.add(this.scaledAxisZ).subtract(this.scaledAxisX).subtract(this.scaledAxisY);
        this.vertex6 = this.center.add(this.scaledAxisZ).add(this.scaledAxisX).subtract(this.scaledAxisY);
        this.vertex7 = this.center.add(this.scaledAxisZ).add(this.scaledAxisX).add(this.scaledAxisY);
        this.vertex8 = this.center.add(this.scaledAxisZ).subtract(this.scaledAxisX).add(this.scaledAxisY);
        this.vertices = new Vec3[]{this.vertex1, this.vertex2, this.vertex3, this.vertex4, this.vertex5, this.vertex6, this.vertex7, this.vertex8};
        return this;
    }

    public boolean contains(Vec3 point) {
        Vector3f distance = point.subtract(this.center).toVector3f();
        distance.mulTranspose(this.rotation);
        return (double)Math.abs(distance.x()) < this.extent.x && (double)Math.abs(distance.y()) < this.extent.y && (double)Math.abs(distance.z()) < this.extent.z;
    }

    public boolean intersects(AABB boundingBox) {
        OrientedBoundingBox otherOBB = (new OrientedBoundingBox(boundingBox)).updateVertex();
        return Intersects(this, otherOBB);
    }

    public boolean intersects(OrientedBoundingBox otherOBB) {
        return Intersects(this, otherOBB);
    }

    public static boolean Intersects(OrientedBoundingBox a, OrientedBoundingBox b) {
        if (Separated(a.vertices, b.vertices, a.scaledAxisX)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisY)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisZ)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, b.scaledAxisX)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, b.scaledAxisY)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, b.scaledAxisZ)) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisX))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisY))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisZ))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisX))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisY))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisZ))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisX))) {
            return false;
        } else if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisY))) {
            return false;
        } else {
            return !Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisZ));
        }
    }

    private static boolean Separated(Vec3[] vertsA, Vec3[] vertsB, Vec3 axis) {
        if (axis.equals(Vec3.ZERO)) {
            return false;
        } else {
            double aMin = Double.POSITIVE_INFINITY;
            double aMax = Double.NEGATIVE_INFINITY;
            double bMin = Double.POSITIVE_INFINITY;
            double bMax = Double.NEGATIVE_INFINITY;

            for(int i = 0; i < 8; ++i) {
                double aDist = vertsA[i].dot(axis);
                aMin = Math.min(aDist, aMin);
                aMax = Math.max(aDist, aMax);
                double bDist = vertsB[i].dot(axis);
                bMin = Math.min(bDist, bMin);
                bMax = Math.max(bDist, bMax);
            }

            double longSpan = Math.max(aMax, bMax) - Math.min(aMin, bMin);
            double sumSpan = aMax - aMin + bMax - bMin;
            return longSpan >= sumSpan;
        }
    }
}
