package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.EventHorizonLanceItem;
import net.goo.armament.client.item.model.EventHorizonLanceItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class EventHorizonLanceItemRenderer extends GeoItemRenderer<EventHorizonLanceItem> {
    public EventHorizonLanceItemRenderer() {
        super(new EventHorizonLanceItemModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
