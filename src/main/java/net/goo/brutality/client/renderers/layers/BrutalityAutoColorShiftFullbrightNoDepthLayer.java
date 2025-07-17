package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityAutoRGBTexture;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BrutalityAutoRGBFullbrightNoDepthLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T>{

    public BrutalityAutoRGBFullbrightNoDepthLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityAutoRGBTexture.getRenderType(this.getTextureResource(animatable));
    }

    private final int[][] colors = new int[][]{{148, 0, 211}, {75, 0, 130}, {0, 0, 255}, {0, 255, 0}, {255, 255, 0}, {255, 127, 0}, {255, 0, 0}};

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType fullbrightRenderType = getRenderType(animatable);

        int[] currentColor = BrutalityTooltipHelper.getCyclingColorFromGradient(1, colors[0], colors[1], colors[2], colors[3], colors[4], colors[5], colors[6]);

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, fullbrightRenderType,
                bufferSource.getBuffer(fullbrightRenderType), partialTick, FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                currentColor[0], currentColor[1], currentColor[1], 1);
    }
}