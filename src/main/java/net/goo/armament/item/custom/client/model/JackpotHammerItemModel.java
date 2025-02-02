package net.goo.armament.item.custom.client.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.JackpotHammerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class JackpotHammerItemModel extends DefaultedItemGeoModel<JackpotHammerItem> {
    public JackpotHammerItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "jackpot_handheld"));
    }

}
