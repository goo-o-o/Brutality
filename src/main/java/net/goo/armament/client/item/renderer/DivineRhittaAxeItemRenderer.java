package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.DivineRhittaAxeItem;
import net.goo.armament.client.item.model.DivineRhittaAxeItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DivineRhittaAxeItemRenderer extends GeoItemRenderer<DivineRhittaAxeItem> {
    public DivineRhittaAxeItemRenderer() {
        super(new DivineRhittaAxeItemModel());
    }
}
