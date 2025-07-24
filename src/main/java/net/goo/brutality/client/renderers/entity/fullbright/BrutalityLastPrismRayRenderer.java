//package net.goo.brutality.client.renderers.entity.fullbright;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.client.renderers.layers.BrutalityAutoColorShiftFullbrightNoDepthLayer;
//import net.goo.brutality.entity.projectile.ray.LastPrismRay;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//public class BrutalityLastPrismRayRenderer<T extends LastPrismRay & BrutalityGeoEntity> extends BrutalityFullbrightDepthNoDepthEntityRenderer<T> {
//    public BrutalityLastPrismRayRenderer(EntityRendererProvider.Context context) {
//        super(context);
//
//        int[][] colors = new int[][]{
//                {255, 0, 0},
//                {255, 127, 0},
//                {255, 255, 0},
//                {0, 255, 0},
//                {0, 0, 255},
//                {75, 0, 130},
//                {148, 0, 211}
//        };
//
//
//        this.addRenderLayer(new BrutalityAutoColorShiftFullbrightNoDepthLayer<>(
//                this, 1, colors[0], colors[1], colors[2], colors[3], colors[4], colors[5], colors[6]));
//    }
//
//    @Override
//    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
//            Entity owner = animatable.getOwner();
//
//            if (owner != null) {
//                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, -owner.yRotO, -owner.getViewYRot(partialTick))));
//                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, owner.xRotO, owner.getViewXRot(partialTick)) + 90F));
//            }
//
//        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
//    }
//}
