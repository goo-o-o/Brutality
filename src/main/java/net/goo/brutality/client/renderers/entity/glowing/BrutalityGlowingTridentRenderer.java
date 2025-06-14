package net.goo.brutality.client.renderers.entity.glowing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class BrutalityGlowingTridentRenderer<T extends Entity & ArmaGeoEntity> extends BrutalityGlowingEntityRenderer<T> {
    public BrutalityGlowingTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

}