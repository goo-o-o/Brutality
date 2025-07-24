//package net.goo.brutality.registry;
//
//import net.goo.brutality.Brutality;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.RegistryObject;
//
//public class ModCreativeModTabs {
//    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
//            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Brutality.MOD_ID);
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_MATERIAL_TAB = CREATIVE_MODE_TABS.register("brutality_material_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.POCKET_BLACK_HOLE.get()))
//                    .title(Component.translatable("creativeTab.brutality_material_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//
//                        output.accept(BrutalityModItems.POCKET_BLACK_HOLE.get());
//                        output.accept(BrutalityModItems.HIGH_FREQUENCY_ALLOY.get());
//
//                    })
//                    .build());
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_MYTHOLOGY_TAB = CREATIVE_MODE_TABS.register("brutality_myth_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.THUNDERBOLT_TRIDENT.get()))
//                    .title(Component.translatable("creativeTab.brutality_myth_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(BrutalityModItems.GUNGNIR_TRIDENT.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.YATA_NO_KAGAMI.get());
//                        output.accept(BrutalityModItems.THUNDERBOLT_TRIDENT.get().getDefaultInstance());
//
//                    }).build());
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_VANITY_TAB = CREATIVE_MODE_TABS.register("brutality_vanity_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.GOLDEN_HEADBAND.get()))
//                    .title(Component.translatable("creativeTab.brutality_vanity_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(BrutalityModItems.SOLAR_SYSTEM.get());
//                        output.accept(BrutalityModItems.WOOLY_BLINDFOLD.get());
//                        output.accept(BrutalityModItems.TRIAL_GUARDIAN_EYEBROWS.get());
//                        output.accept(BrutalityModItems.TRIAL_GUARDIAN_HANDS.get());
//                        output.accept(BrutalityModItems.GOLDEN_HEADBAND.get());
//                        output.accept(BrutalityModItems.SERAPHIM_HALO.get());
//
//                    }).build());
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_RAGE_TAB = CREATIVE_MODE_TABS.register("brutality_rage_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.RAGE_STONE.get()))
//                    .title(Component.translatable("creativeTab.brutality_rage_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(BrutalityModItems.RAGE_STONE.get());
//                        output.accept(BrutalityModItems.PAIN_CATALYST.get());
//                        output.accept(BrutalityModItems.RAMPAGE_CLOCK.get());
//                        output.accept(BrutalityModItems.BLOOD_HOWL_PENDANT.get());
//                        output.accept(BrutalityModItems.SPITE_SHARD.get());
//                        output.accept(BrutalityModItems.HATE_SIGIL.get());
//                        output.accept(BrutalityModItems.HEART_OF_WRATH.get());
//                        output.accept(BrutalityModItems.EYE_FOR_VIOLENCE.get());
//                        output.accept(BrutalityModItems.BATTLE_SCARS.get());
//                        output.accept(BrutalityModItems.MECHANICAL_AORTA.get());
//                        output.accept(BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get());
//                        output.accept(BrutalityModItems.FURY_BAND.get());
//                        output.accept(BrutalityModItems.GRUDGE_TOTEM.get());
//                        output.accept(BrutalityModItems.BLOOD_STONE.get());
//                        output.accept(BrutalityModItems.WRATH_CHARM.get());
//                        output.accept(BrutalityModItems.ANGER_MANAGEMENT.get());
//
//                    }).build());
//
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_GASTRONOMY_TAB = CREATIVE_MODE_TABS.register("brutality_gastronomy_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get()))
//                    .title(Component.translatable("creativeTab.brutality_gastronomy_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(BrutalityModItems.SPATULA_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.IRON_KNIFE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.GOLD_KNIFE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DIAMOND_KNIFE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.VOID_KNIFE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MELONCHOLY_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.APPLE_CORE_LANCE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CHOPSTICK_STAFF.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.BAMBOO_STAFF.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.FRYING_PAN_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.POTATO_MASHER_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.WHISK_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.PANDORAS_CAULDRON.get());
//
//                        output.accept(BrutalityModItems.PEPPER_SHAKER_CHARM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SALT_SHAKER_CHARM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SALT_AND_PEPPER_CHARM.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.SMOKE_STONE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.THE_SMOKEHOUSE.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.BAMBOO_STEAMER.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.SUGAR_GLAZE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.RAINBOW_SPRINKLES.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.ROCK_CANDY_RING.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SEARED_SUGAR_BROOCH.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DUNKED_DONUT.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.LOLLIPOP_OF_ETERNITY.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.BUTTER_GAUNTLETS_HANDS.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.TOMATO_SAUCE_CHARM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CHEESE_SAUCE_CHARM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.PIZZA_SLOP_CHARM.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.HOT_SAUCE_CHARM.get());
//
//                        output.accept(BrutalityModItems.OLIVE_OIL_CHARM.get());
//                        output.accept(BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get());
//
//                        output.accept(BrutalityModItems.FRIDGE_CHARM.get());
//                        output.accept(BrutalityModItems.SMART_FRIDGE_CHARM.get());
//                    }).build());
//
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_MATH_TAB = CREATIVE_MODE_TABS.register("brutality_math_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.COSINE_CHARM.get()))
//                    .title(Component.translatable("creativeTab.brutality_math_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//
//                        output.accept(BrutalityModItems.PI_CHARM.get());
//                        output.accept(BrutalityModItems.EXPONENTIAL_CHARM.get());
//                        output.accept(BrutalityModItems.ADDITION_CHARM.get());
//                        output.accept(BrutalityModItems.SUBTRACTION_CHARM.get());
//                        output.accept(BrutalityModItems.MULTIPLICATION_CHARM.get());
//                        output.accept(BrutalityModItems.DIVISION_CHARM.get());
//                        output.accept(BrutalityModItems.SUM_CHARM.get());
//                        output.accept(BrutalityModItems.SINE_CHARM.get());
//                        output.accept(BrutalityModItems.COSINE_CHARM.get());
//                        output.accept(BrutalityModItems.SCIENTIFIC_CALCULATOR_BELT.get());
//                        output.accept(BrutalityModItems.WOODEN_RULER.get());
//                        output.accept(BrutalityModItems.METAL_RULER.get().getDefaultInstance());
//
//                    }).build());
//
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_TAB = CREATIVE_MODE_TABS.register("brutality_unsorted_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.SEVENTH_STAR_SWORD.get()))
//                    .title(Component.translatable("creativeTab.brutality_unsorted_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//
//                        // region Original Items
//                        output.accept(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CREASE_OF_CREATION_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.EVENT_HORIZON_LANCE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.JACKPOT_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.PROVIDENCE_BOW.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SEVENTH_STAR_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DOUBLE_DOWN_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.WHISPERWALTZ_SWORD.get());
//                        output.accept(BrutalityModItems.OLD_GPU_AXE.get());
//                        output.accept(BrutalityModItems.PAPER_CUT_SWORD.get());
//                        output.accept(BrutalityModItems.MARIANAS_TRENCH_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CHALLENGER_DEEP_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SUPERNOVA_SWORD.get().getDefaultInstance());
//
//                        output.accept(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get());
//                        output.accept(BrutalityModItems.DARKIN_BLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DARKIN_SCYTHE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.RHITTA_AXE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.FROSTMOURNE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MURASAMA_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DULL_KNIFE_DAGGER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.ROYAL_GUARDIAN_SWORD.get().getDefaultInstance());
//
//
//                        output.accept(BrutalityModItems.WATER_COOLER_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.STYROFOAM_CUP.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.COFFEE_MACHINE_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MUG.get().getDefaultInstance());
//
//
//
//                        output.accept(BrutalityModItems.CRYPTO_WALLET_CHARM.get());
//                        output.accept(BrutalityModItems.PORTABLE_MINING_RIG_CHARM.get());
//                        output.accept(BrutalityModItems.THE_CLOUD_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.GOOD_BOOK.get());
//                        output.accept(BrutalityModItems.CELESTIAL_STARBOARD.get());
//                        output.accept(BrutalityModItems.PLUNDER_CHEST_CHARM.get());
//                        output.accept(BrutalityModItems.ROAD_RUNNERS_RING.get());
//                        output.accept(BrutalityModItems.ABYSSAL_NECKLACE.get());
//                        output.accept(BrutalityModItems.BRAIN_ROT_HEAD.get());
//                        output.accept(BrutalityModItems.RESPLENDENT_FEATHER_CHARM.get());
//                        output.accept(BrutalityModItems.NANOMACHINES_HANDS.get());
//                        output.accept(BrutalityModItems.DUELING_GLOVE_HANDS.get());
//
//                        output.accept(BrutalityModItems.GREED_CHARM.get());
//                        output.accept(BrutalityModItems.PRIDE_CHARM.get());
//                        output.accept(BrutalityModItems.SLOTH_CHARM.get());
//                        output.accept(BrutalityModItems.ENVY_CHARM.get());
//                        output.accept(BrutalityModItems.LUST_CHARM.get());
//                        output.accept(BrutalityModItems.GLUTTONY_CHARM.get());
//
//                        output.accept(BrutalityModItems.DRAGONHEART.get());
//                        output.accept(BrutalityModItems.UVOGRE_HEART.get());
//                        output.accept(BrutalityModItems.ZOMBIE_HEART.get());
//                        output.accept(BrutalityModItems.FROZEN_HEART.get());
//                        output.accept(BrutalityModItems.HEART_OF_GOLD.get());
//                        output.accept(BrutalityModItems.SECOND_HEART.get());
//
//
//                        output.accept(BrutalityModItems.TERRA_BLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.TERRATOMERE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.LAST_PRISM_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.EXOBLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.BIOMECH_REACTOR_TRIDENT.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get().getDefaultInstance());
//
//
//
//                        output.accept(BrutalityModItems.NOIR_HELMET.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.NOIR_CHESTPLATE.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.NOIR_LEGGINGS.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.NOIR_BOOTS.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CANOPY_OF_SHADOWS.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.SHADOWSTEP_SWORD.get().getDefaultInstance());
//
//
//
//                    })
//                    .build());
//
//
//    public static final RegistryObject<CreativeModeTab> BRUTALITY_MAGIC_TAB = CREATIVE_MODE_TABS.register("brutality_magic_tab",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.DAEMONIC_TOME.get()))
//                    .title(Component.translatable("creativeTab.brutality_magic_tab"))
//                    .displayItems((itemDisplayParameters, output) -> {
//
//                        output.accept(BrutalityModItems.DAEMONIC_TOME.get().getDefaultInstance());
//
//                    })
//                    .build());
//
//    public static void register(IEventBus eventBus) {
//        CREATIVE_MODE_TABS.register(eventBus);
//    }
//
//}
//
