package net.goo.armament.client.renderers.item;

import net.goo.armament.client.models.weapon.ArmaWeaponLayer;
import net.goo.armament.client.models.weapon.ArmaWeaponModel;
import net.goo.armament.item.ArmaGeoItem;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ArmaAutoGlowingTranslucentCullItemRenderer<T extends Item & ArmaGeoItem> extends ArmaAutoGlowingItemRenderer<T> {

    public ArmaAutoGlowingTranslucentCullItemRenderer() {
        super();
        ((ArmaWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaWeaponLayer<>(this));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}