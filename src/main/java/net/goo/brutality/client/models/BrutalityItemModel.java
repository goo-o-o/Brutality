package net.goo.brutality.client.models;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.item.base.BrutalityBlockItem;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;

public class BrutalityItemModel<T extends Item & BrutalityGeoItem> extends GeoModel<T> {
    public BrutalityItemRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        String model = renderer != null ? animatable.model(renderer.getCurrentItemStack()) : null;
        String identifier = renderer != null ? model != null ? model : animatable.getRegistryName() : animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/block/" + animatable.getRegistryName() + ".geo.json");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/item/" + (animatable instanceof BrutalityCurioItem ? "/curio/" : "") + animatable.getCategoryAsString() + "/" +
                identifier + "_handheld.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        String texture = renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : null;
        String identifier = renderer != null ? texture != null ? texture : animatable.getRegistryName() : animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/block/" + animatable.getRegistryName() + ".png");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/item/" + (animatable instanceof BrutalityCurioItem ? "/curio/" : "") + animatable.getCategoryAsString()
                + "/" + animatable.getRegistryName() + "/" +
                identifier + "_handheld.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        String identifier = animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/block/" + animatable.getRegistryName() + ".animation.json");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/item/" + animatable.getCategoryAsString() + "/" +
                identifier + "_handheld.animation.json");

    }


}