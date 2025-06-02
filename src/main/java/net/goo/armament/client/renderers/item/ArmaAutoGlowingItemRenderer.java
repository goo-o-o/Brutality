package net.goo.armament.client.renderers.item;

import net.goo.armament.item.base.ArmaGeoItem;
import net.goo.armament.client.models.weapon.ArmaWeaponModel;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ArmaAutoGlowingItemRenderer<T extends Item & ArmaGeoItem> extends ArmaItemRenderer<T> {

    public ArmaAutoGlowingItemRenderer() {
        super();
        ((ArmaWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}