package net.goo.brutality.client.models;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.BrutalityGeoBlockEntity;
import net.goo.brutality.client.renderers.block.BrutalityBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class BrutalityBlockModel<T extends BlockEntity & BrutalityGeoBlockEntity> extends GeoModel<T> {
    public BrutalityBlockRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        String model = animatable.model(animatable.getBlockState().getBlock());
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/block/" + (model != null ? model : animatable.getRegistryName()) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        String texture = animatable.texture(animatable.getBlockState().getBlock());
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/block/" + (texture != null ? texture : animatable.getRegistryName()) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/block/" + animatable.getRegistryName() + ".animation.json");
    }



}