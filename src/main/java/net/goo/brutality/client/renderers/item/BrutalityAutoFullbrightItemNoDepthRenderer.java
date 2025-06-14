package net.goo.brutality.client.renderers.item;

import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.minecraft.world.item.Item;

public class BrutalityAutoFullbrightItemNoDepthRenderer<T extends Item & BrutalityGeoItem> extends BrutalityItemRenderer<T> {

    public BrutalityAutoFullbrightItemNoDepthRenderer() {
        super();
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(this));
    }



}