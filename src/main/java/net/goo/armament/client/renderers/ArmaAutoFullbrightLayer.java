package net.goo.armament.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class ArmaAutoFullbrightLayer extends AutoGlowingGeoLayer{

    public ArmaAutoFullbrightLayer(GeoRenderer renderer) {
        super(renderer);
    }


    @Override
    protected RenderType getRenderType(GeoAnimatable animatable) {
        return ArmaAutoFullbrightTexture.getRenderType(this.getTextureResource(animatable));
    }

    @Override
    public void render(PoseStack poseStack, GeoAnimatable animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        VertexConsumer emissiveBuffer = bufferSource.getBuffer(
                RenderType.eyes(getTextureResource(animatable))
        );
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, emissiveBuffer, partialTick, FULL_BRIGHT, packedOverlay);
    }
}