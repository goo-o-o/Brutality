package net.goo.armament.item.custom.client.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.SupernovaSwordItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class SupernovaSwordItemModel extends DefaultedItemGeoModel<SupernovaSwordItem> {
    public SupernovaSwordItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "supernova"));
    }
}
