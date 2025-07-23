package net.goo.brutality.client.renderers.entity;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityEmissiveShurikenRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityShurikenRenderer<T> {
    public BrutalityEmissiveShurikenRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }
}
