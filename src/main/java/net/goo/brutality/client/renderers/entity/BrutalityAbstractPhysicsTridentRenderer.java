package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class BrutalityAbstractPhysicsTridentRenderer<T extends BrutalityAbstractPhysicsThrowingProjectile & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {


    public BrutalityAbstractPhysicsTridentRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<T>> layerConfigurator) {
        super(context, layerConfigurator);
    }

    public BrutalityAbstractPhysicsTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {

        if (!animatable.isNoPhysics()) {

                poseStack.translate(0, (animatable.getModelHeight() / 2) / 16, 0);

                float lerpedRoll = Mth.lerp(partialTick, animatable.prevRoll, animatable.roll);
                float lerpedYaw = Mth.lerp(partialTick, animatable.prevYaw, animatable.yaw);
                float lerpedPitch = Mth.lerp(partialTick, animatable.prevPitch, animatable.pitch);

                // Apply rotations
                poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
                poseStack.mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
                poseStack.mulPose(Axis.XP.rotationDegrees(lerpedRoll));

        }


        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);

    }
}
