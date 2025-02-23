package net.goo.armament.client.entity;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.function.Consumer;

public interface ArmaGeoEntity extends GeoEntity {

    String geoIdentifier();

    default String model(Entity entity) {
        return geoIdentifier();
    }

    default String texture(Entity entity) {
        return model(entity);
    }

    GeoAnimatable cacheItem();

    @OnlyIn(Dist.CLIENT)
    default <T extends Entity & ArmaGeoEntity, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        consumer.accept(new IClientItemExtensions() {
            private R renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    try {
                        // Create a new instance of the renderer class using reflection
                        this.renderer = rendererClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate renderer: " + rendererClass.getSimpleName(), e);
                    }
                }
                return this.renderer;
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
