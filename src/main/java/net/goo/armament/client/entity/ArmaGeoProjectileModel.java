package net.goo.armament.client.entity;

import net.goo.armament.Armament;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ArmaGeoProjectileModel<T extends Entity & ArmaGeoEntity> extends GeoModel<T> {
    public GeoEntityRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Armament.prefix("geo/entity/projectile/" + (renderer != null ? animatable.model(renderer.getAnimatable()) : animatable.geoIdentifier()) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Armament.prefix("textures/entity/projectile/" + (renderer != null ? animatable.texture(renderer.getAnimatable()) : animatable.geoIdentifier()) + ".png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return Armament.prefix("animations/entity/projectile/" + (renderer != null ? animatable.texture(renderer.getAnimatable()) : animatable.geoIdentifier()) + ".animation.json");

    }

}