package net.goo.armament.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ModUtils {

    public static MutableComponent addColorGradientText(Component text, int r1, int g1, int b1, int r2, int g2, int b2) {
        // Define your start and end colors in RGB
        int startColor = rgbToInt(r1, g1, b1);
        int endColor = rgbToInt(r2, g2, b2);

        // Create a component to hold all parts of the gradient text
        MutableComponent gradientTextComponent = Component.empty(); // Initialize with an empty component

        String string = text.getString();
        int length = string.length();

        for (int i = 0; i < length; i++) {
            // Calculate the ratio of the current position to the total length
            float ratio = (float) i / (length - 1);

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

        // Add the complete gradient text to the tooltip as a single line
        return gradientTextComponent;
    }

    public static int rgbToInt(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static MutableComponent addColorGradientText(Component text, int[] rgb1, int[] rgb2) {

        int startColor = rgbToInt(rgb1);
        int endColor = rgbToInt(rgb2);

        MutableComponent gradientTextComponent = Component.empty(); // Initialize with an empty component

        String string = text.getString();
        int length = string.length();

        for (int i = 0; i < length; i++) {
            // Calculate the ratio of the current position to the total length
            float ratio = (float) i / (length - 1);

            // Interpolate between startColor and endColor
            int r = (int) ((1 - ratio) * ((startColor >> 16) & 0xFF) + ratio * ((endColor >> 16) & 0xFF));
            int g = (int) ((1 - ratio) * ((startColor >> 8) & 0xFF) + ratio * ((endColor >> 8) & 0xFF));
            int b = (int) ((1 - ratio) * (startColor & 0xFF) + ratio * (endColor & 0xFF));

            // Create the color integer
            int color = (r << 16) | (g << 8) | b;

            Component letterComponent = Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color));

            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        return gradientTextComponent;
    }

    public static MutableComponent addColorGradientText(Component text, int[] rgb1, int[] rgb2, int[] rgb3) {
        // Define your start, middle, and end colors in RGB
        int startColor = rgbToInt(rgb1);
        int middleColor = rgbToInt(rgb2);
        int endColor = rgbToInt(rgb3);

        // Create a component to hold all parts of the gradient text
        MutableComponent gradientTextComponent = Component.empty(); // Initialize with an empty component

        String string = text.getString();
        int length = string.length();

        for (int i = 0; i < length; i++) {
            // Define a variable for color
            int color;

            // Determine which range of the gradient the current position falls into
            if (i < length / 3) {
                // Start to middle transition
                float ratio = (float) i / (length / 3);
                int r = (int) ((1 - ratio) * ((startColor >> 16) & 0xFF) + ratio * ((middleColor >> 16) & 0xFF));
                int g = (int) ((1 - ratio) * ((startColor >> 8) & 0xFF) + ratio * ((middleColor >> 8) & 0xFF));
                int b = (int) ((1 - ratio) * (startColor & 0xFF) + ratio * (middleColor & 0xFF));
                color = (r << 16) | (g << 8) | b; // Assign color
            } else if (i < (2 * length) / 3) {
                // Middle to end transition
                float ratio = (float) (i - length / 3) / (length / 3);
                int r = (int) ((1 - ratio) * ((middleColor >> 16) & 0xFF) + ratio * ((endColor >> 16) & 0xFF));
                int g = (int) ((1 - ratio) * ((middleColor >> 8) & 0xFF) + ratio * ((endColor >> 8) & 0xFF));
                int b = (int) ((1 - ratio) * (middleColor & 0xFF) + ratio * (endColor & 0xFF));
                color = (r << 16) | (g << 8) | b; // Assign color
            } else {
                // End color
                color = endColor;
            }

            // Create a component for the letter with the computed color
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color));

            // Append the letter component to the main gradient text component
            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        // Add the complete gradient text to the tooltip as a single line
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
