package net.goo.armament.client.renderers.item;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.item.ArmaWeaponLayer;
import net.goo.armament.client.item.ArmaWeaponModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import javax.annotation.Nullable;

public class ArmaWeaponRenderer<T extends Item & ArmaGeoItem> extends GeoItemRenderer<T> {

    public ArmaWeaponRenderer() {
        super(new ArmaWeaponModel<>());
        ((ArmaWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaWeaponLayer<>(this));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}