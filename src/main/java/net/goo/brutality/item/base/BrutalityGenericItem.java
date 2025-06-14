package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;

public class BrutalityGenericItem extends Item implements BrutalityGeoItem {
    private final List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents;
    public String identifier;
    public Rarity rarity;

    public BrutalityGenericItem(Properties pProperties, String identifier, Rarity rarity,
                                List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties);
        this.identifier = identifier;
        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
    }


    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return brutalityNameHandler(pStack, identifier);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityHoverTextHandler(pStack, pTooltipComponents, descriptionComponents, rarity, identifier);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public BrutalityItemCategories category() {
        return BrutalityItemCategories.GENERIC;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
