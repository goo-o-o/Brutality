package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.textures.BrutalityAutoAlphaTexture;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityAutoAlphaLayer<T extends Item & BrutalityGeoItem> extends AutoGlowingGeoLayer<T> {
    public final GeoItemRenderer<T> geoRenderer;

    public BrutalityAutoAlphaLayer(GeoItemRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        geoRenderer = entityRendererIn;
    }


    @Override
    protected RenderType getRenderType(T animatable) {
        return BrutalityAutoAlphaTexture.getRenderType(this.getTextureResource(animatable));
    }
}