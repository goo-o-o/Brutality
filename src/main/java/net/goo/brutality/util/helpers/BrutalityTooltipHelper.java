package net.goo.brutality.util.helpers;

import net.goo.brutality.registry.ModRarities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.awt.*;

import static net.minecraft.util.CommonColors.WHITE;

public class BrutalityTooltipHelper {

    public static int[] changeBrightness(int[] colors, float factor) {
        int[] result = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            result[i] = (int) (colors[i] * factor);
        }
        return result;
    }

    public static Component getRarityName(String translationKey, Rarity rarity) {
        assert Minecraft.getInstance().player != null;
        ModRarities.RarityData rarityData = ModRarities.getGradientForRarity(rarity);
        return BrutalityTooltipHelper.tooltipHelper(translationKey, rarityData.bold, null, rarityData.waveSpeed, rarityData.spread, rarityData.colors);
    }

    public static Component tooltipHelper(String localeKey, boolean bold, ResourceLocation font, float waveSpeed, float spreadMultiplier, int[]... colors) {
        if (font == null) {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), waveSpeed, spreadMultiplier, colors).withStyle(Style.EMPTY.withBold(bold));
            }
        } else {
            if (colors.length == 1) {
                return Component.translatable(localeKey).withStyle(Style.EMPTY.withBold(bold).withFont(font).withColor(rgbToInt(colors[0])));
            } else {
                return addColorGradientText(Component.translatable(localeKey), waveSpeed, spreadMultiplier, colors).withStyle(Style.EMPTY.withBold(bold).withFont(font));
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
        float[] hsv = new float[3];
        Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsv);

        float minBrightness = 25;
        float boost = 0.25F;

        if (hsv[2] < minBrightness) {
            hsv[2] = Math.min(1.0f, hsv[2] + boost);
        }

        // Convert back to RGB
        int rgbInt = Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
        return new int[]{(rgbInt >> 16) & 0xFF, (rgbInt >> 8) & 0xFF, rgbInt & 0xFF};
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

    public static MutableComponent addColorGradientText(Component text, float speed, float spreadMultiplier, int[]... rgbColors) {
        MutableComponent gradientTextComponent = Component.empty();
        String string = text.getString();
        int length = string.length();
        int numColors = rgbColors.length;

        int tickCount = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().player != null) return Minecraft.getInstance().player.tickCount;
            return ((int) (System.currentTimeMillis() / 50L));
        });

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
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i))).withStyle(Style.EMPTY.withColor(color));

            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        return gradientTextComponent;
    }

    public static MutableComponent addColorGradientText(Component text, int[]... rgbColors) {
        MutableComponent gradientTextComponent = Component.empty();

        String string = text.getString();
        int length = string.length();
        int numColors = rgbColors.length;

        if (numColors == 0 || length == 0) {
            return gradientTextComponent;
        }

        for (int i = 0; i < length; i++) {
            // Calculate the percentage based on character index
            int percentage = (i * 100) / (length - 1); // Avoid division by zero for single character strings

            // Get the color from the gradient using the helper method
            int color = getColorFromGradient(percentage, rgbColors);

            // Create a component for the letter with the computed color
            Component letterComponent = Component.literal(String.valueOf(string.charAt(i))).withStyle(Style.EMPTY.withColor(color));

            // Append the letter component to the main gradient text component
            gradientTextComponent = gradientTextComponent.append(letterComponent);
        }

        // Return the complete gradient text component
        return gradientTextComponent;
    }


    public static int rgbToInt(int[] rgb) {
        return (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    public static int rgbToInt(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    public static int[] intToRgb(int color) {
        return new int[]{color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF};
    }


    public static int argbToInt(int[] rgb, int... alpha) {
        int a = alpha.length > 0 ? alpha[0] : 255;
        rgb[0] = Math.max(0, Math.min(255, rgb[0]));
        rgb[1] = Math.max(0, Math.min(255, rgb[1]));
        rgb[2] = Math.max(0, Math.min(255, rgb[2]));
        return (a << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    public static int argbToInt(int r, int g, int b, int a) {
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int[] argbToRgb(int argb) {
        return new int[]{(argb >> 16) & 0xFF, // Red component
                (argb >> 8) & 0xFF,  // Green component
                argb & 0xFF          // Blue component
        };
    }


    public static int getCyclingColors(float speed, int[]... rgbColors) {
        int tickCount = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().player != null) return Minecraft.getInstance().player.tickCount;
            return ((int) (System.currentTimeMillis() / 50L));
        });


        if (rgbColors.length == 0) return WHITE;


        int[][] gradientColors;
        if (rgbColors.length > 1) {
            gradientColors = new int[rgbColors.length + 1][];
            System.arraycopy(rgbColors, 0, gradientColors, 0, rgbColors.length);
            gradientColors[rgbColors.length] = rgbColors[0]; // Close the loop
        } else {
            gradientColors = rgbColors; // Single color (no interpolation)
        }

        float progress = tickCount % (100 * speed) / 100f; // Normalized to [0, 1)

        int percentage = (int) (progress * 100);

        return getColorFromGradient(percentage, gradientColors);
    }

    public static int[] getCyclingColorFromGradient(float speed, int[]... rgbColors) {
        assert Minecraft.getInstance().player != null;
        int tickCount = Minecraft.getInstance().player.tickCount;

        if (rgbColors.length == 0) return new int[]{255, 255, 255}; // fallback: white

        float t = (tickCount * speed) % rgbColors.length;
        int index = (int) Math.floor(t);
        float lerpFactor = t - index;

        int[] colorA = rgbColors[index % rgbColors.length];
        int[] colorB = rgbColors[(index + 1) % rgbColors.length];

        return lerpColor(colorA, colorB, lerpFactor);
    }

    private static int[] lerpColor(int[] a, int[] b, float t) {
        int r = (int) (a[0] + (b[0] - a[0]) * t);
        int g = (int) (a[1] + (b[1] - a[1]) * t);
        int b_ = (int) (a[2] + (b[2] - a[2]) * t);
        return new int[]{r, g, b_};
    }


    public record DescriptionComponent(DescriptionComponents type, int lines) {
    }


    public enum DescriptionComponents {
        ACTIVE, PASSIVE, FULL_SET_PASSIVE, FULL_SET_ACTIVE, ON_HIT, WHEN_THROWN, ON_SWING, ON_RIGHT_CLICK, ON_SHIFT_RIGHT_CLICK, LORE, ON_KILL, ON_SHOOT, ON_HOLD_RIGHT_CLICK, CHARM, DASH_ABILITY
    }
}
