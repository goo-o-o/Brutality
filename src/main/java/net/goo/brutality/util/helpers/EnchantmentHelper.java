package net.goo.brutality.util.helpers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;
import java.util.Set;

public class EnchantmentHelper {
    private static final ThreadLocal<ItemStack> CURRENT_ANVIL_STACK = new ThreadLocal<>();

    public static void setCurrentAnvilStack(ItemStack stack) {
        CURRENT_ANVIL_STACK.set(stack);
    }

    public static ItemStack getCurrentAnvilStack() {
        return CURRENT_ANVIL_STACK.get();
    }

    public static boolean hasInfinity(ItemStack pStack) {
        return pStack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;
    }

    public static boolean restrictEnchants(ItemStack book, Set<Enchantment> allowedEnchants) {
        // Get all enchantments on the book
        Map<Enchantment, Integer> enchants = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantments(book);

        // Check if the book contains at least one allowed enchantment
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (allowedEnchants.contains(enchantment)) {
                return true; // Allow the book if it contains at least one allowed enchantment
            }
        }

        return false; // Disallow the book if it contains no allowed enchantments
    }
}