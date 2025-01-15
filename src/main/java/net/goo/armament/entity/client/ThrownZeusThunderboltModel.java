package net.goo.armament.entity.client;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.ThrownZeusThunderbolt;
import net.minecraft.resources.ResourceLocation;
// Import GeckoLib classes
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ThrownZeusThunderboltModel extends DefaultedEntityGeoModel<ThrownZeusThunderbolt> {

    public ThrownZeusThunderboltModel() {
        super(new ResourceLocation(Armament.MOD_ID, "thrown_zeus_thunderbolt")); // Replace with your model path and texture path
    }
}