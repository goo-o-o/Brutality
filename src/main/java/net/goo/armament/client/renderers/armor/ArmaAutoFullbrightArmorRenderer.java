package net.goo.armament.client.renderers.armor;

import net.goo.armament.client.models.armor.ArmaArmorModel;
import net.goo.armament.client.renderers.ArmaAutoFullbrightLayer;
import net.goo.armament.item.ArmaGeoItem;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaAutoFullbrightArmorRenderer<T extends ArmorItem & ArmaGeoItem> extends ArmaArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public ArmaAutoFullbrightArmorRenderer() {
        super();
        ((ArmaArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaAutoFullbrightLayer(this));
    }

}
