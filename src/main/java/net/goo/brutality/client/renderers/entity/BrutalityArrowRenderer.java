package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

public class BrutalityArrowRenderer<T extends BrutalityArrow & ArmaGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90F));
        float shake = (float) pEntity.shakeTime - pPartialTicks;
        if (shake > 0.0F) {
            float shakeAmount = -Mth.sin(shake * 3.0F) * shake;
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(shakeAmount));
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.popPose();

    }

}
