package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGeoItem;
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
import net.minecraft.world.level.block.Block;
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
                    Mug.class, StyrofoamCup.class);


    @Override
    protected void registerModels() {
        registerItemModels();
        registerConcreteVariantModels();
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


//                System.out.println(basePath);
//                System.out.println("handheldModelPath: " + handheldModel);
//                System.out.println("hasHandheldModel: " + hasHandheldModel);
//                System.out.println("inventoryTexturePath: " + inventoryTexture);
//                System.out.println("hasInventoryTexture: " + hasInventoryTexture);
//                System.out.println("handheldTexturePath: " + handheldTexture);
//                System.out.println("hasHandheldTexture: " + hasHandheldTexture);
//                System.out.println("-------------------------------------------------------------");

                if (hasHandheldTexture && hasInventoryTexture && hasHandheldModel) {
                    Brutality.LOGGER.info("generateSeparateTransforms({})", registryName);
                    generateSeparateTransforms(registryName, inventoryTexture, handheldModel);
                } else if (hasHandheldTexture) {
                    Brutality.LOGGER.info("withExistingParentHandheld({})", registryName);
                    withExistingParent(registryName, handheldModel).texture("layer0", handheldTexture);
                } else if (hasInventoryTexture) {
                    Brutality.LOGGER.info("withExistingParentGenerated({})", registryName);
                    withExistingParent(registryName, mcLoc("item/generated")).texture("layer0", inventoryTexture);
                }
            } else {
                basicItem(item);
            }

        }
    }

    protected void registerConcreteVariantModels() {
        for (DyeColor dyeColor : DyeColor.values()) {
            ResourceLocation stairName =  BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal()).getId();
            withExistingParent(String.valueOf(stairName), ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + stairName.getPath()));

            // Slabs
            ResourceLocation slabName =  BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal()).getId();
            withExistingParent(String.valueOf(slabName), ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + slabName.getPath()));

        }
    }

    private void generateSeparateTransforms(String name, ResourceLocation inventoryTexture, ResourceLocation handheldModel) {
        ItemModelBuilder baseModel = getBuilder(name + "_base")
                .parent(new ModelFile.UncheckedModelFile("minecraft:item/generated"))
                .texture("layer0", inventoryTexture);

        ItemModelBuilder handModelBuilder = getBuilder(name + "_handheld")
                .parent(new ModelFile.UncheckedModelFile(handheldModel));

        getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("minecraft:item/handheld"))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(baseModel)                                      // ← pre-built
                .perspective(ItemDisplayContext.GROUND, handModelBuilder)
                .perspective(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, handModelBuilder)
                .perspective(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, handModelBuilder)
                .end();
    }

}


