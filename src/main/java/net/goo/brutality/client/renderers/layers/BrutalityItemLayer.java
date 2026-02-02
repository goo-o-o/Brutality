package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BrutalityItemLayer<T extends Item & BrutalityGeoItem> extends GeoRenderLayer<T> {
    public final GeoItemRenderer<T> geoRenderer;

    public BrutalityItemLayer(GeoItemRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        geoRenderer = entityRendererIn;
    }


    protected RenderType getRenderType(T animatable) {
        return RenderType.entityCutoutNoCullZOffset(getTextureResource(animatable));
    }


    protected RenderType getRenderTypeGlint() {
        return RenderType.entityGlint();
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.preRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderType(animatable), bufferSource.getBuffer(getRenderType(animatable)), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.5F);
        if (geoRenderer.getCurrentItemStack().isEnchanted()) {
            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderTypeGlint(), bufferSource.getBuffer(getRenderTypeGlint()), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}