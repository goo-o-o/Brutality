package net.goo.brutality.client.entity;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrutalityGeoEntityModel<T extends Entity & ArmaGeoEntity> extends GeoModel<T> {
    public GeoEntityRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Brutality.prefix("geo/entity/" + animatable.model() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Brutality.prefix("textures/entity/" + animatable.texture() + ".png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Brutality.prefix("animations/entity/" + animatable.geoIdentifier() + ".animation.json");

    }

}