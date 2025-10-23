package net.goo.brutality.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ColorUtils {
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);

    // Get cycling color with animation
    public static int getCyclingColor(float speed, int... colors) {
        if (colors.length == 0) return WHITE;

        int tickCount = getTickCount();
        float t = (tickCount * speed) % colors.length;
        int index = (int) Math.floor(t);
        float lerpFactor = t - index;

        int colorA = colors[index % colors.length];
        int colorB = colors[(index + 1) % colors.length];
        return FastColor.ARGB32.lerp(lerpFactor, colorA, colorB);
    }

    // Apply gradient or cycling gradient to text
    public static MutableComponent addColorGradientText(Component text, int[] colors, float speed, float spreadMultiplier) {
        MutableComponent gradientText = Component.empty();
        String string = text.getString();
        int length = string.length();

        if (colors.length == 0 || length == 0) {
            return gradientText;
        }

        // Prepare gradient colors with loop closure
        int[] gradientColors = colors.length > 1 ? new int[colors.length + 1] : colors;
        if (colors.length > 1) {
            System.arraycopy(colors, 0, gradientColors, 0, colors.length);
            gradientColors[colors.length] = colors[0]; // Close the loop
        }

        // Calculate tick-based progress for cycling (if speed > 0)
        float ratio = 0;
        if (speed > 0) {
            int tickCount = getTickCount();
            float inverseSpeed = 1 / speed;
            float effectiveTickCount = tickCount % (inverseSpeed * 20);
            ratio = effectiveTickCount / (inverseSpeed * 20);
        }

        // Apply spread multiplier (default to 1 for static gradient)
        float inverseSpread = spreadMultiplier > 0 ? 1 / spreadMultiplier : 1;
        float effectiveLength = length * inverseSpread;

        // Iterate through each character
        for (int i = 0; i < length; i++) {
            int percentage = speed > 0
                    ? (int) (((((float) i * inverseSpread) / length + ratio) * effectiveLength % effectiveLength) / effectiveLength * 100)
                    : (length > 1 ? (i * 100) / (length - 1) : 0);

            int color = getColorFromGradient(percentage, gradientColors);
            gradientText.append(Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color)));
        }

        return gradientText;
    }

    // Overloaded method for static gradient
    public static MutableComponent addColorGradientText(Component text, int... colors) {
        return addColorGradientText(text, colors, 0F, 1F);
    }

    // Get color from gradient based on percentage
    private static int getColorFromGradient(int percentage, int[] colors) {
        if (colors.length == 1) return colors[0];

        float segment = 100f / (colors.length - 1);
        int index = (int) (percentage / segment);
        float t = (percentage % segment) / segment;

        if (index >= colors.length - 1) {
            return colors[colors.length - 1];
        }

        return FastColor.ARGB32.lerp(t, colors[index], colors[index + 1]);
    }

    // Helper to get tick count safely
    private static int getTickCount() {
        Integer tickCount = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().player != null) return Minecraft.getInstance().player.tickCount;
            return (int) (System.currentTimeMillis() / 50L);
        });
        return tickCount != null ? tickCount : (int) (System.currentTimeMillis() / 50L);
    }
}