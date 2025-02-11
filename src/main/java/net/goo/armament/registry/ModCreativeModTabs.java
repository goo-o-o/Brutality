package net.goo.armament.registry;

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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ZEUS_THUNDERBOLT_TRIDENT.get()))
                    .title(Component.translatable("creativeTab.armament_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModItems.SUPERNOVA_SWORD.get());
//                        output.accept(ModItems.DIVINE_RHITTA_AXE.get());
                        output.accept(ModItems.LEAF_BLOWER.get());
                        output.accept(ModItems.TERRA_BLADE.get());
                        output.accept(ModItems.TERRATON_HAMMER.get());
                        output.accept(ModItems.TRUTHSEEKER_SWORD.get());
                        output.accept(ModItems.DOOMFIST_GAUNTLET.get());
                        output.accept(ModItems.TEST_SWORD.get());
                        output.accept(ModItems.JACKPOT_HAMMER.get());
                        output.accept(ModItems.QUANTUM_DRILL.get());
                        output.accept(ModItems.SHADOWSTEP_SWORD.get());
                        output.accept(ModItems.ZEUS_THUNDERBOLT_TRIDENT.get().getDefaultInstance());
//                        output.accept(ModItems.EVENT_HORIZON_LANCE.get());
                        output.accept(ModItems.RESONANCE_PICKAXE.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
