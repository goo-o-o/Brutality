package net.goo.brutality.client.renderers.entity.fullbright;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.entity.custom.ExplosionRay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class BrutalityRayRendererNoDepth<T extends Entity & ArmaGeoEntity> extends BrutalityFullbrightNoDepthEntityRenderer<ExplosionRay> {
    public BrutalityRayRendererNoDepth(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ExplosionRay entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        int yaw = entity.getSyncedYaw();
        int pitch = entity.getSyncedPitch();
        float renderScale = entity.getSyncedCircleCount() == 0 ? 1 : entity.getSyncedCircleCount();
        renderScale *= 0.5F;
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        poseStack.scale(renderScale, renderScale, renderScale);


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }

}
