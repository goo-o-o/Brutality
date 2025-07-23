package net.goo.brutality.item.base;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public interface BrutalityGeoItem extends GeoItem, ModResources {
    default String getRegistryName() {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey((Item) this)).getPath().toLowerCase(Locale.ROOT);
    }

    BrutalityCategories category();

    default String model(ItemStack stack) {
        return null;
    }

    default String texture(ItemStack stack) {
        return null;
    }

    default String getCategoryAsString() {
        return category().toString().toLowerCase(Locale.ROOT);
    }


    default float shadowSize() {
        return 0.15F;
    }

    GeoAnimatable cacheItem();

    default Component brutalityNameHandler(ItemStack stack) {
        Rarity rarity = stack.getRarity();
        String identifier = getRegistryName();
        if (rarity.equals(Rarity.UNCOMMON) || rarity.equals(Rarity.COMMON) || rarity.equals(Rarity.RARE) || rarity.equals(Rarity.EPIC))
            return Component.translatable("item." + Brutality.MOD_ID + "." + identifier);
        else
            return BrutalityTooltipHelper.getRarityName("item." + Brutality.MOD_ID + "." + identifier, rarity);
    }


    default void brutalityHoverTextHandler(List<Component> pTooltipComponents, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents, Rarity rarity) {
        String identifier = getRegistryName();

        if (!ModList.get().isLoaded("obscuria_tooltips"))
            pTooltipComponents.add(Component.translatable("rarity." + rarity.toString().toLowerCase(Locale.ROOT) + ".name").withStyle(Style.EMPTY.withFont(ModResources.RARITY_FONT)));

        if (descriptionComponents.contains(BrutalityTooltipHelper.DescriptionComponents.LORE))
            pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + "lore"));

        for (BrutalityTooltipHelper.DescriptionComponent descriptionComponent : descriptionComponents) {
            boolean isLore = descriptionComponent.type().equals(BrutalityTooltipHelper.DescriptionComponents.LORE);

            String componentLower = descriptionComponent.type().toString().toLowerCase(Locale.ROOT);
            if (!isLore) {
                pTooltipComponents.add(Component.translatable(
                        Brutality.MOD_ID + ".description.type." + componentLower));

                for (int i = 1; i <= descriptionComponent.lines(); i++) {
                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + componentLower + "." + i));
                }
            } else {
                for (int i = 1; i <= descriptionComponent.lines(); i++) {
                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + componentLower + "." + i).withStyle(ChatFormatting.BLUE));
                }
            }

            if (!descriptionComponent.equals(descriptionComponents.get(descriptionComponents.size() - 1)))
                pTooltipComponents.add(Component.empty());
        }

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
