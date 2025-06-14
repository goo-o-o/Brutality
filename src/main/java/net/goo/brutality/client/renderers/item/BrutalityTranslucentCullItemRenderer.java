package net.goo.brutality.client.renderers.item;

import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.goo.brutality.client.renderers.layers.BrutalityAutoAlphaLayer;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BrutalityTranslucentCullItemRenderer<T extends Item & BrutalityGeoItem> extends GeoItemRenderer<T> {

    public BrutalityTranslucentCullItemRenderer() {
        super(new BrutalityItemModel<>());
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityAutoAlphaLayer<>(this));
    }
}