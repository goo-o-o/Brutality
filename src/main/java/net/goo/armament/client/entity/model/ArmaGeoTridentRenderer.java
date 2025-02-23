package net.goo.armament.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.armament.client.entity.ArmaGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ArmaGeoTridentRenderer<T extends Entity & ArmaGeoEntity> extends ArmaGeoProjectileRenderer<T>{
    public ArmaGeoTridentRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
            VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.getRenderType(entity, getTextureLocation(entity)), false, false);

//            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            poseStack.popPose();
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
