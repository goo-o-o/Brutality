package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;

public class BrokenClock extends BrutalityCurioItem {


    public BrokenClock(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        Optional<ICuriosItemHandler> handler =  CuriosApi.getCuriosInventory(slotContext.entity()).resolve();

        if (handler.isPresent()) {
            if (handler.get().isEquipped(itemStack -> itemStack.getItem() instanceof BrokenClock)) {
                return false;
            }
        }

        return super.canEquip(slotContext, stack);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

}
