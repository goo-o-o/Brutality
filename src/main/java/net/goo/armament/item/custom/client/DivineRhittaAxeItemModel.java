package net.goo.armament.item.custom.client;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.DivineRhittaAxeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class DivineRhittaAxeItemModel extends DefaultedItemGeoModel<DivineRhittaAxeItem> {
    public DivineRhittaAxeItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "divine_rhitta_axe"));
    }

}
