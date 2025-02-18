package net.goo.armament.client.entity.model;

import net.goo.armament.Armament;
import net.goo.armament.entity.base.SwordBeam;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SwordBeamModel extends DefaultedEntityGeoModel<SwordBeam> {
    public SwordBeamModel() {
        super(new ResourceLocation(Armament.MOD_ID, "sword_beam"));
    }
}
