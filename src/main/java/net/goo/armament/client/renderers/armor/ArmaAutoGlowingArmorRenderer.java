package net.goo.armament.client.renderers.armor;

import net.goo.armament.client.models.armor.ArmaArmorModel;
import net.goo.armament.item.ArmaGeoItem;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ArmaAutoGlowingArmorRenderer<T extends ArmorItem & ArmaGeoItem> extends ArmaArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public ArmaAutoGlowingArmorRenderer() {
        super();
        ((ArmaArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}
