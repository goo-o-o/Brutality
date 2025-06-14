package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Brutality.MOD_ID);


    public static final RegistryObject<CreativeModeTab> BRUTALITY_TAB = CREATIVE_MODE_TABS.register("brutality_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.THUNDERBOLT_TRIDENT.get()))
                    .title(Component.translatable("creativeTab.brutality_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

//                        output.accept(ModItems.DIVINE_RHITTA_AXE.get().getDefaultInstance());
                        output.accept(ModItems.ATOMIC_JUDGEMENT_HAMMER.get().getDefaultInstance());
                        output.accept(ModItems.DOOMFIST_GAUNTLET.get().getDefaultInstance());
                        output.accept(ModItems.DARKIN_BLADE.get().getDefaultInstance());
                        output.accept(ModItems.RHITTA_AXE.get().getDefaultInstance());
                        output.accept(ModItems.CREASE_OF_CREATION.get().getDefaultInstance());
                        output.accept(ModItems.EVENT_HORIZON_LANCE.get().getDefaultInstance());
                        output.accept(ModItems.EXCALIBUR_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.FALLEN_SCYTHE.get().getDefaultInstance());
                        output.accept(ModItems.DARKIN_SCYTHE.get().getDefaultInstance());
                        output.accept(ModItems.FROSTMOURNE_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.GUNGNIR_TRIDENT.get().getDefaultInstance());
                        output.accept(ModItems.JACKPOT_HAMMER.get().getDefaultInstance());
                        output.accept(ModItems.LEAF_BLOWER.get().getDefaultInstance());
                        output.accept(ModItems.FIRST_EXPLOSION_STAFF.get().getDefaultInstance());
                        output.accept(ModItems.HF_MURASAMA.get().getDefaultInstance());
                        output.accept(ModItems.MURAMASA_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.PROVIDENCE_BOW.get().getDefaultInstance());
                        output.accept(ModItems.DOUBLE_DOWN_SWORD.get().getDefaultInstance());
//                        output.accept(ModItems.QUANTUM_DRILL.get().getDefaultInstance());
//                        output.accept(ModItems.RESONANCE_PICKAXE.get().getDefaultInstance());

                        output.accept(ModItems.SPATULA_HAMMER.get().getDefaultInstance());
                        output.accept(ModItems.FRYING_PAN_HAMMER.get().getDefaultInstance());
                        output.accept(ModItems.CABBAGE_TRIDENT.get().getDefaultInstance());
                        output.accept(ModItems.WINTERMELON_TRIDENT.get().getDefaultInstance());
                        output.accept(ModItems.KNIFE_BLOCK_ITEM.get().getDefaultInstance());
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        output.accept(ModItems.PI_CHARM.get());


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.TERRA_HELMET.get().getDefaultInstance());
                        output.accept(ModItems.TERRA_CHESTPLATE.get().getDefaultInstance());
                        output.accept(ModItems.TERRA_LEGGINGS.get().getDefaultInstance());
                        output.accept(ModItems.TERRA_BOOTS.get().getDefaultInstance());
                        output.accept(ModItems.TERRA_BLADE.get().getDefaultInstance());
                        output.accept(ModItems.TERRATON_HAMMER.get().getDefaultInstance());

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.NOIR_HELMET.get().getDefaultInstance());
                        output.accept(ModItems.NOIR_CHESTPLATE.get().getDefaultInstance());
                        output.accept(ModItems.NOIR_LEGGINGS.get().getDefaultInstance());
                        output.accept(ModItems.NOIR_BOOTS.get().getDefaultInstance());
                        output.accept(ModItems.CANOPY_OF_SHADOWS.get().getDefaultInstance());
                        output.accept(ModItems.SHADOWSTEP_SWORD.get().getDefaultInstance());

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        output.accept(ModItems.SUPERNOVA_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.DULL_KNIFE_DAGGER.get().getDefaultInstance());
                        output.accept(ModItems.TRUTHSEEKER_SWORD.get().getDefaultInstance());
                        output.accept(ModItems.ROYAL_GUARDIAN_SWORD.get().getDefaultInstance());
//                        output.accept(ModItems.VIPER_RAPIER.get().getDefaultInstance());
                        output.accept(ModItems.THUNDERBOLT_TRIDENT.get().getDefaultInstance());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}

