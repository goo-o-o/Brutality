package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.SeekerOfKnowledgeSwordItem;
import net.goo.armament.item.custom.client.model.SeekerOfKnowledgeSwordItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SeekerOfKnowledgeSwordItemRenderer extends GeoItemRenderer<SeekerOfKnowledgeSwordItem> {
    public SeekerOfKnowledgeSwordItemRenderer() {
        super(new SeekerOfKnowledgeSwordItemModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
