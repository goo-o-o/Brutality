package net.goo.brutality.client.renderers.entity;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityAbstractGlowingPhysicsProjectileRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityAbstractPhysicsProjectileRenderer<T>{
    public BrutalityAbstractGlowingPhysicsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
