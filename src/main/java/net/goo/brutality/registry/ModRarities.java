package net.goo.brutality.registry;

import com.google.common.collect.Maps;
import net.goo.brutality.util.RarityBorderManager;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class ModRarities {

    public static final Rarity LEGENDARY;
    public static final Rarity FABLED;
    public static final Rarity MYTHIC;
    public static final Rarity DIVINE;
    public static final Rarity CATACLYSMIC;
    public static final Rarity GODLY;
    public static final Rarity DARK;
    public static final Rarity GLOOMY;
    public static final Rarity GLACIAL;
    public static final Rarity PRISMATIC;
    public static final Rarity FIRE;
    public static final Rarity STYGIAN;
    public static final Rarity CONDUCTIVE;
    public static final Rarity NOCTURNAL;

    public static final Map<Rarity, RarityData> BY_RARITY = Maps.newIdentityHashMap();

    static {
        LEGENDARY = create("legendary", RarityData.LEGENDARY);
        FABLED = create("fabled", RarityData.FABLED);
        MYTHIC = create("mythic", RarityData.MYTHIC);
        DIVINE = create("divine", RarityData.DIVINE);
        CATACLYSMIC = create("cataclysmic", RarityData.CATACLYSMIC);
        GODLY = create("godly", RarityData.GODLY);
        DARK = create("dark", RarityData.DARK);
        GLOOMY = create("gloomy", RarityData.GLOOMY);
        GLACIAL = create("glacial", RarityData.GLACIAL);
        PRISMATIC = create("prismatic", RarityData.PRISMATIC);
        FIRE = create("fire", RarityData.FIRE);
        STYGIAN = create("stygian", RarityData.STYGIAN);
        NOCTURNAL = create("nocturnal", RarityData.NOCTURNAL);
        CONDUCTIVE = create("conductive", RarityData.CONDUCTIVE);

        // Now safely populate the map
        for (RarityData data : RarityData.values()) {
            BY_RARITY.put(data.rarity, data);

        }
    }

    private static Rarity create(String name, RarityData data) {
        UnaryOperator<Style> formatter = style -> {
            Style s = Style.EMPTY.withColor(data.colors[0]);
            return data.bold ? s.withBold(true) : s;
        };
        Rarity rarity = Rarity.create(name.toUpperCase(Locale.ROOT), formatter);
        data.rarity = rarity;
        return rarity;
    }

    public static @Nullable RarityData from(Rarity rarity) {
        return BY_RARITY.get(rarity);
    }

    public static @Nullable RarityData from(ItemStack stack) {
        return from(stack.getRarity());
    }

    // ===================================================================
    public enum RarityData {
        LEGENDARY(new Color[]{new Color(255, 240, 120), new Color(255, 205, 20), new Color(255, 170, 40)}, 0.35f, 2.0f, false, true),
        FABLED(new Color[]{new Color(255, 255, 255), new Color(128, 200, 230), new Color(200, 150, 255), new Color(210, 105, 225)}, 0.4f, 1.8f, false, true),
        MYTHIC(new Color[]{new Color(20, 205, 255), new Color(20, 255, 165)}, 0.4f, 1.8f, false, true),
        DIVINE(new Color[]{new Color(255, 255, 175), new Color(175, 235, 240)}, 0.77f, 1.8f, true, true),
        CATACLYSMIC(new Color[]{new Color(164, 252, 255), new Color(77, 140, 220), new Color(203, 130, 225), new Color(255, 30, 50), new Color(203, 130, 225), new Color(77, 140, 220)}, 0.4f, 1.8f, true, true),
        GODLY(new Color[]{new Color(255, 90, 90), new Color(255, 180, 90), new Color(255, 255, 90), new Color(120, 255, 120), new Color(120, 255, 255), new Color(120, 120, 255), new Color(150, 100, 255), new Color(255, 100, 255)}, 0.3f, 1.8f, true, true),
        DARK(new Color[]{new Color(90, 90, 95), new Color(39, 42, 53)}, 0.3f, 1.2f, true, true),
        GLOOMY(new Color[]{new Color(0, 242, 255), new Color(18, 141, 165)}, 0.3f, 1.2f, false, true),
        GLACIAL(new Color[]{new Color(150, 220, 245), new Color(255, 255, 255)}, 0.65f, 2.0f, false, true),
        PRISMATIC(new Color[]{new Color(214, 125, 238), new Color(179, 95, 233), new Color(113, 71, 221), new Color(179, 95, 233)}, 0.95f, 2.0f, true, true),
        FIRE(new Color[]{new Color(255, 240, 20), new Color(255, 150, 0), new Color(255, 80, 40)}, 0.9f, 2.2f, false, true),
        STYGIAN(new Color[]{new Color(255, 0, 0), new Color(160, 0, 0)}, 0.65f, 1.2f, true, true),
        NOCTURNAL(new Color[]{new Color(80, 32, 200), new Color(208, 192, 248)}, 0.1f, 2.0f, true, true),
        CONDUCTIVE(new Color[]{new Color(62, 50, 43), new Color(93, 77, 65), new Color(255, 223, 81), new Color(93, 77, 65), new Color(62, 50, 43)}, 1f, 1.0f, true, true);

        public final int[] colors;
        public final float waveSpeed;
        public final float spread;
        public final boolean bold;
        public final boolean shouldCycle;

        // Make this mutable so we can assign it later
        public transient Rarity rarity;
        public volatile RarityBorderManager.BorderData idle, open;

        RarityData(Color[] inputColors, float waveSpeed, float spread, boolean bold, boolean shouldCycle) {
            this.colors = new int[inputColors.length];
            for (int i = 0; i < inputColors.length; i++) {
                Color c = inputColors[i];
                this.colors[i] = FastColor.ARGB32.color(255, c.getRed(), c.getGreen(), c.getBlue());
            }
            this.waveSpeed = waveSpeed;
            this.spread = spread;
            this.bold = bold;
            this.shouldCycle = shouldCycle;
        }

        public void setBorders(RarityBorderManager.BorderData idle, RarityBorderManager.BorderData open) {
            this.idle = idle;
            this.open = open;
        }
    }

}