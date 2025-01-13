package net.goo.arma.item;

import net.goo.arma.Arma;
import net.goo.arma.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Arma.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARMA_TAB = CREATIVE_MODE_TABS.register("arma_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SUPERNOVA_SWORD.get()))
                    .title(Component.translatable("creativeTab.arma_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.SUPERNOVA_SWORD.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
