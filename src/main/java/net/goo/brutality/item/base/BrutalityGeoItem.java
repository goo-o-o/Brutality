package net.goo.brutality.item.base;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.function.Consumer;

public interface BrutalityGeoItem extends GeoItem, ModResources {
    String geoIdentifier();
    BrutalityItemCategories category();

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return model(stack);
    }

    default BrutalityItemCategories getCategory() {
        return category();
    }

    default String getCategoryAsString() {
        return getCategory().name().toLowerCase();
    }

    default float shadowSize() {
        return 0.15F;
    }

    GeoAnimatable cacheItem();

    default Component brutalityNameHandler(ItemStack stack, String identifier) {
        Rarity rarity = stack.getRarity();
        if (rarity.equals(Rarity.UNCOMMON) || rarity.equals(Rarity.COMMON) || rarity.equals(Rarity.EPIC))
            return Component.translatable("item." + Brutality.MOD_ID + "." + identifier);
        else
            return BrutalityTooltipHelper.getRarityName("item." + Brutality.MOD_ID + "." + identifier, rarity);
    }


    default void brutalityHoverTextHandler(ItemStack pStack, List<Component> pTooltipComponents, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents, Rarity rarity, String identifier) {
        if (!ModList.get().isLoaded("obscuria_tooltips"))
            pTooltipComponents.add(Component.translatable("rarity." + rarity + ".name").withStyle(Style.EMPTY.withFont(ModResources.RARITY_FONT)));

        if (descriptionComponents.contains(BrutalityTooltipHelper.DescriptionComponents.LORE))
            pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + "lore"));

        for (BrutalityTooltipHelper.DescriptionComponent descriptionComponent : descriptionComponents) {
            boolean isLore = descriptionComponent.type().equals(BrutalityTooltipHelper.DescriptionComponents.LORE);

            if (!isLore) {
                pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type." + descriptionComponent.type().toString().toLowerCase()).withStyle(ChatFormatting.GOLD));

                for (int i = 1; i <= descriptionComponent.lines(); i++) {
                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + descriptionComponent.type().toString().toLowerCase() + "." + i));
                }
            } else {
                for (int i = 1; i <= descriptionComponent.lines(); i++) {
                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + descriptionComponent.type().toString().toLowerCase() + "." + i).withStyle(ChatFormatting.BLUE));
                }
            }

            pTooltipComponents.add(Component.empty());
        }

    }


    @OnlyIn(Dist.CLIENT)
    default <R extends BlockEntityWithoutLevelRenderer> void initGeo
            (Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        consumer.accept(new IClientItemExtensions() {
            private R renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    try {
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

    default void onDeselected(Player player) {
    }

    default void onDeselected(Player player, ItemStack stack) {
        onDeselected(player);
    }

}
