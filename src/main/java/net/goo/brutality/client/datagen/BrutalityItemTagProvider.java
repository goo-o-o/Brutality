package net.goo.brutality.client.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.BrutalityTags;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BrutalityItemTagProvider extends ItemTagsProvider {
    public BrutalityItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                                    CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, Brutality.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        addCurioTags();
        addAugmentTags();
        addMathTags();
        addMagicTags();
        addRageTags();
        addGastronomistTags();
        addWeaponTags();
        this.tag(ItemTags.CANDLES).add(BrutalityBlocks.MANA_CANDLE.get().asItem());
    }

    private void addWeaponTags() {
        Collection<RegistryObject<Item>> itemsEntries = BrutalityItems.ITEMS.getEntries();

        for (RegistryObject<Item> itemRegistryObject : itemsEntries) {
            Item item = itemRegistryObject.get();
            if (ItemCategoryUtils.isSword(item)) {
                this.tag(ItemTags.SWORDS).add(item);
            }
            if (ItemCategoryUtils.isTool(item)) {
                this.tag(ItemTags.TOOLS).add(item);
            }
            if (ItemCategoryUtils.isPickaxe(item)) {
                this.tag(ItemTags.PICKAXES).add(item);
            }
            if (ItemCategoryUtils.isAxe(item)) {
                this.tag(ItemTags.AXES).add(item);
            }
            if (ItemCategoryUtils.isHoe(item)) {
                this.tag(ItemTags.HOES).add(item);
            }
            if (ItemCategoryUtils.isShovel(item)) {
                this.tag(ItemTags.SHOVELS).add(item);
            }
            if (ItemCategoryUtils.isShear(item)) {
                this.tag(Tags.Items.SHEARS).add(item);
            }
            if (ItemCategoryUtils.isTrident(item)) {
                this.tag(Tags.Items.TOOLS_TRIDENTS).add(item);
            }
            if (ItemCategoryUtils.isShield(item)) {
                this.tag(Tags.Items.TOOLS_SHIELDS).add(item);
            }
            if (ItemCategoryUtils.isBow(item)) {
                this.tag(Tags.Items.TOOLS_BOWS).add(item);
            }

            if (ItemCategoryUtils.isGun(item)) {
                this.tag(BrutalityTags.Items.GUN).add(item);
            }
            if (ItemCategoryUtils.isScythe(item)) {
                this.tag(BrutalityTags.Items.SCYTHE).add(item);
            }
            if (ItemCategoryUtils.isHammer(item)) {
                this.tag(BrutalityTags.Items.HAMMER).add(item);
            }
            if (ItemCategoryUtils.isSpear(item)) {
                this.tag(BrutalityTags.Items.SPEAR).add(item);
            }


        }

    }


    private void addAnkletTags() {
        this.tag(BrutalityTags.Items.ANKLET).add(
                BrutalityItems.DAVYS_ANKLET.get(),
                BrutalityItems.ANKLET_OF_THE_IMPRISONED.get(),
                BrutalityItems.DEBUG_ANKLET.get(),
                BrutalityItems.ANKLE_MONITOR.get(),
                BrutalityItems.REDSTONE_ANKLET.get(),
                BrutalityItems.DEVILS_ANKLET.get(),
                BrutalityItems.BASKETBALL_ANKLET.get(),
                BrutalityItems.EMERALD_ANKLET.get(),
                BrutalityItems.RUBY_ANKLET.get(),
                BrutalityItems.TOPAZ_ANKLET.get(),
                BrutalityItems.SAPPHIRE_ANKLET.get(),
                BrutalityItems.ONYX_ANKLET.get(),
                BrutalityItems.ULTRA_DODGE_ANKLET.get(),
                BrutalityItems.GUNDALFS_ANKLET.get(),
                BrutalityItems.TRIAL_ANKLET.get(),
                BrutalityItems.SUPER_DODGE_ANKLET.get(),
                BrutalityItems.GNOME_KINGS_ANKLET.get(),
                BrutalityItems.SHARPNESS_ANKLET.get(),
                BrutalityItems.ANKLENT.get(),
                BrutalityItems.EMPTY_ANKLET.get(),
                BrutalityItems.BIG_STEPPA.get(),
                BrutalityItems.FIERY_ANKLET.get(),
                BrutalityItems.WINDSWEPT_ANKLET.get(),
                BrutalityItems.CONDUCTITE_ANKLET.get(),
                BrutalityItems.SACRED_SPEED_ANKLET.get(),
                BrutalityItems.VIRENTIUM_ANKLET.get(),
                BrutalityItems.COSMIC_ANKLET.get(),
                BrutalityItems.VOID_ANKLET.get(),
                BrutalityItems.NYXIUM_ANKLET.get(),
                BrutalityItems.GLADIATORS_ANKLET.get(),
                BrutalityItems.IRONCLAD_ANKLET.get(),
                BrutalityItems.BLOOD_CLOT_ANKLET.get()
        );
    }

    private void addCharmTags() {
        // done by AI to save time
        this.tag(BrutalityTags.Items.CHARM).add(
                // Original Items
                BrutalityItems.PLUNDER_CHEST.get(),
                BrutalityItems.CENSORED.get(),
                BrutalityItems.REDACTED.get(),
                BrutalityItems.RESPLENDENT_FEATHER.get(),
                BrutalityItems.CELESTIAL_STARBOARD.get(),
                BrutalityItems.YATA_NO_KAGAMI.get(),
                BrutalityItems.PORTABLE_QUANTUM_THINGAMABOB.get(),
                BrutalityItems.DAEMONIUM_WHETSTONE.get(),
                BrutalityItems.OMNIDIRECTIONAL_MOVEMENT_GEAR.get(),
                BrutalityItems.ERROR_404.get(),
                BrutalityItems.KINETIC_COMPENSATOR.get(),
                BrutalityItems.WAY_OF_THE_WIND.get(),
                BrutalityItems.SOLDIERS_SYRINGE.get(),
                BrutalityItems.INCOGNITO_MODE.get(),
                BrutalityItems.EARTHEN_BLESSING.get(),

                // Utility & General Charms
                BrutalityItems.ESSENTIAL_OILS.get(),
                BrutalityItems.OLD_GUILLOTINE.get(),
                BrutalityItems.PINCUSHION.get(),
                BrutalityItems.DEADSHOT_BROOCH.get(),
                BrutalityItems.ENERGY_FOCUSER.get(),
                BrutalityItems.CHOCOLATE_BAR.get(),
                BrutalityItems.DAEMONIUM_SEWING_KIT.get(),
                BrutalityItems.KNUCKLE_WRAPS.get(),
                BrutalityItems.PENCIL_SHARPENER.get(),
                BrutalityItems.TARGET_CUBE.get(),
                BrutalityItems.ESCAPE_KEY.get(),
                BrutalityItems.BLOOD_PACK.get(),
                BrutalityItems.EMERGENCY_MEETING.get(),
                BrutalityItems.PAPER_AIRPLANE.get(),
                BrutalityItems.MIRACLE_CURE.get(),
                BrutalityItems.LUCKY_INSOLES.get(),
                BrutalityItems.DIVINE_IMMOLATION.get(),
                BrutalityItems.LIGHT_SWITCH.get(),
                BrutalityItems.VINDICATOR_STEROIDS.get(),
                BrutalityItems.WIRE_CUTTERS.get(),
                BrutalityItems.SILVER_RESPAWN_CARD.get(),
                BrutalityItems.DIAMOND_RESPAWN_CARD.get(),
                BrutalityItems.EVIL_KING_RESPAWN_CARD.get(),
                BrutalityItems.SILVER_BOOSTER_PACK.get(),
                BrutalityItems.DIAMOND_BOOSTER_PACK.get(),
                BrutalityItems.EVIL_KING_BOOSTER_PACK.get(),
                BrutalityItems.PORTABLE_TRAMPOLINE.get(),
                BrutalityItems.ELBOW_GREASE.get(),
                BrutalityItems.QUANTUM_LUBRICANT.get(),
                BrutalityItems.AEROPHOBIC_NANOCOATING.get(),
                BrutalityItems.INERTIA_BOOSTER.get(),
                BrutalityItems.PETROLEUM_JELLY.get(),
                BrutalityItems.ZEPHYR_IN_A_BOTTLE.get(),

                // Rage & Spite Set
                BrutalityItems.BLOOD_STONE.get(),
                BrutalityItems.RAGE_STONE.get(),
                BrutalityItems.RAGE_BAIT.get(),
                BrutalityItems.PAIN_CATALYST.get(),
                BrutalityItems.GRUDGE_TOTEM.get(),
                BrutalityItems.RAMPAGE_CLOCK.get(),
                BrutalityItems.SPITE_SHARD.get(),
                BrutalityItems.MECHANICAL_AORTA.get(),
                BrutalityItems.HATE_SIGIL.get(),
                BrutalityItems.FURY_BATTERY.get(),
                BrutalityItems.SEROTONIN_PILLS.get(),
                BrutalityItems.WRATH.get(),
                BrutalityItems.STRESS_PILLS.get(),
                BrutalityItems.ENDER_DRAGON_STEM_CELLS.get(),
                BrutalityItems.BOILING_BLOOD.get(),
                BrutalityItems.PACK_OF_CIGARETTES.get(),

                // Deadly Sins
                BrutalityItems.PRIDE.get(),
                BrutalityItems.ENVY.get(),
                BrutalityItems.GLUTTONY.get(),
                BrutalityItems.LUST.get(),
                BrutalityItems.SLOTH.get(),
                BrutalityItems.GREED.get(),

                // Timekeeper Set
                BrutalityItems.BROKEN_CLOCK.get(),
                BrutalityItems.SHATTERED_CLOCK.get(),
                BrutalityItems.SUNDERED_CLOCK.get(),
                BrutalityItems.TIMEKEEPERS_CLOCK.get(),
                BrutalityItems.THE_CLOCK_OF_FROZEN_TIME.get(),

                // Rare Artifacts
                BrutalityItems.BEAD_OF_LIFE.get(),
                BrutalityItems.LUCKY_BOOKMARK.get(),
                BrutalityItems.DIVERGENT_RECURSOR.get(),
                BrutalityItems.CONVERGENT_RECURSOR.get(),
                BrutalityItems.INFINITE_RECURSOR.get(),

                // Magic & Orbs
                BrutalityItems.SOUL_STONE.get(),
                BrutalityItems.SCRIBES_INDEX.get(),
                BrutalityItems.PRISMATIC_ORB.get(),
                BrutalityItems.ONYX_IDOL.get(),
                BrutalityItems.ECHO_CHAMBER.get(),
                BrutalityItems.CONSERVATIVE_CONCOCTION.get(),
                BrutalityItems.EMERGENCY_FLASK.get(),
                BrutalityItems.BLACK_HOLE_ORB.get(),
                BrutalityItems.FORBIDDEN_ORB.get(),
                BrutalityItems.BLOOD_ORB.get(),
                BrutalityItems.PROFANUM_REACTOR.get(),
                BrutalityItems.MANA_INFUSED_WHETSTONE.get(),
                BrutalityItems.DECK_OF_CARDS.get(),

                // Culinary Set
                BrutalityItems.PEPPER_SHAKER.get(),
                BrutalityItems.SALT_SHAKER.get(),
                BrutalityItems.HOT_SAUCE.get(),
                BrutalityItems.TOMATO_SAUCE.get(),
                BrutalityItems.CHEESE_SAUCE.get(),
                BrutalityItems.PIZZA_SLOP.get(),
                BrutalityItems.OLIVE_OIL.get(),
                BrutalityItems.EXTRA_VIRGIN_OLIVE_OIL.get(),
                BrutalityItems.SALT_AND_PEPPER.get(),
                BrutalityItems.FRIDGE.get(),
                BrutalityItems.SMART_FRIDGE.get(),
                BrutalityItems.MORTAR_AND_PESTLE.get(),
                BrutalityItems.THE_SMOKEHOUSE.get(),
                BrutalityItems.RAINBOW_SPRINKLES.get(),
                BrutalityItems.SMOKE_STONE.get(),
                BrutalityItems.SUGAR_GLAZE.get(),
                BrutalityItems.BAMBOO_STEAMER.get(),
                BrutalityItems.SEARED_SUGAR_BROOCH.get(),
                BrutalityItems.DUNKED_DONUT.get(),
                BrutalityItems.LOLLIPOP_OF_ETERNITY.get(),

                // Vampiric Set
                BrutalityItems.SELF_REPAIR_NEXUS.get(),
                BrutalityItems.BLOOD_CHALICE.get(),
                BrutalityItems.HEMOGRAFT_NEEDLE.get(),
                BrutalityItems.BLOODSTAINED_MIRROR.get(),
                BrutalityItems.VAMPIRIC_TALISMAN.get(),

                // Mathematical Set
                BrutalityItems.PI.get(),
                BrutalityItems.EULERS_NUMBER.get(),
                BrutalityItems.SINE.get(),
                BrutalityItems.COSINE.get(),
                BrutalityItems.ADDITION_CHARM.get(),
                BrutalityItems.SUBTRACTION.get(),
                BrutalityItems.FRACTION.get(),
                BrutalityItems.FLOOR.get(),
                BrutalityItems.CEIL.get(),
                BrutalityItems.MULTIPLICATION.get(),
                BrutalityItems.DIVISION.get(),
                BrutalityItems.SUM.get(),
                BrutalityItems.HYPERBOLIC_FEATHER.get(),

                // Crypto & Tech
                BrutalityItems.CRYPTO_WALLET.get(),
                BrutalityItems.PORTABLE_MINING_RIG.get(),
                BrutalityItems.CARTON_OF_PRISM_SOLUTION_MILK.get()
        );
    }

    private void addBeltTags() {
        this.tag(BrutalityTags.Items.BELT).add(
                BrutalityItems.SCIENTIFIC_CALCULATOR.get(),
                BrutalityItems.BRUTESKIN_BELT.get(),
                BrutalityItems.MINIATURE_ANCHOR.get(),
                BrutalityItems.WARPSLICE_SCABBARD.get(),
                BrutalityItems.POOL_FLOAT.get(),
                BrutalityItems.BATTLE_SCARS.get()
        );
    }

    private void addHandTags() {
        this.tag(BrutalityTags.Items.HANDS).add(
                BrutalityItems.NANOMACHINES.get(),
                BrutalityItems.AQUEOUS_TUNER.get(),
                BrutalityItems.DUELING_GLOVE.get(),
                BrutalityItems.BUTTER_GAUNTLETS.get(),
                BrutalityItems.GOOD_BOOK.get(),
                BrutalityItems.THE_OATH.get(),
                BrutalityItems.EYE_OF_THE_DRAGON.get(),
                BrutalityItems.JURY_NULLIFIER.get(),
                BrutalityItems.STYGIAN_CHAIN.get(),
                BrutalityItems.PHANTOM_FINGER.get(),
                BrutalityItems.ANGER_MANAGEMENT.get(),
                BrutalityItems.CROWBAR.get(),
                BrutalityItems.FIRE_EXTINGUISHER.get(),
                BrutalityItems.DUMBBELL.get(),
                BrutalityItems.HANDCUFFS.get(),
                BrutalityItems.BLOOD_PULSE_GAUNTLETS.get(),
                BrutalityItems.BROKEN_CONTROLLER.get(),
                BrutalityItems.OMEGA_GAUNTLET.get(),
                BrutalityItems.PERFECT_CELL.get(),
                BrutalityItems.SUSPICIOUSLY_LARGE_HANDLE.get(),
                BrutalityItems.ICE_CREAM_SANDWICH.get(),
                BrutalityItems.DELICATE_JEWEL.get(),
                BrutalityItems.GLOBETROTTERS_BADGE.get(),
                BrutalityItems.SCOUTS_BADGE.get(),
                BrutalityItems.APPRENTICES_MANUAL_TO_BASIC_MULTICASTING.get(),
                BrutalityItems.WIZARDS_GUIDEBOOK_TO_ADVANCED_MULTICASTING.get(),
                BrutalityItems.ARCHMAGES_THESIS_TO_MASTERFUL_MULTICASTING.get(),
                BrutalityItems.PARAGON_OF_THE_FIRST_MAGE.get()
        );
    }

    private void addHeadTags() {
        this.tag(BrutalityTags.Items.HEAD).add(
                BrutalityItems.MAGICIANS_TOP_HAT.get(),
                BrutalityItems.HEAD_CUSHION.get(),
                BrutalityItems.SERAPHIM_HALO.get(),
                BrutalityItems.GOLDEN_HEADBAND.get(),
                BrutalityItems.WOOLY_BLINDFOLD.get(),
                BrutalityItems.TRIAL_GUARDIAN_EYEBROWS.get(),
                BrutalityItems.TRIAL_GUARDIAN_HANDS.get(),
                BrutalityItems.SOLAR_SYSTEM.get(),
                BrutalityItems.MASK_OF_MADNESS.get(),
                BrutalityItems.CROWN_OF_DOMINATION.get(),
                BrutalityItems.FACE_PIE.get(),
                BrutalityItems.FALLEN_ANGELS_HALO.get(),
                BrutalityItems.AIR_JORDAN_EARRINGS.get(),
                BrutalityItems.VAMPIRE_FANG.get(),
                BrutalityItems.PROGENITORS_EARRINGS.get(),
                BrutalityItems.HELL_SPECS.get(),
                BrutalityItems.LENS_MAKERS_GLASSES.get(),
                BrutalityItems.SANGUINE_SPECTACLES.get(),
                BrutalityItems.SCOPE_GOGGLES.get(),
                BrutalityItems.CROWN_OF_TYRANNY.get(),
                BrutalityItems.CRITICAL_THINKING.get(),
                BrutalityItems.BRAIN_ROT.get(),
                BrutalityItems.EYE_FOR_VIOLENCE.get(),
                BrutalityItems.SAD_UVOGRE.get(),
                BrutalityItems.SOLAR_LENS.get(),
                BrutalityItems.LUNAR_LENS.get()
        );
    }

    private void addHeartTags() {
        this.tag(BrutalityTags.Items.HEART).add(
                BrutalityItems.RUNE_OF_DELTA.get(),
                BrutalityItems.OVERCLOCKED_CORE.get(),
                BrutalityItems.DRAGONHEART.get(),
                BrutalityItems.UVOGRE_HEART.get(),
                BrutalityItems.ZOMBIE_HEART.get(),
                BrutalityItems.FROZEN_HEART.get(),
                BrutalityItems.SECOND_HEART.get(),
                BrutalityItems.HEART_OF_WRATH.get(),
                BrutalityItems.BROKEN_HEART.get(),
                BrutalityItems.GLASS_HEART.get(),
                BrutalityItems.HEART_OF_GOLD.get(),
                BrutalityItems.NINJA_HEART.get(),
                BrutalityItems.BOX_OF_CHOCOLATES.get(),
                BrutalityItems.BRUTAL_HEART.get(),
                TerramityModItems.CRYSTAL_HEART.get()
        );
    }

    private void addNecklaceTags() {
        this.tag(BrutalityTags.Items.NECKLACE).add(
                BrutalityItems.ABYSSAL_NECKLACE.get(),
                BrutalityItems.BLOOD_HOWL_PENDANT.get(),
                BrutalityItems.HELLSPEC_TIE.get(),
                BrutalityItems.KNIGHTS_PENDANT.get(),
                BrutalityItems.GAMBLERS_CHAIN.get(),
                BrutalityItems.HEMOMATIC_LOCKET.get(),
                BrutalityItems.BLACK_MATTER_NECKLACE.get(),
                BrutalityItems.FUZZY_DICE.get(),
                BrutalityItems.CARAMEL_CRUNCH_MEDALLION.get()
        );
    }

    private void addRingTags() {
        this.tag(BrutalityTags.Items.RING).add(
                BrutalityItems.FURY_BAND.get(),
                BrutalityItems.MICROBLADE_BAND.get(),
                BrutalityItems.RING_OF_MANA.get(),
                BrutalityItems.RING_OF_MANA_PLUS.get(),
                BrutalityItems.SANGUINE_SIGNET.get(),
                BrutalityItems.AQUA_RULER.get(),
                BrutalityItems.ROCK_CANDY_RING.get(),
                BrutalityItems.ROAD_RUNNERS_RING.get()
        );
    }

    private void addFeetTags() {
        this.tag(BrutalityTags.Items.FEET).add(
                BrutalityItems.HIGH_HEALS.get(),
                BrutalityItems.FLAME_WALKER.get(),
                BrutalityItems.FLAME_STOMPER.get(),
                BrutalityItems.SEISMIC_STOMPERS.get(),
                BrutalityItems.SALAMANDERS_STRIDERS.get(),
                BrutalityItems.AMPHIBIAN_BOOTS.get(),
                BrutalityItems.LAVA_WALKERS.get(),
                BrutalityItems.WATER_WALKERS.get(),
                BrutalityItems.VOID_STEPPERS.get(),
                BrutalityItems.UMBRAL_TIPTOES.get(),
                BrutalityItems.PLATED_STEELCAPS.get(),
                BrutalityItems.SLIPSTREAM_TRACERS.get(),
                BrutalityItems.CONSTRUCTION_BOOTS.get(),
                BrutalityItems.VECTOR_STABILIZER.get(),
                BrutalityItems.ICE_SKATES.get()
        );
    }

    private void addRageTags() {
        this.tag(BrutalityTags.Items.RAGE_ITEMS).add(
                BrutalityItems.RAGE_STONE.get(),
                BrutalityItems.PACK_OF_CIGARETTES.get(),
                BrutalityItems.STRESS_PILLS.get(),
                BrutalityItems.SEROTONIN_PILLS.get(),
                BrutalityItems.FURY_BATTERY.get(),
                BrutalityItems.PAIN_CATALYST.get(),
                BrutalityItems.RAMPAGE_CLOCK.get(),
                BrutalityItems.BLOOD_HOWL_PENDANT.get(),
                BrutalityItems.RAGE_BAIT.get(),
                BrutalityItems.OMEGA_GAUNTLET.get(),
                BrutalityItems.SPITE_SHARD.get(),
                BrutalityItems.HATE_SIGIL.get(),
                BrutalityItems.HEART_OF_WRATH.get(),
                BrutalityItems.EYE_FOR_VIOLENCE.get(),
                BrutalityItems.BATTLE_SCARS.get(),
                BrutalityItems.MECHANICAL_AORTA.get(),
                BrutalityItems.BLOOD_PULSE_GAUNTLETS.get(),
                BrutalityItems.BROKEN_CONTROLLER.get(),
                BrutalityItems.FURY_BAND.get(),
                BrutalityItems.GRUDGE_TOTEM.get(),
                BrutalityItems.BLOOD_STONE.get(),
                BrutalityItems.WRATH.get(),
                BrutalityItems.ENDER_DRAGON_STEM_CELLS.get(),
                BrutalityItems.ANGER_MANAGEMENT.get(),
                BrutalityItems.FACE_PIE.get(),
                BrutalityItems.MASK_OF_MADNESS.get()
        );
    }

    private void addGastronomistTags() {
        this.tag(BrutalityTags.Items.GASTRONOMIST_ITEMS).add(
                BrutalityItems.SPATULA_HAMMER.get(),
                BrutalityItems.THE_GOLDEN_SPATULA_HAMMER.get(),
                BrutalityItems.IRON_KNIFE.get(),
                BrutalityItems.GOLD_KNIFE.get(),
                BrutalityItems.DIAMOND_KNIFE.get(),
                BrutalityItems.VOID_KNIFE.get(),
                BrutalityItems.MELONCHOLY_SWORD.get(),
                BrutalityItems.APPLE_CORE_LANCE.get(),
                BrutalityItems.CHOPSTICK_STAFF.get(),
                BrutalityItems.BAMBOO_STAFF.get(),
                BrutalityItems.FRYING_PAN.get(),
                BrutalityItems.POTATO_MASHER.get(),
                BrutalityItems.WHISK_HAMMER.get(),
                BrutalityItems.PEPPER_SHAKER.get(),
                BrutalityItems.SALT_SHAKER.get(),
                BrutalityItems.SALT_AND_PEPPER.get(),
                BrutalityItems.SMOKE_STONE.get(),
                BrutalityItems.THE_SMOKEHOUSE.get(),
                BrutalityItems.BAMBOO_STEAMER.get(),
                BrutalityItems.SUGAR_GLAZE.get(),
                BrutalityItems.RAINBOW_SPRINKLES.get(),
                BrutalityItems.ROCK_CANDY_RING.get(),
                BrutalityItems.SEARED_SUGAR_BROOCH.get(),
                BrutalityItems.DUNKED_DONUT.get(),
                BrutalityItems.CARAMEL_CRUNCH_MEDALLION.get(),
                BrutalityItems.LOLLIPOP_OF_ETERNITY.get(),
                BrutalityItems.ICE_CREAM_SANDWICH.get(),
                BrutalityItems.MORTAR_AND_PESTLE.get(),
                BrutalityItems.BUTTER_GAUNTLETS.get(),
                BrutalityItems.TOMATO_SAUCE.get(),
                BrutalityItems.CHEESE_SAUCE.get(),
                BrutalityItems.PIZZA_SLOP.get(),
                BrutalityItems.HOT_SAUCE.get(),
                BrutalityItems.OLIVE_OIL.get(),
                BrutalityItems.EXTRA_VIRGIN_OLIVE_OIL.get(),
                BrutalityItems.FRIDGE.get(),
                BrutalityItems.SMART_FRIDGE.get()
        );
    }

    private void addCurioTags() {
        addAnkletTags();
        addBeltTags();
        addCharmTags();
        addHandTags();
        addHeadTags();
        addHeartTags();
        addNecklaceTags();
        addRingTags();
        addFeetTags();
    }

    private void addMathTags() {
        this.tag(BrutalityTags.Items.MATH_ITEMS).add(
                BrutalityItems.PI.get(),
                BrutalityItems.EULERS_NUMBER.get(),
                BrutalityItems.ADDITION_CHARM.get(),
                BrutalityItems.SUBTRACTION.get(),
                BrutalityItems.FRACTION.get(),
                BrutalityItems.CEIL.get(),
                BrutalityItems.FLOOR.get(),
                BrutalityItems.DIVISION.get(),
                BrutalityItems.SUM.get(),
                BrutalityItems.SINE.get(),
                BrutalityItems.COSINE.get()
        );
    }

    private void addAugmentTags() {
        this.tag(BrutalityTags.Items.AUGMENTS).addTags(BrutalityTags.Items.MAGIC_AUGMENTS, BrutalityTags.Items.AUGMENTATION_DEVICE, BrutalityTags.Items.SEALS);

        this.tag(BrutalityTags.Items.AUGMENTATION_DEVICE).add(
                BrutalityItems.DIMLITE_AUGMENTATION_DEVICE.get(),
                BrutalityItems.COSMILITE_AUGMENTATION_DEVICE.get(),
                BrutalityItems.VIRENTIUM_AUGMENTATION_DEVICE.get(),
                BrutalityItems.MOLTEN_AUGMENTATION_DEVICE.get(),
                BrutalityItems.COBALT_AUGMENTATION_DEVICE.get(),
                BrutalityItems.VOID_AUGMENTATION_DEVICE.get(),
                BrutalityItems.HELLSPEC_AUGMENTATION_DEVICE.get(),
                BrutalityItems.CONDUCTITE_AUGMENTATION_DEVICE.get(),
                BrutalityItems.NYXIUM_AUGMENTATION_DEVICE.get(),
                BrutalityItems.EXODIUM_AUGMENTATION_DEVICE.get(),
                BrutalityItems.REVERIUM_AUGMENTATION_DEVICE.get(),
                BrutalityItems.ADAMANTITE_AUGMENTATION_DEVICE.get()
        );

        this.tag(BrutalityTags.Items.SEALS).add(
                BrutalityItems.BLACK_SEAL.get(),
                BrutalityItems.BLUE_SEAL.get(),
                BrutalityItems.GREEN_SEAL.get(),
                BrutalityItems.ORANGE_SEAL.get(),
                BrutalityItems.PINK_SEAL.get(),
                BrutalityItems.PURPLE_SEAL.get(),
                BrutalityItems.RED_SEAL.get(),
                BrutalityItems.CYAN_SEAL.get(),
                BrutalityItems.YELLOW_SEAL.get(),
                BrutalityItems.BOMB_SEAL.get(),
                BrutalityItems.COSMIC_SEAL.get(),
                BrutalityItems.GLASS_SEAL.get(),
                BrutalityItems.QUANTITE_SEAL.get(),
                BrutalityItems.VOID_SEAL.get()
        );
    }

    private void addMagicTags() {
        this.tag(BrutalityTags.Items.MAGIC_ITEMS).addTags(BrutalityTags.Items.SPELL_SCROLLS, BrutalityTags.Items.MAGIC_AUGMENTS, BrutalityTags.Items.MAGIC_TOMES);

        this.tag(BrutalityTags.Items.MAGIC_TOMES).add(
                BrutalityItems.DAEMONIC_TOME.get(),
                BrutalityItems.EVERGREEN_TOME.get(),
                BrutalityItems.DARKIST_TOME.get(),
                BrutalityItems.COSMIC_TOME.get(),
                BrutalityItems.VOIDWALKER_TOME.get(),
                BrutalityItems.BRIMWIELDER_TOME.get(),
                BrutalityItems.CELESTIA_TOME.get(),
                BrutalityItems.EXODIC_TOME.get(),
                BrutalityItems.UMBRAL_TOME.get()
        ); // TODO: Add voltweaver tome

        this.tag(BrutalityTags.Items.MAGIC_AUGMENTS).add(
                BrutalityItems.DRAGON_SINEW_BINDING.get(),
                BrutalityItems.QUICKSILVER_SPINE.get(),
                BrutalityItems.QUICKSILVER_INK.get(),
                BrutalityItems.ARCHANGELS_TEARS.get(),
                BrutalityItems.SOUL_INFUSED_INK.get(),
                BrutalityItems.VOID_TOUCHED_INK.get(),
                BrutalityItems.FEATHER_OF_THE_FIRST_WIND.get(),
                BrutalityItems.PROFANED_INK.get(),
                BrutalityItems.FORBIDDEN_MANUSCRIPT.get(),
                BrutalityItems.UVOGRE_VELLUM.get(),
                BrutalityItems.SOLID_SPELL_DRIVE.get(),
                BrutalityItems.IRIDESCENT_BOOKMARK.get(),
                BrutalityItems.VEGAS_VELLUM.get(),
                BrutalityItems.RUNE_OF_THE_ROYAL_FLUSH.get()
        );

        this.tag(BrutalityTags.Items.SPELL_SCROLLS).add(
                BrutalityItems.DAEMONIC_SPELL_SCROLL.get(),
                BrutalityItems.EVERGREEN_SPELL_SCROLL.get(),
                BrutalityItems.DARKIST_SPELL_SCROLL.get(),
                BrutalityItems.COSMIC_SPELL_SCROLL.get(),
                BrutalityItems.VOIDWALKER_SPELL_SCROLL.get(),
                BrutalityItems.BRIMWIELDER_SPELL_SCROLL.get(),
                BrutalityItems.VOLTWEAVER_SPELL_SCROLL.get(),
                BrutalityItems.CELESTIA_SPELL_SCROLL.get(),
                BrutalityItems.EXODIC_SPELL_SCROLL.get(),
                BrutalityItems.UMBRAL_SPELL_SCROLL.get()
        );
    }
}