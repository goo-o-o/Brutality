//package net.goo.brutality.client.entity;
//
//import software.bernie.geckolib.animatable.GeoEntity;
//import software.bernie.geckolib.core.animatable.GeoAnimatable;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//import software.bernie.geckolib.core.animation.AnimationController;
//import software.bernie.geckolib.core.animation.RawAnimation;
//public interface BrutalityGeoEntity extends GeoEntity {
//
//    default String model() {
//        return null;
//    }
//
//    default String texture() {
//        return null;
//    }
//
//
//    GeoAnimatable cacheItem();
//
////    @OnlyIn(Dist.CLIENT)
////    default <T extends Entity & BrutalityGeoEntity, R extends GeoEntityRenderer<T>> void initGeo(Consumer<EntityRendererProvider<T>> consumer, Class<R> rendererClass) {
////        consumer.accept(context -> {
////            try {
////                return rendererClass.getDeclaredConstructor(EntityRendererProvider.Context.class).newInstance(context);
////            } catch (Exception e) {
////                throw new RuntimeException("Failed to instantiate renderer: " + rendererClass.getSimpleName(), e);
////            }
////        });
////    }
//
//    @Override
//    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, (state) ->
//                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
//        );
//    }
//
//
//}
