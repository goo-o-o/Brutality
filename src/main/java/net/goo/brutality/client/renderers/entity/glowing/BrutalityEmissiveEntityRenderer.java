package net.goo.brutality.client.renderers.entity.glowing;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityGlowingEntityRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityGlowingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}
