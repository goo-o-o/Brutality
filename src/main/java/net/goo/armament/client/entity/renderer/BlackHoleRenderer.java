package net.goo.armament.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.client.entity.model.BlackHoleModel;
import net.goo.armament.entity.custom.BlackHole;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlackHoleRenderer extends GeoEntityRenderer<BlackHole> {
    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackHoleModel());
    }

    @Override
    protected int getBlockLightLevel(BlackHole pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(BlackHole pEntity, float entityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight) {
        pPoseStack.pushPose();

        this.model.getRenderType(animatable, getTextureLocation(animatable));

        super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BlackHole animatable) {
        return super.getTextureLocation(animatable);
    }

}
