package net.goo.armament.client.item.model;

import net.goo.armament.item.custom.ResonancePickaxeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ResonancePickaxeItemRenderer extends GeoItemRenderer<ResonancePickaxeItem> {
    public ResonancePickaxeItemRenderer() {
        super(new ResonancePickaxeItemModel());
        this.model.getRenderType(animatable, getTextureLocation(animatable));
    }
}
