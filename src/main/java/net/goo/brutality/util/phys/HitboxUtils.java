package net.goo.brutality.util.phys;

import net.goo.brutality.entity.base.BrutalityRay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

import java.util.List;

public class HitboxUtils {
    public static Vec3 getShoulderPosition(Player player) {
        double shoulderHeight = (double) player.getBbHeight() * 0.15 * (double) player.getScale();
        return player.getEyePosition().add(0F, -shoulderHeight, 0F);
    }

    public static <T extends Entity> List<T> handleRay(Player player, Class<T> clazz, OrientedBoundingBox hitbox, float zOffset, EntityType<? extends BrutalityRay> rayType, boolean needsLos) {
        Vec3 origin = getShoulderPosition(player);
        hitbox.translateWorld(origin);
        hitbox.rotateLocal(fromAngle(player.getXRot(), player.getYRot(), 0));
        hitbox.translateLocal(new Vec3(0,0, zOffset));

        List<T> entitiesHit = hitbox.findEntitiesHit(player, clazz, needsLos, null);

        Vec3 look = player.getLookAngle();
        float maxRange = (float) (hitbox.halfExtents.z() * 2) + zOffset + player.getBbWidth() * 0.5F;

        BlockHitResult blockHit = player.level().clip(new ClipContext(
                origin,
                origin.add(look.scale(maxRange)),           // far enough
                ClipContext.Block.COLLIDER,                // or OUTLINE if you want visual blocks only
                ClipContext.Fluid.NONE,
                player
        ));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            maxRange = (float) blockHit.getLocation().distanceTo(origin);
        }

        if (player.level() instanceof ServerLevel serverLevel && maxRange > 0) {
            BrutalityRay ray = rayType.create(serverLevel);

            if (ray != null) {
                ray.setOwner(player);
                ray.setPos(getShoulderPosition(player).add(player.getLookAngle().scale(zOffset)));
                ray.setYRot(player.getYRot());
                ray.setXRot(player.getXRot());
                ray.setDataMaxLength(maxRange - zOffset);
                serverLevel.addFreshEntity(ray);
            }
        }
        return entitiesHit;
    }

    public static Matrix3f fromAngle(float pitch, float yaw, float roll) {
        // MC yaw CCW → negate
        return new Matrix3f()
                .rotateY((float) Math.toRadians(-yaw))   // MC yaw CCW → negate
                .rotateX((float) Math.toRadians(pitch))
                .rotateZ((float) Math.toRadians(roll));
    }
}

