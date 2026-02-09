package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.custom.CRTMonitorBlock;
import net.goo.brutality.common.block.custom.DustbinBlock;
import net.goo.brutality.common.block.custom.LCDMonitorBlock;
import net.goo.brutality.common.block.custom.OfficeCarpetBlock;
import net.goo.brutality.common.registry.BrutalityBlockFamilies;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
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
        geoBlockWithItem(BrutalityBlocks.WATER_COOLER_BLOCK);
        geoBlockWithItem(BrutalityBlocks.COFFEE_MACHINE_BLOCK);
        geoBlockWithItem(BrutalityBlocks.SUPER_SNIFFER_FIGURE_BLOCK);

        crystalClusterState(BrutalityBlocks.SMALL_MANA_CRYSTAL_BUD.get());
        crystalClusterState(BrutalityBlocks.MEDIUM_MANA_CRYSTAL_BUD.get());
        crystalClusterState(BrutalityBlocks.LARGE_MANA_CRYSTAL_BUD.get());
        crystalClusterState(BrutalityBlocks.MANA_CRYSTAL_CLUSTER.get());


        simpleBlock(BrutalityBlocks.LIQUIFIED_MANA.get(), models().getBuilder("liquid_mana_block").parent(models().getExistingFile(mcLoc("block/water"))));


        for (DyeColor dyeColor : DyeColor.values()) {
            String colorName = dyeColor.getName();
            ResourceLocation texturePath = ResourceLocation.withDefaultNamespace("block/" + colorName + "_concrete");

            // Stairs
            RegistryObject<Block> stair = BrutalityBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal());
            stairsBlock((StairBlock) stair.get(), texturePath, texturePath, texturePath);

            // Slabs
            RegistryObject<Block> slab = BrutalityBlocks.CONCRETE_SLABS.get(dyeColor.ordinal());
            slabBlock((SlabBlock) slab.get(), texturePath, texturePath);
        }

        ModelFile upperHvac = models().getExistingFile(modLoc("block/upper_hvac"));
        horizontalBlock(BrutalityBlocks.UPPER_HVAC.get(), upperHvac);
        ModelFile lowerHvac = models().getExistingFile(modLoc("block/lower_hvac"));
        horizontalBlock(BrutalityBlocks.LOWER_HVAC.get(), lowerHvac);
        ModelFile oldAirCond = models().getExistingFile(modLoc("block/old_air_conditioner"));
        horizontalBlock(BrutalityBlocks.OLD_AIR_CONDITIONER.get(), oldAirCond);
        ModelFile toilet = models().getExistingFile(modLoc("block/toilet"));
        horizontalBlock(BrutalityBlocks.TOILET.get(), toilet);
        ModelFile urinal = models().getExistingFile(modLoc("block/urinal"));
        horizontalBlock(BrutalityBlocks.URINAL.get(), urinal);
        ModelFile oldServerCasing = models().getExistingFile(modLoc("block/old_server_casing"));
        randomRotationVerticalBlock(BrutalityBlocks.OLD_SERVER_CASING.get(), oldServerCasing);
        ModelFile exitSign = models().getExistingFile(modLoc("block/exit_sign"));
        horizontalFaceBlock(BrutalityBlocks.EXIT_SIGN.get(), exitSign);
        ModelFile smallOfficeLight = models().getExistingFile(modLoc("block/small_office_light"));
        horizontalFaceBlock(BrutalityBlocks.SMALL_OFFICE_LIGHT.get(), smallOfficeLight);


        ModelFile oldServerPanelOne = models().getExistingFile(modLoc("block/old_server_panel_one"));
        ModelFile oldServerPanelTwo = models().getExistingFile(modLoc("block/old_server_panel_two"));
        getVariantBuilder(BrutalityBlocks.OLD_SERVER_PANEL.get())
                .forAllStates(state -> {
                    int yRot = (int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
                    return new ConfiguredModel[]{
                            new ConfiguredModel(oldServerPanelOne, 0, yRot, false),
                            new ConfiguredModel(oldServerPanelTwo, 0, yRot, false)
                    };
                });

        ModelFile puddle = models().getExistingFile(modLoc("block/puddle"));
        simpleBlock(BrutalityBlocks.PUDDLE.get(), puddle);
        simpleBlock(BrutalityBlocks.PLASTERBOARD.get());

        simpleBlockWithItem(BrutalityBlocks.TABLE_OF_WIZARDRY.get(), models().getExistingFile(modLoc("block/" + BrutalityBlocks.TABLE_OF_WIZARDRY.getId().getPath())));
        simpleBlockWithItem(BrutalityBlocks.PEDESTAL_OF_WIZARDRY.get(), models().getExistingFile(modLoc("block/" + BrutalityBlocks.PEDESTAL_OF_WIZARDRY.getId().getPath())));
        simpleBlockWithItem(BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get(), models().getExistingFile(modLoc("block/" + BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.getId().getPath())));


        BrutalityBlocks.FILING_CABINETS.forEach(blockRegistryObject -> {
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

        ModelFile crystalBlockModel = models().cubeAll(BrutalityBlocks.MANA_CRYSTAL_BLOCK.getId().getPath(), modLoc("block/" + BrutalityBlocks.MANA_CRYSTAL_BLOCK.getId().getPath()));

        getVariantBuilder(BrutalityBlocks.MANA_CRYSTAL_BLOCK.get())
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(crystalBlockModel)
                        .build()
                );

        MultiPartBlockStateBuilder dustbinBuilder = getMultipartBuilder(BrutalityBlocks.DUSTBIN.get());


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

        getVariantBuilder(BrutalityBlocks.CRT_MONITOR.get())
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


        axisBlockSingleTexture((RotatedPillarBlock) BrutalityBlocks.GRAY_OFFICE_RUG.get(),
                models().modLoc("block/" + BrutalityBlocks.GRAY_OFFICE_CARPET.getId().getPath()));
        axisBlockSingleTexture((RotatedPillarBlock) BrutalityBlocks.LIGHT_GRAY_OFFICE_RUG.get(),
                models().modLoc("block/" + BrutalityBlocks.LIGHT_GRAY_OFFICE_CARPET.getId().getPath()));

        getVariantBuilder(BrutalityBlocks.LCD_MONITOR.get())
                .partialState().with(LCDMonitorBlock.ACTIVE, false)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/lcd_monitor"))).addModel()
                .partialState().with(LCDMonitorBlock.ACTIVE, true)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/lcd_monitor_active"))).addModel();

        getVariantBuilder(BrutalityBlocks.GRAY_OFFICE_CARPET.get())
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/gray_office_carpet"))).addModel()
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/gray_office_carpet"))).rotationY(90).addModel();

        getVariantBuilder(BrutalityBlocks.LIGHT_GRAY_OFFICE_CARPET.get())
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/light_gray_office_carpet"))).addModel()
                .partialState().with(OfficeCarpetBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(models().getExistingFile(modLoc("block/light_gray_office_carpet"))).rotationY(90).addModel();

        createCandleAndCandleCake(BrutalityBlocks.MANA_CANDLE.get(), BrutalityBlocks.MANA_CANDLE_CAKE.get());
        generateFamily(BrutalityBlockFamilies.SOLIDIFIED_MANA);

    }

    public void generateFamily(BlockFamily family) {
        Block base = family.getBaseBlock();
        ResourceLocation texture = blockTexture(base);

        // 1. Generate the base block model and state
        simpleBlock(base);

        // 2. Iterate through the family variants just like vanilla
        family.getVariants().forEach((variant, block) -> {
            switch (variant) {
                case STAIRS -> stairsBlock((StairBlock) block, texture);
                case SLAB -> slabBlock((SlabBlock) block, texture, texture);
                case WALL -> wallBlock((WallBlock) block, texture);
                case BUTTON -> buttonBlock((ButtonBlock) block, texture);
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, texture);
                case FENCE -> fenceBlock((FenceBlock) block, texture);
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, texture);
                case DOOR -> doorBlockWithRenderType((DoorBlock) block,
                        modLoc("block/" + name(block) + "_bottom"),
                        modLoc("block/" + name(block) + "_top"), "cutout");
                case TRAPDOOR -> trapdoorBlockWithRenderType((TrapDoorBlock) block,
                        modLoc("block/" + name(block)), true, "cutout");
            }
        });
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

    private void geoBlockWithItem(RegistryObject<Block> blockRegistryObject) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/block"))
                .texture("particle", modLoc("block/" + name));

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }

    private void crystalClusterState(Block block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        ModelFile model = models().getExistingFile(modLoc("block/" + name));

        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                    .rotationY(((int) dir.toYRot() + 180) % 360)
                    .build();
        });
    }

    private void createCandleAndCandleCake(Block candle, Block candleCake) {
        ResourceLocation texture = modLoc("block/mana_candle");
        ResourceLocation litTexture = modLoc("block/mana_candle_lit");
        // Note: Vanilla candles use the same texture for lit, but you can change it if you wish

        // 1. Create the 8 Block Models for the Candle
        ResourceLocation one = models().withExistingParent("mana_candle_one_candle", "minecraft:block/template_candle")
                .texture("particle", texture)
                .texture("all", texture).getLocation();
        ResourceLocation two = models().withExistingParent("mana_candle_two_candles", "minecraft:block/template_two_candles")
                .texture("particle", texture)
                .texture("all", texture).getLocation();
        ResourceLocation three = models().withExistingParent("mana_candle_three_candles", "minecraft:block/template_three_candles")
                .texture("particle", texture)
                .texture("all", texture).getLocation();
        ResourceLocation four = models().withExistingParent("mana_candle_four_candles", "minecraft:block/template_four_candles")
                .texture("particle", texture)
                .texture("all", texture).getLocation();

        ResourceLocation oneLit = models().withExistingParent("mana_candle_one_candle_lit", "minecraft:block/template_candle")
                .texture("particle", texture)
                .texture("all", litTexture).getLocation();
        ResourceLocation twoLit = models().withExistingParent("mana_candle_two_candles_lit", "minecraft:block/template_two_candles")
                .texture("particle", texture)
                .texture("all", litTexture).getLocation();
        ResourceLocation threeLit = models().withExistingParent("mana_candle_three_candles_lit", "minecraft:block/template_three_candles")
                .texture("particle", texture)
                .texture("all", litTexture).getLocation();
        ResourceLocation fourLit = models().withExistingParent("mana_candle_four_candles_lit", "minecraft:block/template_four_candles")
                .texture("particle", texture)
                .texture("all", litTexture).getLocation();

        // 2. Set up the Candle BlockState Dispatch
        getVariantBuilder(candle).forAllStates(state -> {
            int count = state.getValue(BlockStateProperties.CANDLES);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            ResourceLocation model = switch (count) {
                case 1 -> lit ? oneLit : one;
                case 2 -> lit ? twoLit : two;
                case 3 -> lit ? threeLit : three;
                default -> lit ? fourLit : four;
            };
            return ConfiguredModel.builder().modelFile(models().getExistingFile(model)).build();
        });

        // 3. Create the 2 Block Models for the Candle Cake
        // Vanilla uses a special TextureMapping helper for cakes
        ResourceLocation cakeUnlit = models().withExistingParent("mana_candle_cake", "minecraft:block/template_cake_with_candle")
                .texture("candle", texture)
                .texture("cake", ResourceLocation.parse("block/cake_top"))
                .texture("particle", ResourceLocation.parse("block/cake_top"))
                .getLocation();

        ResourceLocation cakeLit = models().withExistingParent("mana_candle_cake_lit", "minecraft:block/template_cake_with_candle")
                .texture("candle", texture)
                .texture("cake", ResourceLocation.parse("block/cake_top"))
                .texture("particle", ResourceLocation.parse("block/cake_top"))
                .getLocation();

        // 4. Set up the Candle Cake BlockState Dispatch
        getVariantBuilder(candleCake).forAllStates(state -> {
            boolean lit = state.getValue(BlockStateProperties.LIT);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(lit ? cakeLit : cakeUnlit))
                    .build();
        });
    }
}
