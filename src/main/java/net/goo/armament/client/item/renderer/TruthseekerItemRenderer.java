package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.TruthseekerSword;
import net.goo.armament.client.item.model.TruthseekerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class TruthseekerItemRenderer extends GeoItemRenderer<TruthseekerSword> {
    public TruthseekerItemRenderer() {
        super(new TruthseekerItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
