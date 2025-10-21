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

    public static final RegistryObject<CreativeModeTab> BRUTALITY_MATERIAL_TAB = CREATIVE_MODE_TABS.register("brutality_material_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.POCKET_BLACK_HOLE.get()))
                    .title(Component.translatable("creativeTab.brutality_material_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityModItems.POCKET_BLACK_HOLE.get());
                        output.accept(BrutalityModItems.HIGH_FREQUENCY_ALLOY.get());
                        output.accept(BrutalityModItems.QUANTITE_INGOT.get());
                        output.accept(BrutalityModItems.BLUE_SLIME_BALL.get());
                        output.accept(BrutalityModItems.PINK_SLIME_BALL.get());
                        output.accept(BrutalityModItems.UNBRIDLED_RAGE.get());

                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_VANITY_TAB = CREATIVE_MODE_TABS.register("brutality_vanity_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.GOLDEN_HEADBAND.get()))
                    .title(Component.translatable("creativeTab.brutality_vanity_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrutalityModItems.SOLAR_SYSTEM.get());
                        output.accept(BrutalityModItems.WOOLY_BLINDFOLD.get());
                        output.accept(BrutalityModItems.TRIAL_GUARDIAN_EYEBROWS.get());
                        output.accept(BrutalityModItems.TRIAL_GUARDIAN_HANDS.get());
                        output.accept(BrutalityModItems.GOLDEN_HEADBAND.get());
                        output.accept(BrutalityModItems.SERAPHIM_HALO.get());
                        output.accept(BrutalityModItems.SUSPICIOUS_SLOT_MACHINE.get());

                    }).build());



    public static final RegistryObject<CreativeModeTab> BRUTALITY_GASTRONOMY_TAB = CREATIVE_MODE_TABS.register("brutality_gastronomy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get()))
                    .title(Component.translatable("creativeTab.brutality_gastronomy_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrutalityModItems.SPATULA_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.IRON_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.GOLD_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DIAMOND_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.VOID_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.MELONCHOLY_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityModItems.APPLE_CORE_LANCE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CHOPSTICK_STAFF.get().getDefaultInstance());
                        output.accept(BrutalityModItems.BAMBOO_STAFF.get().getDefaultInstance());
                        output.accept(BrutalityModItems.FRYING_PAN.get().getDefaultInstance());
                        output.accept(BrutalityModItems.POTATO_MASHER_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.WHISK_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.PANDORAS_CAULDRON.get());

                        output.accept(BrutalityModItems.PEPPER_SHAKER_CHARM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SALT_SHAKER_CHARM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SALT_AND_PEPPER_CHARM.get().getDefaultInstance());

                        output.accept(BrutalityModItems.SMOKE_STONE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.THE_SMOKEHOUSE.get().getDefaultInstance());

                        output.accept(BrutalityModItems.BAMBOO_STEAMER.get().getDefaultInstance());

                        output.accept(BrutalityModItems.SUGAR_GLAZE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.RAINBOW_SPRINKLES.get().getDefaultInstance());
                        output.accept(BrutalityModItems.ROCK_CANDY_RING.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SEARED_SUGAR_BROOCH.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DUNKED_DONUT.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get().getDefaultInstance());
                        output.accept(BrutalityModItems.LOLLIPOP_OF_ETERNITY.get().getDefaultInstance());

                        output.accept(BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.BUTTER_GAUNTLETS.get().getDefaultInstance());

                        output.accept(BrutalityModItems.TOMATO_SAUCE_CHARM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CHEESE_SAUCE_CHARM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.PIZZA_SLOP_CHARM.get().getDefaultInstance());

                        output.accept(BrutalityModItems.HOT_SAUCE_CHARM.get());

                        output.accept(BrutalityModItems.OLIVE_OIL_CHARM.get());
                        output.accept(BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get());

                        output.accept(BrutalityModItems.FRIDGE_CHARM.get());
                        output.accept(BrutalityModItems.SMART_FRIDGE_CHARM.get());
                    }).build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_CURIO_TAB = CREATIVE_MODE_TABS.register("brutality_curio_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.CARTON_OF_PRISM_SOLUTION_MILK.get()))
                    .title(Component.translatable("creativeTab.brutality_curio_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityModItems.PI_CHARM.get());
                        output.accept(BrutalityModItems.EXPONENTIAL_CHARM.get());
                        output.accept(BrutalityModItems.ADDITION_CHARM.get());
                        output.accept(BrutalityModItems.SUBTRACTION_CHARM.get());
                        output.accept(BrutalityModItems.MULTIPLICATION_CHARM.get());
                        output.accept(BrutalityModItems.DIVISION_CHARM.get());
                        output.accept(BrutalityModItems.SUM_CHARM.get());
                        output.accept(BrutalityModItems.SINE_CHARM.get());
                        output.accept(BrutalityModItems.COSINE_CHARM.get());
                        output.accept(BrutalityModItems.SCIENTIFIC_CALCULATOR.get());
                        output.accept(BrutalityModItems.HYPERBOLIC_FEATHER.get());


                        output.accept(BrutalityModItems.CARTON_OF_PRISM_SOLUTION_MILK.get());
                        output.accept(BrutalityModItems.LIGHT_SWITCH.get());
                        output.accept(BrutalityModItems.FUZZY_DICE.get());
                        output.accept(BrutalityModItems.DIVINE_IMMOLATION.get());
                        output.accept(BrutalityModItems.CRYPTO_WALLET_CHARM.get());
                        output.accept(BrutalityModItems.PORTABLE_MINING_RIG.get());
                        output.accept(BrutalityModItems.DUMBBELL.get());
                        output.accept(BrutalityModItems.BOX_OF_CHOCOLATES.get());
                        output.accept(BrutalityModItems.BROKEN_HEART.get());
//                        output.accept(BrutalityModItems.BIKERS_HELMET.get());
                        output.accept(BrutalityModItems.ESCAPE_KEY.get());
                        output.accept(BrutalityModItems.MIRACLE_CURE.get());
                        output.accept(BrutalityModItems.ESSENTIAL_OILS.get());
                        output.accept(BrutalityModItems.GLASS_HEART.get());
                        output.accept(BrutalityModItems.THE_CLOUD.get().getDefaultInstance());
                        output.accept(BrutalityModItems.GOOD_BOOK.get());
                        output.accept(BrutalityModItems.CELESTIAL_STARBOARD.get());
                        output.accept(BrutalityModItems.PLUNDER_CHEST_CHARM.get());
                        output.accept(BrutalityModItems.ROAD_RUNNERS_RING.get());
                        output.accept(BrutalityModItems.ABYSSAL_NECKLACE.get());
                        output.accept(BrutalityModItems.BRAIN_ROT.get());
                        output.accept(BrutalityModItems.RESPLENDENT_FEATHER_CHARM.get());
                        output.accept(BrutalityModItems.NANOMACHINES.get());
                        output.accept(BrutalityModItems.DUELING_GLOVE.get());
                        output.accept(BrutalityModItems.MICROBLADE_BAND.get());
                        output.accept(BrutalityModItems.CROWN_OF_TYRANNY.get());
                        output.accept(BrutalityModItems.PORTABLE_QUANTUM_THINGAMABOB.get());
                        output.accept(BrutalityModItems.WARPSLICE_SCABBARD.get());
                        output.accept(BrutalityModItems.DAEMONIUM_WHETSTONE.get());
                        output.accept(BrutalityModItems.KNIGHTS_PENDANT.get());
                        output.accept(BrutalityModItems.THE_OATH.get());
                        output.accept(BrutalityModItems.LUCKY_INSOLES.get());
                        output.accept(BrutalityModItems.OLD_GUILLOTINE.get());
                        output.accept(BrutalityModItems.GAMBLERS_CHAIN.get());
                        output.accept(BrutalityModItems.DAEMONIUM_SEWING_KIT.get());
                        output.accept(BrutalityModItems.PINCUSHION.get());
                        output.accept(BrutalityModItems.SOLDIERS_SYRINGE.get());
                        output.accept(BrutalityModItems.TARGET_CUBE.get());
                        output.accept(BrutalityModItems.BRUTESKIN_BELT.get());
                        output.accept(BrutalityModItems.ENERGY_FOCUSER.get());
                        output.accept(BrutalityModItems.CRITICAL_THINKING.get());
                        output.accept(BrutalityModItems.DEADSHOT_BROOCH.get());
                        output.accept(BrutalityModItems.VINDICATOR_STEROIDS.get());
                        output.accept(BrutalityModItems.CROWBAR.get());
                        output.accept(BrutalityModItems.WIRE_CUTTERS.get());
                        output.accept(BrutalityModItems.AIR_JORDAN_EARRINGS.get());
                        output.accept(BrutalityModItems.JURY_NULLIFIER.get());
                        output.accept(BrutalityModItems.EYE_OF_THE_DRAGON.get());
                        output.accept(BrutalityModItems.LENS_MAKERS_GLASSES.get());
                        output.accept(BrutalityModItems.SCOPE_GOGGLES.get());
                        output.accept(BrutalityModItems.BLACK_MATTER_NECKLACE.get());
                        output.accept(BrutalityModItems.PHANTOM_FINGER.get());
                        output.accept(BrutalityModItems.SILVER_BOOSTER_PACK.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DIAMOND_BOOSTER_PACK.get());
                        output.accept(BrutalityModItems.EVIL_KING_BOOSTER_PACK.get());
                        output.accept(BrutalityModItems.SILVER_RESPAWN_CARD.get());
                        output.accept(BrutalityModItems.DIAMOND_RESPAWN_CARD.get());
                        output.accept(BrutalityModItems.EVIL_KING_RESPAWN_CARD.get());
                        output.accept(BrutalityModItems.YATA_NO_KAGAMI.get());

                        output.accept(BrutalityModItems.GREED_CHARM.get());
                        output.accept(BrutalityModItems.PRIDE_CHARM.get());
                        output.accept(BrutalityModItems.SLOTH_CHARM.get());
                        output.accept(BrutalityModItems.ENVY_CHARM.get());
                        output.accept(BrutalityModItems.LUST_CHARM.get());
                        output.accept(BrutalityModItems.GLUTTONY_CHARM.get());

                        output.accept(BrutalityModItems.DRAGONHEART.get());
                        output.accept(BrutalityModItems.UVOGRE_HEART.get());
                        output.accept(BrutalityModItems.ZOMBIE_HEART.get());
                        output.accept(BrutalityModItems.FROZEN_HEART.get());
                        output.accept(BrutalityModItems.HEART_OF_GOLD.get());
                        output.accept(BrutalityModItems.SECOND_HEART.get());
                        output.accept(BrutalityModItems.BRUTAL_HEART.get());
                        output.accept(BrutalityModItems.NINJA_HEART.get());

                        output.accept(BrutalityModItems.EMPTY_ANKLET.get());
                        output.accept(BrutalityModItems.DAVYS_ANKLET.get());
                        output.accept(BrutalityModItems.ANKLET_OF_THE_IMPRISONED.get());
                        output.accept(BrutalityModItems.SHARPNESS_ANKLET.get());
                        output.accept(BrutalityModItems.DEBUG_ANKLET.get());
                        output.accept(BrutalityModItems.REDSTONE_ANKLET.get());
                        output.accept(BrutalityModItems.DEVILS_ANKLET.get());
                        output.accept(BrutalityModItems.BASKETBALL_ANKLET.get());
                        output.accept(BrutalityModItems.EMERALD_ANKLET.get());
                        output.accept(BrutalityModItems.RUBY_ANKLET.get());
                        output.accept(BrutalityModItems.TOPAZ_ANKLET.get());
                        output.accept(BrutalityModItems.SAPPHIRE_ANKLET.get());
                        output.accept(BrutalityModItems.ONYX_ANKLET.get());
                        output.accept(BrutalityModItems.ULTRA_DODGE_ANKLET.get());
                        output.accept(BrutalityModItems.GUNDALFS_ANKLET.get());
                        output.accept(BrutalityModItems.TRIAL_ANKLET.get());
                        output.accept(BrutalityModItems.SUPER_DODGE_ANKLET.get());
                        output.accept(BrutalityModItems.GNOME_KINGS_ANKLET.get());
                        output.accept(BrutalityModItems.ANKLENT.get());
                        output.accept(BrutalityModItems.ANKLE_MONITOR.get());
                        output.accept(BrutalityModItems.FIERY_ANKLET.get());
                        output.accept(BrutalityModItems.SACRED_SPEED_ANKLET.get());
                        output.accept(BrutalityModItems.CONDUCTITE_ANKLET.get());
                        output.accept(BrutalityModItems.BLOOD_CLOT_ANKLET.get());
                        output.accept(BrutalityModItems.VIRENTIUM_ANKLET.get());
                        output.accept(BrutalityModItems.COSMIC_ANKLET.get());
                        output.accept(BrutalityModItems.VOID_ANKLET.get());
                        output.accept(BrutalityModItems.IRONCLAD_ANKLET.get());
                        output.accept(BrutalityModItems.GLADIATORS_ANKLET.get());
                        output.accept(BrutalityModItems.NYXIUM_ANKLET.get());
                        output.accept(BrutalityModItems.EXODIUM_ANKLET.get());
                        output.accept(BrutalityModItems.WINDSWEPT_ANKLET.get());
                        output.accept(BrutalityModItems.BIG_STEPPA.get());

                        output.accept(BrutalityModItems.HELL_SPECS.get());
                        output.accept(BrutalityModItems.STYGIAN_CHAIN.get());
                        output.accept(BrutalityModItems.BLOOD_CHALICE.get());
                        output.accept(BrutalityModItems.HEMOMATIC_LOCKET.get());
                        output.accept(BrutalityModItems.SANGUINE_SIGNET.get());
                        output.accept(BrutalityModItems.SELF_REPAIR_NEXUS.get());
                        output.accept(BrutalityModItems.HEMOGRAFT_NEEDLE.get());
                        output.accept(BrutalityModItems.VAMPIRE_FANG.get());
                        output.accept(BrutalityModItems.SANGUINE_SPECTACLES.get());
                        output.accept(BrutalityModItems.PROGENITORS_EARRINGS.get());
                        output.accept(BrutalityModItems.BLOODSTAINED_MIRROR.get());
                        output.accept(BrutalityModItems.INCOGNITO_MODE.get());
                        output.accept(BrutalityModItems.MINIATURE_ANCHOR.get());
                        output.accept(BrutalityModItems.PAPER_AIRPLANE.get());
                        output.accept(BrutalityModItems.FIRE_EXTINGUISHER.get());
                        output.accept(BrutalityModItems.EMERGENCY_MEETING.get());
                        output.accept(BrutalityModItems.PENCIL_SHARPENER.get());
                        output.accept(BrutalityModItems.CHOCOLATE_BAR.get());
                        output.accept(BrutalityModItems.VAMPIRIC_TALISMAN.get());
                        output.accept(BrutalityModItems.FALLEN_ANGELS_HALO.get());
                        output.accept(BrutalityModItems.HELLSPEC_TIE.get());
                        output.accept(BrutalityModItems.BLOOD_PACK.get());
                        output.accept(BrutalityModItems.RAGE_STONE.get());
                        output.accept(BrutalityModItems.PAIN_CATALYST.get());
                        output.accept(BrutalityModItems.RAMPAGE_CLOCK.get());
                        output.accept(BrutalityModItems.BLOOD_HOWL_PENDANT.get());
                        output.accept(BrutalityModItems.SPITE_SHARD.get());
                        output.accept(BrutalityModItems.HATE_SIGIL.get());
                        output.accept(BrutalityModItems.HEART_OF_WRATH.get());
                        output.accept(BrutalityModItems.EYE_FOR_VIOLENCE.get());
                        output.accept(BrutalityModItems.BATTLE_SCARS.get());
                        output.accept(BrutalityModItems.MECHANICAL_AORTA.get());
                        output.accept(BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get());
                        output.accept(BrutalityModItems.FURY_BAND.get());
                        output.accept(BrutalityModItems.GRUDGE_TOTEM.get());
                        output.accept(BrutalityModItems.BLOOD_STONE.get());
                        output.accept(BrutalityModItems.WRATH_CHARM.get());
                        output.accept(BrutalityModItems.ANGER_MANAGEMENT.get());


                        output.accept(BrutalityModItems.BROKEN_CLOCK.get());
                        output.accept(BrutalityModItems.SHATTERED_CLOCK.get());
                        output.accept(BrutalityModItems.SUNDERED_CLOCK.get());
                        output.accept(BrutalityModItems.TIMEKEEPERS_CLOCK.get());
                        output.accept(BrutalityModItems.THE_CLOCK_OF_FROZEN_TIME.get());


                        output.accept(BrutalityModItems.ARCHMAGES_TRICK.get());
                        output.accept(BrutalityModItems.SOUL_STONE.get());
                        output.accept(BrutalityModItems.EMERGENCY_FLASK.get());
                        output.accept(BrutalityModItems.RING_OF_MANA.get());
                        output.accept(BrutalityModItems.RING_OF_MANA_PLUS.get());
                        output.accept(BrutalityModItems.CONSERVATIVE_CONCOCTION.get());
                        output.accept(BrutalityModItems.RUNE_OF_DELTA.get());
                        output.accept(BrutalityModItems.ECHO_CHAMBER.get());
                        output.accept(BrutalityModItems.ONYX_IDOL.get());
                        output.accept(BrutalityModItems.SCRIBES_INDEX.get());
                        output.accept(BrutalityModItems.BLACK_HOLE_ORB.get());
                        output.accept(BrutalityModItems.FORBIDDEN_ORB.get());
                        output.accept(BrutalityModItems.BLOOD_ORB.get());
                        output.accept(BrutalityModItems.PROFANUM_REACTOR.get());
                        output.accept(BrutalityModItems.MANA_SYRINGE.get());
                        output.accept(BrutalityModItems.DECK_OF_CARDS.get());
                    }).build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_TAB = CREATIVE_MODE_TABS.register("brutality_unsorted_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.SEVENTH_STAR.get()))
                    .title(Component.translatable("creativeTab.brutality_unsorted_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrutalityModItems.BLACK_SEAL.get());
                        output.accept(BrutalityModItems.BLUE_SEAL.get());
                        output.accept(BrutalityModItems.GREEN_SEAL.get());
                        output.accept(BrutalityModItems.ORANGE_SEAL.get());
                        output.accept(BrutalityModItems.PINK_SEAL.get());
                        output.accept(BrutalityModItems.PURPLE_SEAL.get());
                        output.accept(BrutalityModItems.RED_SEAL.get());
                        output.accept(BrutalityModItems.CYAN_SEAL.get());
                        output.accept(BrutalityModItems.YELLOW_SEAL.get());
                        output.accept(BrutalityModItems.BOMB_SEAL.get());
                        output.accept(BrutalityModItems.COSMIC_SEAL.get());
                        output.accept(BrutalityModItems.GLASS_SEAL.get());
                        output.accept(BrutalityModItems.QUANTITE_SEAL.get());
                        output.accept(BrutalityModItems.VOID_SEAL.get());

                        output.accept(BrutalityModItems.CINDER_BLOCK.get());
                        output.accept(BrutalityModItems.BIOMECH_REACTOR.get());
                        output.accept(BrutalityModItems.SCULK_GRENADE.get());
                        output.accept(BrutalityModItems.STICKY_BOMB.get());
                        output.accept(BrutalityModItems.BEACH_BALL.get());
                        output.accept(BrutalityModItems.BLAST_BARREL.get());
                        output.accept(BrutalityModItems.DECK_OF_FATE.get());
                        output.accept(BrutalityModItems.PERFUME_BOTTLE.get());
                        output.accept(BrutalityModItems.ABSOLUTE_ZERO.get());
                        output.accept(BrutalityModItems.CRIMSON_DELIGHT.get());
                        output.accept(BrutalityModItems.CANNONBALL_CABBAGE.get());
                        output.accept(BrutalityModItems.CAVENDISH.get());
                        output.accept(BrutalityModItems.STICK_OF_BUTTER.get());
                        output.accept(BrutalityModItems.GOLDEN_PHOENIX.get());
                        output.accept(BrutalityModItems.WINTER_MELON.get());
                        output.accept(BrutalityModItems.ICE_CUBE.get());
                        output.accept(BrutalityModItems.PERMAFROST_CUBE.get());
                        output.accept(BrutalityModItems.OVERCLOCKED_TOASTER.get());
                        output.accept(BrutalityModItems.HOLY_HAND_GRENADE.get());
                        output.accept(BrutalityModItems.GUNGNIR_TRIDENT.get().getDefaultInstance());
                        output.accept(BrutalityModItems.VAMPIRE_KNIVES.get().getDefaultInstance());
                        output.accept(BrutalityModItems.PHOTON.get());
                        output.accept(BrutalityModItems.POUCH_O_PHOTONS.get());
                        output.accept(BrutalityModItems.DYNAMITE.get());
                        output.accept(BrutalityModItems.STICKY_DYNAMITE.get());
                        output.accept(BrutalityModItems.BOUNCY_DYNAMITE.get());
                        output.accept(BrutalityModItems.THUNDERBOLT_TRIDENT.get().getDefaultInstance());

                        output.accept(BrutalityModItems.WOODEN_RULER.get());
                        output.accept(BrutalityModItems.METAL_RULER.get().getDefaultInstance());
                        // region Original Items
                        output.accept(BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CREASE_OF_CREATION.get().getDefaultInstance());
                        output.accept(BrutalityModItems.EVENT_HORIZON.get().getDefaultInstance());
                        output.accept(BrutalityModItems.JACKPOT_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.PROVIDENCE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SEVENTH_STAR.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DOUBLE_DOWN.get().getDefaultInstance());
                        output.accept(BrutalityModItems.WHISPERWALTZ.get());
                        output.accept(BrutalityModItems.OLD_GPU.get());
                        output.accept(BrutalityModItems.PAPER_CUT.get());

                        output.accept(BrutalityModItems.SUPERNOVA.get().getDefaultInstance());

                        output.accept(BrutalityModItems.BLADE_OF_THE_RUINED_KING.get());
                        output.accept(BrutalityModItems.DARKIN_BLADE_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DARKIN_SCYTHE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.RHITTA_AXE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.FROSTMOURNE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MURASAMA_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DULL_KNIFE_DAGGER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.ROYAL_GUARDIAN_SWORD.get().getDefaultInstance());


                        output.accept(BrutalityModItems.WATER_COOLER_ITEM.get().getDefaultInstance());
                        output.accept(BrutalityModBlocks.GRAY_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.BLUE_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.GREEN_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.RED_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.WHITE_CUBICLE_PANEL.get());
                        output.accept(BrutalityModBlocks.CRT_MONITOR.get());
                        output.accept(BrutalityModBlocks.LCD_MONITOR.get());
                        output.accept(BrutalityModBlocks.DUSTBIN.get());
                        output.accept(BrutalityModBlocks.WET_FLOOR_SIGN.get());
                        output.accept(BrutalityModBlocks.WHITE_OFFICE_CHAIR.get());
                        output.accept(BrutalityModBlocks.BLACK_OFFICE_CHAIR.get());
                        output.accept(BrutalityModBlocks.WHITE_FILING_CABINET.get());
                        output.accept(BrutalityModBlocks.GRAY_FILING_CABINET.get());
                        output.accept(BrutalityModBlocks.OFFICE_LIGHT.get());
                        output.accept(BrutalityModBlocks.GRAY_OFFICE_CARPET.get());
                        output.accept(BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get());
                        output.accept(BrutalityModItems.IMPORTANT_DOCUMENTS.get().getDefaultInstance());
                        output.accept(BrutalityModItems.STYROFOAM_CUP.get().getDefaultInstance());
                        output.accept(BrutalityModItems.COFFEE_MACHINE_ITEM.get().getDefaultInstance());
                        output.accept(BrutalityModItems.MUG.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SUPER_SNIFFER_FIGURE_ITEM.get().getDefaultInstance());

                        output.accept(BrutalityModItems.ONYX_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.RUBY_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SAPPHIRE_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityModItems.TOPAZ_PHASESABER.get().getDefaultInstance());



//                        output.accept(BrutalityModItems.TERRA_BLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.TERRATOMERE_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityModItems.LAST_PRISM_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.EXOBLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.BIOMECH_REACTOR_TRIDENT.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MARIANAS_TRENCH.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CHALLENGER_DEEP.get().getDefaultInstance());

                        output.accept(BrutalityModItems.TERRA_HELMET.get().getDefaultInstance());
                        output.accept(BrutalityModItems.TERRA_CHESTPLATE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.TERRA_LEGGINGS.get().getDefaultInstance());
                        output.accept(BrutalityModItems.TERRA_BOOTS.get().getDefaultInstance());

                        output.accept(BrutalityModItems.NOIR_HELMET.get().getDefaultInstance());
                        output.accept(BrutalityModItems.NOIR_CHESTPLATE.get().getDefaultInstance());
                        output.accept(BrutalityModItems.NOIR_LEGGINGS.get().getDefaultInstance());
                        output.accept(BrutalityModItems.NOIR_BOOTS.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CANOPY_OF_SHADOWS.get().getDefaultInstance());
                        output.accept(BrutalityModItems.SHADOWSTEP.get().getDefaultInstance());



                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_MAGIC_TAB = CREATIVE_MODE_TABS.register("brutality_magic_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityModItems.DAEMONIC_TOME.get()))
                    .title(Component.translatable("creativeTab.brutality_magic_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityModItems.DAEMONIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.EVERGREEN_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.DARKIST_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.COSMIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.VOID_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.BRIMWIELDER_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.CELESTIA_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.EXODIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityModItems.UMBRAL_TOME.get().getDefaultInstance());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}

