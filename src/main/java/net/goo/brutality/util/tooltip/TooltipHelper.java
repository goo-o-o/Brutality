package net.goo.brutality.util.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.BrutalityGastronomyCurioItem;
import net.goo.brutality.util.build_archetypes.GastronomyDebuffContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;

public class TooltipHelper {
    public static void handleGastronomyItemDescriptions(ItemStack stack, BrutalityGastronomyCurioItem item, List<Component> pTooltipComponents) {
        List<ItemDescriptionComponent> manualComponents = item.descriptionComponents;

        // 1. Track which automated sections we have already processed via manual components
        boolean processedTrueMelee = false;
        boolean processedOnHit = false;

        for (int j = 0; j < manualComponents.size(); j++) {
            ItemDescriptionComponent comp = manualComponents.get(j);
            ItemDescriptionComponent.ItemDescriptionComponents type = comp.type();
            String typeStr = type.toString().toLowerCase(Locale.ROOT);

            // Header
            if (type != ItemDescriptionComponent.ItemDescriptionComponents.LORE) {
                pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type." + typeStr).withStyle(ChatFormatting.GOLD));
            }

            // True Melee specific warning
            if (type == ItemDescriptionComponent.ItemDescriptionComponents.ON_TRUE_MELEE_HIT) {
                pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".gastronomy_item.only_works_with_gastronomist_weapons").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
                processedTrueMelee = true;
            } else if (type == ItemDescriptionComponent.ItemDescriptionComponents.ON_HIT) {
                processedOnHit = true;
            }

            // Add Manual Lines
            for (int i = 1; i <= comp.lines(); i++) {
                pTooltipComponents.add(Component.translatable(stack.getDescriptionId() + "." + typeStr + "." + i));
            }

            // Inject Automated Debuffs underneath the manual lines
            if (type == ItemDescriptionComponent.ItemDescriptionComponents.ON_TRUE_MELEE_HIT) {
                for (GastronomyDebuffContainer container : item.trueMeleeDebuffs) {
                    addInflictionLine(pTooltipComponents, container);
                }
            } else if (type == ItemDescriptionComponent.ItemDescriptionComponents.ON_HIT) {
                for (GastronomyDebuffContainer container : item.nonTrueMeleeDebuffs) {
                    addInflictionLine(pTooltipComponents, container);
                }
            }

            // Handle Cooldowns/Keys
            if (comp.cooldownTicks() != null) {
                pTooltipComponents.add(getCooldownComponent(comp.cooldownTicks()).withStyle(ChatFormatting.DARK_AQUA));
            }

            if (j < manualComponents.size() - 1) {
                pTooltipComponents.add(Component.empty());
            }
        }


        // Fallback for True Melee
        if (!processedTrueMelee && !item.trueMeleeDebuffs.isEmpty()) {
            if (!pTooltipComponents.isEmpty()) pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type.on_true_melee_hit").withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".gastronomy_item.only_works_with_gastronomist_weapons").withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
            for (GastronomyDebuffContainer container : item.trueMeleeDebuffs) {
                addInflictionLine(pTooltipComponents, container);
            }
        }

        // Fallback for On Hit
        if (!processedOnHit && !item.nonTrueMeleeDebuffs.isEmpty()) {
            if (!pTooltipComponents.isEmpty()) pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable(Brutality.MOD_ID + ".description.type.on_hit").withStyle(ChatFormatting.GOLD));
            for (GastronomyDebuffContainer container : item.nonTrueMeleeDebuffs) {
                addInflictionLine(pTooltipComponents, container);
            }
        }
    }

    private static void addInflictionLine(List<Component> tooltip, GastronomyDebuffContainer container) {
        tooltip.add(Component.translatable("item." + Brutality.MOD_ID + ".gastronomy_item.inflicts",
                container.effect().get().getDisplayName(),
                Component.translatable("enchantment.level." + container.levels()),
                container.duration() / 20));
    }

    public static void handleItemDescriptions(ItemStack stack, List<Component> pTooltipComponents, List<ItemDescriptionComponent> descriptionComponents) {
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
                pTooltipComponents.add(getCooldownComponent(descriptionComponent.cooldownTicks()).withStyle(ChatFormatting.DARK_AQUA));
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

    public static MutableComponent getCooldownComponent(int cooldownTicks) {
        int totalSecs = (int) (cooldownTicks / 20F);
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


        return Component.literal(" -" + formatted + " ")
                .append(Component.translatable("message." + Brutality.MOD_ID + ".cooldown"));
    }
}
