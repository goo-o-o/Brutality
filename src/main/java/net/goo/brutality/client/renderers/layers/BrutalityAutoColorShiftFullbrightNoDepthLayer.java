package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityAutoRGBTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.getCyclingColors;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.intToRgb;
import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BrutalityAutoColorShiftFullbrightNoDepthLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T>{
    private final int[][] colors;
    private final float speed;
    public BrutalityAutoColorShiftFullbrightNoDepthLayer(GeoRenderer<T> renderer, float speed, int[]... colors) {
        super(renderer);
        this.colors = colors;
        this.speed = speed;
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityAutoRGBTexture.getRenderType(this.getTextureResource(animatable));
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType fullbrightRenderType = getRenderType(animatable);

        int[] currentColor = intToRgb(getCyclingColors(speed, colors));

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, fullbrightRenderType,
                bufferSource.getBuffer(fullbrightRenderType), partialTick, FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                (float) currentColor[0] / 255, (float) currentColor[1] / 255, (float) currentColor[2] / 255, 1);
    }

}