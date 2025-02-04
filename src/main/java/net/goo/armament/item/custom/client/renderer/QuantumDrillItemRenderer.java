package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.QuantumDrillItem;
import net.goo.armament.item.custom.client.model.QuantumDrillItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class QuantumDrillItemRenderer extends GeoItemRenderer<QuantumDrillItem> {
    public QuantumDrillItemRenderer() {
        super(new QuantumDrillItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}