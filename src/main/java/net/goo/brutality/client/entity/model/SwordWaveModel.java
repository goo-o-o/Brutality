package net.goo.brutality.client.entity.model;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.base.SwordWave;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SwordWaveModel extends DefaultedEntityGeoModel<SwordWave> {
    public SwordWaveModel() {
        super(new ResourceLocation(Brutality.MOD_ID, "sword_wave"));
    }
}
