package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BrutalityBlockLayer<T extends BlockEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    public BrutalityBlockLayer(GeoBlockRenderer<T> renderer) {
        super(renderer);
    }

    protected RenderType getRenderType(T animatable) {
        return RenderType.entityCutoutNoCullZOffset(getTextureResource(animatable));
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        getRenderer().reRender(
                bakedModel,
                poseStack,
                bufferSource,
                animatable,
                getRenderType(animatable),
                bufferSource.getBuffer(getRenderType(animatable)),
                partialTick,
                packedLight,
                packedOverlay,
                1.0F, 1.0F, 1.0F, 1.0F);

    }
}
