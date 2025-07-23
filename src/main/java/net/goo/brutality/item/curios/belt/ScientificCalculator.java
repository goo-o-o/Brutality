package net.goo.brutality.item.curios;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class ScientificCalculatorBelt extends BrutalityCurioItem {


    public ScientificCalculatorBelt(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("curios.modifiers.belt").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("item.brutality.scientific_calculator_belt.passive.1")); // Allows the use of mathematical functions
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

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

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.BELT;
    }
}
