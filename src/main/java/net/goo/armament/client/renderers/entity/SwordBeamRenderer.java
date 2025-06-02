package net.goo.armament.client.renderers.entity;

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

    public String getIdentifier() {
        return getAnimatable().geoIdentifier();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SwordBeam animatable) {
        return Armament.prefix("textures/entity/projectiles/" + getIdentifier() + ".png");
    }

    @Override
    public RenderType getRenderType(SwordBeam animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(texture);
    }

    @Override
    public void render(@NotNull SwordBeam pEntity, float entityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            Vec3 moveVec = pEntity.getDeltaMovement();

                moveVec = moveVec.normalize();

                double angle = Math.atan2(-moveVec.z, moveVec.x);
                double pitch = Math.asin(moveVec.y);
                pPoseStack.pushPose();
                pPoseStack.translate(0.0D, pEntity.getBbHeight() / 2, 0.0D);
                pPoseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle)));
                pPoseStack.mulPose(Axis.ZP.rotationDegrees(((float) Math.toDegrees(pitch))));
                pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getRandomRoll()));

        }

        pPoseStack.scale(pEntity.getRenderScale(), pEntity.getRenderScale(), pEntity.getRenderScale());

        super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

        pPoseStack.popPose();
    }
}
