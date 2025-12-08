package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityScytheItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeGrindstoneHandler {
    @SubscribeEvent
    public static void onGrindstoneUse(GrindstoneEvent.OnPlaceItem event) {
        ItemStack bottomItem = event.getBottomItem();
        ItemStack topItem = event.getTopItem();
        ItemStack targetStack = bottomItem.isEmpty() ? topItem : bottomItem;

        if (targetStack.getItem() == BrutalityModItems.THUNDERBOLT_TRIDENT.get()) {
            handleGrindstoneEvent(
                    event,
                    targetStack,
                    Map.of(Enchantments.LOYALTY, 5, Enchantments.INFINITY_ARROWS, 1), // Allowed enchantments
                    Map.of(Enchantments.LOYALTY, 5, Enchantments.INFINITY_ARROWS, 1)  // Forced enchantments
            );
//        } else if (targetStack.getItem() == BrutalityModItems.TERRATON_HAMMER.get()) {
//            handleGrindstoneEvent(
//                    event,
//                    targetStack,
//                    Map.of(Enchantments.KNOCKBACK, 4), // Allowed enchantments
//                    Map.of(Enchantments.KNOCKBACK, 4)  // Forced enchantments
//            );
        } else if (targetStack.getItem() instanceof BrutalityScytheItem) {
            handleGrindstoneEvent(
                    event,
                    targetStack,
                    Map.of(Enchantments.SWEEPING_EDGE, 5), // Allowed enchantments
                    Map.of(Enchantments.SWEEPING_EDGE, 5)  // Forced enchantments
            );
        } else if (targetStack.getItem() == BrutalityModItems.SUPERNOVA.get()) {
            handleGrindstoneEvent(
                    event,
                    targetStack,
                    Map.of(Enchantments.SWEEPING_EDGE, 5), // Allowed enchantments
                    Map.of(Enchantments.KNOCKBACK, 3)      // Forced enchantments
            );
        }
    }

    private static void handleGrindstoneEvent(GrindstoneEvent.OnPlaceItem event, ItemStack targetStack, Map<Enchantment, Integer> allowedEnchantments, Map<Enchantment, Integer> forcedEnchantments) {
        ItemStack resultStack = targetStack.copy();

        // Filter out unwanted enchantments and keep curses and allowed enchantments
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(resultStack);
        enchantments = enchantments.entrySet().stream()
                .filter(entry -> entry.getKey().isCurse() || allowedEnchantments.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Add forced enchantments
        enchantments.putAll(forcedEnchantments);

        // Apply the modified enchantments to the result stack
        EnchantmentHelper.setEnchantments(enchantments, resultStack);

        // Set the output and XP
        event.setXp(0);
        event.setOutput(resultStack);
    }
}