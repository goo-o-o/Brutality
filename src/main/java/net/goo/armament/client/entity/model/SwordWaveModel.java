package net.goo.armament.client.entity.model;

import net.goo.armament.Armament;
import net.goo.armament.entity.base.SwordWave;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SwordWaveModel extends DefaultedEntityGeoModel<SwordWave> {
    public SwordWaveModel() {
        super(new ResourceLocation(Armament.MOD_ID, "sword_wave"));
    }
}
