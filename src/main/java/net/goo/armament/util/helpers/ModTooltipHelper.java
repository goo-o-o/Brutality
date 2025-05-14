package net.goo.armament.util.helpers;

import net.goo.armament.util.ModResources;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ModTooltipHelper {

    public static int[] changeBrightness(int[] colors, float factor) {
        int[] result = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            result[i] = (int) (colors[i] * factor);
        }
        return result;
    }

    public static Component tooltipHelper(String localeKey, boolean bold, ResourceLocation font, long tickCount, float waveSpeed, float spreadMultiplier, int[]... colors) {
        if (font == null) {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), tickCount, waveSpeed, spreadMultiplier, colors).withStyle(Style.EMPTY.withBold(bold));
            }
        } else {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withFont(font).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), tickCount, waveSpeed, spreadMultiplier, colors).withStyle(Style.EMPTY.withBold(bold).withFont(font));
            }
        }
    }

    public static MutableComponent tooltipHelper(String localeKey, boolean bold, ResourceLocation font, int[]... colors) {
        if (font == null) {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), colors).withStyle(Style.EMPTY.withBold(bold));
            }
        } else {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withFont(font).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), colors).withStyle(Style.EMPTY.withBold(bold).withFont(font));
            }
        }
    }

    public static int[] ensureVisible(int[] rgb) {
        // Convert 0-255 RGB to 0.0-1.0 HSV
        float[] hsv = new float[3];
        Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);

        // Define thresholds in 0.0-1.0 range
        float minBrightness = 25; // ~10/255
        float boost = 0.25F;          // ~25/255

        // Apply brightness boost if needed
        if (hsv[2] < minBrightness) {
            hsv[2] = Math.min(1.0f, hsv[2] + boost); // Clamp to max 1.0
        }

        // Convert back to RGB
        int rgbInt = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
        return new int[]{
                (rgbInt >> 16) & 0xFF,
                (rgbInt >> 8) & 0xFF,
                rgbInt & 0xFF
        };
    }

    public static int getColorFromGradient(int percentage, int[]... rgbColors) {
        // Handle edge cases when there are less than 2 colors
        if (rgbColors.length < 2) {
            return rgbToInt(rgbColors[0]);
        }

        // Calculate the ratio based on the percentage
        float ratio = percentage / 100f;
        int totalSegments = rgbColors.length - 1; // Number of segments created by color stops

        // Determine the segment range for the given ratio
        int segment = Math.min((int) (ratio * totalSegments), totalSegments - 1);
        float localRatio = (ratio * totalSegments) - segment;

        // Interpolate between the two colors
        int[] color1 = rgbColors[segment];
        int[] color2 = rgbColors[segment + 1];

        // Calculate the resulting color
        int r = (int) ((1 - localRatio) * color1[0] + localRatio * color2[0]);
        int g = (int) ((1 - localRatio) * color1[1] + localRatio * color2[1]);
        int b = (int) ((1 - localRatio) * color1[2] + localRatio * color2[2]);

        return (r << 16) | (g << 8) | b; // Return the final color
    }

    // Method to add color gradient text with individual RGB color parameters
    public static MutableComponent addColorGradientText(Component text, long tickCount, float speed, float spreadMultiplier, int[]... rgbColors) {
        MutableComponent gradientTextComponent = Component.empty();
        String string = text.getString();
        int length = string.length();
        int numColors = rgbColors.length;

        // Handle edge cases for empty input
        if (numColors == 0 || length == 0) {
            return gradientTextComponent; // Return empty component if no colors or no text
        }

        int[][] adjustedColors = new int[numColors + 1][3];
        System.arraycopy(rgbColors, 0, adjustedColors, 0, numColors);
        adjustedColors[numColors] = rgbColors[0]; // Duplicate the first color for looping

        speed = 1 / speed;
        float effectiveTickCount = tickCount % (speed * 20);
        float ratio = effectiveTickCount / (speed * 20);

        // Create a float spread multiplier for more smoother effect
        // Use 2.0f for double, 3.0f for triple effect
        spreadMultiplier = 1 / spreadMultiplier;
        int effectiveLength = (int) (length * spreadMultiplier);

        for (int i = 0; i < length; i++) {
            // Calculate the adjusted index within the effective length
            // Scale the index by the ratio and the effective length
            float adjustedIndex = (((float) i * spreadMultiplier) / length + ratio) * effectiveLength;

            // Normalize the adjusted index to a value between 0 and effectiveLength - 1
            adjustedIndex = adjustedIndex % effectiveLength;

            // Calculate the color based on adjusted index as a percentage
            int percentage = (int) ((adjustedIndex / effectiveLength) * 100);

            // Get color from gradient
            int color = getColorFromGradient(percentage, adjustedColors);

            // Create the colored component for the current character
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color));

            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        return gradientTextComponent;
    }

    public static MutableComponent addColorGradientText(Component text, int[]... rgbColors) {
        // Create a component to hold all the parts of the gradient text
        MutableComponent gradientTextComponent = Component.empty();

        String string = text.getString();
        int length = string.length();
        int numColors = rgbColors.length; // Number of color stops

        if (numColors == 0 || length == 0) {
            return gradientTextComponent; // Return empty component if no colors or no text
        }

        for (int i = 0; i < length; i++) {
            // Calculate the percentage based on character index
            int percentage = (i * 100) / (length - 1); // Avoid division by zero for single character strings

            // Get the color from the gradient using the helper method
            int color = getColorFromGradient(percentage, rgbColors);

            // Create a component for the letter with the computed color
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i)))
                    .withStyle(Style.EMPTY.withColor(color));

            // Append the letter component to the main gradient text component
            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        // Return the complete gradient text component
        return gradientTextComponent;
    }


    // Example RGB conversion method
    public static int rgbToInt(int[] rgb) {
        return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2]; // Assume RGB value is in the range [0, 255]
    }

    public static int rgbToInt(int r, int g, int b) {
        return (r << 16) | (g << 8) | b; // Assume RGB value is in the range [0, 255]
    }

    public static int argbToInt(int[] rgb, int... alpha) {
        int a = alpha.length > 0 ? alpha[0] : 255;
        rgb[0] = Math.max(0, Math.min(255, rgb[0]));
        rgb[1] = Math.max(0, Math.min(255, rgb[1]));
        rgb[2] = Math.max(0, Math.min(255, rgb[2]));
        return (a << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    public static int[] argbToRgb(int argb) {
        return new int[]{
                (argb >> 16) & 0xFF, // Red component
                (argb >> 8) & 0xFF,  // Green component
                argb & 0xFF          // Blue component
        };
    }

    public static int[][] getLoreColors(Class<?> itemClass) {
        return ModResources.LORE_COLOR_CACHE.computeIfAbsent(itemClass, cls -> {
            int[][] baseColors = ModResources.BASE_COLOR_MAP.get(cls);
            if (baseColors == null || baseColors.length < 2) return null;

            return new int[][] {
                    ensureVisible(baseColors[0]),
                    ensureVisible(baseColors[1])
            };
        });
    }

    public static int[][] getBackgroundColors(Class<?> itemClass) {
        return ModResources.DARKENED_COLOR_CACHE.computeIfAbsent(itemClass, cls -> {
            int[][] baseColors = ModResources.BASE_COLOR_MAP.get(cls);
            if (baseColors == null || baseColors.length < 2) return null;

            return new int[][] {
                    changeBrightness(baseColors[0], 0.4f),
                    changeBrightness(baseColors[1], 0.4f)
            };
        });
    }
}
