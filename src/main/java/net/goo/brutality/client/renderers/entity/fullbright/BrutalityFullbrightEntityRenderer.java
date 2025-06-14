package net.goo.brutality.client.renderers.entity.fullbright;

import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityEntityRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class BrutalityFullbrightEntityRenderer<T extends Entity & ArmaGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityFullbrightEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new BrutalityAutoFullbrightLayer<>(this));
    }


}
