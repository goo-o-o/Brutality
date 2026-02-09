package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Brutality.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BRUTALITY_MISCELLANEOUS_TAB = CREATIVE_MODE_TABS.register("brutality_miscellaneous_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.POCKET_BLACK_HOLE.get()))
                    .title(Component.translatable("creativeTab.brutality_miscellaneous_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityItems.POCKET_BLACK_HOLE.get());
                        output.accept(BrutalityItems.HIGH_FREQUENCY_ALLOY.get());
                        output.accept(BrutalityItems.QUANTITE_INGOT.get());
                        output.accept(BrutalityItems.BLUE_SLIME_BALL.get());
                        output.accept(BrutalityItems.PINK_SLIME_BALL.get());
                        output.accept(BrutalityItems.UNBRIDLED_RAGE.get());
                        output.accept(BrutalityItems.SOLIDIFIED_MANA.get());
                        output.accept(BrutalityItems.LIQUIFIED_MANA_BUCKET.get());

                        output.accept(BrutalityItems.BLACK_SEAL.get());
                        output.accept(BrutalityItems.BLUE_SEAL.get());
                        output.accept(BrutalityItems.GREEN_SEAL.get());
                        output.accept(BrutalityItems.ORANGE_SEAL.get());
                        output.accept(BrutalityItems.PINK_SEAL.get());
                        output.accept(BrutalityItems.PURPLE_SEAL.get());
                        output.accept(BrutalityItems.RED_SEAL.get());
                        output.accept(BrutalityItems.CYAN_SEAL.get());
                        output.accept(BrutalityItems.YELLOW_SEAL.get());
                        output.accept(BrutalityItems.BOMB_SEAL.get());
                        output.accept(BrutalityItems.COSMIC_SEAL.get());
                        output.accept(BrutalityItems.GLASS_SEAL.get());
                        output.accept(BrutalityItems.QUANTITE_SEAL.get());
                        output.accept(BrutalityItems.VOID_SEAL.get());
                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_BLOCK_TAB = CREATIVE_MODE_TABS.register("brutality_block_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityBlocks.CRT_MONITOR.get()))
                    .title(Component.translatable("creativeTab.brutality_block_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        BrutalityBlocks.CONCRETE_SLABS.forEach(block -> output.accept(block.get()));
                        BrutalityBlocks.CONCRETE_STAIRS.forEach(block -> output.accept(block.get()));

                        output.accept(BrutalityBlocks.TABLE_OF_WIZARDRY.get());
                        output.accept(BrutalityBlocks.PEDESTAL_OF_WIZARDRY.get());
                        output.accept(BrutalityBlocks.MANA_CANDLE.get());
                        output.accept(BrutalityBlocks.BOOKSHELF_OF_WIZARDRY.get());

                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_BLOCK.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_STAIRS.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_SLAB.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_WALL.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_DOOR.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_TRAPDOOR.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_BUTTON.get());
                        output.accept(BrutalityBlocks.SOLIDIFIED_MANA_PRESSURE_PLATE.get());

                        output.accept(BrutalityItems.WATER_COOLER_ITEM.get());
                        output.accept(BrutalityBlocks.PLASTERBOARD.get());
                        output.accept(BrutalityBlocks.LIGHT_GRAY_OFFICE_RUG.get());
                        output.accept(BrutalityBlocks.GRAY_OFFICE_RUG.get());
                        output.accept(BrutalityBlocks.OLD_SERVER_CASING.get());
                        output.accept(BrutalityBlocks.OLD_SERVER_PANEL.get());
                        output.accept(BrutalityBlocks.PUDDLE.get());
//                        output.accept(BrutalityModBlocks.PLASTERBOARD.get());
                        output.accept(BrutalityBlocks.EXIT_SIGN.get());
                        output.accept(BrutalityBlocks.TOILET.get());
                        output.accept(BrutalityBlocks.URINAL.get());
                        output.accept(BrutalityBlocks.LOWER_HVAC.get());
                        output.accept(BrutalityBlocks.UPPER_HVAC.get());
                        output.accept(BrutalityBlocks.OLD_AIR_CONDITIONER.get());
                        output.accept(BrutalityBlocks.GRAY_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.LIGHT_GRAY_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.BLUE_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.GREEN_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.RED_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.WHITE_CUBICLE_PANEL.get());
                        output.accept(BrutalityBlocks.CRT_MONITOR.get());
                        output.accept(BrutalityBlocks.LCD_MONITOR.get());
                        output.accept(BrutalityBlocks.DUSTBIN.get());
                        output.accept(BrutalityBlocks.WET_FLOOR_SIGN.get());
                        output.accept(BrutalityBlocks.WHITE_OFFICE_CHAIR.get());
                        output.accept(BrutalityBlocks.BLACK_OFFICE_CHAIR.get());
                        output.accept(BrutalityBlocks.WHITE_FILING_CABINET.get());
                        output.accept(BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get());
                        output.accept(BrutalityBlocks.GRAY_FILING_CABINET.get());
                        output.accept(BrutalityBlocks.OFFICE_LIGHT.get());
                        output.accept(BrutalityBlocks.SMALL_OFFICE_LIGHT.get());
                        output.accept(BrutalityBlocks.GRAY_OFFICE_CARPET.get());
                        output.accept(BrutalityBlocks.LIGHT_GRAY_OFFICE_CARPET.get());
                        output.accept(BrutalityItems.IMPORTANT_DOCUMENTS.get());

                        output.accept(BrutalityItems.COFFEE_MACHINE_ITEM.get().getDefaultInstance());
                        output.accept(BrutalityItems.SUPER_SNIFFER_FIGURE_ITEM.get().getDefaultInstance());


                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_VANITY_TAB = CREATIVE_MODE_TABS.register("brutality_vanity_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.GOLDEN_HEADBAND.get()))
                    .title(Component.translatable("creativeTab.brutality_vanity_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrutalityItems.SOLAR_SYSTEM.get());
                        output.accept(BrutalityItems.WOOLY_BLINDFOLD.get());
                        output.accept(BrutalityItems.TRIAL_GUARDIAN_EYEBROWS.get());
                        output.accept(BrutalityItems.TRIAL_GUARDIAN_HANDS.get());
                        output.accept(BrutalityItems.GOLDEN_HEADBAND.get());
                        output.accept(BrutalityItems.SERAPHIM_HALO.get());
//                        output.accept(BrutalityModItems.SUSPICIOUS_SLOT_MACHINE.get());

                    }).build());



    public static final RegistryObject<CreativeModeTab> BRUTALITY_GASTRONOMY_TAB = CREATIVE_MODE_TABS.register("brutality_gastronomy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.THE_GOLDEN_SPATULA_HAMMER.get()))
                    .title(Component.translatable("creativeTab.brutality_gastronomy_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BrutalityItems.SPATULA_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityItems.THE_GOLDEN_SPATULA_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityItems.IRON_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityItems.GOLD_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityItems.DIAMOND_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityItems.VOID_KNIFE.get().getDefaultInstance());
                        output.accept(BrutalityItems.MELONCHOLY_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityItems.APPLE_CORE_LANCE.get().getDefaultInstance());
                        output.accept(BrutalityItems.CHOPSTICK_STAFF.get().getDefaultInstance());
                        output.accept(BrutalityItems.BAMBOO_STAFF.get().getDefaultInstance());
                        output.accept(BrutalityItems.FRYING_PAN.get().getDefaultInstance());
                        output.accept(BrutalityItems.POTATO_MASHER.get().getDefaultInstance());
                        output.accept(BrutalityItems.WHISK_HAMMER.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.PANDORAS_CAULDRON.get());

                        output.accept(BrutalityItems.PEPPER_SHAKER.get().getDefaultInstance());
                        output.accept(BrutalityItems.SALT_SHAKER.get().getDefaultInstance());
                        output.accept(BrutalityItems.SALT_AND_PEPPER.get().getDefaultInstance());

                        output.accept(BrutalityItems.SMOKE_STONE.get().getDefaultInstance());
                        output.accept(BrutalityItems.THE_SMOKEHOUSE.get().getDefaultInstance());

                        output.accept(BrutalityItems.BAMBOO_STEAMER.get().getDefaultInstance());

                        output.accept(BrutalityItems.SUGAR_GLAZE.get().getDefaultInstance());
                        output.accept(BrutalityItems.RAINBOW_SPRINKLES.get().getDefaultInstance());
                        output.accept(BrutalityItems.ROCK_CANDY_RING.get().getDefaultInstance());
                        output.accept(BrutalityItems.SEARED_SUGAR_BROOCH.get().getDefaultInstance());
                        output.accept(BrutalityItems.DUNKED_DONUT.get().getDefaultInstance());
                        output.accept(BrutalityItems.CARAMEL_CRUNCH_MEDALLION.get().getDefaultInstance());
                        output.accept(BrutalityItems.LOLLIPOP_OF_ETERNITY.get().getDefaultInstance());
                        output.accept(BrutalityItems.ICE_CREAM_SANDWICH.get().getDefaultInstance());

                        output.accept(BrutalityItems.MORTAR_AND_PESTLE.get().getDefaultInstance());
                        output.accept(BrutalityItems.BUTTER_GAUNTLETS.get().getDefaultInstance());

                        output.accept(BrutalityItems.TOMATO_SAUCE.get().getDefaultInstance());
                        output.accept(BrutalityItems.CHEESE_SAUCE.get().getDefaultInstance());
                        output.accept(BrutalityItems.PIZZA_SLOP.get().getDefaultInstance());

                        output.accept(BrutalityItems.HOT_SAUCE.get());

                        output.accept(BrutalityItems.OLIVE_OIL.get());
                        output.accept(BrutalityItems.EXTRA_VIRGIN_OLIVE_OIL.get());

                        output.accept(BrutalityItems.FRIDGE.get());
                        output.accept(BrutalityItems.SMART_FRIDGE.get());
                    }).build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_CURIO_TAB = CREATIVE_MODE_TABS.register("brutality_curio_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.CARTON_OF_PRISM_SOLUTION_MILK.get()))
                    .title(Component.translatable("creativeTab.brutality_curio_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityItems.PI.get());
                        output.accept(BrutalityItems.EXPONENTIAL_CHARM.get());
                        output.accept(BrutalityItems.ADDITION_CHARM.get());
                        output.accept(BrutalityItems.SUBTRACTION.get());
                        output.accept(BrutalityItems.MULTIPLICATION.get());
                        output.accept(BrutalityItems.DIVISION.get());
                        output.accept(BrutalityItems.SUM.get());
                        output.accept(BrutalityItems.SINE.get());
                        output.accept(BrutalityItems.COSINE.get());
                        output.accept(BrutalityItems.SCIENTIFIC_CALCULATOR.get());
                        output.accept(BrutalityItems.HYPERBOLIC_FEATHER.get());


                        output.accept(BrutalityItems.CARTON_OF_PRISM_SOLUTION_MILK.get());
                        output.accept(BrutalityItems.LIGHT_SWITCH.get());
                        output.accept(BrutalityItems.FUZZY_DICE.get());
                        output.accept(BrutalityItems.DIVINE_IMMOLATION.get());
                        output.accept(BrutalityItems.CRYPTO_WALLET.get());
                        output.accept(BrutalityItems.PORTABLE_MINING_RIG.get());
                        output.accept(BrutalityItems.DUMBBELL.get());
                        output.accept(BrutalityItems.BOX_OF_CHOCOLATES.get());
                        output.accept(BrutalityItems.BROKEN_HEART.get());
//                        output.accept(BrutalityModItems.BIKERS_HELMET.get());
                        output.accept(BrutalityItems.ESCAPE_KEY.get());
                        output.accept(BrutalityItems.MIRACLE_CURE.get());
                        output.accept(BrutalityItems.ESSENTIAL_OILS.get());
                        output.accept(BrutalityItems.GLASS_HEART.get());
                        output.accept(BrutalityItems.GOOD_BOOK.get());
                        output.accept(BrutalityItems.CELESTIAL_STARBOARD.get());
                        output.accept(BrutalityItems.PLUNDER_CHEST.get());
                        output.accept(BrutalityItems.ROAD_RUNNERS_RING.get());
                        output.accept(BrutalityItems.ABYSSAL_NECKLACE.get());
                        output.accept(BrutalityItems.BRAIN_ROT.get());
                        output.accept(BrutalityItems.RESPLENDENT_FEATHER.get());
                        output.accept(BrutalityItems.NANOMACHINES.get());
                        output.accept(BrutalityItems.DUELING_GLOVE.get());
                        output.accept(BrutalityItems.MICROBLADE_BAND.get());
                        output.accept(BrutalityItems.CROWN_OF_TYRANNY.get());
                        output.accept(BrutalityItems.PORTABLE_QUANTUM_THINGAMABOB.get());
                        output.accept(BrutalityItems.WARPSLICE_SCABBARD.get());
                        output.accept(BrutalityItems.DAEMONIUM_WHETSTONE.get());
                        output.accept(BrutalityItems.KNIGHTS_PENDANT.get());
                        output.accept(BrutalityItems.THE_OATH.get());
                        output.accept(BrutalityItems.LUCKY_INSOLES.get());
                        output.accept(BrutalityItems.OLD_GUILLOTINE.get());
                        output.accept(BrutalityItems.GAMBLERS_CHAIN.get());
                        output.accept(BrutalityItems.DAEMONIUM_SEWING_KIT.get());
                        output.accept(BrutalityItems.PINCUSHION.get());
                        output.accept(BrutalityItems.SOLDIERS_SYRINGE.get());
                        output.accept(BrutalityItems.TARGET_CUBE.get());
                        output.accept(BrutalityItems.BRUTESKIN_BELT.get());
                        output.accept(BrutalityItems.ENERGY_FOCUSER.get());
                        output.accept(BrutalityItems.CRITICAL_THINKING.get());
                        output.accept(BrutalityItems.DEADSHOT_BROOCH.get());
                        output.accept(BrutalityItems.KNUCKLE_WRAPS.get());
                        output.accept(BrutalityItems.VINDICATOR_STEROIDS.get());
                        output.accept(BrutalityItems.CROWBAR.get());
                        output.accept(BrutalityItems.WIRE_CUTTERS.get());
                        output.accept(BrutalityItems.AIR_JORDAN_EARRINGS.get());
                        output.accept(BrutalityItems.JURY_NULLIFIER.get());
                        output.accept(BrutalityItems.EYE_OF_THE_DRAGON.get());
                        output.accept(BrutalityItems.LENS_MAKERS_GLASSES.get());
                        output.accept(BrutalityItems.SCOPE_GOGGLES.get());
                        output.accept(BrutalityItems.BLACK_MATTER_NECKLACE.get());
                        output.accept(BrutalityItems.PHANTOM_FINGER.get());
                        output.accept(BrutalityItems.SILVER_BOOSTER_PACK.get().getDefaultInstance());
                        output.accept(BrutalityItems.DIAMOND_BOOSTER_PACK.get());
                        output.accept(BrutalityItems.EVIL_KING_BOOSTER_PACK.get());
                        output.accept(BrutalityItems.SILVER_RESPAWN_CARD.get());
                        output.accept(BrutalityItems.DIAMOND_RESPAWN_CARD.get());
                        output.accept(BrutalityItems.EVIL_KING_RESPAWN_CARD.get());
                        output.accept(BrutalityItems.YATA_NO_KAGAMI.get());

                        output.accept(BrutalityItems.GREED.get());
                        output.accept(BrutalityItems.PRIDE.get());
                        output.accept(BrutalityItems.SLOTH.get());
                        output.accept(BrutalityItems.ENVY.get());
                        output.accept(BrutalityItems.LUST.get());
                        output.accept(BrutalityItems.GLUTTONY.get());

                        output.accept(BrutalityItems.DRAGONHEART.get());
                        output.accept(BrutalityItems.UVOGRE_HEART.get());
                        output.accept(BrutalityItems.ZOMBIE_HEART.get());
                        output.accept(BrutalityItems.FROZEN_HEART.get());
                        output.accept(BrutalityItems.HEART_OF_GOLD.get());
                        output.accept(BrutalityItems.SECOND_HEART.get());
                        output.accept(BrutalityItems.BRUTAL_HEART.get());
                        output.accept(BrutalityItems.NINJA_HEART.get());

                        output.accept(BrutalityItems.EMPTY_ANKLET.get());
                        output.accept(BrutalityItems.DAVYS_ANKLET.get());
                        output.accept(BrutalityItems.ANKLET_OF_THE_IMPRISONED.get());
                        output.accept(BrutalityItems.SHARPNESS_ANKLET.get());
                        output.accept(BrutalityItems.DEBUG_ANKLET.get());
                        output.accept(BrutalityItems.REDSTONE_ANKLET.get());
                        output.accept(BrutalityItems.DEVILS_ANKLET.get());
                        output.accept(BrutalityItems.BASKETBALL_ANKLET.get());
                        output.accept(BrutalityItems.EMERALD_ANKLET.get());
                        output.accept(BrutalityItems.RUBY_ANKLET.get());
                        output.accept(BrutalityItems.TOPAZ_ANKLET.get());
                        output.accept(BrutalityItems.SAPPHIRE_ANKLET.get());
                        output.accept(BrutalityItems.ONYX_ANKLET.get());
                        output.accept(BrutalityItems.ULTRA_DODGE_ANKLET.get());
                        output.accept(BrutalityItems.GUNDALFS_ANKLET.get());
                        output.accept(BrutalityItems.TRIAL_ANKLET.get());
                        output.accept(BrutalityItems.SUPER_DODGE_ANKLET.get());
                        output.accept(BrutalityItems.GNOME_KINGS_ANKLET.get());
                        output.accept(BrutalityItems.ANKLENT.get());
                        output.accept(BrutalityItems.ANKLE_MONITOR.get());
                        output.accept(BrutalityItems.FIERY_ANKLET.get());
                        output.accept(BrutalityItems.SACRED_SPEED_ANKLET.get());
                        output.accept(BrutalityItems.CONDUCTITE_ANKLET.get());
                        output.accept(BrutalityItems.BLOOD_CLOT_ANKLET.get());
                        output.accept(BrutalityItems.VIRENTIUM_ANKLET.get());
                        output.accept(BrutalityItems.COSMIC_ANKLET.get());
                        output.accept(BrutalityItems.VOID_ANKLET.get());
                        output.accept(BrutalityItems.IRONCLAD_ANKLET.get());
                        output.accept(BrutalityItems.GLADIATORS_ANKLET.get());
                        output.accept(BrutalityItems.NYXIUM_ANKLET.get());
                        output.accept(BrutalityItems.EXODIUM_ANKLET.get());
                        output.accept(BrutalityItems.WINDSWEPT_ANKLET.get());
                        output.accept(BrutalityItems.BIG_STEPPA.get());

                        output.accept(BrutalityItems.HELL_SPECS.get());
                        output.accept(BrutalityItems.STYGIAN_CHAIN.get());
                        output.accept(BrutalityItems.BLOOD_CHALICE.get());
                        output.accept(BrutalityItems.HEMOMATIC_LOCKET.get());
                        output.accept(BrutalityItems.SANGUINE_SIGNET.get());
                        output.accept(BrutalityItems.SELF_REPAIR_NEXUS.get());
                        output.accept(BrutalityItems.HEMOGRAFT_NEEDLE.get());
                        output.accept(BrutalityItems.VAMPIRE_FANG.get());
                        output.accept(BrutalityItems.SANGUINE_SPECTACLES.get());
                        output.accept(BrutalityItems.PROGENITORS_EARRINGS.get());
                        output.accept(BrutalityItems.BLOODSTAINED_MIRROR.get());
                        output.accept(BrutalityItems.INCOGNITO_MODE.get());
                        output.accept(BrutalityItems.MINIATURE_ANCHOR.get());
                        output.accept(BrutalityItems.PAPER_AIRPLANE.get());
                        output.accept(BrutalityItems.FIRE_EXTINGUISHER.get());
                        output.accept(BrutalityItems.LUCKY_BOOKMARK.get());
                        output.accept(BrutalityItems.EMERGENCY_MEETING.get());
                        output.accept(BrutalityItems.PENCIL_SHARPENER.get());
                        output.accept(BrutalityItems.CHOCOLATE_BAR.get());
                        output.accept(BrutalityItems.VAMPIRIC_TALISMAN.get());
                        output.accept(BrutalityItems.FALLEN_ANGELS_HALO.get());
                        output.accept(BrutalityItems.HELLSPEC_TIE.get());
                        output.accept(BrutalityItems.BLOOD_PACK.get());



                        output.accept(BrutalityItems.RAGE_STONE.get());
                        output.accept(BrutalityItems.PACK_OF_CIGARETTES.get());
                        output.accept(BrutalityItems.STRESS_PILLS.get());
                        output.accept(BrutalityItems.SEROTONIN_PILLS.get());
                        output.accept(BrutalityItems.FURY_BATTERY.get());
                        output.accept(BrutalityItems.PAIN_CATALYST.get());
                        output.accept(BrutalityItems.RAMPAGE_CLOCK.get());
                        output.accept(BrutalityItems.BLOOD_HOWL_PENDANT.get());
                        output.accept(BrutalityItems.RAGE_BAIT.get());
                        output.accept(BrutalityItems.OMEGA_GAUNTLET.get());
                        output.accept(BrutalityItems.SPITE_SHARD.get());
                        output.accept(BrutalityItems.HATE_SIGIL.get());
                        output.accept(BrutalityItems.HEART_OF_WRATH.get());
                        output.accept(BrutalityItems.EYE_FOR_VIOLENCE.get());
                        output.accept(BrutalityItems.BATTLE_SCARS.get());
                        output.accept(BrutalityItems.MECHANICAL_AORTA.get());
                        output.accept(BrutalityItems.BLOOD_PULSE_GAUNTLETS.get());
                        output.accept(BrutalityItems.BROKEN_CONTROLLER.get());
                        output.accept(BrutalityItems.FURY_BAND.get());
                        output.accept(BrutalityItems.GRUDGE_TOTEM.get());
                        output.accept(BrutalityItems.BLOOD_STONE.get());
                        output.accept(BrutalityItems.WRATH.get());
                        output.accept(BrutalityItems.ENDER_DRAGON_STEM_CELLS.get());
                        output.accept(BrutalityItems.ANGER_MANAGEMENT.get());
                        output.accept(BrutalityItems.BOILING_BLOOD.get());
                        output.accept(BrutalityItems.FACE_PIE.get().getDefaultInstance());
                        output.accept(BrutalityItems.MASK_OF_MADNESS.get().getDefaultInstance());

                        output.accept(BrutalityItems.CROWN_OF_DOMINATION.get().getDefaultInstance());
                        output.accept(BrutalityItems.BEAD_OF_LIFE.get());
                        output.accept(BrutalityItems.PERFECT_CELL.get());
                        output.accept(BrutalityItems.SAD_UVOGRE.get());
                        output.accept(BrutalityItems.SUSPICIOUSLY_LARGE_HANDLE.get());

                        output.accept(BrutalityItems.BROKEN_CLOCK.get());
                        output.accept(BrutalityItems.SHATTERED_CLOCK.get());
                        output.accept(BrutalityItems.SUNDERED_CLOCK.get());
                        output.accept(BrutalityItems.TIMEKEEPERS_CLOCK.get());
                        output.accept(BrutalityItems.THE_CLOCK_OF_FROZEN_TIME.get());
                        output.accept(BrutalityItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get());
                        output.accept(BrutalityItems.PORTABLE_TRAMPOLINE.get());
                        output.accept(BrutalityItems.QUANTUM_LUBRICANT.get());
                        output.accept(BrutalityItems.AEROPHOBIC_NANOCOATING.get());
                        output.accept(BrutalityItems.INERTIA_BOOSTER.get());
                        output.accept(BrutalityItems.ELBOW_GREASE.get());
                        output.accept(BrutalityItems.EARTHEN_BLESSING.get());
                        output.accept(BrutalityItems.PETROLEUM_JELLY.get());
                        output.accept(BrutalityItems.ZEPHYR_IN_A_BOTTLE.get());


                        output.accept(BrutalityItems.SOUL_STONE.get());
                        output.accept(BrutalityItems.EMERGENCY_FLASK.get());
                        output.accept(BrutalityItems.RING_OF_MANA.get());
                        output.accept(BrutalityItems.RING_OF_MANA_PLUS.get());
                        output.accept(BrutalityItems.CONSERVATIVE_CONCOCTION.get());
                        output.accept(BrutalityItems.RUNE_OF_DELTA.get());
                        output.accept(BrutalityItems.ECHO_CHAMBER.get());
                        output.accept(BrutalityItems.ONYX_IDOL.get());
                        output.accept(BrutalityItems.SCRIBES_INDEX.get());
                        output.accept(BrutalityItems.BLACK_HOLE_ORB.get());
                        output.accept(BrutalityItems.FORBIDDEN_ORB.get());
                        output.accept(BrutalityItems.BLOOD_ORB.get());
                        output.accept(BrutalityItems.PROFANUM_REACTOR.get());
                        output.accept(BrutalityItems.MANA_SYRINGE.get());
                        output.accept(BrutalityItems.PRISMATIC_ORB.get());
                        output.accept(BrutalityItems.AQUA_RULER.get());
                        output.accept(BrutalityItems.DECK_OF_CARDS.get());
                        output.accept(BrutalityItems.DIVERGENT_RECURSOR.get());
                        output.accept(BrutalityItems.CONVERGENT_RECURSOR.get());
                        output.accept(BrutalityItems.INFINITE_RECURSOR.get());
                        output.accept(BrutalityItems.APPRENTICES_MANUAL_TO_BASIC_MULTICASTING.get());
                        output.accept(BrutalityItems.WIZARDS_GUIDEBOOK_TO_ADVANCED_MULTICASTING.get());
                        output.accept(BrutalityItems.ARCHMAGES_THESIS_TO_MASTERFUL_MULTICASTING.get());
                        output.accept(BrutalityItems.PARAGON_OF_THE_FIRST_MAGE.get());
                    }).build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_WEAPON_TAB = CREATIVE_MODE_TABS.register("brutality_weapon_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.SEVENTH_STAR.get()))
                    .title(Component.translatable("creativeTab.brutality_weapon_tab"))
                    .displayItems((itemDisplayParameters, output) -> {


                        output.accept(BrutalityItems.CINDER_BLOCK.get());
                        output.accept(BrutalityItems.BIOMECH_REACTOR.get());
                        output.accept(BrutalityItems.SCULK_GRENADE.get());
                        output.accept(BrutalityItems.STICKY_BOMB.get());
                        output.accept(BrutalityItems.BEACH_BALL.get());
                        output.accept(BrutalityItems.BLAST_BARREL.get());
                        output.accept(BrutalityItems.DECK_OF_FATE.get());
                        output.accept(BrutalityItems.PERFUME_BOTTLE.get());
                        output.accept(BrutalityItems.ABSOLUTE_ZERO.get());
                        output.accept(BrutalityItems.CRIMSON_DELIGHT.get());
                        output.accept(BrutalityItems.CANNONBALL_CABBAGE.get());
                        output.accept(BrutalityItems.CAVENDISH.get());
                        output.accept(BrutalityItems.STICK_OF_BUTTER.get());
                        output.accept(BrutalityItems.GOLDEN_PHOENIX.get());
                        output.accept(BrutalityItems.WINTER_MELON.get());
                        output.accept(BrutalityItems.ICE_CUBE.get());
                        output.accept(BrutalityItems.PERMAFROST_CUBE.get());
                        output.accept(BrutalityItems.OVERCLOCKED_TOASTER.get());
                        output.accept(BrutalityItems.HOLY_HAND_GRENADE.get());
                        output.accept(BrutalityItems.GUNGNIR_TRIDENT.get().getDefaultInstance());
                        output.accept(BrutalityItems.VAMPIRE_KNIVES.get().getDefaultInstance());
                        output.accept(BrutalityItems.PHOTON.get());
                        output.accept(BrutalityItems.POUCH_O_PHOTONS.get());
                        output.accept(BrutalityItems.DYNAMITE.get());
                        output.accept(BrutalityItems.STICKY_DYNAMITE.get());
                        output.accept(BrutalityItems.BOUNCY_DYNAMITE.get());
                        output.accept(BrutalityItems.THUNDERBOLT_TRIDENT.get().getDefaultInstance());

                        output.accept(BrutalityItems.DEATHSAW.get());
                        output.accept(BrutalityItems.WOODEN_RULER.get());
                        output.accept(BrutalityItems.METAL_RULER.get().getDefaultInstance());
                        // region Original Items
                        output.accept(BrutalityItems.ATOMIC_JUDGEMENT_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityItems.CREASE_OF_CREATION.get().getDefaultInstance());
                        output.accept(BrutalityItems.EVENT_HORIZON.get().getDefaultInstance());
                        output.accept(BrutalityItems.PUREBLOOD.get().getDefaultInstance());
                        output.accept(BrutalityItems.JACKPOT_HAMMER.get().getDefaultInstance());
                        output.accept(BrutalityItems.PROVIDENCE.get().getDefaultInstance());
                        output.accept(BrutalityItems.SEVENTH_STAR.get().getDefaultInstance());
                        output.accept(BrutalityItems.DOUBLE_DOWN.get().getDefaultInstance());
                        output.accept(BrutalityItems.WHISPERWALTZ.get());
                        output.accept(BrutalityItems.OLD_GPU.get());
                        output.accept(BrutalityItems.PAPER_CUT.get());

                        output.accept(BrutalityItems.SUPERNOVA.get().getDefaultInstance());

                        output.accept(BrutalityItems.BLADE_OF_THE_RUINED_KING.get());
                        output.accept(BrutalityItems.PRISMATIC_GREATSWORD.get());
                        output.accept(BrutalityItems.WORLD_TREE_SWORD.get());
                        output.accept(BrutalityItems.RHONGOMYNIAD.get());
                        output.accept(BrutalityItems.CALDRITH.get());
                        output.accept(BrutalityItems.SCHISM.get());
                        output.accept(BrutalityItems.HELLSPEC_CLEAVER.get());
                        output.accept(BrutalityItems.CONDUCTITE_CAPACITOR.get());
                        output.accept(BrutalityItems.SHADOWFLAME_SCISSOR_BLADE.get());
                        output.accept(BrutalityItems.CRIMSON_SCISSOR_BLADE.get());
                        output.accept(BrutalityItems.DARKIN_BLADE_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityItems.DARKIN_SCYTHE.get().getDefaultInstance());
                        output.accept(BrutalityItems.RHITTA_AXE.get().getDefaultInstance());
                        output.accept(BrutalityItems.FROSTMOURNE_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityItems.AMATERASU.get());
                        output.accept(BrutalityItems.TSUKUYOMI.get());
//                        output.accept(BrutalityModItems.MURASAMA_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityItems.DULL_KNIFE_DAGGER.get().getDefaultInstance());
                        output.accept(BrutalityItems.ROYAL_GUARDIAN_SWORD.get().getDefaultInstance());

                        output.accept(BrutalityItems.STYROFOAM_CUP.get().getDefaultInstance());
                        output.accept(BrutalityItems.MUG.get().getDefaultInstance());

                        output.accept(BrutalityItems.ONYX_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityItems.RUBY_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityItems.SAPPHIRE_PHASESABER.get().getDefaultInstance());
                        output.accept(BrutalityItems.TOPAZ_PHASESABER.get().getDefaultInstance());



//                        output.accept(BrutalityModItems.TERRA_BLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.TERRATOMERE_SWORD.get().getDefaultInstance());
                        output.accept(BrutalityItems.LAST_PRISM_ITEM.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.EXOBLADE_SWORD.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.BIOMECH_REACTOR_TRIDENT.get().getDefaultInstance());
                        output.accept(BrutalityItems.DEPTH_CRUSHER_TRIDENT.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.MARIANAS_TRENCH.get().getDefaultInstance());
//                        output.accept(BrutalityModItems.CHALLENGER_DEEP.get().getDefaultInstance());

                        output.accept(BrutalityItems.TERRA_HELMET.get().getDefaultInstance());
                        output.accept(BrutalityItems.TERRA_CHESTPLATE.get().getDefaultInstance());
                        output.accept(BrutalityItems.TERRA_LEGGINGS.get().getDefaultInstance());
                        output.accept(BrutalityItems.TERRA_BOOTS.get().getDefaultInstance());

                        output.accept(BrutalityItems.NOIR_HELMET.get().getDefaultInstance());
                        output.accept(BrutalityItems.NOIR_CHESTPLATE.get().getDefaultInstance());
                        output.accept(BrutalityItems.NOIR_LEGGINGS.get().getDefaultInstance());
                        output.accept(BrutalityItems.NOIR_BOOTS.get().getDefaultInstance());

                        output.accept(BrutalityItems.VAMPIRE_LORD_HELMET.get().getDefaultInstance());
                        output.accept(BrutalityItems.VAMPIRE_LORD_CHESTPLATE.get().getDefaultInstance());
                        output.accept(BrutalityItems.VAMPIRE_LORD_LEGGINGS.get().getDefaultInstance());
                        output.accept(BrutalityItems.VAMPIRE_LORD_BOOTS.get().getDefaultInstance());
                        output.accept(BrutalityItems.CANOPY_OF_SHADOWS.get().getDefaultInstance());
                        output.accept(BrutalityItems.SHADOWSTEP.get().getDefaultInstance());

                        output.accept(BrutalityItems.BASIC_STAT_TRAKKER.get().getDefaultInstance());
                        output.accept(BrutalityItems.GOLDEN_STAT_TRAKKER.get().getDefaultInstance());
                        output.accept(BrutalityItems.PRISMATIC_STAT_TRAKKER.get().getDefaultInstance());
                        output.accept(BrutalityItems.THE_CLOUD.get().getDefaultInstance());
                        output.accept(BrutalityItems.HANDCUFFS.get().getDefaultInstance());
                        output.accept(BrutalityItems.PRISON_KEY.get());


                    })
                    .build());


    public static final RegistryObject<CreativeModeTab> BRUTALITY_MAGIC_TAB = CREATIVE_MODE_TABS.register("brutality_magic_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BrutalityItems.DAEMONIC_TOME.get()))
                    .title(Component.translatable("creativeTab.brutality_magic_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(BrutalityItems.DAEMONIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.EVERGREEN_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.DARKIST_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.COSMIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.VOIDWALKER_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.BRIMWIELDER_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.CELESTIA_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.EXODIC_TOME.get().getDefaultInstance());
                        output.accept(BrutalityItems.UMBRAL_TOME.get().getDefaultInstance());

                        output.accept(BrutalityItems.DAEMONIC_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.EVERGREEN_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.DARKIST_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.COSMIC_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.VOIDWALKER_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.BRIMWIELDER_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.CELESTIA_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.EXODIC_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.UMBRAL_SPELL_SCROLL.get().getDefaultInstance());
                        output.accept(BrutalityItems.VOLTWEAVER_SPELL_SCROLL.get().getDefaultInstance());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}

