package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.JackpotHammerItem;
import net.goo.armament.item.custom.client.model.JackpotHammerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class JackpotHammerItemRenderer extends GeoItemRenderer<JackpotHammerItem> {
    public JackpotHammerItemRenderer() {
        super(new JackpotHammerItemModel());
    }
}