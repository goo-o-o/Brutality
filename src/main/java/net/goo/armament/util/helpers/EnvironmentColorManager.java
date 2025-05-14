package net.goo.armament.util.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.armament.Armament;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.EnumMap;

import static net.goo.armament.util.helpers.ModTooltipHelper.rgbToInt;
@Mod.EventBusSubscriber (modid = Armament.MOD_ID)
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
     * @param type The color type to modify
     * @param r red component
     * @param g green component
     * @param b blue component
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
}