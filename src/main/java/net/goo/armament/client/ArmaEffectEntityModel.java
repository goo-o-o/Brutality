package net.goo.armament.client;


import net.goo.armament.Armament;
import net.goo.armament.entity.ArmaEffectEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ArmaEffectEntityModel extends GeoModel<ArmaEffectEntity> {

    @Override
    public ResourceLocation getModelResource(ArmaEffectEntity animatable) {
        return Armament.prefix("geo/" + animatable.getVisualType().getModel().getModelName() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArmaEffectEntity animatable) {
        return animatable.getVisualType().getFrames() > 0 ?
                Armament.prefix("textures/entity/visuals/" + animatable.getVisualType().getTexture() + "/" + animatable.getVisualType().getTexture() + animatable.getFrameLevel() + ".png") :
                Armament.prefix("textures/entity/visuals/" + animatable.getVisualType().getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ArmaEffectEntity animatable) {
        return Armament.prefix("animations/cs_effect.animation.json");
    }
}