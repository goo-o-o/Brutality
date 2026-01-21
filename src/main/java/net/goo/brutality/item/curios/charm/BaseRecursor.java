package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BaseRecursor extends BrutalityCurioItem {
    public BaseRecursor(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler ->
            handler.isEquipped(itemStack -> itemStack.getItem() instanceof BaseRecursor)
        ).orElse(super.canEquip(slotContext, stack));
    }
}
