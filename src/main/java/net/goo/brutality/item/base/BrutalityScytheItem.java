package net.goo.brutality.item.base;

import net.goo.brutality.item.BrutalityItemCategories;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;

public class BrutalityScytheItem extends SwordItem implements BrutalityGeoItem {
    protected String identifier;
    protected Rarity rarity;
    protected List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents;

    public BrutalityScytheItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
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
        return BrutalityItemCategories.SCYTHE;
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

    @Override
    public @NotNull AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return target.getBoundingBox().inflate(1.5F, 0.25F, 1.5F);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.SWEEPING_EDGE, 5);
        return stack;
    }

}
