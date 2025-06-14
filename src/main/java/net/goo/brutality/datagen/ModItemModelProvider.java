package net.goo.brutality.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.item.base.BrutalityBowItem;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.weapon.custom.DullKnifeDagger;
import net.goo.brutality.item.weapon.custom.ExcaliburSword;
import net.goo.brutality.registry.ModItems;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

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
        super(output, Brutality.MOD_ID, existingFileHelper);
    }

    private boolean isExcluded(BrutalityGeoItem item) {
        return item instanceof ExcaliburSword || item instanceof DullKnifeDagger;
    }

    @Override
    protected void registerModels() {
        Collection<RegistryObject<Item>> items = ModItems.ITEMS.getEntries();

        // Iterate through all items
        for (RegistryObject<Item> item : items) {
            // Check if the item is an instance of ArmaGeoItem and not excluded
            if (item.get() instanceof BrutalityGeoItem armaItem) {
                if (!isExcluded(armaItem)) {
                    // Generate models for the item
                    generateArmaGeoItemModel(armaItem);
                }
            }

        }

    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = Brutality.MOD_ID; // Change this to your mod id

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
                getBuilder(currentTrimName).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", armorItemResLoc).texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(), mcLoc("item/generated")).override().model(new ModelFile.UncheckedModelFile(trimNameResLoc)).predicate(mcLoc("trim_type"), trimValue).end().texture("layer0", new ResourceLocation(MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private void simpleBrutalityItem(BrutalityGeoItem item) {
        withExistingParent(item.geoIdentifier(), mcLoc("item/generated"))
                .texture("layer0", new ResourceLocation(
                        Brutality.MOD_ID,
                        "item/" + item.getCategoryAsString() + "/" + item.geoIdentifier() + "/" + item.geoIdentifier()
                ));
    }


    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Brutality.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory")).texture("texture", new ResourceLocation(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory")).texture("texture", new ResourceLocation(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory")).texture("wall", new ResourceLocation(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld")).texture("layer0", new ResourceLocation(Brutality.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Brutality.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Brutality.MOD_ID, "block/" + item.getId().getPath()));
    }

    private void generateArmaGeoItemModel(BrutalityGeoItem item) {
        ResourceLocation handheldTextureLoc =
                new ResourceLocation(Brutality.MOD_ID, "item/" + item.getCategoryAsString() + "/" + item.geoIdentifier() + "/" + item.geoIdentifier() + "_handheld");

        // Generate the missing models
        if (item instanceof BrutalityArmorItem armorItem) {
            generateArmorModel(armorItem);
        } else if (existingFileHelper.exists(handheldTextureLoc, ModelProvider.TEXTURE)){
            generateSeparateTransformsModel(item);
        } else {
            simpleBrutalityItem(item);
        }
    }


    private void generateArmorModel(BrutalityArmorItem item) {


        ResourceLocation textureLocation = new ResourceLocation(Brutality.MOD_ID, "item/" + item.getCategoryAsString() + "/" + item.geoIdentifier() + "_inventory");

        if (existingFileHelper.exists(textureLocation, ModelProvider.TEXTURE)) {

            withExistingParent(item.geoIdentifier() + (item instanceof BrutalityArmorItem ? "" : "_inventory"), mcLoc("item/generated")).texture("layer0", textureLocation);
        } else {
            Brutality.LOGGER.warn("Skipping inventory model generation for {}: Texture {} does not exist", item.geoIdentifier(), textureLocation);
        }
    }


    private JsonObject createModelWithPerspectives(JsonObject baseModel, String textureLocation) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:item/handheld");
        modelJson.addProperty("loader", "forge:separate_transforms");
        modelJson.add("base", baseModel);

        JsonObject perspectives = new JsonObject();

        for (String perspectiveType : List.of("gui", "fixed")) {
            JsonObject perspective = new JsonObject();
            perspective.addProperty("parent", "minecraft:item/generated");

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", textureLocation);
            perspective.add("textures", textures);

            perspectives.add(perspectiveType, perspective);
        }

        modelJson.add("perspectives", perspectives);
        return modelJson;
    }

    private void generateSeparateTransformsModel(BrutalityGeoItem item) {
        String texturePath = "item/" + item.getCategoryAsString() + "/" + (item instanceof BrutalityArmorItem ? "" : (item.geoIdentifier() + "/")) + item.geoIdentifier() + "_inventory";
        String textureLocation = new ResourceLocation(Brutality.MOD_ID, texturePath).toString();
        String handheldModel = new ResourceLocation(Brutality.MOD_ID, "item/" + item.getCategoryAsString() + "/" + item.geoIdentifier() + "_handheld").toString();

        if (item instanceof BrutalityBowItem) {
            // Create the main model with separate_transforms loader

            JsonObject baseModelJson = withExistingParent(item.geoIdentifier(), mcLoc("item/handheld")).toJson();


            baseModelJson.addProperty("parent", "minecraft:item/handheld");
            baseModelJson.addProperty("loader", "forge:separate_transforms");

            // Add base model
            JsonObject base = new JsonObject();
            base.addProperty("parent", handheldModel);
            baseModelJson.add("base", base);

            // Create overrides
            JsonArray overrides = new JsonArray();
            float[] pullValues = {0.0F, 0.65F, 0.9F};

            for (int i = 0; i < 3; i++) {
                String variantModelName = item.geoIdentifier() + "_inventory_pull_" + i;
                JsonObject texture = new JsonObject();

// How can i generate this here
                texture.addProperty("layer0", textureLocation + "_pull_" + i);

                // Add override
                JsonObject predicate = new JsonObject();
                predicate.addProperty("pulling", 1);
                predicate.addProperty("pull", pullValues[i]);

                JsonObject override = new JsonObject();
                override.add("predicate", predicate);
                override.addProperty("model", Brutality.MOD_ID + ":item/" + variantModelName);
                overrides.add(override);

                withExistingParent(item.geoIdentifier() + "_inventory_pull_" + i, mcLoc("item/generated"));
                generatePullModel(item, i); // These 2 lines handle the pull files
            }
            baseModelJson.add("overrides", overrides);

            JsonObject perspectives = new JsonObject();
            JsonObject texture = new JsonObject();
            texture.addProperty("layer0", textureLocation);
            JsonObject gui = new JsonObject();
            JsonObject fixed = new JsonObject();
            gui.add("textures", texture);
            gui.addProperty("parent", "item/generated");
            fixed.add("textures", texture);
            fixed.addProperty("parent", "item/generated");
            perspectives.add("fixed", fixed);
            perspectives.add("gui", gui);
            baseModelJson.add("perspectives", perspectives);

            saveModel(item.geoIdentifier(), baseModelJson);
        } else {
            JsonObject baseModelJson = withExistingParent(item.geoIdentifier(), mcLoc("item/handheld")).toJson();
            baseModelJson.addProperty("parent", handheldModel);
            JsonObject modelJson = createModelWithPerspectives(baseModelJson, textureLocation);
            saveModel(item.geoIdentifier(), modelJson);
        }
    }


    private void generatePullModel(BrutalityGeoItem item, int pullStage) {
        String modelName = item.geoIdentifier() + "_inventory_pull_" + pullStage;
        String texturePath = "item/" + item.getCategoryAsString() + "/" +
                item.geoIdentifier() + "/" + item.geoIdentifier() + "_inventory_pull_" + pullStage;
        String handheldModel = new ResourceLocation(Brutality.MOD_ID,
                "item/" + item.getCategoryAsString() + "/" + item.geoIdentifier() + "_handheld").toString();

        // Create the root model object
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:item/generated");
        modelJson.addProperty("loader", "forge:separate_transforms");

        // Create the base model
        JsonObject base = new JsonObject();
        base.addProperty("parent", "minecraft:item/generated");

        JsonObject baseTextures = new JsonObject();
        baseTextures.addProperty("layer0", new ResourceLocation(Brutality.MOD_ID, texturePath).toString());
        base.add("textures", baseTextures);

        modelJson.add("base", base);

        // Create perspectives
        JsonObject perspectives = new JsonObject();

        String[] perspectiveNames = {
                "ground",
                "thirdperson_lefthand",
                "thirdperson_righthand",
                "firstperson_lefthand",
                "firstperson_righthand"
        };

        for (String perspective : perspectiveNames) {
            JsonObject perspectiveObj = new JsonObject();
            perspectiveObj.addProperty("parent", handheldModel);
            perspectives.add(perspective, perspectiveObj);
        }

        modelJson.add("perspectives", perspectives);

        // Save the model
        saveModel(modelName, modelJson);
    }

    private void saveModel(String itemName, JsonObject modelJson) {
        Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(Brutality.MOD_ID + "/models/item/" + itemName + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, modelJson.toString());
            Brutality.LOGGER.info("Saved model for {} at {}", itemName, path);
        } catch (IOException e) {
            Brutality.LOGGER.error("Failed to save model for {}", itemName, e);
        }
    }

}


