package net.goo.armament.client.entity;

import net.goo.armament.client.item.ArmaGeoGlowingWeaponRenderer;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.item.ArmaGeoWeaponRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return model(stack);
    }

    GeoAnimatable cacheItem();

    @OnlyIn(Dist.CLIENT)
    default <T extends Item & ArmaGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer, int rendererID) {
        if (rendererID == 0) {
            consumer.accept(new IClientItemExtensions() {
                private ArmaGeoWeaponRenderer<T> renderer;

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    if (this.renderer == null) this.renderer = new ArmaGeoWeaponRenderer<>();
                    return this.renderer;
                }

            });
        } else if (rendererID == 1) {
            consumer.accept(new IClientItemExtensions() {
                private ArmaGeoGlowingWeaponRenderer<T> renderer;

                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    if (this.renderer == null) this.renderer = new ArmaGeoGlowingWeaponRenderer<>();
                    return this.renderer;
                }

            });
        }
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }


}
