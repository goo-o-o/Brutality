package net.goo.armament.util;

import net.minecraft.world.item.ItemStack;

public class EnchantmentHelper {
    private static final ThreadLocal<ItemStack> CURRENT_ANVIL_STACK = new ThreadLocal<>();

    public static void setCurrentAnvilStack(ItemStack stack) {
        CURRENT_ANVIL_STACK.set(stack);
    }

    public static ItemStack getCurrentAnvilStack() {
        return CURRENT_ANVIL_STACK.get();
    }
}