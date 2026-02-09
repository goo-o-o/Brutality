package net.goo.brutality.util.tooltip;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
@Mod.EventBusSubscriber (value = Dist.CLIENT)
public class SpellTooltips {
    static Component BAR = Component.literal(" | ").withStyle(ChatFormatting.DARK_GRAY);

    public static void renderSpellEntry(SpellStorage.SpellEntry spellEntry, List<Component> componentList) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        int originalSpellLevel = spellEntry.level();
        BrutalitySpell spell = spellEntry.spell();
        int actualSpellLevel = IBrutalitySpell.getActualSpellLevel(player, spell, originalSpellLevel);

        // name
        componentList.add(getSpellEntryHeader(spell, actualSpellLevel, originalSpellLevel));
    }

    public static MutableComponent getSpellEntryHeader(BrutalitySpell spell, int actualSpellLevel, int originalSpellLevel) {
        int spellLevelBonus = actualSpellLevel - originalSpellLevel;
        MutableComponent header = spell.getTranslatedSpellName().append(BAR).append(actualSpellLevel + " + " + spellLevelBonus).append(BAR);

        for (IBrutalitySpell.SpellCategory spellCategory : spell.getCategories()) {
            header.append(spellCategory.icon);
        }
        return header;
    }

    public enum SpellStatComponents {
        RANGE("📏", " 🟫"),
        SPEED("⏲", " ⏲"),
        SIZE("📐", " 🟫"),
        CHANCE("🎲", "%"),
        QUANTITY("🔢", " ◽"),
        PIERCE("💘", " 🧟"),
        DURATION("🕒", "s"),
        DEFENSE("🦺", " 🛡"),
        HEALING("⛨", " ❤");

        public final MutableComponent icon;
        public final MutableComponent unit;

        SpellStatComponents(String icon, String unit) {
            this.icon = Component.literal(icon).withStyle(s -> s.withFont(ModResources.ICON_FONT));
            this.unit = Component.literal(unit).withStyle(s -> s.withFont(ModResources.ICON_FONT));
        }

    }

    public record SpellStatComponent(SpellStatComponents type, float base, float levelDelta, Float min, Float max) {
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
