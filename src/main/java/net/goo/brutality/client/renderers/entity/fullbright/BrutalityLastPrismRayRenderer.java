package net.goo.brutality.client.renderers.entity.fullbright;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class BrutalityFullbrightRGBRayRendererNoDepth<T extends Entity & BrutalityGeoEntity> extends BrutalityFullbrightRayRendererNoDepth<T> {
    public BrutalityFullbrightRGBRayRendererNoDepth(EntityRendererProvider.Context context) {
        super(context);
    }
}
