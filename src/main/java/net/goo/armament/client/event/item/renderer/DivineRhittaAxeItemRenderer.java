package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.DivineRhittaAxeItem;
import net.goo.armament.client.event.item.model.DivineRhittaAxeItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DivineRhittaAxeItemRenderer extends GeoItemRenderer<DivineRhittaAxeItem> {
    public DivineRhittaAxeItemRenderer() {
        super(new DivineRhittaAxeItemModel());
    }
}
