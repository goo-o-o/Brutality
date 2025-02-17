package net.goo.armament.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModUtils {

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

    public static Component tooltipHelper(String localeKey, boolean bold, ResourceLocation font, int[]... colors) {
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
            float adjustedIndex = (((float)i * spreadMultiplier) / length + ratio) * effectiveLength;

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

    public static BlockPos LookingAtBlock(Player player, boolean isFluid, float hitDistance){
        HitResult block =  player.pick(hitDistance, 1.0F, isFluid);

        if (block.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            return blockpos;
        }
        return null;
    }


    public static Entity getEntityPlayerLookingAt(Player pPlayer, double range) {
        // Get player's eye position
        Vec3 playerPos = pPlayer.getEyePosition(1.0F);
        Vec3 viewVector = pPlayer.getViewVector(1.0F).normalize();

        // Get the list of entities within the specified range
        List<Entity> entities = pPlayer.level().getEntitiesOfClass(Entity.class, new AABB(playerPos, playerPos).inflate(range));
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE; // Start with a very large distance

        for (Entity entity : entities) {
            // Calculate direction to the entity
            Vec3 entityPos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            Vec3 directionToEntity = entityPos.subtract(playerPos);
            double distanceToEntity = directionToEntity.length();

            if (distanceToEntity > 0) {
                directionToEntity = directionToEntity.normalize();
                double dotProduct = viewVector.dot(directionToEntity);

                // Check if the angle is within range
                if (dotProduct > 1.0D - 0.075D / distanceToEntity) { // Adjust threshold as needed
                    // Check line of sight
                    if (pPlayer.hasLineOfSight(entity)) {
                        // If this entity is closer than the closest found so far, update
                        if (distanceToEntity < closestDistance) {
                            closestEntity = entity;
                            closestDistance = distanceToEntity; // Update the closest distance
                        }
                    }
                }
            }
        }
        return closestEntity; // Return the closest entity or null if none found
    }

    public static double calculateXLook(LivingEntity player) {
        return player.getLookAngle().x();
    }

    public static double calculateYLook(LivingEntity player, double yMult) {
        double lookY = player.getLookAngle().y();

        if (lookY > 0) return lookY * yMult;
        else return lookY * 0.5;
    }

    public static double calculateYLook(LivingEntity player) {
        return player.getLookAngle().y();
    }

    public static double calculateZLook(LivingEntity player) {
        return player.getLookAngle().z();
    }

}
