package net.goo.armament.client.item.renderer;

import net.goo.armament.item.custom.ShadowstepSwordItem;
import net.goo.armament.client.item.model.ShadowstepSwordItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ShadowstepSwordItemRenderer extends GeoItemRenderer<ShadowstepSwordItem> {
    public ShadowstepSwordItemRenderer() {
        super(new ShadowstepSwordItemModel());
    }
}