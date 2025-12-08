package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.block_entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<BlockEntityType<WaterCoolerBlockEntity>> WATER_COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("water_cooler",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new WaterCoolerBlockEntity(BrutalityModBlockEntities.WATER_COOLER_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.WATER_COOLER_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<CoffeeMachineBlockEntity>> COFFEE_MACHINE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("coffee_machine",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new CoffeeMachineBlockEntity(BrutalityModBlockEntities.COFFEE_MACHINE_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<SuperSnifferFigureBlockEntity>> SUPER_SNIFFER_FIGURE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("super_sniffer_figure",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new SuperSnifferFigureBlockEntity(BrutalityModBlockEntities.SUPER_SNIFFER_FIGURE_BLOCK_ENTITY.get(), pos, state),
                            BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<WhiteFilingCabinetBlockEntity>> WHITE_FILING_CABINET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("white_filing_cabinet",
                    () -> BlockEntityType.Builder.of(
                            WhiteFilingCabinetBlockEntity::create,
                            BrutalityModBlocks.WHITE_FILING_CABINET.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<LightGrayFilingCabinetBlockEntity>> LIGHT_GRAY_FILING_CABINET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("light_gray_filing_cabinet",
                    () -> BlockEntityType.Builder.of(
                            LightGrayFilingCabinetBlockEntity::create,
                            BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<GrayFilingCabinetBlockEntity>> GRAY_FILING_CABINET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gray_filing_cabinet",
                    () -> BlockEntityType.Builder.of(
                            GrayFilingCabinetBlockEntity::create,
                            BrutalityModBlocks.GRAY_FILING_CABINET.get()
                    ).build(null));

    public static final RegistryObject<BlockEntityType<WhiteOfficeChairBlockEntity>> WHITE_OFFICE_CHAIR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("white_office_chair",
                    () -> BlockEntityType.Builder.of(WhiteOfficeChairBlockEntity::create,
                            BrutalityModBlocks.WHITE_OFFICE_CHAIR.get()).build(null));

    public static final RegistryObject<BlockEntityType<WetFloorSignBlockEntity>> WET_FLOOR_SIGN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wet_floor_sign",
                    () -> BlockEntityType.Builder.of(WetFloorSignBlockEntity::new,
                            BrutalityModBlocks.WET_FLOOR_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<LCDMonitorBlockEntity>> LCD_MONITOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("lcd_monitor",
                    () -> BlockEntityType.Builder.of(LCDMonitorBlockEntity::new,
                            BrutalityModBlocks.LCD_MONITOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlackOfficeChairBlockEntity>> BLACK_OFFICE_CHAIR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("black_office_chair",
                    () -> BlockEntityType.Builder.of(BlackOfficeChairBlockEntity::create,
                            BrutalityModBlocks.BLACK_OFFICE_CHAIR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
