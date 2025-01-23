package net.goo.armament.item.custom.client.model;

import net.goo.armament.Armament;
import net.goo.armament.item.custom.SeekerOfKnowledgeSwordItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class SeekerOfKnowledgeSwordItemModel extends DefaultedItemGeoModel<SeekerOfKnowledgeSwordItem> {
    public SeekerOfKnowledgeSwordItemModel() {
        super(new ResourceLocation(Armament.MOD_ID, "seeker_of_knowledge"));
    }

}
