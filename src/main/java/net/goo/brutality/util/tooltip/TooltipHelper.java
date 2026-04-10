package net.goo.brutality.util.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.List;
import java.util.Locale;

public class TooltipHelper {
    public static void handleItemDescriptions(ItemStack stack, List<Component> pTooltipComponents, List<ItemDescriptionComponent> descriptionComponents, Rarity rarity) {
        for (ItemDescriptionComponent descriptionComponent : descriptionComponents) {

            String componentLower = descriptionComponent.type().toString().toLowerCase(Locale.ROOT);
            if (!descriptionComponent.type().equals(ItemDescriptionComponent.ItemDescriptionComponents.LORE)) {
                pTooltipComponents.add(Component.translatable(
                        Brutality.MOD_ID + ".description.type." + componentLower));
            }

            for (int i = 1; i <= descriptionComponent.lines(); i++) {
                pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + stack.getItem() + "." + componentLower + "." + i));
            }

            if (descriptionComponent.cooldownTicks() != null) {
                int totalSecs = (int) (descriptionComponent.cooldownTicks() / 20F);
                int hours = (int) (totalSecs / 3600F);
                int minutes = (int) ((totalSecs % 3600) / 60F);
                int seconds = totalSecs % 60;

                StringBuilder formatted = new StringBuilder();
                boolean first = true;
                if (hours > 0) {
                    formatted.append(hours).append(" hour");
                    first = false;
                }
                if (minutes > 0) {
                    if (!first) formatted.append(" ");
                    formatted.append(minutes).append(" minute");
                    first = false;
                }
                if (seconds > 0 || (hours == 0 && minutes == 0)) {
                    if (!first) formatted.append(" ");
                    formatted.append(seconds).append(" second");
                }


                MutableComponent component = Component.literal(" -" + formatted + " ")
                        .append(Component.translatable("message." + Brutality.MOD_ID + ".cooldown"));

                pTooltipComponents.add(component.withStyle(ChatFormatting.DARK_AQUA));
            }

            if (descriptionComponent.keySupplier() != null) {
                InputConstants.Key key = descriptionComponent.keySupplier().get();
                if (key != null) {
                    MutableComponent component = Component.literal("(")
                            .append(Component.translatable("key.brutality.current_keybind"))
                            .append(": ")
                            .append(key.getDisplayName())
                            .append(")");
                    pTooltipComponents.add(component.withStyle(ChatFormatting.GRAY));

                }
            }


            if (!descriptionComponent.equals(descriptionComponents.get(descriptionComponents.size() - 1)))
                pTooltipComponents.add(Component.empty());
        }

        if (stack.isEnchanted() && ((6 & ItemStack.TooltipPart.ENCHANTMENTS.getMask()) == 0)) {
            pTooltipComponents.add(Component.empty());
        }
    }
}
