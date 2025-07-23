package net.goo.brutality.client.renderers.entity.fullbright;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityEntityRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightAlphaLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BrutalityFullbrightAlphaEntityRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
    public BrutalityFullbrightAlphaEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new BrutalityAutoFullbrightAlphaLayer<>(this));
    }


}
