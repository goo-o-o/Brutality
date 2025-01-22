package net.goo.armament.item.custom.client;

import net.goo.armament.item.custom.TerratonHammerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TerratonHammerItemRenderer extends GeoItemRenderer<TerratonHammerItem> {
    public TerratonHammerItemRenderer() {
        super(new TerratonHammerItemModel());
    }
}
