package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Brutality.MOD_ID);


    // Block item registration moved to ModItems (better organization)
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    // Helper method if you need to register multiple similar blocks
    private static RegistryObject<Block> registerGlassVariant(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

}