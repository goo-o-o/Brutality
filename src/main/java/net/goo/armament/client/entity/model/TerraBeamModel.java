package net.goo.armament.client.entity.model;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.TerraBeamEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TerraBeamModel extends DefaultedEntityGeoModel<TerraBeamEntity> {
    public TerraBeamModel() {
        super(new ResourceLocation(Armament.MOD_ID, "terra_beam_entity"));
    }

}
