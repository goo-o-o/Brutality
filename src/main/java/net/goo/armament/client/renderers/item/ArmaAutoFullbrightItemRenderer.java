package net.goo.armament.client.renderers.item;

import net.goo.armament.item.base.ArmaGeoItem;
import net.goo.armament.client.models.weapon.ArmaWeaponModel;
import net.goo.armament.client.renderers.ArmaAutoFullbrightLayer;
import net.minecraft.world.item.Item;

public class ArmaAutoFullbrightItemRenderer<T extends Item & ArmaGeoItem> extends ArmaItemRenderer<T> {

    public ArmaAutoFullbrightItemRenderer() {
        super();
        ((ArmaWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaAutoFullbrightLayer(this));
    }

}