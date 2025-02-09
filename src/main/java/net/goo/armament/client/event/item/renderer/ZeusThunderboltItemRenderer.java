package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.ZeusThunderboltItem;
import net.goo.armament.client.event.item.model.ZeusThunderboltItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ZeusThunderboltItemRenderer extends GeoItemRenderer<ZeusThunderboltItem> {
    public ZeusThunderboltItemRenderer() {
        super(new ZeusThunderboltItemModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }


}
