//package net.goo.brutality.client.renderers.entity.fullbright;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.client.renderers.entity.BrutalityArrowRenderer;
//import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightLayer;
//import net.goo.brutality.entity.base.BrutalityArrow;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//public class BrutalityFullbrightArrowRenderer<T extends BrutalityArrow & BrutalityGeoEntity> extends BrutalityArrowRenderer<T> {
//    public BrutalityFullbrightArrowRenderer(EntityRendererProvider.Context context) {
//        super(context);
//        this.addRenderLayer(new BrutalityAutoFullbrightLayer<>(this));
//    }
//
//}
