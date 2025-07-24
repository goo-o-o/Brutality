//package net.goo.brutality.client.renderers.entity.fullbright;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.client.renderers.entity.BrutalityEntityRenderer;
//import net.goo.brutality.client.renderers.layers.BrutalityAutoFullbrightNoDepthLayer;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//public class BrutalityFullbrightNoDepthEntityRenderer<T extends Entity & BrutalityGeoEntity> extends BrutalityEntityRenderer<T> {
//    public BrutalityFullbrightNoDepthEntityRenderer(EntityRendererProvider.Context context) {
//        super(context);
//        this.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(this));
//    }
//
//
//}
