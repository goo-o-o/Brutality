package net.goo.brutality.util;

import net.minecraft.util.FastColor;

import java.awt.*;
import java.util.Locale;

public class ColorUtils {

    public static int getGradientAt(float x, float spread, float speed, int... colors) {
        // Use the game's actual clock for smooth animation that respects pause menus
        // Divide by 1000 to convert millis to seconds
        float time = (System.currentTimeMillis() % 100000L) / 1000.0F;

        // Offset is (position / spread) + (time * speed)
        // We multiply speed by time so the wave moves
        float progress = ((x / spread) + (time * speed)) % 1.0F;
        if (progress < 0) progress += 1.0F;

        int numColors = colors.length;
        float scaledProgress = progress * numColors;
        int index = (int) scaledProgress;
        int nextIndex = (index + 1) % numColors;
        float segmentProgress = scaledProgress - index;

        return FastColor.ARGB32.lerp(segmentProgress, colors[index], colors[nextIndex]);
    }


    // Get cycling color with animation
    public static int getCyclingColor(float speed, int... colors) {

        float time = (System.currentTimeMillis() % 100000L) / 1000.0F;
        float t = (time * speed) % colors.length;
        int index = (int) Math.floor(t);
        float lerpFactor = t - index;

        int colorA = colors[index % colors.length];
        int colorB = colors[(index + 1) % colors.length];
        return FastColor.ARGB32.lerp(lerpFactor, colorA, colorB);
    }

    // ===================================================================
    public enum ColorData {
        LEGENDARY(new Color[]{
                new Color(255, 240, 120),
                new Color(255, 205, 20),
                new Color(255, 170, 40)
        },0.35f, 200, false, true),

        FABLED(new Color[]{
                new Color(255, 255, 255),
                new Color(128, 200, 230),
                new Color(200, 150, 255),
                new Color(210, 105, 225)
        },0.4f, 180, false, true),
        MYTHIC(new Color[]{
                new Color(20, 205, 255),
                new Color(20, 255, 165)
        },0.4f, 180, false, true),
        DIVINE(new Color[]{
                new Color(255, 255, 175),
                new Color(175, 235, 240)
        },0.77f, 180, true, true),
        CATACLYSMIC(new Color[]{
                new Color(164, 252, 255),
                new Color(77, 140, 220),
                new Color(203, 130, 225),
                new Color(255, 30, 50),
                new Color(203, 130, 225),
                new Color(77, 140, 220)
        },0.4f, 180, true, true),
        GODLY(new Color[]{
                new Color(255, 90, 90),
                new Color(255, 180, 90),
                new Color(255, 255, 90),
                new Color(120, 255, 120),
                new Color(120, 255, 255),
                new Color(120, 120, 255),
                new Color(150, 100, 255),
                new Color(255, 100, 255)
        },0.3f, 180, true, true),
        DARK(new Color[]{
                new Color(90, 90, 95),
                new Color(39, 42, 53)
        },0.3f, 120, true, true),
        GLOOMY(new Color[]{
                new Color(0, 242, 255),
                new Color(18, 141, 165)
        },0.3f, 120, false, true),
        GLACIAL(new Color[]{
                new Color(150, 220, 245),
                new Color(255, 255, 255)
        },0.65f, 200, false, true),
        PRISMATIC(new Color[]{
                new Color(214, 125, 238),
                new Color(179, 95, 233),
                new Color(113, 71, 221),
                new Color(179, 95, 233)
        },0.95f, 200, true, true),
        FIRE(new Color[]{
                new Color(255, 240, 20),
                new Color(255, 150, 0),
                new Color(255, 80, 40)
        },0.9f, 220, false, true),
        STYGIAN(new Color[]{
                new Color(255, 0, 0),
                new Color(160, 0, 0)
        },0.65f, 120, true, true),
        NOCTURNAL(new Color[]{
                new Color(80, 32, 200),
                new Color(208, 192, 248)
        },0.1f, 200, true, true),

        CONDUCTIVE(new Color[]{
                new Color(62, 50, 43),
                new Color(93, 77, 65),
                new Color(255, 223, 81),
                new Color(93, 77, 65),
                new Color(62, 50, 43)
        },1f, 50, true, true),

        NULL(new Color[]{
                new Color(245, 0, 245),
                new Color(0, 0, 0)
        },1f, 50, true, true),

        VOLTWEAVER(new Color[]{
                new Color(255, 204, 0),
                new Color(255, 240, 178),
                new Color(255, 254, 252),
                new Color(255, 240, 178),
                new Color(255, 240, 178),
                new Color(255, 254, 252),
                new Color(255, 240, 178),
                new Color(255, 254, 252),
                new Color(255, 204, 0),
                new Color(255, 204, 0),
        },1f, 50, true, true),

        BRIMWIELDER(new Color[]{
                new Color(255, 0, 0),
                new Color(160, 0, 0),
                new Color(17, 1, 0)
        },0.65f, 120, true, true),

        DAEMONIC(new Color[]{
                new Color(126, 129, 168),
                new Color(126, 129, 168),
                new Color(126, 129, 168),
                new Color(107, 236, 255),
                new Color(28, 20, 36),
                new Color(28, 20, 36),
                new Color(28, 20, 36)
        },0.65f, 120, true, true),

        VOIDWALKER(new Color[]{
                new Color(250, 252, 255),
                new Color(191, 202, 223),
                new Color(27, 28, 56),
                new Color(0, 0, 0)
        },1.5f, 100, true, true),

        DARKIST(new Color[]{
                new Color(129, 255, 248),
                new Color(20, 184, 191),
                new Color(14, 113, 135),
                new Color(10, 80, 96)
        },0.5f, 100, true, true),

        EVERGREEN(new Color[]{
                new Color(73, 183, 40),
                new Color(14, 145, 36),
                new Color(35, 99, 34),
                new Color(23, 68, 24)
        },0.65f, 200, true, true),

        CELESTIA(new Color[]{
                new Color(255, 255, 175),
                new Color(175, 235, 240)
        },0.77f, 180, true, true),

        UMBRANCY(new Color[]{
                new Color(255, 255, 255),
                new Color(66, 73, 87)
        },0.77f, 180, true, true),

        EXODIC(new Color[]{
                new Color(230, 121, 127),
                new Color(60, 37, 60),
                new Color(172, 180, 207),
                new Color(47, 50, 61),
        },0.77f, 180, true, true),

        COSMIC(new Color[]{
                new Color(253, 245, 95),
                new Color(233, 163, 38),
                new Color(160, 85, 234),
                new Color(115, 38, 210),
        }, 1F, 100, false, true);

        public final int[] colors;
        public final float waveSpeed;
        public final float spread; // in pixels
        public final boolean bold;
        public final boolean shouldCycle;

        public volatile RarityBorderManager.BorderData idle, open;

        ColorData(Color[] inputColors, float waveSpeed, float spread, boolean bold, boolean shouldCycle) {
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

        public static ColorData getSafe(String name) {
            try { return valueOf(name.toUpperCase(Locale.ROOT)); }
            catch (Exception e) { return null; }
        }
    }
}