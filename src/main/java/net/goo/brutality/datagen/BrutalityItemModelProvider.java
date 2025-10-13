package net.goo.brutality.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.*;
import net.goo.brutality.item.weapon.generic.MugItem;
import net.goo.brutality.item.weapon.generic.StyrofoamCupItem;
import net.goo.brutality.item.weapon.generic.TheCloudItem;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
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
import java.util.Objects;

public class BrutalityItemModelProvider extends ItemModelProvider {
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

    public BrutalityItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Brutality.MOD_ID, existingFileHelper);
    }

    private boolean isExcluded(BrutalityGeoItem item) {
        return item instanceof DullKnifeSword || item instanceof DarkinScythe || item instanceof TheCloudItem || item instanceof StyrofoamCupItem || item instanceof MugItem;
    }

    @Override
    protected void registerModels() {
        Collection<RegistryObject<Item>> items = BrutalityModItems.ITEMS.getEntries();

        for (RegistryObject<Item> item : items) {
            Item rawItem = item.get();
            if (rawItem instanceof BrutalityGeoItem armaItem) {
                if (!isExcluded(armaItem)) {
                    generateBrutalityGeoItemModel(armaItem);
                }
            } else if (rawItem instanceof BlockItem blockItem) {
                System.out.println("simpleBlockItem(" + blockItem.getBlock() + ")");
                geoBlockItem(blockItem.getBlock());
            } else {
                System.out.println("generatedItem(" + item + ")");
                generatedItem(item);
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
                ResourceLocation armorItemResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.tryParse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.fromNamespaceAndPath(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", armorItemResLoc).texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(), mcLoc("item/generated")).override().model(new ModelFile.UncheckedModelFile(trimNameResLoc)).predicate(mcLoc("trim_type"), trimValue).end().texture("layer0", ResourceLocation.fromNamespaceAndPath(MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private void simpleBrutalityItem(BrutalityGeoItem item) {
        String identifier = item.getRegistryName();


        withExistingParent(identifier, mcLoc("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                        Brutality.MOD_ID, "item/" +
                                (item instanceof BrutalityCurioItem ? "curio/" : "") + item.getCategoryAsString() + "/" + identifier + "/" + identifier + "_inventory"
                ));
    }


    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Brutality.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory")).texture("texture", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory")).texture("texture", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory")).texture("wall", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), "item/handheld").texture("layer0", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" + item.getId().getPath() + "_inventory"));
    }

    private void generatedItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" + item.getId().getPath() + "_inventory"));
    }

    private ItemModelBuilder geoBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" + item.getId().getPath()));
    }

    public void geoBlockItem(Block block) {
        String path = ForgeRegistries.BLOCKS.getKey(block).getPath();
        withExistingParent(path, ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + path +
                "_handheld")).texture("layer0", ResourceLocation.fromNamespaceAndPath("brutality", "block/" + path));
    }


    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + item.getId().getPath()));
    }

    private void generateBrutalityGeoItemModel(BrutalityGeoItem item) {
        String identifier = item.model(null) != null ? item.model(null) : item.getRegistryName();
//        System.out.println(identifier);
        String folder = Objects.equals(item.category(), BrutalityCategories.ItemType.BLOCK) ? BrutalityCategories.ItemType.BLOCK.toString().toLowerCase() : "item";
        boolean isBlockItem = item instanceof BrutalityBlockItem;
        String category = isBlockItem ? "" : item.getCategoryAsString() + "/";

        ResourceLocation handheldTextureLoc =
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, folder + "/" + (item instanceof BrutalityCurioItem ? "curio/" : "") +
                        category + identifier + (isBlockItem ? "" : "/" + identifier + "_handheld"));

        ResourceLocation geoModelLoc = ResourceLocation.fromNamespaceAndPath(
                Brutality.MOD_ID,
                "geo/" + folder + "/" + category + identifier + (isBlockItem ? "" : "_handheld") + ".geo.json"
        );

        ResourceLocation inventoryTextureLoc = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, folder + "/" + category + identifier + "/" + identifier + "_inventory");


        if (item instanceof BrutalityArmorItem armorItem) {
            generateArmorModel(armorItem);
            System.out.println("Generating Armor Model for " + item);
        } else if (!existingFileHelper.exists(inventoryTextureLoc, ModelProvider.TEXTURE)
                && existingFileHelper.exists(geoModelLoc, PackType.CLIENT_RESOURCES)) {
            System.out.println("Generating Handheld Only Model for " + item);
            generateHandheldOnlyModel(item);
        } else if (existingFileHelper.exists(handheldTextureLoc, ModelProvider.TEXTURE)
                && existingFileHelper.exists(geoModelLoc, PackType.CLIENT_RESOURCES)) {
            System.out.println("Generating Separate Transforms Model for " + item);
            generateSeparateTransformsModel(item);
        } else {
            System.out.println("Generating Simple Item Model for " + item);
            simpleBrutalityItem(item);
        }

    }

    private void generateHandheldOnlyModel(BrutalityGeoItem item) {
        String identifier = item.getRegistryName();
        boolean isBlockItem = item instanceof BlockItem;
        String folder = item.category() == BrutalityCategories.ItemType.BLOCK
                ? BrutalityCategories.ItemType.BLOCK.toString().toLowerCase()
                : "item";
        String category = isBlockItem ? "" : item.getCategoryAsString() + "/";
        String texturePath = isBlockItem ? "" : identifier + "/";
        String curioPath = item instanceof BrutalityCurioItem ? "curio/" : "";

        withExistingParent(
                identifier,
                ResourceLocation.fromNamespaceAndPath(
                        Brutality.MOD_ID,
                        folder + "/" + category + identifier + "_handheld"
                )
        ).texture(
                "layer0",
                ResourceLocation.fromNamespaceAndPath(
                        Brutality.MOD_ID,
                        folder + "/" + curioPath + category + texturePath + identifier + (isBlockItem ? "" : "_handheld")
                )
        );
    }


    private void generateArmorModel(BrutalityArmorItem item) {
        String identifier = item.getRegistryName();


        ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" + item.getCategoryAsString() + "/" + identifier + "_inventory");

        if (existingFileHelper.exists(textureLocation, ModelProvider.TEXTURE)) {

            withExistingParent(identifier + (item instanceof BrutalityArmorItem ? "" : "_inventory"), mcLoc("item/generated")).texture("layer0", textureLocation);
        } else {
            Brutality.LOGGER.warn("Skipping inventory model generation for {}: Texture {} does not exist", identifier, textureLocation);
        }
    }


    private JsonObject createModelWithPerspectives(JsonObject baseModel, String modelLocation) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:item/handheld");
        modelJson.addProperty("loader", "forge:separate_transforms");
        modelJson.add("base", baseModel);

        JsonObject perspectives = new JsonObject();

        for (String perspectiveType : List.of("ground", "thirdperson_lefthand", "thirdperson_righthand", "firstperson_lefthand", "firstperson_righthand")) {
            JsonObject perspective = new JsonObject();
//            perspective.addProperty("parent", "minecraft:item/generated");
            perspective.addProperty("parent", modelLocation);

//            JsonObject textures = new JsonObject();
//            textures.addProperty("layer0", textureLocation);
//            perspective.add("textures", textures);


            perspectives.add(perspectiveType, perspective);
        }

        modelJson.add("perspectives", perspectives);
        return modelJson;
    }

    private void generateSeparateTransformsModel(BrutalityGeoItem item) {
        String identifier = item.getRegistryName();

        String texturePath = "item/" + (item instanceof BrutalityCurioItem ? "curio/" : "") + item.getCategoryAsString() + "/" + (item instanceof BrutalityArmorItem ? "" : (identifier + "/")) + identifier + "_inventory";
        String textureLocation = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, texturePath).toString();
        String handheldModel = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" + (item instanceof BrutalityCurioItem ? "curio/" : "") +
                item.getCategoryAsString() + "/" + identifier + "_handheld").toString();


        JsonObject baseModelJson = withExistingParent(identifier, mcLoc("item/generated")).toJson();
//        baseModelJson.addProperty("parent", "item/generated");
//        baseModelJson.addProperty("loader", "forge:separate_transforms");

        if (item instanceof BrutalityBowItem) {
            // Create the main model with separate_transforms loader

            // Add base model

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", textureLocation);
            baseModelJson.add("textures", textures);

            // Create overrides
            JsonArray overrides = new JsonArray();
            float[] pullValues = {0.0F, 0.65F, 0.9F};

            for (int i = 0; i < 3; i++) {
                String variantModelName = identifier + "_inventory_pull_" + i;
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

                withExistingParent(identifier + "_inventory_pull_" + i, mcLoc("item/generated"));
                generatePullModel(item, i); // These 2 lines handle the pull files
            }

            baseModelJson.add("overrides", overrides);

            JsonObject texture = new JsonObject();
            texture.addProperty("layer0", textureLocation);

            JsonObject modelJson = createModelWithPerspectives(baseModelJson, handheldModel);

            saveModel(identifier, modelJson);
        } else {


            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", textureLocation);
            baseModelJson.add("textures", textures);

            JsonObject modelJson = createModelWithPerspectives(baseModelJson, handheldModel);
            saveModel(identifier, modelJson);
        }
    }


    private void generatePullModel(BrutalityGeoItem item, int pullStage) {
        String identifier = item.getRegistryName();

        String modelName = identifier + "_inventory_pull_" + pullStage;
        String texturePath = "item/" + (item instanceof BrutalityCurioItem ? "curio/" : "") + item.getCategoryAsString() + "/" +
                identifier + "/" + identifier + "_inventory_pull_" + pullStage;
        String handheldModel = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "item/" +
                (item instanceof BrutalityCurioItem ? "curio/" : "") +
                item.getCategoryAsString() + "/" + identifier + "_handheld").toString();

        // Create the root model object
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:item/generated");
        modelJson.addProperty("loader", "forge:separate_transforms");

        // Create the base model
        JsonObject base = new JsonObject();
        base.addProperty("parent", "minecraft:item/generated");

        JsonObject baseTextures = new JsonObject();
        baseTextures.addProperty("layer0", ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, texturePath).toString());
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


