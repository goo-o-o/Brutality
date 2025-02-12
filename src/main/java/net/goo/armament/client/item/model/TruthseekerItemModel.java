package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.TruthseekerSword;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TruthseekerItemModel extends DefaultedItemGeoModel<TruthseekerSword> {
    public TruthseekerItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "truthseeker_handheld"));
    }

}
