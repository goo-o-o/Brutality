package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.entity.base.SwordBeam;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BrutalitySwordBeamRenderer extends BrutalityEntityRenderer<SwordBeam> {


    public BrutalitySwordBeamRenderer(EntityRendererProvider.Context context, Consumer<BrutalityEntityRenderer<SwordBeam>> layerConfigurator) {
        super(context, layerConfigurator);
    }

    public BrutalitySwordBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public RenderType getRenderType(SwordBeam animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    protected void applyRotations(SwordBeam animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Vec3 moveVec = animatable.getDeltaMovement();

        moveVec = moveVec.normalize();

        double angle = Math.atan2(-moveVec.z, moveVec.x);
        double pitch = Math.asin(moveVec.y);
        poseStack.translate(0.0D, animatable.getBbHeight() / 2, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(((float) Math.toDegrees(pitch))));
        poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRandomRoll()));

        poseStack.scale(animatable.getRenderScale(), animatable.getRenderScale(), animatable.getRenderScale());

        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }
}
