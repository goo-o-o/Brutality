package net.goo.armament.datagen;

import com.google.gson.JsonObject;
import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.item.custom.ExcaliburSword;
import net.goo.armament.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();

    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Armament.MOD_ID, existingFileHelper);
    }

    private boolean isArmaGeoItem(RegistryObject<Item> item) {
        return item.get() instanceof ArmaGeoItem;
    }

    private boolean isExcluded(ArmaGeoItem item) {
        return item instanceof ExcaliburSword;
    }

    @Override
    protected void registerModels() {
        Collection<RegistryObject<Item>> items = ModItems.ITEMS.getEntries();

        // Iterate through all items
        for (RegistryObject<Item> item : items) {
            // Check if the item is an instance of ArmaGeoItem and not excluded
            if (item.get() instanceof ArmaGeoItem armaItem) {
                if (!isExcluded(armaItem)) {
                    // Generate models for the item
                    generateArmaGeoItemModel(armaItem);
                }
            }

        }

    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = Armament.MOD_ID; // Change this to your mod id

        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Armament.MOD_ID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Armament.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(Armament.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(Armament.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(Armament.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Armament.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Armament.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Armament.MOD_ID, "block/" + item.getId().getPath()));
    }

    private void generateArmaGeoItemModel(ArmaGeoItem item) {
        ResourceLocation modelLocation = new ResourceLocation(Armament.MOD_ID, "item/" + item.geoIdentifier());

        // Generate the missing models
        generateInventoryModel(item);
        generateSeparateTransformsModel(item);
    }


    private void generateInventoryModel(ArmaGeoItem item) {
        ResourceLocation textureLocation = new ResourceLocation(Armament.MOD_ID, "item/" + item.geoIdentifier() + "_inventory");
        if (existingFileHelper.exists(textureLocation, ModelProvider.TEXTURE)) {
            withExistingParent(item.geoIdentifier() + "_inventory", mcLoc("item/handheld"))
                    .texture("layer0", textureLocation);
        } else {
            Armament.LOGGER.warn("Skipping inventory model generation for {}: Texture {} does not exist", item.geoIdentifier(), textureLocation);
        }
    }

    private void generateSeparateTransformsModel(ArmaGeoItem item) {
        String handHeldModel = new ResourceLocation(Armament.MOD_ID, "item/" + item.geoIdentifier() + "_handheld").toString();
        String inventoryModel = new ResourceLocation(Armament.MOD_ID, "item/" + item.geoIdentifier() + "_inventory").toString();

        // Create the base model (default perspective)
        JsonObject baseModelJson = withExistingParent(item.geoIdentifier(), mcLoc("item/handheld")).toJson();

        baseModelJson.addProperty("parent", handHeldModel);

        // Create the perspectives object
        JsonObject perspectives = new JsonObject();

        // GUI perspective
        JsonObject guiModelJson = new JsonObject();
        guiModelJson.addProperty("parent", inventoryModel);

        perspectives.add("gui", guiModelJson);

        // Ground perspective
        JsonObject groundModelJson = new JsonObject();
        groundModelJson.addProperty("parent", handHeldModel);

        perspectives.add("ground", groundModelJson);

        // Fixed perspective
        JsonObject fixedModelJson = new JsonObject();
        fixedModelJson.addProperty("parent", inventoryModel);

        perspectives.add("fixed", fixedModelJson);

        // Create the main JSON object
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:item/handheld");
        modelJson.addProperty("loader", "forge:separate_transforms");
        modelJson.add("base", baseModelJson);
        modelJson.add("perspectives", perspectives);

        // Save the JSON file
        saveModel(item.geoIdentifier(), modelJson);


    }

    private void saveModel(String itemName, JsonObject modelJson) {
        Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(Armament.MOD_ID + "/models/item/" + itemName + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, modelJson.toString().getBytes(StandardCharsets.UTF_8));
            Armament.LOGGER.info("Saved model for {} at {}", itemName, path);
        } catch (IOException e) {
            Armament.LOGGER.error("Failed to save model for {}", itemName, e);
        }
    }

    private boolean modelExists(ResourceLocation modelLocation) {
        return existingFileHelper.exists(modelLocation, PackType.CLIENT_RESOURCES, ".json", "models");
    }
}


