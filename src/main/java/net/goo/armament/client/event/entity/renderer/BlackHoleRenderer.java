package net.goo.armament.client.event.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.client.event.entity.model.BlackHoleModel;
import net.goo.armament.entity.custom.BlackHoleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlackHoleRenderer extends GeoEntityRenderer<BlackHoleEntity> {
    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackHoleModel());
    }

    @Override
    protected int getBlockLightLevel(BlackHoleEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(BlackHoleEntity pEntity, float entityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight) {
        pPoseStack.pushPose();

        this.model.getRenderType(animatable, getTextureLocation(animatable));

        super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BlackHoleEntity animatable) {
        return super.getTextureLocation(animatable);
    }

}
