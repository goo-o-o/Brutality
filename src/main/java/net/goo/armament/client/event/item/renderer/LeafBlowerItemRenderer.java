package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.LeafBlowerItem;
import net.goo.armament.client.event.item.model.LeafBlowerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LeafBlowerItemRenderer extends GeoItemRenderer<LeafBlowerItem> {
    public LeafBlowerItemRenderer() {
        super(new LeafBlowerItemModel());
    }
}