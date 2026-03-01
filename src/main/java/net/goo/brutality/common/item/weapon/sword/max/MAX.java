package net.goo.brutality.common.item.weapon.sword.max;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MAX extends BrutalitySwordItem {


    public MAX(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.1"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.2"));
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".max.lore.3")
                .withStyle(style -> style.withInsertion(ColorUtils.ColorData.MAX.name()).withBold(true)));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);


    }
}
