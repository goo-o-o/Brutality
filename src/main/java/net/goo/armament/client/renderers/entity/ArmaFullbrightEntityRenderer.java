package net.goo.armament.client.renderers.entity;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.renderers.ArmaAutoFullbrightLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class ArmaFullbrightEntityRenderer<T extends Entity & ArmaGeoEntity> extends ArmaEntityRenderer<T> {
    public ArmaFullbrightEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new ArmaAutoFullbrightLayer(this));
    }


}
