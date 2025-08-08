package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.projectile.generic.StarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class BrutalityShurikenRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {


    public BrutalityShurikenRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<T>> layerConfigurator) {
        super(context, layerConfigurator);
    }

    public BrutalityShurikenRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot())));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));


        if (animatable instanceof StarEntity starEntity) {
            if (starEntity.inGround) {
                if (!starEntity.renderForLayer)
                    poseStack.mulPose(Axis.YP.rotationDegrees(starEntity.getRandomYaw()));
            } else {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(Mth.lerp(partialTick, (ageInTicks - 1) * 180F, ageInTicks * 180F))));
            }
        }

        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }
}
