package net.goo.armament.item;

import net.goo.armament.Armament;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Armament.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARMAMENT_TAB = CREATIVE_MODE_TABS.register("armament_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SUPERNOVA_SWORD.get()))
                    .title(Component.translatable("creativeTab.armament_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.SUPERNOVA_SWORD.get());
                        output.accept(ModItems.ZEUS_THUNDERBOLT_TRIDENT.get());
                        output.accept(ModItems.TEST_TRIDENT.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
