package net.goo.brutality.client.renderers.armor;

import net.goo.brutality.client.models.armor.BrutalityArmorModel;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaAutoFullbrightArmorRenderer<T extends ArmorItem & BrutalityGeoItem> extends ArmaArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public ArmaAutoFullbrightArmorRenderer() {
        super();
        ((BrutalityArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer(this));
    }

}
