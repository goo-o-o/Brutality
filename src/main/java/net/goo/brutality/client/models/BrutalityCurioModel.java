package net.goo.brutality.client.models;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.curio.BrutalityCurioRenderer;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BrutalityCurioModel<T extends BrutalityCurioItem> extends GeoModel<T> {
    public BrutalityCurioRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(BrutalityCurioItem animatable) {


        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/item/curio/" +
                (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrutalityCurioItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/item/curio/" + animatable + "/" +
                (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable) + ".png");
    }


    public ResourceLocation getAnimationResource(BrutalityCurioItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/item/curio/" +
                (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable) + ".animation.json");

    }

}