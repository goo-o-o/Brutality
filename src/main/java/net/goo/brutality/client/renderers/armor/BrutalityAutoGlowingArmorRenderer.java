package net.goo.brutality.client.renderers.armor;

import net.goo.brutality.client.models.BrutalityArmorModel;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BrutalityAutoGlowingArmorRenderer<T extends ArmorItem & BrutalityGeoItem> extends BrutalityArmorRenderer<T> {
    public GeoArmorRenderer<T> renderer;

    public BrutalityAutoGlowingArmorRenderer() {
        super();
        ((BrutalityArmorModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}
