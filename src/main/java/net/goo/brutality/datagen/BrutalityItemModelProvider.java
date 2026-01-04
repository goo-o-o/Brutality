package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.item.weapon.generic.TheCloudItem;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.sword.DullKnifeSword;
import net.goo.brutality.item.weapon.throwing.Mug;
import net.goo.brutality.item.weapon.throwing.StyrofoamCup;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class BrutalityItemModelProvider extends ItemModelProvider {
    public BrutalityItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Brutality.MOD_ID, existingFileHelper);
    }

    private static final Set<Class<? extends BrutalityGeoItem>> EXCLUDED_ITEMS =
            Set.of(DullKnifeSword.class, DarkinScythe.class, TheCloudItem.class,
                    Mug.class, StyrofoamCup.class, Deathsaw.class);


    @Override
    protected void registerModels() {
        registerItemModels();
        registerConcreteVariantModels();

        withExistingParent(BrutalityModBlocks.TOILET.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.TOILET.getId().getPath()));
        withExistingParent(BrutalityModBlocks.URINAL.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.URINAL.getId().getPath()));
        withExistingParent(BrutalityModBlocks.UPPER_HVAC.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.UPPER_HVAC.getId().getPath()));
        withExistingParent(BrutalityModBlocks.LOWER_HVAC.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.LOWER_HVAC.getId().getPath()));
        withExistingParent(BrutalityModBlocks.OLD_AIR_CONDITIONER.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.OLD_AIR_CONDITIONER.getId().getPath()));
        withExistingParent(BrutalityModBlocks.EXIT_SIGN.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.EXIT_SIGN.getId().getPath()));
        withExistingParent(BrutalityModBlocks.OLD_SERVER_CASING.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.OLD_SERVER_CASING.getId().getPath()));
        withExistingParent(BrutalityModBlocks.OLD_SERVER_PANEL.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.OLD_SERVER_PANEL.getId().getPath() + "_one"));

        cubeAll(BrutalityModBlocks.PLASTERBOARD.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.PLASTERBOARD.getId().getPath()));

        BrutalityModBlocks.FILING_CABINETS.forEach(blockRegistryObject -> {
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

        withExistingParent(BrutalityModBlocks.PUDDLE.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.PUDDLE.getId().getPath())).renderType(mcLoc("translucent"));

        withExistingParent(BrutalityModBlocks.SMALL_OFFICE_LIGHT.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.SMALL_OFFICE_LIGHT.getId().getPath()));

        withExistingParent(BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.getId().getPath()));
        withExistingParent(BrutalityModBlocks.GRAY_OFFICE_RUG.getId().getPath(),
                modLoc("block/" + BrutalityModBlocks.GRAY_OFFICE_RUG.getId().getPath()));
    }


    protected void registerItemModels() {
        Collection<RegistryObject<Item>> itemsEntries = BrutalityModItems.ITEMS.getEntries();

        for (RegistryObject<Item> itemRegistryObject : itemsEntries) {
            Item item = itemRegistryObject.get();
            if (item instanceof BlockItem) continue;

            if (item instanceof BrutalityGeoItem geoItem) {
                if (EXCLUDED_ITEMS.contains(geoItem.getClass())) continue;

                String registryName = geoItem.getRegistryName();
                String category = geoItem.getCategoryAsString();
                boolean isCurio = geoItem instanceof ICurioItem;
                ResourceLocation basePath =
                        modLoc("item/" + (isCurio ? "curio/" : "") + category + "/" + (
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
            ResourceLocation stairName = BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();
            if (stairName != null) {
                withExistingParent(String.valueOf(stairName), ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + stairName.getPath()));
            }

            // Slabs
            ResourceLocation slabName = BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();
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


