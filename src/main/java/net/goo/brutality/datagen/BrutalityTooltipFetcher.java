package net.goo.brutality.datagen;

import com.google.gson.*;
import net.goo.brutality.Brutality;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityTooltipFetcher {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path generatedRoot;

    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent event) {
        String projectDir = System.getProperty("user.dir");

        if (projectDir.endsWith("run")) {
            // Go up one level to the project root
            projectDir = Paths.get(projectDir).getParent().toString();
            Brutality.LOGGER.info("Detected 'run' folder, moving up to: {}", projectDir);
        }

        generatedRoot = Paths.get(projectDir, "src/generated/");

        event.enqueueWork(() -> {
            try {
                updateAllItemFiles();
            } catch (Exception e) {
                Brutality.LOGGER.error("Failed to update generated item files with tooltips", e);
            }
        });
    }

    private static void updateAllItemFiles() {
        ForgeRegistries.ITEMS.forEach(item -> {
            String modId = item.getCreatorModId(item.getDefaultInstance());

            if (!BrutalityDataFetcher.shouldProcess(modId)) return;
            Path filePath = generatedRoot.resolve("resources").resolve("data")
                    .resolve(modId)
                    .resolve("stats")
                    .resolve("item")
                    .resolve(item + ".json");

            try {
                // Read the JSON file
                JsonObject json;
                try (FileReader reader = new FileReader(filePath.toFile())) {
                    json = JsonParser.parseReader(reader).getAsJsonObject();
                }

                ItemStack stack = item.getDefaultInstance();

                stack.hideTooltipPart(ItemStack.TooltipPart.ENCHANTMENTS);
                stack.hideTooltipPart(ItemStack.TooltipPart.UPGRADES);
                stack.hideTooltipPart(ItemStack.TooltipPart.CAN_DESTROY);
                stack.hideTooltipPart(ItemStack.TooltipPart.CAN_PLACE);
                stack.hideTooltipPart(ItemStack.TooltipPart.UNBREAKABLE);
                stack.hideTooltipPart(ItemStack.TooltipPart.DYE);
                stack.hideTooltipPart(ItemStack.TooltipPart.MODIFIERS);


                List<String> actualTooltips = stack.getTooltipLines(null, TooltipFlag.NORMAL)
                        .stream()
                        .map(Component::getString)
                        .skip(1)
                        .toList();

                JsonArray existingTooltips = json.has("tooltips") ?
                        json.getAsJsonArray("tooltips") : new JsonArray();

                JsonArray newTooltipsArray = new JsonArray();
                actualTooltips.forEach(newTooltipsArray::add);

                if (!existingTooltips.equals(newTooltipsArray)) {
                    json.add("tooltips", newTooltipsArray);

                    try (FileWriter writer = new FileWriter(filePath.toFile())) {
                        GSON.toJson(json, writer);
                        Brutality.LOGGER.debug("Updated tooltips for: {}", item);
                    }
                }

            } catch (Exception e) {
            }

        });
    }

}