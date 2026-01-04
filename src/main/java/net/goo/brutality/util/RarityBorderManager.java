package net.goo.brutality.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import net.goo.brutality.Brutality;
import net.goo.brutality.registry.ModRarities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Rarity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RarityBorderManager extends SimpleJsonResourceReloadListener {
    private final Map<Pair<Rarity, BorderType>, BorderData> borderDataMap = new ConcurrentHashMap<>() {};
    private static final String DIRECTORY = "textures/rarity_borders";
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

                Rarity rarity = Rarity.valueOf(parts[0].toUpperCase());
                BorderType type = BorderType.valueOf(parts[1].toUpperCase());

                int frameWidth = GsonHelper.getAsInt(json, "frame_width");
                int frameHeight = GsonHelper.getAsInt(json, "frame_height");
                int cornerSize = GsonHelper.getAsInt(json, "corner_size");
                int frameTime = GsonHelper.getAsInt(json, "frame_time", 2);
                boolean colorShift = GsonHelper.getAsBoolean(json, "color_shift", true);

                ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                        resourceLocation.getNamespace(),
                        "textures/rarity_borders/" + path + ".png"
                );

                // Compute frame count from texture
                int frameCount = computeFrameCount(resourceManager, texture, frameHeight);

                BorderData data = new BorderData(
                        rarity, type, frameWidth, frameHeight,
                        cornerSize, frameTime, texture,
                        resourceLocation, frameCount, colorShift
                );

                borderDataMap.put(Pair.of(rarity, type), data);

            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to load border: {}", resourceLocation, e);
            }
        });

        updateRarityData();
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


    private void updateRarityData() {
        for (ModRarities.RarityData rarityData : ModRarities.RarityData.values()) {
            BorderData idleBorder = getBorderData(rarityData.rarity, BorderType.IDLE);
            BorderData openBorder = getBorderData(rarityData.rarity, BorderType.OPEN);

            rarityData.setBorders(idleBorder, openBorder != null ? openBorder : idleBorder);
        }
    }

    public BorderData getBorderData(Rarity rarity, BorderType type) {
        return borderDataMap.get(Pair.of(rarity, type));
    }

    public BorderData getIdleBorder(Rarity rarity) {
        return getBorderData(rarity, BorderType.IDLE);
    }

    public BorderData getOpenBorder(Rarity rarity) {
        return getBorderData(rarity, BorderType.OPEN);
    }

    public record BorderData(
            Rarity rarity,
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