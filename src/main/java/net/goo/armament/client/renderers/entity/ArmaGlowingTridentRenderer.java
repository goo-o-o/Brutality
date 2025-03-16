package net.goo.armament.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.armament.client.entity.ArmaGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ArmaGlowingTridentRenderer<T extends Entity & ArmaGeoEntity> extends ArmaGlowingEntityRenderer<T> {
    public ArmaGlowingTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Apply transformations (same as vanilla ThrownTrident)
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));

        // Let GeckoLib handle the rendering
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        // Pop the transformation stack
        poseStack.popPose();
    }

}