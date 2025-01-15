package net.goo.armament.item.custom.client;

import net.goo.armament.item.custom.SupernovaSwordItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SupernovaSwordItemRenderer extends GeoItemRenderer<SupernovaSwordItem> {
    public SupernovaSwordItemRenderer() {
        super(new SupernovaSwordItemModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
