package net.goo.brutality.client.renderers.item;

import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.Item;

public class BrutalityAutoFullbrightItemRenderer<T extends Item & BrutalityGeoItem> extends BrutalityItemRenderer<T> {

    public BrutalityAutoFullbrightItemRenderer() {
        super();
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityAutoFullbrightLayer(this));
    }



}