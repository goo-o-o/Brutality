package net.goo.brutality.util.phys;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CircleAABB extends AABB {
    private final Vec3 center;
    private final float radius;
    private final float innerRadius; // For ring shape, 0 for full circle

    public CircleAABB(Vec3 center, float radius, float innerRadius) {
        super(center.add(-radius, 0, -radius), center.add(radius, 0, radius));
        this.center = center;
        this.radius = radius;
        this.innerRadius = innerRadius;
    }

    public CircleAABB(Vec3 center, float radius) {
        this(center, radius, 0.0F); // Full circle, no inner radius
    }

    @Override
    public boolean contains(@NotNull Vec3 point) {
        float distanceSqr = (float) center.distanceToSqr(point);
        float outerRadiusSqr = radius * radius;
        float innerRadiusSqr = innerRadius * innerRadius;
        return distanceSqr <= outerRadiusSqr && (innerRadius == 0 || distanceSqr >= innerRadiusSqr);
    }

    @Override
    public boolean intersects(AABB pOther) {
        Vec3 closestPoint = new Vec3(
                Math.max(pOther.minX, Math.min(center.x, pOther.maxX)),
                Math.max(pOther.minY, Math.min(center.y, pOther.maxY)),
                Math.max(pOther.minZ, Math.min(center.z, pOther.maxZ))
        );
        return contains(closestPoint);
    }
}