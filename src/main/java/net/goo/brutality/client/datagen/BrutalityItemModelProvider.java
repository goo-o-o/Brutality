package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.goo.brutality.common.item.weapon.axe.Deathsaw;
import net.goo.brutality.common.item.weapon.generic.TheCloud;
import net.goo.brutality.common.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.common.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.common.item.weapon.throwing.Mug;
import net.goo.brutality.common.item.weapon.throwing.StyrofoamCup;
import net.goo.brutality.common.registry.BrutalityBlockFamilies;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class BrutalityItemModelProvider extends ItemModelProvider {
    public BrutalityItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Brutality.MOD_ID, existingFileHelper);
    }

    private static final Set<Class<? extends BrutalityGeoItem>> EXCLUDED_ITEMS =
            Set.of(DullKnifeSword.class, DarkinScythe.class, TheCloud.class,
                    Mug.class, StyrofoamCup.class, Deathsaw.class);


    @Override
    protected void registerModels() {

        registerItemModels();
        registerConcreteVariantModels();

        withExistingParent(BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.getId().getPath()));
        withExistingParent(BrutalityBlocks.TOILET.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.TOILET.getId().getPath()));
        withExistingParent(BrutalityBlocks.URINAL.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.URINAL.getId().getPath()));
        withExistingParent(BrutalityBlocks.UPPER_HVAC.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.UPPER_HVAC.getId().getPath()));
        withExistingParent(BrutalityBlocks.LOWER_HVAC.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.LOWER_HVAC.getId().getPath()));
        withExistingParent(BrutalityBlocks.OLD_AIR_CONDITIONER.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.OLD_AIR_CONDITIONER.getId().getPath()));
        withExistingParent(BrutalityBlocks.EXIT_SIGN.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.EXIT_SIGN.getId().getPath()));
        withExistingParent(BrutalityBlocks.OLD_SERVER_CASING.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.OLD_SERVER_CASING.getId().getPath()));
        withExistingParent(BrutalityBlocks.OLD_SERVER_PANEL.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.OLD_SERVER_PANEL.getId().getPath() + "_one"));

        cubeAll(BrutalityBlocks.PLASTERBOARD.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.PLASTERBOARD.getId().getPath()));

        BrutalityBlocks.FILING_CABINETS.forEach(blockRegistryObject -> {
            String name = blockRegistryObject.getId().getPath();
            getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("builtin/entity")))
                    .texture("particle", modLoc("block/" + name))
                    .transforms()
                    .transform(ItemDisplayContext.GUI)
                    .rotation(30, -135, 0)
                    .scale(0.625f)
                    .end()
                    .transform(ItemDisplayContext.GROUND)
                    .translation(0, 3, 0)
                    .scale(0.25f)
                    .end()
                    .transform(ItemDisplayContext.FIXED)
                    .rotation(0, 180, 0)
                    .scale(0.5f)
                    .end()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(75, 45, 0)
                    .translation(0, 2.5f, 0)
                    .scale(0.375f)
                    .end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, 45, 0)
                    .scale(0.4f)
                    .end();
        });

        withExistingParent(BrutalityBlocks.PUDDLE.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.PUDDLE.getId().getPath())).renderType(mcLoc("translucent"));

        withExistingParent(BrutalityBlocks.SMALL_OFFICE_LIGHT.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.SMALL_OFFICE_LIGHT.getId().getPath()));

        withExistingParent(BrutalityBlocks.LIGHT_GRAY_OFFICE_RUG.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.LIGHT_GRAY_OFFICE_RUG.getId().getPath()));
        withExistingParent(BrutalityBlocks.GRAY_OFFICE_RUG.getId().getPath(),
                modLoc("block/" + BrutalityBlocks.GRAY_OFFICE_RUG.getId().getPath()));


        withExistingParent(ForgeRegistries.ITEMS.getKey(BrutalityBlocks.MANA_CANDLE.get().asItem()).getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", modLoc("item/mana_candle"));

        generateFamilyItems(BrutalityBlockFamilies.SOLIDIFIED_MANA);
    }

    public void generateFamilyItems(BlockFamily family) {
        // Base Block
        Block base = family.getBaseBlock();
        String baseName = name(base);

        // 1. Base Item Model (Standard Cube)
        // In ItemModelProvider, this usually creates a model that parents the block model
        ResourceLocation baseTex = modLoc("block/" + baseName);
        withExistingParent(baseName, baseTex);

        // 2. Variant Loop
        family.getVariants().forEach((variant, block) -> {
            String name = name(block);
            switch (variant) {
                case WALL -> wallInventory(name, baseTex);
                case BUTTON -> buttonInventory(name, baseTex);
                case FENCE -> fenceInventory(name, baseTex);
                case DOOR -> basicItem(block.asItem());
                case TRAPDOOR -> withExistingParent(name, modLoc("block/" + name + "_bottom"));
                default -> withExistingParent(name, modLoc("block/" + name));
            }
        });
    }

    private String name(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }


    protected void registerItemModels() {
        Collection<RegistryObject<Item>> itemsEntries = BrutalityItems.ITEMS.getEntries();

        for (RegistryObject<Item> itemRegistryObject : itemsEntries) {
            Item item = itemRegistryObject.get();
            if (item instanceof BlockItem) continue;

            if (item instanceof BrutalityGeoItem geoItem) {
                if (EXCLUDED_ITEMS.contains(geoItem.getClass())) continue;

                String registryName = geoItem.getRegistryName();
                String category = geoItem.getCategoryAsString();
                ResourceLocation basePath =
                        modLoc("item/" + category + "/" + (
                                geoItem instanceof ArmorItem armorItem ? armorItem.getMaterial().toString().toLowerCase(Locale.ROOT) : registryName) + "/" + registryName);

                ResourceLocation handheldTexture = basePath.withSuffix("_handheld"), inventoryTexture = basePath.withSuffix("_inventory");
                ResourceLocation handheldModel = modLoc("item/" + category + "/" + registryName + "_handheld");
                boolean hasHandheldTexture = existingFileHelper.exists(handheldTexture, TEXTURE);
                boolean hasInventoryTexture = existingFileHelper.exists(inventoryTexture, TEXTURE);
                boolean hasHandheldModel = existingFileHelper.exists(handheldModel, MODEL);


                if (hasHandheldTexture && hasInventoryTexture && hasHandheldModel) {
                    Brutality.LOGGER.info("generateSeparateTransforms({})", registryName);
                    generateSeparateTransforms(registryName, handheldTexture, inventoryTexture, handheldModel);
                } else if (hasHandheldTexture) {
                    if (hasHandheldModel) {
                        Brutality.LOGGER.info("withExistingParentHandheld({})", registryName);
                        withExistingParent(registryName, handheldModel).texture("layer0", handheldTexture);
                    } else {
                        withExistingParent(registryName, "item/handheld").texture("layer0", handheldTexture);
                    }
                } else if (hasInventoryTexture) {
                    Brutality.LOGGER.info("withExistingParentGenerated({})", registryName);
                    withExistingParent(registryName, mcLoc("item/generated")).texture("layer0", inventoryTexture);
                }

                Brutality.LOGGER.warn("Missing textures for {}", item);
            } else {
                Brutality.LOGGER.info("basicItem({})", item);
                basicItem(item);
            }

        }
    }

    protected void registerConcreteVariantModels() {
        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation stairName = BrutalityBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();
            if (stairName != null) {
                withExistingParent(String.valueOf(stairName), ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + stairName.getPath()));
            }

            // Slabs
            ResourceLocation slabName = BrutalityBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();
            if (slabName != null) {
                withExistingParent(String.valueOf(slabName), ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + slabName.getPath()));
            }

        }
    }

    private void generateSeparateTransforms(String name, ResourceLocation handheldTexture, ResourceLocation inventoryTexture, ResourceLocation handheldModel) {
        ItemModelBuilder baseModel = new ItemModelBuilder(modLoc(name + "_inventory"), existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile("minecraft:item/generated"))
                .texture("layer0", inventoryTexture);

        ItemModelBuilder handModelBuilder = new ItemModelBuilder(modLoc(name + "_handheld"), existingFileHelper)
                .parent(new ModelFile.UncheckedModelFile(handheldModel))
                .texture("layer0", handheldTexture);

        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("minecraft:item/handheld"))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(baseModel)                   // ← pre-built
                .perspective(ItemDisplayContext.GROUND, handModelBuilder)
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, handModelBuilder)
                .end();
    }

}


