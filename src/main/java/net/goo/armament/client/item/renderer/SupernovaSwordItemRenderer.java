package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.SupernovaSwordItem;
import net.goo.armament.client.item.model.SupernovaSwordItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SupernovaSwordItemRenderer extends GeoItemRenderer<SupernovaSwordItem> {
    public SupernovaSwordItemRenderer() {
        super(new SupernovaSwordItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
