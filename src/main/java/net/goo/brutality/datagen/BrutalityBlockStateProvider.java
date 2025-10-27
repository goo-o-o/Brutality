package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
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

        ModelFile upperHvac = models().getExistingFile(modLoc("block/upper_hvac"));
        horizontalBlock(BrutalityModBlocks.UPPER_HVAC.get(), upperHvac);
        ModelFile lowerHvac = models().getExistingFile(modLoc("block/lower_hvac"));
        horizontalBlock(BrutalityModBlocks.LOWER_HVAC.get(), lowerHvac);
        ModelFile oldAirCond = models().getExistingFile(modLoc("block/old_air_conditioner"));
        horizontalBlock(BrutalityModBlocks.OLD_AIR_CONDITIONER.get(), oldAirCond);
        ModelFile oldServerCasing = models().getExistingFile(modLoc("block/old_server_casing"));
        randomRotationVerticalBlock(BrutalityModBlocks.OLD_SERVER_CASING.get(), oldServerCasing);
        ModelFile exitSign = models().getExistingFile(modLoc("block/exit_sign"));
        horizontalFaceBlock(BrutalityModBlocks.EXIT_SIGN.get(), exitSign);

        ModelFile oldServerPanelOne = models().getExistingFile(modLoc("block/old_server_panel_one"));
        ModelFile oldServerPanelTwo = models().getExistingFile(modLoc("block/old_server_panel_two"));
        getVariantBuilder(BrutalityModBlocks.OLD_SERVER_PANEL.get())
                .forAllStates(state -> {
                    int yRot = (int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
                    return new ConfiguredModel[]{
                            new ConfiguredModel(oldServerPanelOne, 0, yRot, false),
                            new ConfiguredModel(oldServerPanelTwo, 0, yRot, false)
                    };
                });

        ModelFile puddle = models().getExistingFile(modLoc("block/puddle"));
        simpleBlock(BrutalityModBlocks.PUDDLE.get(), puddle);

    }

    private void randomRotationVerticalBlock(Block block, ModelFile modelFile) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.allYRotations(modelFile, 0, false));
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("particle", modLoc("block/" + name));

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }
}
