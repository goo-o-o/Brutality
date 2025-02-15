package net.goo.armament.item;

import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.util.ModResources;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public class ArmaSwordItem extends SwordItem implements ArmaGeoItem {
    public String identifier;
    public ModItemCategories category;
    protected int[][] colors;
    public Rarity rarity;

    public ArmaSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
        this.identifier = identifier;
        this.rarity = rarity;
    }

    @Override
    public Component getName(ItemStack pStack) {
        Level pLevel = Minecraft.getInstance().level;
        return ModUtils.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), pLevel.getGameTime(), 0.5F, 2, colors);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        getName(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("rarity.armament." + rarity).withStyle(Style.EMPTY.withFont(ModResources.RARITY)));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.1", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.2", false, null, colors[0]));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.3", false, null, colors[1]));

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

}
