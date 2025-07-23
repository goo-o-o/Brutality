package net.goo.brutality.mixin;

//@Mixin(LivingEntityRenderer.class)
//public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
//
//    @Shadow
//    public abstract boolean addLayer(RenderLayer<T, M> pLayer);
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void onInit(EntityRendererProvider.Context context, EntityModel model, float shadowRadius, CallbackInfo ci) {
//        LivingEntityRenderer<T, M> renderer = (LivingEntityRenderer<T, M>) (Object) this;
//
//        this.addLayer(new StarLayer<>(context, renderer));
//        this.addLayer(new EyeOfViolenceLayer<>(renderer));
//
//        if (((Object) this) instanceof PlayerRenderer) {
//            this.addLayer(new EnragedOverlayLayer<>(renderer));
//        }
//    }
//}