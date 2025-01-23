package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.DivineRhittaAxeItem;
import net.goo.armament.item.custom.client.model.DivineRhittaAxeItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DivineRhittaAxeItemRenderer extends GeoItemRenderer<DivineRhittaAxeItem> {
    public DivineRhittaAxeItemRenderer() {
        super(new DivineRhittaAxeItemModel());
    }
}
