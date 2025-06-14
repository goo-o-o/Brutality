package net.goo.brutality.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import java.util.HashMap;
import java.util.Map;

public class ModRarities {

    public static final Rarity LEGENDARY = Rarity.create("legendary", ChatFormatting.GOLD);
    public static final Rarity MYTHICAL = Rarity.create("mythic", ChatFormatting.LIGHT_PURPLE);
    public static final Rarity FABLED = Rarity.create("fabled", ChatFormatting.RED);
    public static final Rarity GODLY = Rarity.create("godly", ChatFormatting.RED);
    public static final Rarity DARK = Rarity.create("dark", ChatFormatting.RED);
    public static final Rarity SCULK = Rarity.create("sculk", ChatFormatting.RED);
    public static final Rarity ICE = Rarity.create("ice", ChatFormatting.RED);
    public static final Rarity FIRE = Rarity.create("fire", ChatFormatting.RED);
    public static final Rarity STYGIAN = Rarity.create("stygian", ChatFormatting.RED);

    public static final Map<Rarity, RarityData> RARITY_TO_GRADIENT = new HashMap<>();

    static {
        RARITY_TO_GRADIENT.put(LEGENDARY, RarityData.LEGENDARY);
        RARITY_TO_GRADIENT.put(FABLED, RarityData.FABLED);
        RARITY_TO_GRADIENT.put(MYTHICAL, RarityData.MYTHICAL);
        RARITY_TO_GRADIENT.put(GODLY, RarityData.GODLY);
        RARITY_TO_GRADIENT.put(DARK, RarityData.DARK);
        RARITY_TO_GRADIENT.put(SCULK, RarityData.SCULK);
        RARITY_TO_GRADIENT.put(ICE, RarityData.ICE);
        RARITY_TO_GRADIENT.put(FIRE, RarityData.FIRE);
        RARITY_TO_GRADIENT.put(STYGIAN, RarityData.STYGIAN);
    }


    public enum RarityData {
        LEGENDARY(new int[][]{{255, 240, 120}, {255, 205, 20}, {255, 170, 40}}, 0.35f, 2.0f, false),
        FABLED(new int[][]{{255, 255, 255}, {128, 200, 230}, {200, 150, 255}, {210, 105, 225}}, 0.4f, 1.8f, true),
        MYTHICAL(new int[][]{{20, 205, 255}, {20, 255, 205}}, 0.4f, 1.8f, true),
        GODLY(new int[][]{{255, 90, 90}, {255, 180, 90}, {255, 255, 90}, {120, 255, 120}, {120, 255, 255}, {120, 120, 255}, {150, 100, 255}, {255, 100, 255}}, 0.3f, 1.8f, true),
        DARK(new int[][]{{90, 90, 95}, {39, 42, 53}}, 0.3f, 1.2f, true),
        SCULK(new int[][]{{0, 242, 255}, {18, 141, 165}}, 0.3f, 1.2f, false),
        ICE(new int[][]{{150, 220, 245}, {255, 255, 255}}, 0.65f, 2.0f, false),
        FIRE(new int[][]{{255, 240, 20}, {255, 150, 0}, {255, 80, 40}}, 0.9f, 2.2f, false),
        STYGIAN(new int[][]{{255, 0, 0}, {160, 0, 0}}, 0.65f, 1.2f, true);

        public final int[][] colors;
        public final float waveSpeed;
        public final float spread;
        public final boolean bold;

        RarityData(int[][] colors, float waveSpeed, float spread, boolean bold) {
            this.colors = colors;
            this.waveSpeed = waveSpeed;
            this.spread = spread;
            this.bold = bold;
        }
    }

    public static RarityData getGradientForRarity(Rarity rarity) {
        return RARITY_TO_GRADIENT.get(rarity);
    }
}