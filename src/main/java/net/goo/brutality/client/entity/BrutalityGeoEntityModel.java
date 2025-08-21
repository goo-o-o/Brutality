package net.goo.brutality.client.entity;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.projectile.ray.LastPrismRay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BrutalityGeoEntityModel<T extends Entity & BrutalityGeoEntity> extends GeoModel<T> {
    public GeoEntityRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "geo/entity/" + (animatable.model() != null ? animatable.model() : ForgeRegistries.ENTITY_TYPES.getKey(animatable.getType()).getPath()) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/entity/" + (animatable.texture() != null ? animatable.texture() : ForgeRegistries.ENTITY_TYPES.getKey(animatable.getType()).getPath()) + ".png");
    }


    public ResourceLocation getAnimationResource(T animatable) {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animations/entity/" + (animatable.animation() != null ? animatable.animation() :
                ForgeRegistries.ENTITY_TYPES.getKey(animatable.getType()).getPath()) + ".animation.json");

    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animatable instanceof LastPrismRay lastPrismRay) {
            CoreGeoBone mainBeam = this.getAnimationProcessor().getBone("main_beam");

            if (mainBeam != null) {
                mainBeam.setScaleY(lastPrismRay.getDataMaxLength());
            }
        }
    }

}