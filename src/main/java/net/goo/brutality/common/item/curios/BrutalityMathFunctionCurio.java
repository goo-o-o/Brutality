package net.goo.brutality.common.item.curios;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BrutalityMathFunctionCurio extends BrutalityCurioItem {
    public BrutalityMathFunctionCurio(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public BrutalityMathFunctionCurio(Rarity rarity) {
        super(rarity);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (!pStack.is(BrutalityItems.SCIENTIFIC_CALCULATOR.get())) {
            pTooltipComponents.add(Component.translatable("message." + Brutality.MOD_ID + ".requires_scientific_calculator"));
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler -> slotContext.entity().tickCount < 20 || stack.is(BrutalityItems.SCIENTIFIC_CALCULATOR.get()) || handler.isEquipped(BrutalityItems.SCIENTIFIC_CALCULATOR.get()))
                .orElse(false);
    }

}
