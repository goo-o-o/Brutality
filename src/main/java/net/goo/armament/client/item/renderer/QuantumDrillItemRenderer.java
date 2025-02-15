package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.unused.QuantumDrillItem;
import net.goo.armament.client.item.model.QuantumDrillItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class QuantumDrillItemRenderer extends GeoItemRenderer<QuantumDrillItem> {
    public QuantumDrillItemRenderer() {
        super(new QuantumDrillItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}