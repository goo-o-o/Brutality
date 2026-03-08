package net.goo.brutality.common.item.generic.augments;

import net.goo.brutality.Brutality;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BrutalityAugmentationItem extends Item {
    public int maxSlots;

    public BrutalityAugmentationItem(Properties pProperties, int maxSlots) {
        super(pProperties);
        this.maxSlots = maxSlots;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + " .augmentation_item.description.1"));
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + " .augmentation_item.description.2", maxSlots));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
