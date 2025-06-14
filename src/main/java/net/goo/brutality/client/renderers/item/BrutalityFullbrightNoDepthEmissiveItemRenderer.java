package net.goo.brutality.client.renderers.item;

import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityFullbrightNoDepthEmissiveItemRenderer<T extends Item & BrutalityGeoItem> extends BrutalityItemRenderer<T> {

    public BrutalityFullbrightNoDepthEmissiveItemRenderer() {
        super();
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(this));
    }

}