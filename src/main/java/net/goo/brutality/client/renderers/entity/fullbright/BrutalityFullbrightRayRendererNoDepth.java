package net.goo.brutality.client.renderers.entity.fullbright;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.custom.ExplosionRay;
import net.goo.brutality.entity.custom.LastPrismRay;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class BrutalityRayRendererNoDepth<T extends Entity & BrutalityGeoEntity> extends BrutalityFullbrightNoDepthEntityRenderer<T> {
    public BrutalityRayRendererNoDepth(EntityRendererProvider.Context context) {
        super(context);
    }

    private Entity owner = null;

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (animatable instanceof ExplosionRay explosionRay) {
            int yaw = explosionRay.getSyncedYaw();
            int pitch = explosionRay.getSyncedPitch();
            float renderScale = explosionRay.getSyncedCircleCount() == 0 ? 1 : explosionRay.getSyncedCircleCount();
            renderScale *= 0.5F;
            poseStack.pushPose();

            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
            poseStack.scale(renderScale, renderScale, renderScale);

            super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

            poseStack.popPose();
        }

        if (animatable instanceof LastPrismRay lastPrismRay) {
            if (owner == null) {
                int ownerId = lastPrismRay.getSynchedOwnerId();
                owner = lastPrismRay.level().getEntity(ownerId);
            }

            if (owner instanceof LivingEntity livingOwner) {
                float yaw = livingOwner.getZRot();
                float pitch = livingOwner.getYRot();

            }

        }

    }

}
