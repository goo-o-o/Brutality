package net.goo.brutality.client.models;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityItemRenderer;
import net.goo.brutality.common.item.base.BrutalityBlockItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;

import java.util.Locale;

public class BrutalityItemModel<T extends Item & BrutalityGeoItem> extends GeoModel<T> {
    public BrutalityItemRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        String model = renderer != null ? animatable.model(renderer.getCurrentItemStack()) : null;
        String identifier = renderer != null ? model != null ? model : animatable.getRegistryName() : animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/block/" + animatable.getRegistryName() + ".geo.json");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/item/" + (animatable instanceof BrutalityCurioItem ? "/curio/" : "") + getCategoryAsString(animatable) + "/" +
                identifier + "_handheld.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        String texture = renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : null;
        String identifier = renderer != null ? texture != null ? texture : animatable.getRegistryName() : animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/block/" + animatable.getRegistryName() + ".png");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/item/" + (animatable instanceof BrutalityCurioItem ? "/curio/" : "") + getCategoryAsString(animatable)
                + "/" + animatable.getRegistryName() + "/" +
                identifier + "_handheld.png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        String animation = renderer != null ? animatable.animation(renderer.getCurrentItemStack()) : null;
        String identifier = renderer != null ? animation != null ? animation : animatable.getRegistryName() : animatable.getRegistryName();

        if (animatable instanceof BrutalityBlockItem) {
            return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/block/" + animatable.getRegistryName() + ".animation.json");
        }

        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/item/" + getCategoryAsString(animatable) + "/" +
                identifier + "_handheld.animation.json");

    }

    private String getCategoryAsString(T animatable) {
        return ItemCategoryUtils.getCategory(animatable.getDefaultInstance()).toString().toLowerCase(Locale.ROOT);
    }


}