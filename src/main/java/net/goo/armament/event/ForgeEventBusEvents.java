package net.goo.armament.event;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    @SubscribeEvent
    public static void onGrindstoneUse(GrindstoneEvent.OnPlaceItem event) {
        // Get the items in the grindstone slots
        ItemStack bottomItem = event.getBottomItem();
        ItemStack topItem = event.getTopItem();

        // Identify the item being processed
        ItemStack targetStack = bottomItem.isEmpty() ? topItem : bottomItem;

        // Check if the target item is your custom Thunderbolt
        if (targetStack.getItem() == ModItems.ZEUS_THUNDERBOLT_TRIDENT.get()) {
            // Create a copy of the target item so we can modify it
            ItemStack resultStack = targetStack.copy();

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(resultStack);
            enchantments = enchantments.entrySet().stream().filter(entry ->
                    entry.getKey().isCurse() || entry.getKey() == Enchantments.LOYALTY || entry.getKey() == Enchantments.INFINITY_ARROWS)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            enchantments.put(Enchantments.LOYALTY, 5);
            enchantments.put(Enchantments.INFINITY_ARROWS, 1);
            EnchantmentHelper.setEnchantments(enchantments, resultStack);
            event.setXp(0);
            event.setOutput(resultStack);
        }
    }
}
