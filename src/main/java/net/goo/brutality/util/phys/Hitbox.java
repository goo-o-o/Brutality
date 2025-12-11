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

public abstract class Hitbox {
    public Vec3 center;
    public final Matrix3f rotation = new Matrix3f(); // every hitbox can be rotated

    public abstract AABB getAABB();

    public abstract Hitbox copy();

    public abstract void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay);

    public Hitbox translateWorld(Vec3 v) {
        center = center.add(v);
        return this;
    }

    public abstract boolean intersectsAABB(AABB aabb);

    public Hitbox translateLocal(Vec3 local) {
        Vector3f t = local.toVector3f();
        rotation.transform(t);
        center = center.add(t.x, t.y, t.z);
        return this;
    }

    public Hitbox rotateWorld(Matrix3f rot) {
        rotation.mul(rot);
        return this;
    }

    public void rotateLocal(Matrix3f rot) {
        rotation.mulLocal(rot);
    }

    public Hitbox inflate(float f) {
        return this;
    }

    public Hitbox inWorld(Player player, Vec3 localOffset) {
        return this.copy()
                .translateWorld(HitboxUtils.getShoulderPosition(player))
                .rotateWorld(HitboxUtils.fromAngle(player.getXRot(), player.getYRot(), 0))
                .translateLocal(localOffset.add(0,0, player.getBbWidth() / 2));
    }

    public <T extends Entity> List<T> filter(Player player, List<T> entities, boolean needsLos) {
        return entities.stream().filter(entity -> intersectsAABB(entity.getBoundingBox()) && (!needsLos || player.hasLineOfSight(entity))).toList();
    }

    public abstract <T extends Entity> List<T> findEntitiesHit(Player player, Class<T> clazz, boolean needsLos, Predicate<? super T> filter);

}