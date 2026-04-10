package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class BrutalityAbstractPhysicsProjectileRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {


    public BrutalityAbstractPhysicsProjectileRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<T>> layerConfigurator) {
        super(context, layerConfigurator);
    }

    public BrutalityAbstractPhysicsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if (animatable instanceof BrutalityAbstractPhysicsProjectile object && !object.noPhysics) {

                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, object.prevYaw, object.yaw)));
                poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, object.prevPitch, object.pitch)));
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, object.prevRoll, object.roll)));

        }


        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);

    }
}
