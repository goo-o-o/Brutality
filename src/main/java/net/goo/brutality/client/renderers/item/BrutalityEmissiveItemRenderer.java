package net.goo.brutality.client.renderers.item;

import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.client.models.weapon.BrutalityItemModel;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityEmissiveItemRenderer<T extends Item & BrutalityGeoItem> extends BrutalityItemRenderer<T> {

    public BrutalityEmissiveItemRenderer() {
        super();
        ((BrutalityItemModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}