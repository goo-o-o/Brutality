package net.goo.armament.client.event.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.TerratonHammerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class TerratonHammerItemModel extends DefaultedItemGeoModel<TerratonHammerItem> {
    public TerratonHammerItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "terraton_hammer_handheld"));
    }

}
