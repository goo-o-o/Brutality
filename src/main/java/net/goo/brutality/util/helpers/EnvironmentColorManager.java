package net.goo.brutality.util.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.Brutality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.rgbToInt;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class EnvironmentColorManager {
    // Color type definitions
    public enum ColorType {
        FOG(false),       // Doesn't require chunk reload
        WATER(true),      // Requires chunk reload
        WATER_FOG(false),
        SKY(false),
        FOLIAGE(true),
        GRASS(true);

        public final boolean requiresReload;

        ColorType(boolean requiresReload) {
            this.requiresReload = requiresReload;
        }
    }

    // Color storage (-1 means no override)
    private static final EnumMap<ColorType, Integer> COLOR_OVERRIDES = new EnumMap<>(ColorType.class);

    static {
        // Initialize all color types to -1 (no override)
        for (ColorType type : ColorType.values()) {
            COLOR_OVERRIDES.put(type, -1);
        }
    }

    /**
     * Set a color override
     *
     * @param type The color type to modify
     * @param r    red component
     * @param g    green component
     * @param b    blue component
     */
    public static void setColor(ColorType type, int r, int g, int b) {
        int rgbColor = rgbToInt(r, g, b);
        int clampedColor = rgbColor & 0xFFFFFF; // Ensure valid RGB
        if (COLOR_OVERRIDES.get(type) != clampedColor) {
            COLOR_OVERRIDES.put(type, clampedColor);

            // Update rendering if needed
            if (type.requiresReload) {
                Minecraft.getInstance().levelRenderer.allChanged();
            }

            // Special case for water fog
            if (type == ColorType.WATER_FOG) {
                FogRenderer.biomeChangedTime = -1L; // Force fog update
            }
        }
    }

    public static void setColor(ColorType type, int[] rgb) {
        int rgbColor = rgbToInt(rgb[0], rgb[1], rgb[2]);
        int clampedColor = rgbColor & 0xFFFFFF; // Ensure valid RGB
        if (COLOR_OVERRIDES.get(type) != clampedColor) {
            COLOR_OVERRIDES.put(type, clampedColor);

            // Update rendering if needed
            if (type.requiresReload) {
                Minecraft.getInstance().levelRenderer.allChanged();
            }

            // Special case for water fog
            if (type == ColorType.WATER_FOG) {
                FogRenderer.biomeChangedTime = -1L; // Force fog update
            }
        }
    }

    /**
     * Reset a color to default
     */
    public static void resetColor(ColorType type) {
        if (COLOR_OVERRIDES.get(type) != -1) {
            COLOR_OVERRIDES.put(type, -1);

            if (type.requiresReload) {
                Minecraft.getInstance().levelRenderer.allChanged();
            }
        }
    }

    /**
     * Reset all color overrides
     */
    public static void resetAllColors() {
        boolean needsReload = COLOR_OVERRIDES.entrySet().stream()
                .filter(entry -> entry.getValue() != -1) // Only changed colors
                .peek(entry -> entry.setValue(-1)) // Reset them
                .anyMatch(entry -> entry.getKey().requiresReload); // Check if any need reload

        if (needsReload) {
            Minecraft.getInstance().levelRenderer.allChanged();
        }
    }

    /**
     * Get the current override for a color type
     *
     * @return The override color or -1 if not set
     */
    public static int getColorOverride(ColorType type) {
        return COLOR_OVERRIDES.get(type);
    }

    private static final Vector3f skyLightColor = new Vector3f(0.5f, 0.7f, 1.0f);
    private static boolean customSkyLight = false;

    public static void setCustomSkyLight(float r, float g, float b) {
        skyLightColor.set(r, g, b);
        customSkyLight = true;
    }

    public static void setCustomSkyLight(int[] color) {
        skyLightColor.set((float) color[0] / 255, (float) color[1] / 255, (float) color[2] / 255);
        customSkyLight = true;
    }

    public static void resetCustomSkyLight() {
        customSkyLight = false;
    }

    @SubscribeEvent
    public static void onRenderSky(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY && customSkyLight) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(skyLightColor.x(), skyLightColor.y(), skyLightColor.z(), 1f);

            // Draw fullscreen quad
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            buffer.vertex(-100, -100, -100).endVertex();
            buffer.vertex(-100, 100, -100).endVertex();
            buffer.vertex(100, 100, -100).endVertex();
            buffer.vertex(100, -100, -100).endVertex();
            BufferUploader.drawWithShader(buffer.end());

            RenderSystem.setShader(() -> {
                ShaderInstance shader = GameRenderer.getRendertypeEntityTranslucentShader();
                shader.safeGetUniform("Brightness").set(-0.5f); // Negative = darker
                return shader;
            });
        }
    }


    public static class ProximityColorSet {
        public final EnumMap<ColorType, int[]> inRangeColors = new EnumMap<>(ColorType.class);
        public final EnumMap<ColorType, int[]> outOfRangeColors = new EnumMap<>(ColorType.class);
        public final EnumMap<ColorType, Boolean> shouldReset = new EnumMap<>(ColorType.class);

        public ProximityColorSet setColor(ColorType type, int[] inRange, int[] outOfRange) {
            inRangeColors.put(type, inRange);
            outOfRangeColors.put(type, outOfRange);
            shouldReset.put(type, false); // default: do NOT reset
            return this;
        }

        public ProximityColorSet setColorAutoReset(ColorType type, int[] inRange) {
            inRangeColors.put(type, inRange);
            shouldReset.put(type, true);
            return this;
        }
    }

    private static final EnumMap<ColorType, int[]> lastApplied = new EnumMap<>(ColorType.class);

    static {
        for (ColorType type : ColorType.values()) {
            lastApplied.put(type, null);
        }
    }

    public static final Map<Object, EnumMap<ColorType, int[]>> activeColorSources = new HashMap<>();

    public static void apply(Object source, boolean inRange, ProximityColorSet colors) {
        EnumMap<ColorType, int[]> newColors = new EnumMap<>(ColorType.class);

        for (ColorType type : ColorType.values()) {
            if (inRange && colors.inRangeColors.containsKey(type)) {
                newColors.put(type, colors.inRangeColors.get(type));
            } else if (!inRange) {
                if (colors.shouldReset.getOrDefault(type, false)) {
                    newColors.put(type, null);
                } else if (colors.outOfRangeColors.containsKey(type)) {
                    newColors.put(type, colors.outOfRangeColors.get(type));
                }
            }
        }

        activeColorSources.put(source, newColors);
    }

    public static void resolveAndApplyColors() {
        EnumMap<ColorType, int[]> resolvedColors = new EnumMap<>(ColorType.class);

        for (ColorType type : ColorType.values()) {
            for (var entry : activeColorSources.values()) {
                int[] color = entry.get(type);
                if (color != null) {
                    resolvedColors.put(type, color);
                    break; // Only the first (highest priority) active one
                }
            }

            int[] resolved = resolvedColors.get(type);
            int[] current = lastApplied.get(type);

            if (!Arrays.equals(resolved, current)) {
                if (resolved == null) {
                    resetColor(type);
                } else {
                    setColor(type, resolved);
                }
                lastApplied.put(type, resolved); // even null, meaning reset
            }
        }
    }


}