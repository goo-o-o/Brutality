package net.goo.brutality.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// used to run code whenever the item leaves the inventory
public interface ItemLeftInventoryTriggerable {
    void onLeaveInventory(Player player, ItemStack stack);
}
