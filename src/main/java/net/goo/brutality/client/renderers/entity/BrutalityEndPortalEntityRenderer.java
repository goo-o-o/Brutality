package net.goo.brutality.client.renderers.entity;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.client.entity.BrutalityGeoEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class BrutalityEndPortalEntityRenderer<T extends Entity & BrutalityGeoEntity> extends GeoEntityRenderer<T> {
    public BrutalityEndPortalEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BrutalityGeoEntityModel<>());
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.endPortal();
    }
}