//package net.goo.brutality.client.renderers.entity;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//
//public class BrutalityAbstractPhysicsProjectileRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
//    public BrutalityAbstractPhysicsProjectileRenderer(EntityRendererProvider.Context context) {
//        super(context);
//
//    }
//
//    @Override
//    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
//        if (animatable instanceof BrutalityAbstractPhysicsProjectile object && !object.isNoPhysics()) {
//
//                poseStack.translate(0, (object.getModelHeight() / 2) / 16, 0);
//
//                float lerpedRoll = Mth.lerp(partialTick, object.prevRoll, object.roll);
//                float lerpedYaw = Mth.lerp(partialTick, object.prevYaw, object.yaw);
//                float lerpedPitch = Mth.lerp(partialTick, object.prevPitch, object.pitch);
//
//                // Apply rotations
//                poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
//                poseStack.mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
//                poseStack.mulPose(Axis.XP.rotationDegrees(lerpedRoll));
//
//        }
//
//
//        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
//
//    }
//}
