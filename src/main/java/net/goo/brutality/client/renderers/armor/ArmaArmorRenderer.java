package net.goo.brutality.client.renderers.armor;

import net.goo.brutality.client.models.armor.BrutalityArmorLayer;
import net.goo.brutality.client.models.armor.BrutalityArmorModel;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaArmorRenderer<T extends ArmorItem & BrutalityGeoItem> extends GeoArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public ArmaArmorRenderer() {
        super(new BrutalityArmorModel<>());
        ((BrutalityArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityArmorLayer<>(this));
    }

}
