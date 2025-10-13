package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.custom.CoffeeMachineBlock;
import net.goo.brutality.block.custom.CubiclePanel;
import net.goo.brutality.block.custom.SuperSnifferFigureBlock;
import net.goo.brutality.block.custom.WaterCoolerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
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

    public static final RegistryObject<Block> CUBICLE_PANEL =
            registerBlock("cubicle_panel", () -> new CubiclePanel(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.WOOL).noOcclusion()));


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