package net.goo.armament.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ModUtils {

    public static MutableComponent addColorGradientText(Component text, int[]... rgbColors) {
        // Create a component to hold all the parts of the gradient text
        MutableComponent gradientTextComponent = Component.empty(); // Initialize with an empty component

        String string = text.getString();
        int length = string.length();
        int numColors = rgbColors.length; // Number of color stops

        if (numColors == 0 || length == 0) {
            return gradientTextComponent; // Return empty component if no colors or no text
        }

        for (int i = 0; i < length; i++) {
            // Determine which segment of the gradient the current index falls into
            int segment = Math.min((i * numColors) / length, numColors - 1);
            int nextSegment = Math.min(segment + 1, numColors - 1);

            // Calculate ratio for interpolation between current color and the next
            float ratio = (float) (i - (segment * length) / numColors) / ((float) (length) / numColors);

            // Get current and next colors
            int startColor = rgbToInt(rgbColors[segment]);
            int endColor = rgbToInt(rgbColors[nextSegment]);

            // Interpolate between startColor and endColor
            int r = (int) ((1 - ratio) * ((startColor >> 16) & 0xFF) + ratio * ((endColor >> 16) & 0xFF));
            int g = (int) ((1 - ratio) * ((startColor >> 8) & 0xFF) + ratio * ((endColor >> 8) & 0xFF));
            int b = (int) ((1 - ratio) * (startColor & 0xFF) + ratio * (endColor & 0xFF));

            // Create the color integer
            int color = (r << 16) | (g << 8) | b;

            // Create a component for the letter with the computed color
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color));

            // Append the letter component to the main gradient text component
            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        // Return the complete gradient text component
        return gradientTextComponent;
    }

    public static int getColorFromGradient(int percentage, int[] rgb1, int[] rgb2, int[] rgb3) {
        // Normalize the percentage to a value between 0 and 1
        float ratio = percentage / 100f;
        int color;

        // Determine the position in the gradient
        if (ratio < 1f / 3f) {
            // Interpolate between startColor (rgb1) and middleColor (rgb2)
            float localRatio = ratio / (1f / 3f);
            int r = (int) ((1 - localRatio) * rgb1[0] + localRatio * rgb2[0]);
            int g = (int) ((1 - localRatio) * rgb1[1] + localRatio * rgb2[1]);
            int b = (int) ((1 - localRatio) * rgb1[2] + localRatio * rgb2[2]);
            color = (r << 16) | (g << 8) | b;
        } else if (ratio < 2f / 3f) {
            // Interpolate between middleColor (rgb2) and endColor (rgb3)
            float localRatio = (ratio - (1f / 3f)) / (1f / 3f);
            int r = (int) ((1 - localRatio) * rgb2[0] + localRatio * rgb3[0]);
            int g = (int) ((1 - localRatio) * rgb2[1] + localRatio * rgb3[1]);
            int b = (int) ((1 - localRatio) * rgb2[2] + localRatio * rgb3[2]);
            color = (r << 16) | (g << 8) | b;
        } else {
            // Directly use the endColor (rgb3)
            color = (rgb3[0] << 16) | (rgb3[1] << 8) | rgb3[2];
        }

        return color;
    }

    public static int rgbToInt(int[] rgb) {
        if (rgb.length < 3) {
            throw new IllegalArgumentException("RGB array must have three elements: [r, g, b]");
        }
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];
        return (r << 16) | (g << 8) | b;
    }

}
