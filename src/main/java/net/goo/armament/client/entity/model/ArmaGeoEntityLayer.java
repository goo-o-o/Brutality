package net.goo.armament.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.armament.client.entity.ArmaGeoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ArmaGeoEntityLayer<T extends Entity & ArmaGeoEntity> extends GeoRenderLayer<T> {
    public final GeoEntityRenderer<T> geoRenderer;
    public boolean emissive = false;

    public ArmaGeoEntityLayer(GeoEntityRenderer<T> entityRendererIn, boolean emissive) {
        super(entityRendererIn);
        geoRenderer = entityRendererIn;
        this.emissive = emissive;
    }

    protected RenderType getRenderTypeEyes(T animatable) {
        if (emissive) return RenderType.entityTranslucentEmissive(getTextureResource(animatable));
        else return RenderType.entityTranslucent(getTextureResource(animatable));
    }


    protected RenderType getRenderTypeGlint() {
        return RenderType.entityGlint();
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        this.getGeoModel().getRenderType(animatable, getTextureResource(animatable));
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        poseStack.popPose();
    }
}