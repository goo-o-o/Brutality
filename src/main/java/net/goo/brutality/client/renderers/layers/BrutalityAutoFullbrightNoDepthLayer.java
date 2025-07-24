package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityAutoFullbrightNoDepthTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BrutalityAutoFullbrightNoDepthLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T>{

    public BrutalityAutoFullbrightNoDepthLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityAutoFullbrightNoDepthTexture.getRenderType(this.getTextureResource(animatable));
    }


    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType fullbrightRenderType = getRenderType(animatable);

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, fullbrightRenderType,
                bufferSource.getBuffer(fullbrightRenderType), partialTick, FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}