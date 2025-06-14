package net.goo.brutality.client.renderers.entity.fullbright;

import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityEntityRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class BrutalityFullbrightNoDepthEntityRenderer<T extends Entity & ArmaGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityFullbrightNoDepthEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(this));
    }


}
