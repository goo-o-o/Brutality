package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.custom.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BrutalityModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Brutality.MOD_ID);


    // Block item registration moved to BrutalityModItems (better organization)
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        BrutalityModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    // Helper method if you need to register multiple similar blocks
    private static RegistryObject<Block> registerGlassVariant(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        BrutalityModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static final RegistryObject<Block> STYROFOAM_CUP =
            BLOCKS.register("styrofoam_cup", () -> new MugBlock(BlockBehaviour.Properties
                    .copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> MUG =
            BLOCKS.register("mug", () -> new MugBlock(BlockBehaviour.Properties
                    .copy(Blocks.TERRACOTTA).noOcclusion().strength(0.1F)));

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

    public static final RegistryObject<Block> WHITE_FILING_CABINET =
            BLOCKS.register("white_filing_cabinet", () -> new WhiteFilingCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> GRAY_FILING_CABINET =
            BLOCKS.register("gray_filing_cabinet", () -> new GrayFilingCabinetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

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