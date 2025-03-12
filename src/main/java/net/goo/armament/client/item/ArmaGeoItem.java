package net.goo.armament.client.item;

import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.function.Consumer;

public interface ArmaGeoItem extends GeoItem, ModResources {

    String geoIdentifier();

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return model(stack);
    }

    GeoAnimatable cacheItem();

    default Component armaNameHandler(Level pLevel, int[][] colors, ModItemCategories category, String identifier) {
        return ModUtils.ModTooltipHelper.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 2, colors);
    }

    default void armaHoverTextHandler(ItemStack pStack, List<Component> pTooltipComponents, int abilityCount, int[][] colors, Rarity rarity, String identifier) {
        pTooltipComponents.add(Component.translatable("rarity.armament." + rarity).withStyle(Style.EMPTY.withFont(ModResources.RARITY)));
        pTooltipComponents.add(ModUtils.ModTooltipHelper.tooltipHelper("item.armament." + identifier + ".lore", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));

        for (int i = 1; i <= abilityCount; i++) {
            pTooltipComponents.add(ModUtils.ModTooltipHelper.tooltipHelper("item.armament." + identifier + ".ability.name." + i, false, null, colors[0]));
            pTooltipComponents.add(ModUtils.ModTooltipHelper.tooltipHelper("item.armament." + identifier + ".ability.desc." + i, false, null, colors[1]));
            if (i != abilityCount) pTooltipComponents.add(Component.literal(""));
        }
        if (pStack.isEnchanted()) pTooltipComponents.add(Component.literal(""));
    }

    default ResourceLocation getFontFromCategory(ModItemCategories category) {
        return switch (category) {
            case SILLY -> SILLY;
            case SPACE -> SPACE;
            case TECHNOLOGY -> TECHNOLOGY;
            case FANTASY -> FANTASY;
        };
    }

    @OnlyIn(Dist.CLIENT)
    default <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
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
}
