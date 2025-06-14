package net.goo.brutality.client.models.armor;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BrutalityArmorModel<T extends ArmorItem & BrutalityGeoItem> extends GeoModel<T> {
    public GeoArmorRenderer<T> renderer;


    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Brutality.prefix("geo/" + animatable.getCategoryAsString() + "/" + animatable.getMaterial() + "_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Brutality.prefix("textures/armor/" + "/" + animatable.getMaterial() + "_armor.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Brutality.prefix("animations/" + animatable.getCategoryAsString() + "/" + animatable.getMaterial() + "_armor.animation.json");

    }
}