package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.block_entity.WhiteFilingCabinetBlockEntity;
import net.goo.brutality.gui.menu.FilingCabinetMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<MenuType<FilingCabinetMenu>> FILING_CABINET_MENU =
            MENU_TYPES.register("filing_cabinet", () ->
                    IForgeMenuType.create((windowId, inv, data) -> {
                        if (data == null) {
                            return new FilingCabinetMenu(windowId, inv, null, true);
                        }
                        BlockPos pos = data.readBlockPos();
                        boolean isUpper = data.readBoolean();
                        WhiteFilingCabinetBlockEntity blockEntity = (WhiteFilingCabinetBlockEntity) inv.player.level().getBlockEntity(pos);
                        if (blockEntity == null) {
                            return new FilingCabinetMenu(windowId, inv, null, isUpper);
                        }
                        return new FilingCabinetMenu(windowId, inv, blockEntity, isUpper);
                    }));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}