package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.custom.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
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

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return BrutalityModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    // Helper method if you need to register multiple similar blocks
    private static RegistryObject<Block> registerGlassVariant(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        BrutalityModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

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

    public static final RegistryObject<Block> GRAY_OFFICE_CARPET =
            registerBlock("gray_office_carpet", () -> new OfficeCarpetBlock(DyeColor.GRAY, BlockBehaviour.Properties.copy(Blocks.GRAY_CARPET).noOcclusion()));
    public static final RegistryObject<Block> LIGHT_GRAY_OFFICE_CARPET =
            registerBlock("light_gray_office_carpet", () -> new OfficeCarpetBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.copy(Blocks.LIGHT_GRAY_CARPET).noOcclusion()));


    public static final RegistryObject<Block> WATER_COOLER_BLOCK =
            BLOCKS.register("water_cooler", () ->
                    new WaterCoolerBlock(BlockBehaviour.Properties
                            .copy(Blocks.IRON_BLOCK)
                    ));

    public static final RegistryObject<Block> COFFEE_MACHINE_BLOCK =
            BLOCKS.register("coffee_machine", () ->
                    new CoffeeMachineBlock(BlockBehaviour.Properties
                            .copy(Blocks.IRON_BLOCK)
                    ));

    public static final RegistryObject<Block> SUPER_SNIFFER_FIGURE_BLOCK =
            BLOCKS.register("super_sniffer_figure", () ->
                    new SuperSnifferFigureBlock(BlockBehaviour.Properties
                            .copy(Blocks.WHITE_WOOL)
                    ));

}