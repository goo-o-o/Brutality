package net.goo.brutality.item.curios;

import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BrutalityMathCurio extends BrutalityCurioItem {
    public BrutalityMathCurio(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public BrutalityMathCurio(Rarity rarity) {
        super(rarity);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler -> stack.is(BrutalityModItems.SCIENTIFIC_CALCULATOR.get()) || handler.isEquipped(BrutalityModItems.SCIENTIFIC_CALCULATOR.get()))
                .orElse(false);
    }

}
