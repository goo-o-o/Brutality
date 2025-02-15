package net.goo.armament.client.item.renderer;

import net.goo.armament.client.item.model.ResonancePickaxeItemModel;
import net.goo.armament.item.custom.unused.ResonancePickaxeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ResonancePickaxeItemRenderer extends GeoItemRenderer<ResonancePickaxeItem> {
    public ResonancePickaxeItemRenderer() {
        super(new ResonancePickaxeItemModel());
        this.model.getRenderType(animatable, getTextureLocation(animatable));
    }
}
