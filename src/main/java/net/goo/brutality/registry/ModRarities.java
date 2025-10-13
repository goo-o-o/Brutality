package net.goo.brutality.registry;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Rarity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ModRarities {

    public static final Rarity LEGENDARY = createRarity("LEGENDARY", RarityData.LEGENDARY);
    public static final Rarity FABLED = createRarity("FABLED", RarityData.FABLED);
    public static final Rarity MYTHIC = createRarity("MYTHIC", RarityData.MYTHIC);
    public static final Rarity DIVINE = createRarity("DIVINE", RarityData.DIVINE);
    public static final Rarity CATACLYSMIC = createRarity("CATACLYSMIC", RarityData.CATACLYSMIC);
    public static final Rarity GODLY = createRarity("GODLY", RarityData.GODLY);
    public static final Rarity DARK = createRarity("DARK", RarityData.DARK);
    public static final Rarity SCULK = createRarity("SCULK", RarityData.SCULK);
    public static final Rarity ICE = createRarity("ICE", RarityData.ICE);
    public static final Rarity PRISMATIC = createRarity("PRISMATIC", RarityData.PRISMATIC);
    public static final Rarity FIRE = createRarity("FIRE", RarityData.FIRE);
    public static final Rarity STYGIAN = createRarity("STYGIAN", RarityData.STYGIAN);

    public static final Map<Rarity, RarityData> RARITY_TO_GRADIENT = new HashMap<>();

    private static Rarity createRarity(String name, RarityData rarityData) {
        UnaryOperator<Style> styleModifier = style -> {
            Style baseStyle = Style.EMPTY.withColor(rarityData.colors[0]);
            return rarityData.bold ? baseStyle.withBold(true) : baseStyle;
        };
        return Rarity.create(name, styleModifier);
    }

    static {
        RARITY_TO_GRADIENT.put(LEGENDARY, RarityData.LEGENDARY);
        RARITY_TO_GRADIENT.put(FABLED, RarityData.FABLED);
        RARITY_TO_GRADIENT.put(MYTHIC, RarityData.MYTHIC);
        RARITY_TO_GRADIENT.put(DIVINE, RarityData.DIVINE);
        RARITY_TO_GRADIENT.put(CATACLYSMIC, RarityData.CATACLYSMIC);
        RARITY_TO_GRADIENT.put(GODLY, RarityData.GODLY);
        RARITY_TO_GRADIENT.put(DARK, RarityData.DARK);
        RARITY_TO_GRADIENT.put(SCULK, RarityData.SCULK);
        RARITY_TO_GRADIENT.put(ICE, RarityData.ICE);
        RARITY_TO_GRADIENT.put(FIRE, RarityData.FIRE);
        RARITY_TO_GRADIENT.put(STYGIAN, RarityData.STYGIAN);
        RARITY_TO_GRADIENT.put(PRISMATIC, RarityData.PRISMATIC);
    }


    public enum RarityData {
        LEGENDARY(new Color[]{
                new Color(255, 240, 120), new Color(255, 205, 20), new Color(255, 170, 40)
        }, 0.35f, 2.0f, false),

        FABLED(new Color[]{
                new Color(255, 255, 255), new Color(128, 200, 230),
                new Color(200, 150, 255), new Color(210, 105, 225)
        }, 0.4f, 1.8f, false),

        MYTHIC(new Color[]{
                new Color(20, 205, 255), new Color(20, 255, 165)
        }, 0.4f, 1.8f, false),

        DIVINE(new Color[]{
                new Color(255, 255, 175), new Color(175, 235, 240)
        }, 0.77f, 1.8f, true),

        CATACLYSMIC(new Color[]{
                new Color(164, 252, 255), new Color(77, 140, 220), new Color(203, 130, 225),
                new Color(255, 30, 50), new Color(203, 130, 225), new Color(77, 140, 220)
        }, 0.4f, 1.8f, true),

        GODLY(new Color[]{
                new Color(255, 90, 90), new Color(255, 180, 90), new Color(255, 255, 90),
                new Color(120, 255, 120), new Color(120, 255, 255), new Color(120, 120, 255),
                new Color(150, 100, 255), new Color(255, 100, 255)
        }, 0.3f, 1.8f, true),

        DARK(new Color[]{
                new Color(90, 90, 95), new Color(39, 42, 53)
        }, 0.3f, 1.2f, true),

        SCULK(new Color[]{
                new Color(0, 242, 255), new Color(18, 141, 165)
        }, 0.3f, 1.2f, false),

        ICE(new Color[]{
                new Color(150, 220, 245), new Color(255, 255, 255)
        }, 0.65f, 2.0f, false),

        PRISMATIC(new Color[]{
                new Color(214, 125, 238), new Color(179, 95, 233),
                new Color(113, 71, 221), new Color(179, 95, 233)
        }, 0.95f, 2.0f, true),

        FIRE(new Color[]{
                new Color(255, 240, 20), new Color(255, 150, 0), new Color(255, 80, 40)
        }, 0.9f, 2.2f, false),

        STYGIAN(new Color[]{
                new Color(255, 0, 0), new Color(160, 0, 0)
        }, 0.65f, 1.2f, true);

        public final int[] colors;
        public final float waveSpeed;
        public final float spread;
        public final boolean bold;

        RarityData(Color[] inputColors, float waveSpeed, float spread, boolean bold) {
            this.colors = new int[inputColors.length];
            for (int i = 0; i < inputColors.length; i++) {
                this.colors[i] = FastColor.ARGB32.color(255, inputColors[i].getRed(), inputColors[i].getGreen(), inputColors[i].getBlue());
            }
            // I like the fact that I can preview colors using the Colors class, but I need packed int for performance
            this.waveSpeed = waveSpeed;
            this.spread = spread;
            this.bold = bold;
        }
    }

    public static RarityData getGradientForRarity(Rarity rarity) {
        return RARITY_TO_GRADIENT.get(rarity);
    }
}
