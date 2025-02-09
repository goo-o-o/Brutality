package net.goo.armament.client.event.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.DoomfistGauntletItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class DoomfistGauntletItemModel extends DefaultedItemGeoModel<DoomfistGauntletItem> {
    public DoomfistGauntletItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "doomfist_gauntlet_handheld"));
    }

}
