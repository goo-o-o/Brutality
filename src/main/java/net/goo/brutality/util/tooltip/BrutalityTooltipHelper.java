package net.goo.brutality.util.tooltip;

import net.goo.brutality.common.registry.BrutalityRarities;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BrutalityTooltipHelper {
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    public static Component getRarityName(String translationKey, Rarity rarity) {
        assert Minecraft.getInstance().player != null;
        BrutalityRarities.RarityData rarityData = BrutalityRarities.from(rarity);
        if (rarityData == null) {
            return BrutalityTooltipHelper.tooltipHelper(translationKey, false, null, 1F, 1F, WHITE);
        }
        return BrutalityTooltipHelper.tooltipHelper(translationKey, rarityData.bold, null, rarityData.waveSpeed, rarityData.spread, rarityData.colors);
    }

    public static Component tooltipHelper(String localeKey, boolean bold, ResourceLocation font, Float waveSpeed, Float spreadMultiplier, int... colors) {
        if (colors.length == 1) {
            return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withFont(font).withColor(TextColor.fromRgb(colors[0])));
        } else {
            return ColorUtils.addColorGradientText(Component.translatable(localeKey), colors, waveSpeed, spreadMultiplier).withStyle(Style.EMPTY.withBold(bold));
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

        public final String icon;
        public final String unit;

        SpellStatComponents(String icon, String unit) {
            this.icon = icon;
            this.unit = unit;
        }

    }

}
