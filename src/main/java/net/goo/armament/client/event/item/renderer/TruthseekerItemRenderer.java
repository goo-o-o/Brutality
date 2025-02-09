package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.TruthseekerSwordItem;
import net.goo.armament.client.event.item.model.TruthseekerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class TruthseekerItemRenderer extends GeoItemRenderer<TruthseekerSwordItem> {
    public TruthseekerItemRenderer() {
        super(new TruthseekerItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
