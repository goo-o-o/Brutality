package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class BrutalityArrowRenderer<T extends BrutalityArrow & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {


    public BrutalityArrowRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<T>> layerConfigurator) {
        super(context, layerConfigurator);
    }

    public BrutalityArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 90F));
        float shake = (float) animatable.shakeTime - partialTick;
        if (shake > 0.0F) {
            float shakeAmount = -Mth.sin(shake * 3.0F) * shake;
            poseStack.mulPose(Axis.ZP.rotationDegrees(shakeAmount));
        }

        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }
}
