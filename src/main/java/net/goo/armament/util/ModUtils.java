package net.goo.armament.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ModUtils {

    public static MutableComponent addGradientText(Component text, int r1, int g1, int b1, int r2, int g2, int b2) {
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

    public static MutableComponent addGradientText(Component text, int[] rgb1, int[] rgb2) {
        // Define your start and end colors in RGB

        int startColor = rgbToInt(rgb1);
        int endColor = rgbToInt(rgb2);

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
