package net.goo.brutality.block;

import net.goo.brutality.client.renderers.block.BrutalityBlockRenderer;
import net.goo.brutality.client.renderers.layers.BrutalityBlockLayer;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.util.ModResources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Objects;

public interface BrutalityGeoBlockEntity extends GeoBlockEntity, ModResources {
    default String getRegistryName() {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey((Block) this)).getPath();
    }

    BrutalityCategories category();

    default String model(Block block) {
        return null;
    }

    default String texture(Block block) {
        return null;
    }

//
//    default Component brutalityNameHandler(ItemStack stack) {
//        Rarity rarity = stack.getRarity();
//        String identifier = getRegistryName();
//        if (rarity.equals(Rarity.UNCOMMON) || rarity.equals(Rarity.COMMON) || rarity.equals(Rarity.RARE) || rarity.equals(Rarity.EPIC))
//            return Component.translatable("item." + Brutality.MOD_ID + "." + identifier);
//        else
//            return BrutalityTooltipHelper.getRarityName("item." + Brutality.MOD_ID + "." + identifier, rarity);
//    }
//
//
//    default void brutalityHoverTextHandler(List<Component> pTooltipComponents, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents, Rarity rarity) {
//        String identifier = getRegistryName();
//
//        if (!ModList.get().isLoaded("obscuria_tooltips"))
//            pTooltipComponents.add(Component.translatable("rarity." + rarity.toString().toLowerCase() + ".name").withStyle(Style.EMPTY.withFont(ModResources.RARITY_FONT)));
//
//        if (descriptionComponents.contains(BrutalityTooltipHelper.DescriptionComponents.LORE))
//            pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + "lore"));
//
//        for (BrutalityTooltipHelper.DescriptionComponent descriptionComponent : descriptionComponents) {
//            boolean isLore = descriptionComponent.type().equals(BrutalityTooltipHelper.DescriptionComponents.LORE);
//
//            if (!isLore) {
//                pTooltipComponents.add(Component.translatable(
//                        Brutality.MOD_ID + ".description.type." + descriptionComponent.type().toString().toLowerCase()));
//
//                for (int i = 1; i <= descriptionComponent.lines(); i++) {
//                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + descriptionComponent.type().toString().toLowerCase() + "." + i));
//                }
//            } else {
//                for (int i = 1; i <= descriptionComponent.lines(); i++) {
//                    pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + descriptionComponent.type().toString().toLowerCase() + "." + i).withStyle(ChatFormatting.BLUE));
//                }
//            }
//
//            if (!descriptionComponent.equals(descriptionComponents.get(descriptionComponents.size() - 1)))
//                pTooltipComponents.add(Component.empty());
//        }
//
//    }

    @OnlyIn(Dist.CLIENT)
    default <T extends BlockEntity & BrutalityGeoBlockEntity> BrutalityBlockRenderer<T> createRenderer() {
        BrutalityBlockRenderer<T> renderer = new BrutalityBlockRenderer<>();
        configureLayers(renderer);
        return renderer;
    }

    @OnlyIn(Dist.CLIENT)
    default <T extends BlockEntity & BrutalityGeoBlockEntity> void configureLayers(BrutalityBlockRenderer<T> renderer) {
        renderer.addRenderLayer(new BrutalityBlockLayer<>(renderer));
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }


}
