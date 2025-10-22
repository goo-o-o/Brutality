package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityBlockStateProvider extends BlockStateProvider {
    public BrutalityBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Brutality.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BrutalityModBlocks.WATER_COOLER_BLOCK);
        blockWithItem(BrutalityModBlocks.COFFEE_MACHINE_BLOCK);
        blockWithItem(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK);

        for (DyeColor dyeColor : DyeColor.values()) {
            String colorName = dyeColor.getName();
            ResourceLocation texturePath = ResourceLocation.withDefaultNamespace("block/" + colorName + "_concrete");

            // Stairs
            RegistryObject<Block> stair = BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal());
            stairsBlock((StairBlock) stair.get(), texturePath, texturePath, texturePath);

            // Slabs
            RegistryObject<Block> slab = BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal());
            slabBlock((SlabBlock) slab.get(), texturePath, texturePath);
        }

    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("particle", modLoc("block/" + name));

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }
}
