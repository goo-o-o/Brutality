package net.goo.armament.client.models.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.armament.item.base.ArmaGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ArmaArmorLayer<T extends ArmorItem & ArmaGeoItem> extends GeoRenderLayer<T> {
    public final GeoArmorRenderer<T> geoRenderer;

    public ArmaArmorLayer(GeoArmorRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        geoRenderer = entityRendererIn;
    }

    protected RenderType getRenderType(T animatable) {
        return RenderType.itemEntityTranslucentCull(getTextureResource(animatable));
    }


    protected RenderType getRenderTypeGlint() {
        return RenderType.entityGlint();
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderType(animatable), bufferSource.getBuffer(getRenderType(animatable)), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.5F);
        if (geoRenderer.getCurrentStack().isEnchanted()) {
            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderTypeGlint(), bufferSource.getBuffer(getRenderTypeGlint()), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}