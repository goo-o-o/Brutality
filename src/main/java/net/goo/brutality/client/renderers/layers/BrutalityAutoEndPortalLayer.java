package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityAutoEndPortalTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class BrutalityAutoEndPortalLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T>{

    public BrutalityAutoEndPortalLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityAutoEndPortalTexture.getRenderType(this.getTextureResource(animatable));
    }


    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType end_port_render_type = getRenderType(animatable);

        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, end_port_render_type,
                bufferSource.getBuffer(end_port_render_type), partialTick, FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}