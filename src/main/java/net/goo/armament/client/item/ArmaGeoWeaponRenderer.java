package net.goo.armament.client.item;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.Nullable;

public class ArmaGeoWeaponRenderer<T extends Item & ArmaGeoItem> extends GeoItemRenderer<T> {

    public ArmaGeoWeaponRenderer() {
        super(new ArmaGeoWeaponModel<>());
        ((ArmaGeoWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaGeoWeaponLayer<>(this, false));
    }


    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

}