package net.goo.armament.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.entity.base.ArmaArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

public class ArmaGlowingArrowRenderer<T extends ArmaArrow & ArmaGeoEntity> extends ArmaGlowingEntityRenderer<T> {
    public ArmaGlowingArrowRenderer(EntityRendererProvider.Context context) {
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
