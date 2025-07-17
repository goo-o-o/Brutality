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
        String identifier = animatable.getRegistryName();
        String model = animatable.model(renderer.getCurrentItemStack());


        return Brutality.prefix("geo/item/" + animatable.getCategoryAsString() + "/" +
                (renderer != null && model != null ? model : identifier) + "_handheld.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        String identifier = animatable.getRegistryName();
        String texture = animatable.texture(renderer.getCurrentItemStack());

        return Brutality.prefix("textures/item/" + animatable.getCategoryAsString()
                + "/" + identifier + "/" +
                (renderer != null && texture != null ? texture : identifier) + "_handheld.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        String identifier = animatable.getRegistryName();

        return Brutality.prefix("animations/item/" + animatable.getCategoryAsString() + "/" +
                (renderer != null ? identifier : "") + "_handheld.animation.json");

    }



}