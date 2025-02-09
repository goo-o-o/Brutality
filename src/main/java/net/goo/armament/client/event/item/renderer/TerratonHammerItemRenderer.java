package net.goo.armament.client.event.item.renderer;

import net.goo.armament.item.custom.TerratonHammerItem;
import net.goo.armament.client.event.item.model.TerratonHammerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TerratonHammerItemRenderer extends GeoItemRenderer<TerratonHammerItem> {
    public TerratonHammerItemRenderer() {
        super(new TerratonHammerItemModel());
    }
}
