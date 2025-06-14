package net.goo.brutality.client.renderers.item;

import net.goo.brutality.client.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.layers.BrutalityItemLayer;
import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public class BrutalityTranslucentNoCullItemRenderer<T extends Item & BrutalityGeoItem> extends BrutalityItemRenderer<T> {

    public BrutalityTranslucentNoCullItemRenderer() {
        super();
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityItemLayer<>(this));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return BrutalityRenderTypes.getEntityTranslucentNoCull(texture);
    }
}