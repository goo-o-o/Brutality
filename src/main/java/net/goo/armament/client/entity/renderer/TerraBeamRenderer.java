package net.goo.armament.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.armament.client.entity.model.TerraBeamModel;
import net.goo.armament.entity.custom.TerraBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TerraBeamRenderer extends GeoEntityRenderer<TerraBeam> {
    public TerraBeamRenderer(EntityRendererProvider.Context context) {
        super(context, new TerraBeamModel());
    }

    @Override
    protected int getBlockLightLevel(TerraBeam pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(TerraBeam pEntity, float entityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            Vec3 moveVec = pEntity.getDeltaMovement();

            if (moveVec.x != 0 || moveVec.y != 0 || moveVec.z != 0 ) {
                moveVec = moveVec.normalize();

                double angle = Math.atan2(-moveVec.z, moveVec.x);
                double pitch = Math.asin(moveVec.y); // Calculate pitch
                pPoseStack.pushPose();
                pPoseStack.translate(0.0D, 0.5D, 0.0D);
                pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle))); // Yaw
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(((float) Math.toDegrees(pitch)))); // Pitch
                pPoseStack.mulPose(Axis.XP.rotationDegrees((float) pEntity.getRandomRoll())); // Roll
            }
        }

            pPoseStack.scale(2f, 2f, 2f);

            super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

            pPoseStack.popPose();
    }

}
