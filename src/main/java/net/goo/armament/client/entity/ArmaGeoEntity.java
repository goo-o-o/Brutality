package net.goo.armament.client.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.function.Consumer;

public interface ArmaGeoEntity extends GeoEntity {

    String geoIdentifier();

    default String model(Entity stack) {
        return geoIdentifier();
    }

    default String texture(Entity stack) {
        return model(stack);
    }

    GeoAnimatable cacheItem();

    @OnlyIn(Dist.CLIENT)
    default <T extends Entity & ArmaGeoEntity, R extends GeoEntityRenderer<T>> void initGeo(Consumer<EntityRendererProvider<T>> consumer, Class<R> rendererClass) {
        consumer.accept(context -> {
            try {
                // Create a new instance of the renderer using the provided class
                return rendererClass.getDeclaredConstructor(EntityRendererProvider.Context.class).newInstance(context);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate renderer: " + rendererClass.getSimpleName(), e);
            }
        });
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }


}
