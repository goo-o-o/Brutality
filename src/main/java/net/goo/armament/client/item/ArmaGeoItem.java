package net.goo.armament.client.item;

import net.goo.armament.item.ModItemCategories;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
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

import static net.goo.armament.util.ModResources.*;

public interface ArmaGeoItem extends GeoItem {

    String geoIdentifier();

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return model(stack);
    }

    GeoAnimatable cacheItem();

    default ResourceLocation getFontFromCategory(ModItemCategories category) {
        return switch (category) {
            case SILLY -> SILLY; // Make sure SILLY is defined somewhere
            case SPACE -> SPACE; // Make sure SPACE is defined somewhere
            case TECHNOLOGY -> TECHNOLOGY; // Make sure TECHNOLOGY is defined somewhere
            case FANTASY -> FANTASY; // Make sure FANTASY is defined somewhere
        };
    }

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
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
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
