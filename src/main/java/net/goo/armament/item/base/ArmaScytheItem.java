package net.goo.armament.item.base;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

public class ArmaScytheItem extends SwordItem implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    protected int[][] colors;
    public Rarity rarity;
    public int abilityCount;

    public ArmaScytheItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, (int) pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
        this.identifier = identifier;
        this.rarity = rarity;
        this.abilityCount = abilityCount;
    }

    @Override
    public Component getName(ItemStack pStack) {
        Level pLevel = Minecraft.getInstance().level;
        return ModUtils.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 2, colors);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("rarity.armament." + rarity).withStyle(Style.EMPTY.withFont(ModResources.RARITY)));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".lore", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));

        for (int i = 1; i <= abilityCount; i++) {
            pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".ability.name." + i, false, null, colors[0]));
            pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".ability.desc." + i, false, null, colors[1]));
            pTooltipComponents.add(Component.literal(""));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
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
