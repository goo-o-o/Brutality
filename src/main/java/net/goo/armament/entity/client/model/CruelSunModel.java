package net.goo.armament.entity.client.model;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.CruelSunEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CruelSunModel extends DefaultedEntityGeoModel<CruelSunEntity> {
    public CruelSunModel() {
        super(ResourceLocation.fromNamespaceAndPath(Armament.MOD_ID, "cruel_sun"));
    }
}
