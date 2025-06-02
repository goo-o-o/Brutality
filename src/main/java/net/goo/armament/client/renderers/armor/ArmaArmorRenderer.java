package net.goo.armament.client.renderers.armor;

import net.goo.armament.client.models.armor.ArmaArmorLayer;
import net.goo.armament.client.models.armor.ArmaArmorModel;
import net.goo.armament.item.base.ArmaGeoItem;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaArmorRenderer<T extends ArmorItem & ArmaGeoItem> extends GeoArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public ArmaArmorRenderer() {
        super(new ArmaArmorModel<>());
        ((ArmaArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaArmorLayer<>(this));
    }

}
