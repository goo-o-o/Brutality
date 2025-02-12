package net.goo.armament.item;

import net.goo.armament.Armament;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ArmaGeoWeaponModel<T extends Item & ArmaGeoItem> extends GeoModel<T> {
    public GeoItemRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Armament.prefix("geo/item/" + (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Armament.prefix("textures/item/" + (renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Armament.prefix("animations/item/" + (renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.animation.json");
    }

}