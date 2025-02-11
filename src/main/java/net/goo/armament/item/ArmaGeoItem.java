package net.goo.armament.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public interface ArmaGeoItem extends GeoItem {

    String geoIdentifier();

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return geoIdentifier();
    }

    GeoAnimatable cacheItem();

    @OnlyIn(Dist.CLIENT)
    default HumanoidModel.ArmPose getArmPose() {
        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    default AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(cacheItem());
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("animation.weapon.none")))
        );
    }


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

                @Override
                public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                    return ArmaGeoItem.this.getArmPose();
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

                @Override
                public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                    return ArmaGeoItem.this.getArmPose();
                }
            });
        }
    }
}
