package net.goo.brutality.client.renderers.armor;

import net.goo.brutality.client.renderers.layers.BrutalityArmorLayer;
import net.goo.brutality.client.models.BrutalityArmorModel;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BrutalityArmorRenderer<T extends ArmorItem & BrutalityGeoItem> extends GeoArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public BrutalityArmorRenderer() {
        super(new BrutalityArmorModel<>());
        ((BrutalityArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new BrutalityArmorLayer<>(this));
    }

}
