package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.QuantumDrillItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class QuantumDrillItemModel extends DefaultedItemGeoModel<QuantumDrillItem> {
    public QuantumDrillItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "quantum_drill_handheld"));
    }


}
