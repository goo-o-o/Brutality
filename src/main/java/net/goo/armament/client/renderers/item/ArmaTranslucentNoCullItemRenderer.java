package net.goo.armament.client.renderers.item;

import net.goo.armament.client.ArmaRenderTypes;
import net.goo.armament.client.models.weapon.ArmaWeaponLayer;
import net.goo.armament.client.models.weapon.ArmaWeaponModel;
import net.goo.armament.item.base.ArmaGeoItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public class ArmaTranslucentNoCullItemRenderer<T extends Item & ArmaGeoItem> extends ArmaItemRenderer<T> {

    public ArmaTranslucentNoCullItemRenderer() {
        super();
        ((ArmaWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new ArmaWeaponLayer<>(this));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return ArmaRenderTypes.getEntityTranslucentNoCull(texture);
    }
}