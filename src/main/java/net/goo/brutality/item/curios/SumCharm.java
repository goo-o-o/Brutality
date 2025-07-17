package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class DivisionCharm extends BrutalityCurioItem {

    public DivisionCharm(String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(identifier, rarity, descriptionComponents);
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(ModItems.SCIENTIFIC_CALCULATOR_BELT.get()).isPresent()
                )
                .orElse(false);
    }
}
