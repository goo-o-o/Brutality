package net.goo.brutality.mixin;

//@Mixin(ItemEntityRenderer.class)
//public abstract class ItemEntityRendererMixin {
//
//    @Inject(
//            method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
//            at = @At("HEAD")
//    )
//    private void brutality$beforeRender(ItemEntity entity, float entityYaw, float partialTicks,
//                                        PoseStack poseStack, MultiBufferSource buffer, int packedLight,
//                                        CallbackInfo ci) {
//        if (!entity.getItem().isEmpty() && entity.getItem().getItem() instanceof BrutalityGeoItem brutalityGeoItem) {
//            ((EntityRendererAccessor)this).setShadowRadius(brutalityGeoItem.shadowSize());
//        }
//    }
//}