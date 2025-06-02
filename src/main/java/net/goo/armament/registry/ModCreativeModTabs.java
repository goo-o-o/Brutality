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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.THUNDERBOLT_TRIDENT.get()))
                    .title(Component.translatable("creativeTab.armament_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

//                        output.accept(ModItems.DIVINE_RHITTA_AXE.get());
                        output.accept(ModItems.ATOMIC_JUDGEMENT_HAMMER.get());
                        output.accept(ModItems.DOOMFIST_GAUNTLET.get());
                        output.accept(ModItems.EVENT_HORIZON_LANCE.get());
                        output.accept(ModItems.EXCALIBUR_SWORD.get());
                        output.accept(ModItems.FALLEN_SCYTHE.get().getDefaultInstance());
                        output.accept(ModItems.FROSTMOURNE_SWORD.get());
                        output.accept(ModItems.GUNGNIR_TRIDENT.get());
                        output.accept(ModItems.JACKPOT_HAMMER.get());
                        output.accept(ModItems.LEAF_BLOWER.get());
                        output.accept(ModItems.FIRST_EXPLOSION_STAFF.get().getDefaultInstance());
                        output.accept(ModItems.MURASAMA_SWORD.get());
                        output.accept(ModItems.MURAMASA_SWORD.get());
                        output.accept(ModItems.PROVIDENCE_BOW.get());
//                        output.accept(ModItems.QUANTUM_DRILL.get());
//                        output.accept(ModItems.RESONANCE_PICKAXE.get());

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.TERRA_HELMET.get());
                        output.accept(ModItems.TERRA_CHESTPLATE.get());
                        output.accept(ModItems.TERRA_LEGGINGS.get());
                        output.accept(ModItems.TERRA_BOOTS.get());
                        output.accept(ModItems.TERRA_BLADE.get());
                        output.accept(ModItems.TERRATON_HAMMER.get());

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.NOIR_HELMET.get());
                        output.accept(ModItems.NOIR_CHESTPLATE.get());
                        output.accept(ModItems.NOIR_LEGGINGS.get());
                        output.accept(ModItems.NOIR_BOOTS.get());
                        output.accept(ModItems.CANOPY_OF_SHADOWS.get());
                        output.accept(ModItems.SHADOWSTEP_SWORD.get());

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.SUPERNOVA_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.DULL_KNIFE_DAGGER.get().getDefaultInstance());
                        output.accept(ModItems.TRUTHSEEKER_SWORD.get());
//                        output.accept(ModItems.VIPER_RAPIER.get());
                        output.accept(ModItems.THUNDERBOLT_TRIDENT.get().getDefaultInstance());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}

