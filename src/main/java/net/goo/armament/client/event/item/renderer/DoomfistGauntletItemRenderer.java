package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.DoomfistGauntletItem;
import net.goo.armament.client.event.item.model.DoomfistGauntletItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DoomfistGauntletItemRenderer extends GeoItemRenderer<DoomfistGauntletItem> {
    public DoomfistGauntletItemRenderer() {
        super(new DoomfistGauntletItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
