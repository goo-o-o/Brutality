package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;

public class BrutalitySwordItem extends SwordItem implements BrutalityGeoItem {
    protected String identifier;
    protected Rarity rarity;
    protected List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents;

    public BrutalitySwordItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public BrutalityItemCategories category() {
        return BrutalityItemCategories.SWORD;
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
