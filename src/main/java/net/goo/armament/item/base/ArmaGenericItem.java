package net.goo.armament.item.base;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;

public class ArmaGenericItem extends Item implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    protected int[][] colors;
    public Rarity rarity;
    public int abilityCount;

    public ArmaGenericItem(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties);
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
    public int getMaxStackSize(ItemStack stack) {
        return 1;
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
}
