package net.goo.brutality.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RarityBorderManager extends SimpleJsonResourceReloadListener {
    private final Map<Pair<String, BorderType>, BorderData> borderDataMap = new ConcurrentHashMap<>();

    private static final String DIRECTORY = "textures/gui/tooltip_borders";
    private static RarityBorderManager INSTANCE;

    public RarityBorderManager() {
        super(new Gson(), DIRECTORY);
        INSTANCE = this;
    }

    public static RarityBorderManager getInstance() {
        return INSTANCE;
    }

    public static void setInstance(RarityBorderManager instance) {
        INSTANCE = instance;
    }

    public enum BorderType {
        OPEN, IDLE
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        borderDataMap.clear();

        jsons.forEach((resourceLocation, jsonElement) -> {
            try {
                JsonObject json = jsonElement.getAsJsonObject();
                String path = resourceLocation.getPath();
                String[] parts = path.split("/");

                if (parts.length != 2) return;

                String identifier = parts[0].toLowerCase(Locale.ROOT);
                BorderType type = BorderType.valueOf(parts[1].toUpperCase(Locale.ROOT));

                int frameWidth = GsonHelper.getAsInt(json, "frame_width");
                int frameHeight = GsonHelper.getAsInt(json, "frame_height");
                int cornerSize = GsonHelper.getAsInt(json, "corner_size");
                int frameTime = GsonHelper.getAsInt(json, "frame_time", 2);
                boolean colorShift = GsonHelper.getAsBoolean(json, "color_shift", true);

                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                        resourceLocation.getNamespace(),
                        DIRECTORY + "/" + path + ".png"
                );

                // Compute frame count from texture
                int frameCount = computeFrameCount(resourceManager, texture, frameHeight);

                BorderData data = new BorderData(
                        identifier, type, frameWidth, frameHeight, // BorderData record needs updating too
                        cornerSize, frameTime, texture,
                        resourceLocation, frameCount, colorShift
                );

                borderDataMap.put(Pair.of(identifier, type), data);
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load border: {}", resourceLocation, e);
            }
        });

        updateColorDataBorders();
    }


    private int computeFrameCount(ResourceManager resourceManager, ResourceLocation texture, int frameHeight) {
        try {
            Resource resource = resourceManager.getResource(texture).orElseThrow();
            NativeImage image = NativeImage.read(resource.open());
            int height = image.getHeight();
            image.close();
            return height / frameHeight;
        } catch (Exception e) {
            return 1; // Default to 1 frame
        }
    }


    private void updateColorDataBorders() {
        // Loop through your RarityData enum directly
        for (ColorUtils.ColorData data : ColorUtils.ColorData.values()) {
            // Use the enum name (e.g., "LEGENDARY") to look up the border
            String name = data.name().toLowerCase(Locale.ROOT);

            BorderData idleBorder = getBorderData(name, BorderType.IDLE);
            BorderData openBorder = getBorderData(name, BorderType.OPEN);

            // Assign the borders directly to the RarityData instance
            data.setBorders(idleBorder, openBorder != null ? openBorder : idleBorder);
        }
    }

    public BorderData getBorderData(String identifier, BorderType type) {
        return borderDataMap.get(Pair.of(identifier, type));
    }

    public BorderData getIdleBorder(String identifier) {
        return getBorderData(identifier, BorderType.IDLE);
    }

    public BorderData getOpenBorder(String identifier) {
        return getBorderData(identifier, BorderType.OPEN);
    }

    public record BorderData(
            String rarity,
            BorderType type,
            int frameWidth,
            int frameHeight,
            int cornerSize,
            int frameTime,
            ResourceLocation texture,
            ResourceLocation source,
            int frameCount,
            boolean colorShift
    ) {
        public boolean hasAnimation() {
            return frameCount > 1;
        }

        public int getAnimationDurationTicks() {
            return frameCount * frameTime;
        }

        public int getFrameTimeMs() {
            return frameTime * 50;
        }

        public int getFrameForTime(long timeMs) {
            if (frameCount <= 1) return 0;
            return (int) ((timeMs / getFrameTimeMs()) % frameCount);
        }

        public int getAnimationDurationMs() {
            return getAnimationDurationTicks() * 50;
        }
    }
}