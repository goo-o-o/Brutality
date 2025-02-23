package net.goo.armament.client.entity.model;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.entity.ArmaGeoProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ArmaGeoProjectileRenderer<T extends Entity & ArmaGeoEntity> extends GeoEntityRenderer<T> {

    public ArmaGeoProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArmaGeoProjectileModel<>());
        ((ArmaGeoProjectileModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaGeoEntityLayer<>(this, false));
    }

    public String getIdentifier() {
        return getAnimatable().geoIdentifier();
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return super.getTextureLocation(animatable);
    }


}
