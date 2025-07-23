package net.goo.brutality.client.renderers.entity.fullbright;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.projectile.ray.ExplosionRay;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BrutalityFullbrightRayRendererNoDepth<T extends Entity & BrutalityGeoEntity> extends BrutalityFullbrightNoDepthEntityRenderer<T> {
    public BrutalityFullbrightRayRendererNoDepth(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {

        if (animatable instanceof ExplosionRay explosionRay) {
            int yaw = explosionRay.getSyncedYaw();
            int pitch = explosionRay.getSyncedPitch();
            float renderScale = explosionRay.getSyncedCircleCount() == 0 ? 1 : explosionRay.getSyncedCircleCount();
            renderScale *= 0.5F;

            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
            poseStack.scale(renderScale, renderScale, renderScale);

        }



        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }
}
