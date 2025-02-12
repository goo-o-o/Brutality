package net.goo.armament.item;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import javax.annotation.Nullable;

public class ArmaGeoGlowingWeaponRenderer<T extends Item & ArmaGeoItem> extends ArmaGeoWeaponRenderer<T> {

    public ArmaGeoGlowingWeaponRenderer() {
        super();
        ((ArmaGeoWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaGeoWeaponLayer<>(this));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }


    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

}