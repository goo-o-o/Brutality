package net.goo.armament.client.event.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.ShadowstepSwordItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ShadowstepSwordItemModel extends DefaultedItemGeoModel<ShadowstepSwordItem> {
    public ShadowstepSwordItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "shadowstep_handheld"));
    }

}
