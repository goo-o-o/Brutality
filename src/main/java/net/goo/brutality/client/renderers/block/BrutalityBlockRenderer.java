//package net.goo.brutality.client.renderers.block;
//
//import net.goo.brutality.block.BrutalityGeoBlockEntity;
//import net.goo.brutality.client.models.BrutalityBlockModel;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import software.bernie.geckolib.renderer.GeoBlockRenderer;
//
//import javax.annotation.Nullable;
//
//public class BrutalityBlockRenderer<T extends BlockEntity & BrutalityGeoBlockEntity> extends GeoBlockRenderer<T> {
//
//    public BrutalityBlockRenderer() {
//        super(new BrutalityBlockModel<>());
//        ((BrutalityBlockModel<T>)getGeoModel()).renderer = this;
//    }
//
//    public BrutalityBlockRenderer(BlockEntityRendererProvider.Context context) {
//        super(new BrutalityBlockModel<>());
//        ((BrutalityBlockModel<T>)getGeoModel()).renderer = this;
//    }
//
//
//    @Override
//    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
//        return RenderType.entityCutoutNoCullZOffset(texture);
//    }
//
//
//}