package net.goo.armament.client.renderers.entity;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ArmaGlowingEntityRenderer<T extends Entity & ArmaGeoEntity> extends ArmaEntityRenderer<T> {
    public ArmaGlowingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}
