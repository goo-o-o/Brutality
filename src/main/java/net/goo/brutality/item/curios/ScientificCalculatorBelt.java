package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ScientificCalculatorBeltCurio extends BrutalityCurioItem {
    public ScientificCalculatorBeltCurio(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("curios.modifiers.belt").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.brutality.scientific_calculator_belt.passive.1")); // Allows the use of mathematical functions
    }

}
