package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.unused.ResonancePickaxeItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ResonancePickaxeItemModel extends DefaultedItemGeoModel<ResonancePickaxeItem> {
    public ResonancePickaxeItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "resonance_pickaxe_handheld"));
    }

    @Override
    public RenderType getRenderType(ResonancePickaxeItem animatable, ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(texture);
    }
}
