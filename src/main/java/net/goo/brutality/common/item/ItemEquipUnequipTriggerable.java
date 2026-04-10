package net.goo.brutality.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

// called when the item is put in main hand or when it is removed from main hand
public interface ItemEquipUnequipTriggerable {

   default void onEnterMainHand(LivingEntity livingEntity, ItemStack stack) {};

   default void onLeaveMainHand(LivingEntity livingEntity, ItemStack stack) {};

}
