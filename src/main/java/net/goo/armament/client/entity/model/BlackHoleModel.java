package net.goo.armament.client.entity.model;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.BlackHole;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BlackHoleModel extends DefaultedEntityGeoModel<BlackHole> {
    public BlackHoleModel() {
        super(new ResourceLocation(Armament.MOD_ID, "black_hole_entity"));
    }


    @Override
    public RenderType getRenderType(BlackHole animatable, ResourceLocation texture) {
        return RenderType.entityTranslucentCull(texture);
    }
}
