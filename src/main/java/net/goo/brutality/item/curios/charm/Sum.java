package net.goo.brutality.item.curios.charm;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Sum extends BrutalityMathFunctionCurio {
    public static String SUM_DAMAGE = "sumDamage";

    public Sum(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".sum.damage_stored.1"));
        pTooltipComponents.add(Component.literal(String.valueOf(pStack.getOrCreateTag().getFloat(SUM_DAMAGE))).withStyle(ChatFormatting.DARK_GRAY));
    }

}
