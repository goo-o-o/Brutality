package net.goo.brutality.client.models;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Locale;

public class BrutalityArmorModel<T extends ArmorItem & BrutalityGeoItem> extends GeoModel<T> {
    public GeoArmorRenderer<T> renderer;


    @Override
    public ResourceLocation getModelResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                Brutality.MOD_ID, "geo/armor/" + animatable.getMaterial().toString().toLowerCase(Locale.ROOT) + "_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                Brutality.MOD_ID, "textures/armor/" + animatable.getMaterial().toString().toLowerCase(Locale.ROOT) + "_armor.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(
                Brutality.MOD_ID, "animations/armor/" + animatable.getMaterial().toString().toLowerCase(Locale.ROOT) + "_armor.animation.json");

    }
}