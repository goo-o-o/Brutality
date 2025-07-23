package net.goo.brutality.client.renderers.entity.glowing;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityAbstractPhysicsProjectileRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityAbstractEmissivePhysicsProjectileRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityAbstractPhysicsProjectileRenderer<T> {
    public BrutalityAbstractEmissivePhysicsProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
