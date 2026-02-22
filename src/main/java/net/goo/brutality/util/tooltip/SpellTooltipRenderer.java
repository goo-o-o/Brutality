package net.goo.brutality.util.tooltip;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SpellTooltipRenderer {
    static Component BAR = Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY);
    static Style ICON = Style.EMPTY.withFont(ModResources.ICON_FONT).withColor(ChatFormatting.WHITE);
    static Component SWORD_ICON = Component.literal("\uD83D\uDDE1").withStyle(ICON);
    static Component MANA_ICON = Component.literal("\uD83D\uDCA7").withStyle(ICON);
    static Component CAST_TIME_ICON = Component.literal("\uD83E\uDE84").withStyle(ICON);
    static Component HEART_ICON = Component.literal("\u2764").withStyle(ICON);
    static Component COOLDOWN_ICON = Component.literal("⌛").withStyle(ICON);

    public static void renderSpellEntry(SpellStorage.SpellEntry spellEntry, List<Component> componentList, boolean extended) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        int originalSpellLevel = spellEntry.level();
        BrutalitySpell spell = spellEntry.spell();
        int actualSpellLevel = spell.getActualSpellLevel(player, originalSpellLevel);

        // name
        componentList.add(getSpellEntryHeader(spell, actualSpellLevel, originalSpellLevel, extended));
        componentList.add(getSpellDescriptionsTooltip(spell).withStyle(ChatFormatting.DARK_GRAY));

        componentList.add(Component.empty());
        componentList.add(getSpellDamageLine(player, spell, originalSpellLevel, extended));
        componentList.add(getManaCostLine(player, spell, originalSpellLevel, extended));
        componentList.add(getSpellCooldownLine(player, spell, originalSpellLevel, extended));
        componentList.add(getCastTimeLine(player, spell, originalSpellLevel, extended));

        for (SpellStatComponent statComponent : spell.getStatComponents()) {
            componentList.add(getStatLine(spell, statComponent, actualSpellLevel, extended));
        }

        componentList.add(Component.empty());

    }

    public static @NotNull MutableComponent getCastTimeLine(LivingEntity caster, BrutalitySpell spell, int level, boolean extended) {
        // 1. Calculate values
        float base = spell.getBaseCastTime() * 0.05F;
        float castTimeLevelScaling = spell.getCastTimeLevelScaling() * 0.05F;
        float actualCastTime = spell.getActualCastTime(caster, level) * 0.05F;
        float attributeMult = (float) caster.getAttributeValue(BrutalityAttributes.CAST_TIME.get());

        MutableComponent component = Component.empty();

        // 2. Icon and Label
        component.append(CAST_TIME_ICON);
        if (extended) {
            component.append(Component.literal(" ᴄᴀѕᴛ ᴛɪᴍᴇ"));
        }
        component.append(BAR);

        // 3. Simple View: Just the final number
        if (!extended) {
            return component.append(Component.literal(String.format("%.1f", actualCastTime) + "s").withStyle(ChatFormatting.DARK_GREEN));
        }

        // 4. Extended View: The Full Formula
        String op = castTimeLevelScaling >= 0 ? " + " : " - ";
        boolean showMult = attributeMult != 1.0;

        // Base Part: (Base ❤️ + (Scaling ❤️ * level))
        MutableComponent formula = Component.literal(base + "s").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(op + "(").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(Math.abs(castTimeLevelScaling) + "s").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" × level)").withStyle(ChatFormatting.GRAY));

        // Multiplier Part: * Multiplier
        if (showMult) {
            formula.append(Component.literal(" × " + String.format("%.2f", attributeMult))
                    .withStyle(ChatFormatting.BLUE));
        }

        // Final Result: = Total ❤️
        formula.append(Component.literal(" = ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(String.format("%.1f", actualCastTime) + "s").withStyle(ChatFormatting.DARK_GREEN));

        return component.append(formula);
    }

    public static MutableComponent getSpellDamageLine(LivingEntity caster, BrutalitySpell spell, int level, boolean extended) {
        // 1. Calculate values
        float base = spell.getBaseDamage();
        float damageLevelScaling = spell.getDamageLevelScaling();
        float finalDamage = spell.getActualDamage(caster, level);
        float attributeMult = (float) caster.getAttributeValue(BrutalityAttributes.SPELL_DAMAGE.get());

        MutableComponent component = Component.empty();

        // 2. Icon and Label
        component.append(SWORD_ICON);
        if (extended) {
            component.append(" ᴅᴀᴍᴀɢᴇ");
        }
        component.append(BAR);

        // 3. Simple View: Just the final number
        if (!extended) {
            return component.append(Component.literal(String.format("%.1f", finalDamage) + " ").withStyle(ChatFormatting.DARK_GREEN))
                    .append(HEART_ICON);
        }

        // 4. Extended View: The Full Formula
        String op = damageLevelScaling >= 0 ? " + " : " - ";
        boolean showMult = attributeMult != 1.0;

        // Base Part: (Base ❤️ + (Scaling ❤️ * level))
        MutableComponent formula = Component.literal(base + " ").withStyle(ChatFormatting.GOLD)
                .append(HEART_ICON)
                .append(Component.literal(op + "(").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(Math.abs(damageLevelScaling) + " ").withStyle(ChatFormatting.GOLD))
                .append(HEART_ICON)
                .append(Component.literal(" × level)").withStyle(ChatFormatting.GRAY));

        // Multiplier Part: * Multiplier
        if (showMult) {
            formula.append(Component.literal(" × " + String.format("%.2f", attributeMult))
                    .withStyle(ChatFormatting.BLUE));
        }

        // Final Result: = Total ❤️
        formula.append(Component.literal(" = ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(String.format("%.1f", finalDamage) + " ").withStyle(ChatFormatting.DARK_GREEN))
                .append(HEART_ICON);

        return component.append(formula);
    }

    public static MutableComponent getManaCostLine(LivingEntity caster, BrutalitySpell spell, int level, boolean extended) {
        // 1. Calculate values
        float base = spell.getBaseManaCost();
        float manaCostLevelScaling = spell.getManaCostLevelScaling();
        float actualManaCost = spell.getActualManaCost(caster, level);
        float attributeMult = (float) caster.getAttributeValue(BrutalityAttributes.MANA_COST.get());

        MutableComponent component = Component.empty();

        // 2. Icon and Label
        component.append(MANA_ICON);
        if (extended) {
            component.append(" ᴍᴀɴᴀ ᴄᴏѕᴛ");
        }
        component.append(BAR);

        // 3. Simple View: Just the final number
        if (!extended) {
            return component.append(Component.literal(String.format("%.1f", actualManaCost) + " ").withStyle(ChatFormatting.DARK_GREEN))
                    .append(MANA_ICON);
        }

        // 4. Extended View: The Full Formula
        String op = manaCostLevelScaling >= 0 ? " + " : " - ";
        boolean showMult = attributeMult != 1.0;

        // Base Part: (Base ❤️ + (Scaling ❤️ * level))
        MutableComponent formula = Component.literal(base + " ").withStyle(ChatFormatting.GOLD)
                .append(MANA_ICON)
                .append(Component.literal(op + "(").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(Math.abs(manaCostLevelScaling) + " ").withStyle(ChatFormatting.GOLD))
                .append(MANA_ICON)
                .append(Component.literal(" × level)").withStyle(ChatFormatting.GRAY));

        // Multiplier Part: * Multiplier
        if (showMult) {
            formula.append(Component.literal(" × " + String.format("%.2f", attributeMult))
                    .withStyle(ChatFormatting.BLUE));
        }

        // Final Result: = Total ❤️
        formula.append(Component.literal(" = ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(String.format("%.1f", actualManaCost) + " ").withStyle(ChatFormatting.DARK_GREEN))
                .append(MANA_ICON);

        return component.append(formula);
    }

    public static MutableComponent getSpellCooldownLine(LivingEntity caster, BrutalitySpell spell, int level, boolean extended) {
        // 1. Calculate values
        float base = spell.getBaseCooldown() * 0.05F;
        float cooldownLevelScaling = spell.getCooldownLevelScaling() * 0.05F;
        float finalCooldown = spell.getActualCooldown(caster, level) * 0.05F;
        float attributeMult = (float) caster.getAttributeValue(BrutalityAttributes.SPELL_COOLDOWN.get());

        MutableComponent component = Component.empty();

        // 2. Icon and Label
        component.append(COOLDOWN_ICON);
        if (extended) {
            component.append(" ᴄᴏᴏʟᴅᴏᴡɴ");
        }
        component.append(BAR);

        // 3. Simple View: Just the final number
        if (!extended) {
            return component.append(Component.literal(String.format("%.1f", finalCooldown) + "s").withStyle(ChatFormatting.DARK_GREEN));
        }

        // 4. Extended View: The Full Formula
        String op = cooldownLevelScaling >= 0 ? " + " : " - ";
        boolean showMult = attributeMult != 1.0;

        // Base Part: (Base ❤️ + (Scaling ❤️ * level))
        MutableComponent formula = Component.literal(base + "s").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(op + "(").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(Math.abs(cooldownLevelScaling) + "s").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" × level)").withStyle(ChatFormatting.GRAY));

        // Multiplier Part: * Multiplier
        if (showMult) {
            formula.append(Component.literal(" × " + String.format("%.2f", attributeMult))
                    .withStyle(ChatFormatting.BLUE));
        }

        // Final Result: = Total ❤️
        formula.append(Component.literal(" = ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(String.format("%.1f", finalCooldown) + "s").withStyle(ChatFormatting.DARK_GREEN));

        return component.append(formula);
    }

    public static MutableComponent getStatLine(BrutalitySpell spell, SpellStatComponent statComponent, int level, boolean extended) {
        float base = statComponent.base();
        float levelDelta = statComponent.levelDelta();
        float finalValue = spell.getFinalStat(level, statComponent);

        MutableComponent component = Component.empty();

        // Ensure icons are unformatted (white/default)
        // .withStyle(ChatFormatting.WHITE) forces them to ignore inherited colors
        MutableComponent icon = statComponent.type.icon.copy().withStyle(ChatFormatting.WHITE);
        MutableComponent unit = statComponent.type.unit.copy().withStyle(ChatFormatting.WHITE);

        // 1. Icon
        component.append(icon);

        // 2. Name (Extended Only)
        if (extended) {
            // We append the space separately so it doesn't get the SMALL_CAPS font
            component.append(" ");
            MutableComponent statName = Component.literal(statComponent.type().name().toUpperCase(Locale.ROOT))
                    .withStyle(s -> s.withFont(ModResources.SMALL_CAPS).withColor(ChatFormatting.WHITE));

            component.append(statName);
        }

        // BAR usually has its own style (DARK_GRAY), so we append it directly
        component.append(BAR);

        // 3. Simple View: Just the final number
        if (!extended) {
            component.append(Component.literal(String.format("%.1f", finalValue)).withStyle(ChatFormatting.DARK_GREEN));
            return component.append(unit);
        }

        // 4. Extended View: The Full Formula
        String op = levelDelta >= 0 ? " + " : " - ";

        // Base Part: Base [Unit]
        component.append(Component.literal(String.valueOf(base)).withStyle(ChatFormatting.GOLD));
        component.append(unit);

        // Scaling Part: op (LevelDelta [Unit] x level)
        component.append(Component.literal(op + "(").withStyle(ChatFormatting.GRAY));
        component.append(Component.literal(String.valueOf(Math.abs(levelDelta))).withStyle(ChatFormatting.GOLD));
        component.append(unit);
        component.append(Component.literal(" × level)").withStyle(ChatFormatting.GRAY));

        // Final Result: = Total [Unit]
        component.append(Component.literal(" = ").withStyle(ChatFormatting.GRAY));
        component.append(Component.literal(String.format("%.1f", finalValue)).withStyle(ChatFormatting.DARK_GREEN));
        component.append(unit);

        // 5. Min/Max - Fixed colors and formatting
        if (statComponent.min() != null) {
            component.append(" "); // Separation space
            // Using Component.literal for the label to apply RED specifically
            component.append(Component.literal("ᴍɪɴ " + statComponent.min()).withStyle(ChatFormatting.RED));
            component.append(unit);
        }

        if (statComponent.max() != null) {
            component.append(" "); // Separation space
            // Using Component.literal for the label to apply GREEN specifically
            component.append(Component.literal("ᴍᴀx " + statComponent.max()).withStyle(ChatFormatting.GREEN));
            component.append(unit);
        }

        return component;
    }

    public static MutableComponent getSpellEntryHeader(BrutalitySpell spell, int actualSpellLevel, int originalSpellLevel, boolean extended) {
        MutableComponent header = Component.empty();
        MutableComponent name = spell.getTranslatedSpellName().withStyle(s -> s.withInsertion(spell.getSchool().name()));
        header.append(name);
        header.append(BAR);
        header.append(getSpellLevelTooltip(actualSpellLevel, originalSpellLevel, extended));
        header.append(BAR);
        header.append(getSpellCategoriesTooltip(spell, extended));

        return header;
    }

    public static MutableComponent getSpellLevelTooltip(int actualSpellLevel, int originalSpellLevel, boolean extended) {
        if (!extended)
            return Component.literal(String.valueOf(actualSpellLevel));
        int spellLevelBonus = actualSpellLevel - originalSpellLevel;

        return Component.literal(String.valueOf(originalSpellLevel)).withStyle(ChatFormatting.BOLD).append(Component.literal(" + " + spellLevelBonus));
    }

    public static MutableComponent getSpellCategoriesTooltip(BrutalitySpell spell, boolean extended) {
        MutableComponent component = Component.empty();
        for (int i = 0; i < spell.getCategories().size(); i++) {
            IBrutalitySpell.SpellCategory spellCategory = spell.getCategories().get(i);
            component.append(spellCategory.icon);
            if (extended) {
                component.append(" ");
                component.append(Component.literal(spellCategory.name().toUpperCase(Locale.ROOT)).withStyle(s -> s.withFont(ModResources.SMALL_CAPS)));
            }

            if (i < spell.getCategories().size() - 1) {
                component.append(" ");
            }
        }
        return component;
    }

    public static MutableComponent getSpellDescriptionsTooltip(BrutalitySpell spell) {
        MutableComponent component = Component.empty();
        for (int i = 1; i <= spell.getDescriptionCount(); i++) {
            component.append(spell.getSpellDescription(i));
            if (i < spell.getDescriptionCount()) {
                component.append(Component.literal(". "));
            } else {
                component.append(Component.literal("."));
            }
        }
        return component;
    }

    public enum SpellStatComponentType {
        RANGE("📏", Component.literal(" ").append(Component.literal("🟫").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        SPEED("⏲", Component.literal(" ").append(Component.literal("⏲").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        SIZE("📐", Component.literal(" ").append(Component.literal("🟫").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        CHANCE("🎲", Component.literal("%")),
        QUANTITY("🔢", Component.literal(" ").append(Component.literal("◽").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        PIERCE("💘", Component.literal(" ").append(Component.literal("🧟").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        DURATION("🕒", Component.literal("s")),
        DEFENSE("🦺", Component.literal(" ").append(Component.literal("🛡").withStyle(s -> s.withFont(ModResources.ICON_FONT)))),
        HEALING("⛨", Component.literal(" ").append(Component.literal("❤").withStyle(s -> s.withFont(ModResources.ICON_FONT))));

        public final MutableComponent icon;
        public final MutableComponent unit;

        SpellStatComponentType(String icon, MutableComponent unit) {
            this.icon = Component.literal(icon).withStyle(s -> s.withFont(ModResources.ICON_FONT));
            this.unit = unit;
        }

    }

    public record SpellStatComponent(SpellStatComponentType type, float base, float levelDelta, Float min, Float max) {
        public float base() {
            return base;
        }

        public float levelDelta() {
            return levelDelta;
        }

        public Float min() {
            return min;
        }

        public Float max() {
            return max;
        }
    }
}
