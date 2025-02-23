package net.goo.armament.client.entity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import javax.annotation.Nullable;

public class ArmaGeoGlowingEntityRenderer<T extends Entity & ArmaGeoEntity> extends GeoEntityRenderer<T> {
    public ArmaGeoGlowingEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, new ArmaGeoEntityModel<>());
        ((ArmaGeoEntityModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new GeoRenderLayer<T>(this) {
            @Override
            public GeoModel<T> getGeoModel() {
                return super.getGeoModel();
            }
        });
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
