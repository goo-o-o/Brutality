package net.goo.armament.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AbstractWorldAlignedTrailParticle extends AbstractTrailParticle{
    private final float pitch; // Rotation around X-axis (vertical tilt)
    private final float yaw;   // Rotation around Y-axis (horizontal turn)
    private final float roll;  // Rotation around Z-axis (twist)
    private final int EntityId;
    private final float width;
    private final float height;

    public AbstractWorldAlignedTrailParticle(ClientLevel world, double x, double y, double z, float r, float g, float b,
                          float width, float height, int EntityId, float pitch, float yaw, float roll) {
        super(world, x, y, z, 0, 0, 0, r, g, b);
        this.EntityId = EntityId;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.width = width;
        this.height = height;
    }

    public Vec3 getPlaneNormal() {
        Vec3 normal = new Vec3(0, 1, 0); // Default: vertical (Y-axis)
        // Apply rotations (pitch, yaw, roll)
        normal = rotateAroundX(normal, pitch);
        normal = rotateAroundY(normal, yaw);
        normal = rotateAroundZ(normal, roll);
        return normal;
    }

    private Vec3 rotateAroundX(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x,
                vec.y * cos - vec.z * sin,
                vec.y * sin + vec.z * cos
        );
    }

    private Vec3 rotateAroundY(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x * cos + vec.z * sin,
                vec.y,
                -vec.x * sin + vec.z * cos
        );
    }

    private Vec3 rotateAroundZ(Vec3 vec, float angle) {
        float cos = Mth.cos(angle);
        float sin = Mth.sin(angle);
        return new Vec3(
                vec.x * cos - vec.y * sin,
                vec.x * sin + vec.y * cos,
                vec.z
        );
    }

    @Override
    public float getTrailHeight() {
        return 0;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return null;
    }
}
