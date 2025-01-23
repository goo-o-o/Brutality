package net.goo.armament.item.custom.client.renderer;

import net.goo.armament.item.custom.TerratonHammerItem;
import net.goo.armament.item.custom.client.model.TerratonHammerItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TerratonHammerItemRenderer extends GeoItemRenderer<TerratonHammerItem> {
    public TerratonHammerItemRenderer() {
        super(new TerratonHammerItemModel());
    }
}
