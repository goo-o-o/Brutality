package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.EventHorizonLanceItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class EventHorizonLanceItemModel extends DefaultedItemGeoModel<EventHorizonLanceItem> {
    public EventHorizonLanceItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "event_horizon_handheld"));
    }

}
