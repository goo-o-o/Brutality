package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class BrutalityAbstractPhysicsProjectileRenderer<T extends Entity & ArmaGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityAbstractPhysicsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);

    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        if (entity instanceof BrutalityAbstractPhysicsProjectile object && !object.isNoPhysics()) {

            poseStack.translate(0, (object.getModelHeight() / 2) / 16, 0);

            float lerpedRoll = Mth.lerp(partialTick, object.prevRoll, object.roll);
            float lerpedYaw = Mth.lerp(partialTick, object.prevYaw, object.yaw);
            float lerpedPitch = Mth.lerp(partialTick, object.prevPitch, object.pitch);

            // Apply rotations
            poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
            poseStack.mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
            poseStack.mulPose(Axis.XP.rotationDegrees(lerpedRoll));

        }
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

}
