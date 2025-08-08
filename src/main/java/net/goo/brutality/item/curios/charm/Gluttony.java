package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Gluttony extends BrutalityCurioItem {
    public static final String SOULS = "souls";

    public Gluttony(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));

        float souls = stack.getOrCreateTag().getInt(SOULS) * 0.01F;
        String formattedValue = souls % 1 == 0 ?
                String.format("%.0f", souls) :
                String.format("%.1f", souls);


        tooltip.add(Component.literal((souls >= 0 ? "+" : "") + formattedValue + " ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(ChatFormatting.BLUE));

    }

}

