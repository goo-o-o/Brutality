package net.goo.brutality.client.renderers.entity.fullbright;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityArrowRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BrutalityFullbrightNoDepthArrowRenderer<T extends BrutalityArrow & BrutalityGeoEntity> extends BrutalityArrowRenderer<T> {
    public BrutalityFullbrightNoDepthArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(this));
    }

}
