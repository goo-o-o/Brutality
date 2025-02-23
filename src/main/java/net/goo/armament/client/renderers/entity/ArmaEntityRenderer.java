package net.goo.armament.client.renderers.entity;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.entity.ArmaGeoEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class ArmaEntityRenderer<T extends Entity & ArmaGeoEntity> extends GeoEntityRenderer<T> {
    public ArmaEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ArmaGeoEntityModel<>());

    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentCull(texture);
    }
}
