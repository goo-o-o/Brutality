package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.EventHorizonLanceItem;
import net.goo.armament.item.custom.client.model.EventHorizonLanceItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class EventHorizonLanceItemRenderer extends GeoItemRenderer<EventHorizonLanceItem> {
    public EventHorizonLanceItemRenderer() {
        super(new EventHorizonLanceItemModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
