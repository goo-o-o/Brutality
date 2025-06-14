package net.goo.brutality.client.models.weapon;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BrutalityItemModel<T extends Item & BrutalityGeoItem> extends GeoModel<T> {
    public GeoItemRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Brutality.prefix("geo/item/" + animatable.getCategoryAsString() + "/" + (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Brutality.prefix("textures/item/" + animatable.getCategoryAsString() + "/" + animatable.geoIdentifier() + "/" + (renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Brutality.prefix("animations/item/" + animatable.getCategoryAsString() + "/" + (renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + "_handheld.animation.json");

    }
}