package net.goo.armament.client.item.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.ZeusThunderboltItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ZeusThunderboltItemModel extends DefaultedItemGeoModel<ZeusThunderboltItem> {
    public ZeusThunderboltItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "zeus_thunderbolt_handheld"));
    }
}
