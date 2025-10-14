package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.block_entity.CoffeeMachineBlockEntity;
import net.goo.brutality.block.block_entity.FilingCabinetBlockEntity;
import net.goo.brutality.block.block_entity.SuperSnifferFigureBlockEntity;
import net.goo.brutality.block.block_entity.WaterCoolerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<BlockEntityType<WaterCoolerBlockEntity>> WATER_COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("water_cooler",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new WaterCoolerBlockEntity(ModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.WATER_COOLER_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<CoffeeMachineBlockEntity>> COFFEE_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("coffee_machine",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new CoffeeMachineBlockEntity(ModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<SuperSnifferFigureBlockEntity>> SUPER_SNIFFER_FIGURE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("super_sniffer_figure",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new SuperSnifferFigureBlockEntity(ModBlockEntities.SUPER_SNIFFER_FIGURE_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<FilingCabinetBlockEntity>> FILING_CABINET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("filing_cabinet_block_entity",
                    () -> BlockEntityType.Builder.of(
                            FilingCabinetBlockEntity::new,
                            BrutalityModBlocks.FILING_CABINET_BLOCK.get()
                    ).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
