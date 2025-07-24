//package net.goo.brutality.client.renderers.entity;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//public class BrutalityTridentRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
//    public BrutalityTridentRenderer(EntityRendererProvider.Context context) {
//        super(context);
//
//    }
//
//    @Override
//    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
//        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 90.0F));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) + 90.0F));
//
//        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
//    }
//}
