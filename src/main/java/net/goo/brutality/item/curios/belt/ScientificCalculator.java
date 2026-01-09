package net.goo.brutality.item.curios.belt;

import net.goo.brutality.item.curios.BrutalityMathCurio;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class ScientificCalculator extends BrutalityMathCurio {


    public ScientificCalculator(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity instanceof Player player) {
            CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
                List<SlotResult> mathItems = handler.findCurios(itemStack ->
                                itemStack.is(ModTags.Items.MATH_ITEMS))
                        .stream().toList();

                mathItems.forEach(slotResult -> {
                    SlotContext resultContext = slotResult.slotContext();
                    ItemStack toRemove = slotResult.stack();

                    handler.getStacksHandler(resultContext.identifier()).
                            ifPresent(stacksHandler ->
                                    stacksHandler.getStacks().setStackInSlot(resultContext.index(), ItemStack.EMPTY));

                    if (!player.addItem(toRemove)) {
                        entity.spawnAtLocation(toRemove);
                    }
                });
            });
        }

    }

}
