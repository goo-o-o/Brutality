package net.goo.armament.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.Armament;
import net.goo.armament.client.entity.model.SwordWaveModel;
import net.goo.armament.entity.base.SwordWave;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SwordWaveRenderer extends GeoEntityRenderer<SwordWave> {

    public SwordWaveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SwordWaveModel());
    }

    public String getIdentifier() {
        return getAnimatable().geoIdentifier();
    }

    @Override
    public ResourceLocation getTextureLocation(SwordWave animatable) {
        return Armament.prefix("textures/entity/projectiles/" + getIdentifier() + ".png");
    }

    @Override
    public RenderType getRenderType(SwordWave animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(texture);
    }

    @Override
    public void render(@NotNull SwordWave pEntity, float entityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        pPoseStack.pushPose();

        pPoseStack.scale(pEntity.getRenderScale(), 1, pEntity.getRenderScale());

        super.render(pEntity, entityYaw, pPartialTicks, pPoseStack, bufferSource, packedLight);

        pPoseStack.popPose();
    }
}
