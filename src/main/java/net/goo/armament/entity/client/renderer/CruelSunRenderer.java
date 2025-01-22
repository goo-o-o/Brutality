package net.goo.armament.entity.client.renderer;

import net.goo.armament.entity.client.model.CruelSunModel;
import net.goo.armament.entity.custom.CruelSunEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class CruelSunRenderer extends GeoEntityRenderer<CruelSunEntity> {
    public CruelSunRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CruelSunModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
