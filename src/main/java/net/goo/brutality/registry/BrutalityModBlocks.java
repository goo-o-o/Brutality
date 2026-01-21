package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.HorizontalDirectionalBlock;
import net.goo.brutality.block.custom.*;
import net.mcreator.terramity.init.TerramityModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class BrutalityModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Brutality.MOD_ID);

    public static List<RegistryObject<Block>> CONCRETE_SLABS;
    public static List<RegistryObject<Block>> CONCRETE_STAIRS;
    public static List<Block> CONCRETE_BLOCKS;
    public static List<RegistryObject<Block>> FILING_CABINETS;

    // Block item registration moved to BrutalityModItems (better organization)
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

        FILING_CABINETS = List.of(
                BrutalityModBlocks.WHITE_FILING_CABINET,
                BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET,
                BrutalityModBlocks.GRAY_FILING_CABINET
        );

        CONCRETE_BLOCKS = List.of(
                Blocks.WHITE_CONCRETE,
                Blocks.ORANGE_CONCRETE,
                Blocks.MAGENTA_CONCRETE,
                Blocks.LIGHT_BLUE_CONCRETE,
                Blocks.YELLOW_CONCRETE,
                Blocks.LIME_CONCRETE,
                Blocks.PINK_CONCRETE,
                Blocks.GRAY_CONCRETE,
                Blocks.LIGHT_GRAY_CONCRETE,
                Blocks.CYAN_CONCRETE,
                Blocks.PURPLE_CONCRETE,
                Blocks.BLUE_CONCRETE,
                Blocks.BROWN_CONCRETE,
                Blocks.GREEN_CONCRETE,
                Blocks.RED_CONCRETE,
                Blocks.BLACK_CONCRETE
        );

        CONCRETE_SLABS = List.of(
                BrutalityModBlocks.WHITE_CONCRETE_SLAB,
                BrutalityModBlocks.ORANGE_CONCRETE_SLAB,
                BrutalityModBlocks.MAGENTA_CONCRETE_SLAB,
                BrutalityModBlocks.LIGHT_BLUE_CONCRETE_SLAB,
                BrutalityModBlocks.YELLOW_CONCRETE_SLAB,
                BrutalityModBlocks.LIME_CONCRETE_SLAB,
                BrutalityModBlocks.PINK_CONCRETE_SLAB,
                BrutalityModBlocks.GRAY_CONCRETE_SLAB,
                BrutalityModBlocks.LIGHT_GRAY_CONCRETE_SLAB,
                BrutalityModBlocks.CYAN_CONCRETE_SLAB,
                BrutalityModBlocks.PURPLE_CONCRETE_SLAB,
                BrutalityModBlocks.BLUE_CONCRETE_SLAB,
                BrutalityModBlocks.BROWN_CONCRETE_SLAB,
                BrutalityModBlocks.GREEN_CONCRETE_SLAB,
                BrutalityModBlocks.RED_CONCRETE_SLAB,
                BrutalityModBlocks.BLACK_CONCRETE_SLAB
        );

        CONCRETE_STAIRS = List.of(
                BrutalityModBlocks.WHITE_CONCRETE_STAIRS,
                BrutalityModBlocks.ORANGE_CONCRETE_STAIRS,
                BrutalityModBlocks.MAGENTA_CONCRETE_STAIRS,
                BrutalityModBlocks.LIGHT_BLUE_CONCRETE_STAIRS,
                BrutalityModBlocks.YELLOW_CONCRETE_STAIRS,
                BrutalityModBlocks.LIME_CONCRETE_STAIRS,
                BrutalityModBlocks.PINK_CONCRETE_STAIRS,
                BrutalityModBlocks.GRAY_CONCRETE_STAIRS,
                BrutalityModBlocks.LIGHT_GRAY_CONCRETE_STAIRS,
                BrutalityModBlocks.CYAN_CONCRETE_STAIRS,
                BrutalityModBlocks.PURPLE_CONCRETE_STAIRS,
                BrutalityModBlocks.BLUE_CONCRETE_STAIRS,
                BrutalityModBlocks.BROWN_CONCRETE_STAIRS,
                BrutalityModBlocks.GREEN_CONCRETE_STAIRS,
                BrutalityModBlocks.RED_CONCRETE_STAIRS,
                BrutalityModBlocks.BLACK_CONCRETE_STAIRS
        );
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        BrutalityModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Block> registerSlab(Block parent) {
        return registerBlock(ForgeRegistries.BLOCKS.getKey(parent).getPath() + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(parent)));
    }

    private static RegistryObject<Block> registerStair(Block parent) {
        return registerBlock(ForgeRegistries.BLOCKS.getKey(parent).getPath() + "_stair", () -> new StairBlock(parent::defaultBlockState, BlockBehaviour.Properties.copy(parent)));
    }

    public static RegistryObject<Block> WHITE_CONCRETE_STAIRS = registerStair(Blocks.WHITE_CONCRETE);
    public static RegistryObject<Block> ORANGE_CONCRETE_STAIRS = registerStair(Blocks.ORANGE_CONCRETE);
    public static RegistryObject<Block> MAGENTA_CONCRETE_STAIRS = registerStair(Blocks.MAGENTA_CONCRETE);
    public static RegistryObject<Block> LIGHT_BLUE_CONCRETE_STAIRS = registerStair(Blocks.LIGHT_BLUE_CONCRETE);
    public static RegistryObject<Block> YELLOW_CONCRETE_STAIRS = registerStair(Blocks.YELLOW_CONCRETE);
    public static RegistryObject<Block> LIME_CONCRETE_STAIRS = registerStair(Blocks.LIME_CONCRETE);
    public static RegistryObject<Block> PINK_CONCRETE_STAIRS = registerStair(Blocks.PINK_CONCRETE);
    public static RegistryObject<Block> GRAY_CONCRETE_STAIRS = registerStair(Blocks.GRAY_CONCRETE);
    public static RegistryObject<Block> LIGHT_GRAY_CONCRETE_STAIRS = registerStair(Blocks.LIGHT_GRAY_CONCRETE);
    public static RegistryObject<Block> CYAN_CONCRETE_STAIRS = registerStair(Blocks.CYAN_CONCRETE);
    public static RegistryObject<Block> PURPLE_CONCRETE_STAIRS = registerStair(Blocks.PURPLE_CONCRETE);
    public static RegistryObject<Block> BLUE_CONCRETE_STAIRS = registerStair(Blocks.BLUE_CONCRETE);
    public static RegistryObject<Block> BROWN_CONCRETE_STAIRS = registerStair(Blocks.BROWN_CONCRETE);
    public static RegistryObject<Block> GREEN_CONCRETE_STAIRS = registerStair(Blocks.GREEN_CONCRETE);
    public static RegistryObject<Block> RED_CONCRETE_STAIRS = registerStair(Blocks.RED_CONCRETE);
    public static RegistryObject<Block> BLACK_CONCRETE_STAIRS = registerStair(Blocks.BLACK_CONCRETE);

    public static RegistryObject<Block> WHITE_CONCRETE_SLAB = registerSlab(Blocks.WHITE_CONCRETE);
    public static RegistryObject<Block> ORANGE_CONCRETE_SLAB = registerSlab(Blocks.ORANGE_CONCRETE);
    public static RegistryObject<Block> MAGENTA_CONCRETE_SLAB = registerSlab(Blocks.MAGENTA_CONCRETE);
    public static RegistryObject<Block> LIGHT_BLUE_CONCRETE_SLAB = registerSlab(Blocks.LIGHT_BLUE_CONCRETE);
    public static RegistryObject<Block> YELLOW_CONCRETE_SLAB = registerSlab(Blocks.YELLOW_CONCRETE);
    public static RegistryObject<Block> LIME_CONCRETE_SLAB = registerSlab(Blocks.LIME_CONCRETE);
    public static RegistryObject<Block> PINK_CONCRETE_SLAB = registerSlab(Blocks.PINK_CONCRETE);
    public static RegistryObject<Block> GRAY_CONCRETE_SLAB = registerSlab(Blocks.GRAY_CONCRETE);
    public static RegistryObject<Block> LIGHT_GRAY_CONCRETE_SLAB = registerSlab(Blocks.LIGHT_GRAY_CONCRETE);
    public static RegistryObject<Block> CYAN_CONCRETE_SLAB = registerSlab(Blocks.CYAN_CONCRETE);
    public static RegistryObject<Block> PURPLE_CONCRETE_SLAB = registerSlab(Blocks.PURPLE_CONCRETE);
    public static RegistryObject<Block> BLUE_CONCRETE_SLAB = registerSlab(Blocks.BLUE_CONCRETE);
    public static RegistryObject<Block> BROWN_CONCRETE_SLAB = registerSlab(Blocks.BROWN_CONCRETE);
    public static RegistryObject<Block> GREEN_CONCRETE_SLAB = registerSlab(Blocks.GREEN_CONCRETE);
    public static RegistryObject<Block> RED_CONCRETE_SLAB = registerSlab(Blocks.RED_CONCRETE);
    public static RegistryObject<Block> BLACK_CONCRETE_SLAB = registerSlab(Blocks.BLACK_CONCRETE);


    public static final RegistryObject<Block> UPPER_HVAC = registerBlock("upper_hvac", () -> new HorizontalDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> LOWER_HVAC = registerBlock("lower_hvac", () -> new HorizontalDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> OLD_AIR_CONDITIONER = registerBlock("old_air_conditioner", () -> new OldAirConditionerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> EXIT_SIGN = registerBlock("exit_sign", () -> new ExitSignBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> OLD_SERVER_CASING = registerBlock("old_server_casing", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> OLD_SERVER_PANEL = registerBlock("old_server_panel", () -> new HorizontalDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).lightLevel(state -> 3)));

    public static final RegistryObject<Block> PLASTERBOARD = registerBlock("plasterboard", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO)));

    public static final RegistryObject<Block> PUDDLE = registerBlock("puddle", () -> new PuddleBlock(Block.Properties.of().liquid().sound(SoundType.WOOL).noCollission().strength(100F).noLootTable().replaceable()));

    public static final RegistryObject<Block> STYROFOAM_CUP = BLOCKS.register("styrofoam_cup", () -> new MugBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> MUG = BLOCKS.register("mug", () -> new MugBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).noOcclusion().strength(0.1F)));




    public static final RegistryObject<Block> GRAY_CUBICLE_PANEL =
            registerBlock("gray_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> LIGHT_GRAY_CUBICLE_PANEL =
            registerBlock("light_gray_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> BLUE_CUBICLE_PANEL =
            registerBlock("blue_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> GREEN_CUBICLE_PANEL =
            registerBlock("green_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> WHITE_CUBICLE_PANEL =
            registerBlock("white_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> RED_CUBICLE_PANEL =
            registerBlock("red_cubicle_panel", () -> new CubiclePanelBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));


    public static final RegistryObject<Block> TOILET = registerBlock("toilet", () -> new ToiletBlock(BlockBehaviour.Properties
            .copy(Blocks.TERRACOTTA).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS)));
    public static final RegistryObject<Block> URINAL = registerBlock("urinal", () -> new UrinalBlock(BlockBehaviour.Properties
            .copy(Blocks.TERRACOTTA).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS)));

    public static final RegistryObject<Block> CRT_MONITOR = registerBlock("crt_monitor", () -> new CRTMonitorBlock(BlockBehaviour.Properties
            .copy(Blocks.TERRACOTTA).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS)));

    public static final RegistryObject<Block> LCD_MONITOR = registerBlock("lcd_monitor", () -> new LCDMonitorBlock(BlockBehaviour.Properties
            .copy(Blocks.TERRACOTTA).noOcclusion().sound(SoundType.DEEPSLATE_BRICKS)));


    public static final RegistryObject<Block> DUSTBIN = registerBlock("dustbin", () -> new DustbinBlock(BlockBehaviour.Properties
            .copy(Blocks.CHAIN).noOcclusion()));

    public static final RegistryObject<Block> WHITE_OFFICE_CHAIR = registerBlock("white_office_chair", () -> new WhiteOfficeChairBlock(BlockBehaviour.Properties
            .copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> BLACK_OFFICE_CHAIR = registerBlock("black_office_chair", () -> new BlackOfficeChairBlock(BlockBehaviour.Properties
            .copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> WET_FLOOR_SIGN = registerBlock("wet_floor_sign", () -> new WetFloorSignBlock(BlockBehaviour.Properties
            .copy(Blocks.OAK_TRAPDOOR).sound(SoundType.SCAFFOLDING).noOcclusion()));

    public static final RegistryObject<Block> IMPORTANT_DOCUMENTS_BLOCK =
            BLOCKS.register("important_documents", () -> new ImportantDocumentsBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)
                    .noOcclusion().pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> GRAY_OFFICE_CARPET =
            registerBlock("gray_office_carpet", () -> new OfficeCarpetBlock(DyeColor.GRAY, BlockBehaviour.Properties.copy(Blocks.GRAY_CARPET).noOcclusion()));
    public static final RegistryObject<Block> LIGHT_GRAY_OFFICE_CARPET =
            registerBlock("light_gray_office_carpet", () -> new OfficeCarpetBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_CARPET).noOcclusion()));

    public static final RegistryObject<Block> GRAY_OFFICE_RUG = registerBlock("gray_office_rug", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));
    public static final RegistryObject<Block> LIGHT_GRAY_OFFICE_RUG = registerBlock("light_gray_office_rug", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));

    public static final RegistryObject<Block> OFFICE_LIGHT =
            registerBlock("office_light", () -> new Block(BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).lightLevel((blockState) -> 15).isRedstoneConductor(BrutalityModBlocks::never)));
    public static final RegistryObject<Block> SMALL_OFFICE_LIGHT =
            registerBlock("small_office_light", () -> new SmallOfficeLightBlock(BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).lightLevel((blockState) -> 15).isRedstoneConductor(BrutalityModBlocks::never)));
    public static final RegistryObject<Block> TABLE_OF_WIZARDRY =
            registerBlock("table_of_wizardry", () -> new TableOfWizardryBlock(BlockBehaviour.Properties.copy(TerramityModBlocks.MOONSTONE_BRICKS.get())));

    public static final RegistryObject<Block> PEDESTAL_OF_WIZARDRY =
            registerBlock("pedestal_of_wizardry", () -> new PedestalOfWizardryBlock(BlockBehaviour.Properties.copy(TerramityModBlocks.MOONSTONE_BRICKS.get())));


    public static final RegistryObject<Block> WHITE_FILING_CABINET =
            BLOCKS.register("white_filing_cabinet", () -> new WhiteFilingCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> LIGHT_GRAY_FILING_CABINET =
            BLOCKS.register("light_gray_filing_cabinet", () -> new LightGrayFilingCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> GRAY_FILING_CABINET =
            BLOCKS.register("gray_filing_cabinet", () -> new DarkGrayFilingCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> WATER_COOLER_BLOCK =
            BLOCKS.register("water_cooler", () -> new WaterCoolerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> COFFEE_MACHINE_BLOCK =
            BLOCKS.register("coffee_machine", () -> new CoffeeMachineBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> SUPER_SNIFFER_FIGURE_BLOCK =
            BLOCKS.register("super_sniffer_figure", () -> new SuperSnifferFigureBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)));

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

}