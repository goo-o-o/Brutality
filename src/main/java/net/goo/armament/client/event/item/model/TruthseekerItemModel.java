package net.goo.armament.client.event.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.TruthseekerSwordItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TruthseekerItemModel extends DefaultedItemGeoModel<TruthseekerSwordItem> {
    public TruthseekerItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "truthseeker_handheld"));
    }

}
