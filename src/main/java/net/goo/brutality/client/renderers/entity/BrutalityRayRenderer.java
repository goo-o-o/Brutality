package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityRay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class BrutalityRayRenderer<T extends BrutalityRay & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityRayRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<T>> layerConfigurator) {
        super(context);
        layerConfigurator.accept(this);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Entity owner = animatable.getOwner();

        if (animatable.shouldFollowOwner) {
            if (owner != null) {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, -owner.yRotO, -owner.getViewYRot(partialTick))));
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, owner.xRotO, owner.getViewXRot(partialTick)) + 90F));
            }
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(-animatable.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getXRot() + 90F));
        }
    }
}
