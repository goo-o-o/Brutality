package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.custom.CRTMonitorBlock;
import net.goo.brutality.block.custom.DustbinBlock;
import net.goo.brutality.block.custom.LCDMonitorBlock;
import net.goo.brutality.block.custom.OfficeCarpetBlock;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityBlockStateProvider extends BlockStateProvider {
    public BrutalityBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Brutality.MOD_ID, exFileHelper);
    }

    private static final String[] WORDS = {
            "", "one", "two", "three", "four", "five", "six", "seven",
            "eight", "nine", "ten", "eleven", "twelve", "thirteen",
            "fourteen", "fifteen", "sixteen"
    };

    private static String toWord(int i) {
        return (i >= 1 && i <= 16) ? WORDS[i] : String.valueOf(i);
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
        ModelFile toilet = models().getExistingFile(modLoc("block/toilet"));
        horizontalBlock(BrutalityModBlocks.TOILET.get(), toilet);
        ModelFile urinal = models().getExistingFile(modLoc("block/urinal"));
        horizontalBlock(BrutalityModBlocks.URINAL.get(), urinal);
        ModelFile oldServerCasing = models().getExistingFile(modLoc("block/old_server_casing"));
        randomRotationVerticalBlock(BrutalityModBlocks.OLD_SERVER_CASING.get(), oldServerCasing);
        ModelFile exitSign = models().getExistingFile(modLoc("block/exit_sign"));
        horizontalFaceBlock(BrutalityModBlocks.EXIT_SIGN.get(), exitSign);
        ModelFile smallOfficeLight = models().getExistingFile(modLoc("block/small_office_light"));
        horizontalFaceBlock(BrutalityModBlocks.SMALL_OFFICE_LIGHT.get(), smallOfficeLight);


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
        simpleBlock(BrutalityModBlocks.PLASTERBOARD.get());

        BrutalityModBlocks.FILING_CABINETS.forEach(blockRegistryObject -> {
            getVariantBuilder(blockRegistryObject.get())
                    .forAllStates(state -> {
                        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                        int yRot = (dir.get2DDataValue() + 1) % 4 * 90;
                        assert blockRegistryObject.getId() != null;
                        return ConfiguredModel.builder().modelFile(
                                        models().getExistingFile(modLoc("block/" + blockRegistryObject.getId().getPath())))
                                .rotationY(yRot).build();
                    });

        });

        MultiPartBlockStateBuilder dustbinBuilder = getMultipartBuilder(BrutalityModBlocks.DUSTBIN.get());


        dustbinBuilder.part()
                .modelFile(models().getExistingFile(modLoc("block/dustbin/none")))
                .addModel()
                .end();

        for (int i = 1; i <= 16; i++) {
            String modelName = i == 16 ? "fifteen" : toWord(i);
            dustbinBuilder.part()
                    .modelFile(models().getExistingFile(modLoc("block/dustbin/" + modelName)))
                    .addModel()
                    .condition(DustbinBlock.PAPERS, i)
                    .end();
        }

        getVariantBuilder(BrutalityModBlocks.CRT_MONITOR.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(CRTMonitorBlock.FACING);
                    boolean active = state.getValue(CRTMonitorBlock.ACTIVE);
                    String model = active ? "crt_monitor_active" : "crt_monitor";

                    ConfiguredModel.Builder<?> modelFile = ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/" + model)));

                    if (facing != Direction.SOUTH) {
                        int rotation = switch (facing) {
                            case NORTH -> 180;
                            case EAST -> 270;
                            case WEST -> 90;
                            default -> 0;
                        };
                        modelFile.rotationY(rotation);
                    }

                    return modelFile.build();
                });


        axisBlockSingleTexture((RotatedPillarBlock) BrutalityModBlocks.GRAY_OFFICE_RUG.get(),
                models().modLoc("block/" + BrutalityModBlocks.GRAY_OFFICE_CARPET.getId().getPath()));
        axisBlockSingleTexture((RotatedPillarBlock) BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.get(),
                models().modLoc("block/" + BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.getId().getPath()));

        getVariantBuilder(BrutalityModBlocks.LCD_MONITOR.get())
                .partialState().with(LCDMonitorBlock.ACTIVE, false)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/lcd_monitor"))).addModel()
                .partialState().with(LCDMonitorBlock.ACTIVE, true)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/lcd_monitor_active"))).addModel();

        getVariantBuilder(BrutalityModBlocks.GRAY_OFFICE_CARPET.get())
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/gray_office_carpet"))).addModel()
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/gray_office_carpet"))).rotationY(90).addModel();

        getVariantBuilder(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get())
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/light_gray_office_carpet"))).addModel()
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/light_gray_office_carpet"))).rotationY(90).addModel();

    }

    private void randomRotationVerticalBlock(Block block, ModelFile modelFile) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.allYRotations(modelFile, 0, false));
    }

    public void axisBlockSingleTexture(RotatedPillarBlock block, ResourceLocation texture) {
        axisBlock(block,
                models().cubeColumn(name(block), texture, texture),
                models().cubeColumnHorizontal(name(block), texture, texture));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("particle", modLoc("block/" + name));

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }
}
