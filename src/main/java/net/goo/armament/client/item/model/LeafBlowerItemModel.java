package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.LeafBlowerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class LeafBlowerItemModel extends DefaultedItemGeoModel<LeafBlowerItem> {
    public LeafBlowerItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "leaf_blower_handheld"));
    }

}
