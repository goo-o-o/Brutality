package net.goo.brutality.client.entity.model;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.base.SwordBeam;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SwordBeamModel extends DefaultedEntityGeoModel<SwordBeam> {
    public SwordBeamModel() {
        super(new ResourceLocation(Brutality.MOD_ID, "sword_beam"));
    }
}
