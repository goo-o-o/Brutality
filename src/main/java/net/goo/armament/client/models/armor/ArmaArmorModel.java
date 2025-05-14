package net.goo.armament.client.models.armor;

import net.goo.armament.Armament;
import net.goo.armament.item.ArmaGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaArmorModel<T extends ArmorItem & ArmaGeoItem> extends GeoModel<T> {
    public GeoArmorRenderer<T> renderer;


    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Armament.prefix("geo/" + animatable.getCategoryAsString().toLowerCase() + "/" + animatable.getMaterial() + "_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Armament.prefix("textures/armor/" + "/" + animatable.getMaterial() + "_armor.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Armament.prefix("animations/" + animatable.getCategoryAsString().toLowerCase() + "/" + animatable.getMaterial() + "_armor.animation.json");

    }
}