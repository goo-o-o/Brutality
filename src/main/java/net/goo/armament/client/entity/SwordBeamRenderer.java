package net.goo.armament.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.armament.Armament;
import net.goo.armament.client.entity.model.SwordBeamModel;
import net.goo.armament.entity.base.SwordBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SwordBeamRenderer extends GeoEntityRenderer<SwordBeam> {

    public SwordBeamRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SwordBeamModel());
    }

    public int getCurrentFrame() {
        return this.getAnimatable().getCurrentFrame();
    }

    @Override
    public ResourceLocation getTextureLocation(SwordBeam animatable) {
        return Armament.prefix("textures/entity/projectiles/terra_beam" + animatable.getCurrentFrame() + ".png");
    }

    @Override
    public RenderType getRenderType(SwordBeam animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture, true);
    }

    @Override
    public void render(SwordBeam pEntity, float entityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            Vec3 moveVec = pEntity.getDeltaMovement();

            if (moveVec.x != 0 || moveVec.y != 0 || moveVec.z != 0 ) {
                moveVec = moveVec.normalize();

                double angle = Math.atan2(-moveVec.z, moveVec.x);
                double pitch = Math.asin(moveVec.y); // Calculate pitch
                pPoseStack.pushPose();
                pPoseStack.translate(0.0D, 0.5D, 0.0D);
                pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle)));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(((float) Math.toDegrees(pitch))));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getRandomRoll()));
            }
        }

        pPoseStack.scale(pEntity.getRenderScale(), pEntity.getRenderScale(), pEntity.getRenderScale());

        super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

        pPoseStack.popPose();
    }
}
