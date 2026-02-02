package net.goo.brutality.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.Brutality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    // Color storage (null means no override)
    private static final EnumMap<ColorType, Integer> COLOR_OVERRIDES = new EnumMap<>(ColorType.class);

    static {
        // Initialize all color types to null (no override)
        for (ColorType type : ColorType.values()) {
            COLOR_OVERRIDES.put(type, null);
        }
    }

    /**
     * Set a color override
     *
     * @param type The color type to modify
     */
    public static void setColor(ColorType type, int rgbColor) {
        int clampedColor = rgbColor & 0xFFFFFF; // Ensure valid RGB
        if (!Integer.valueOf(clampedColor).equals(COLOR_OVERRIDES.get(type))) {
            COLOR_OVERRIDES.put(type, clampedColor);

            // Update rendering if needed
            if (type.requiresReload) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft != null) {
                    minecraft.execute(minecraft.levelRenderer::allChanged);
                }
            }
            // Special case for water fog
            if (type == ColorType.WATER_FOG) {
                FogRenderer.biomeChangedTime = -1L; // Force fog update
            }
        }
    }

    public static void setColor(ColorType type, int[] rgb) {
        int rgbColor = FastColor.ARGB32.color(255, rgb[0], rgb[1], rgb[2]);
        int clampedColor = rgbColor & 0xFFFFFF; // Ensure valid RGB
        if (!Integer.valueOf(clampedColor).equals(COLOR_OVERRIDES.get(type))) {
            COLOR_OVERRIDES.put(type, clampedColor);

            // Update rendering if needed
            if (type.requiresReload) {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.execute(minecraft.levelRenderer::allChanged);
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
        if (COLOR_OVERRIDES.get(type) != null) {
            COLOR_OVERRIDES.put(type, null);

            if (type.requiresReload) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft != null) {
                    minecraft.execute(minecraft.levelRenderer::allChanged);
                }
            }
        }
    }

    /**
     * Reset all color overrides
     */
    public static void resetAllColors() {
        boolean needsReload = COLOR_OVERRIDES.entrySet().stream()
                .filter(entry -> entry.getValue() != null) // Only changed colors
                .peek(entry -> entry.setValue(null)) // Reset them
                .anyMatch(entry -> entry.getKey().requiresReload); // Check if any need reload

        if (needsReload) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft != null) {
                minecraft.execute(minecraft.levelRenderer::allChanged);
            }
        }
    }

    /**
     * Get the current override for a color type
     *
     * @return The override color or null if not set
     */
    public static Integer getColorOverride(ColorType type) {
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
        public final EnumMap<ColorType, Integer> inRangeColors = new EnumMap<>(ColorType.class);
        public final EnumMap<ColorType, Integer> outOfRangeColors = new EnumMap<>(ColorType.class);
        public final EnumMap<ColorType, Boolean> shouldReset = new EnumMap<>(ColorType.class);

        public ProximityColorSet setColor(ColorType type, int inRangeColor, int outOfRangeColor) {
            inRangeColors.put(type, inRangeColor);
            outOfRangeColors.put(type, outOfRangeColor);
            shouldReset.put(type, false); // default: do NOT reset
            return this;
        }

        public ProximityColorSet setColorAutoReset(ColorType type, int inRangeColor) {
            inRangeColors.put(type, inRangeColor);
            shouldReset.put(type, true);
            return this;
        }
    }

    private static final EnumMap<ColorType, Integer> lastApplied = new EnumMap<>(ColorType.class);

    static {
        for (ColorType type : ColorType.values()) {
            lastApplied.put(type, null);
        }
    }

    public static final Map<Object, EnumMap<ColorType, Integer>> activeColorSources = new HashMap<>();

    public static void apply(Object source, boolean inRange, ProximityColorSet colors) {
        EnumMap<ColorType, Integer> newColors = new EnumMap<>(ColorType.class);

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
        EnumMap<ColorType, Integer> resolvedColors = new EnumMap<>(ColorType.class);

        for (ColorType type : ColorType.values()) {
            for (var entry : activeColorSources.values()) {
                Integer color = entry.get(type);
                if (color != null) {
                    resolvedColors.put(type, color);
                    break; // Only the first (highest priority) active one
                }
            }

            Integer resolved = resolvedColors.get(type);
            Integer current = lastApplied.get(type);

            if (!Objects.equals(resolved, current)) {
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