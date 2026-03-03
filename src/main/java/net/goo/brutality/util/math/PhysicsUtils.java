package net.goo.brutality.util.math;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PhysicsUtils {

    public static Vec3i toVector3i(Vec3 vec3) {
        return new Vec3i((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }

    public static Vector3f toVector3f(Vec3 vec3) {
        return new Vector3f((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }
    public static Vec3 fromVector3f(Vector3f vec3) {
        return new Vec3((int) vec3.x, (int) vec3.y, (int) vec3.z);
    }


}
