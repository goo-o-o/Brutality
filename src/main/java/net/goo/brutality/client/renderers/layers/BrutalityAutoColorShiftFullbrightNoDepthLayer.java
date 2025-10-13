package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityColorShiftTexture;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BrutalityAutoColorShiftFullbrightNoDepthLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T> {
    private final int[] colors;
    private final float speed;

    public BrutalityAutoColorShiftFullbrightNoDepthLayer(GeoRenderer<T> renderer, float speed, int... colors) {
        super(renderer);
        this.colors = colors;
        this.speed = speed;
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityColorShiftTexture.getRenderType(getTextureResource(animatable));
    }



    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType colorShiftRenderType = getRenderType(animatable);

        int currentColor = ColorUtils.getCyclingColor(speed, colors);
        float red = FastColor.ARGB32.red(currentColor) / 255F;
        float green = FastColor.ARGB32.green(currentColor)  / 255F;
        float blue = FastColor.ARGB32.blue(currentColor)  / 255F;

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, colorShiftRenderType,
                bufferSource.getBuffer(colorShiftRenderType), partialTick, FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                red, green, blue, 1);
    }

}