package net.goo.brutality.client.renderers.item;

import net.goo.brutality.client.models.BrutalityItemModel;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.Nullable;

public class BrutalityItemRenderer<T extends Item & BrutalityGeoItem> extends GeoItemRenderer<T> {

    public BrutalityItemRenderer() {
        super(new BrutalityItemModel<>());
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
    }


    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCullZOffset(texture);
    }

    public static <T extends Item & BrutalityGeoItem> BrutalityItemRenderer<T> createRenderer() {
        return new BrutalityItemRenderer<>();
    }


}