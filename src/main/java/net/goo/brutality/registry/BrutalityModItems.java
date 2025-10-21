package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.armor.NoirArmorItem;
import net.goo.brutality.item.armor.TerraArmorItem;
import net.goo.brutality.item.block.*;
import net.goo.brutality.item.curios.anklet.*;
import net.goo.brutality.item.curios.belt.*;
import net.goo.brutality.item.curios.charm.*;
import net.goo.brutality.item.curios.hands.*;
import net.goo.brutality.item.curios.head.*;
import net.goo.brutality.item.curios.heart.*;
import net.goo.brutality.item.curios.necklace.*;
import net.goo.brutality.item.curios.ring.*;
import net.goo.brutality.item.curios.vanity.*;
import net.goo.brutality.item.material.*;
import net.goo.brutality.item.seals.*;
import net.goo.brutality.item.weapon.axe.OldGpuAxe;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.bow.Providence;
import net.goo.brutality.item.weapon.generic.*;
import net.goo.brutality.item.weapon.hammer.*;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.spear.AppleCoreSpear;
import net.goo.brutality.item.weapon.spear.EventHorizonSpear;
import net.goo.brutality.item.weapon.staff.BambooStaff;
import net.goo.brutality.item.weapon.staff.ChopstickStaff;
import net.goo.brutality.item.weapon.sword.*;
import net.goo.brutality.item.weapon.sword.phasesaber.OnyxPhasesaber;
import net.goo.brutality.item.weapon.sword.phasesaber.RubyPhasesaber;
import net.goo.brutality.item.weapon.sword.phasesaber.SapphirePhasesaber;
import net.goo.brutality.item.weapon.sword.phasesaber.TopazPhasesaber;
import net.goo.brutality.item.weapon.throwing.*;
import net.goo.brutality.item.weapon.tome.*;
import net.goo.brutality.item.weapon.trident.DepthCrusherTrident;
import net.goo.brutality.item.weapon.trident.GungnirTrident;
import net.goo.brutality.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.ItemDescriptionComponents.*;

public class BrutalityModItems {

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Brutality.MOD_ID);

    public static final RegistryObject<Item> POCKET_BLACK_HOLE = ITEMS.register("pocket_black_hole", () -> new PocketBlackHoleItem(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_FREQUENCY_ALLOY = ITEMS.register("high_frequency_alloy", () -> new HighFrequencyAlloyItem(new Item.Properties()));
    public static final RegistryObject<Item> QUANTITE_INGOT = ITEMS.register("quantite_ingot", () -> new QuantiteIngot(new Item.Properties()));
    public static final RegistryObject<Item> UNBRIDLED_RAGE = ITEMS.register("unbridled_rage", () -> new UnbridledRageItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_SLIME_BALL = ITEMS.register("blue_slime_ball", () -> new BlueSlimeBall(new Item.Properties()));
    public static final RegistryObject<Item> PINK_SLIME_BALL = ITEMS.register("pink_slime_ball", () -> new PinkSlimeBall(new Item.Properties()));


    // region BlockItems

    //    public static final RegistryObject<Item> WATER_COOLER_ITEM =
//            ITEMS.register("water_cooler", () ->
//                    new WaterCoolerBlockItem(ModBlocks.WATER_COOLER_BLOCK.get(), new Item.Properties()));
//
    public static final RegistryObject<Item> WATER_COOLER_ITEM =
            ITEMS.register("water_cooler", () ->
                    new WaterCoolerBlockItem(BrutalityModBlocks.WATER_COOLER_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> COFFEE_MACHINE_ITEM =
            ITEMS.register("coffee_machine", () ->
                    new CoffeeMachineBlockItem(BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> SUPER_SNIFFER_FIGURE_ITEM =
            ITEMS.register("super_sniffer_figure", () ->
                    new SuperSnifferFigureBlockItem(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> WHITE_FILING_CABINET_ITEM =
            ITEMS.register("white_filing_cabinet", () ->
                    new FilingCabinetBlockItem(BrutalityModBlocks.WHITE_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> GRAY_FILING_CABINET_ITEM =
            ITEMS.register("gray_filing_cabinet", () ->
                    new FilingCabinetBlockItem(BrutalityModBlocks.GRAY_FILING_CABINET.get(), new Item.Properties()));

    public static final RegistryObject<Item> IMPORTANT_DOCUMENTS = ITEMS.register("important_documents", () ->
            new ImportantDocumentsBlockItem(BrutalityModBlocks.IMPORTANT_DOCUMENTS_BLOCK.get(), new Item.Properties()));


    // endregion


    // region Game References

    public static final RegistryObject<Item> BLACK_SEAL = ITEMS.register("black_seal", () -> new BlackSealItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_SEAL = ITEMS.register("blue_seal", () -> new BlueSealItem(new Item.Properties()));
    public static final RegistryObject<Item> GREEN_SEAL = ITEMS.register("green_seal", () -> new GreenSealItem(new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_SEAL = ITEMS.register("orange_seal", () -> new OrangeSealItem(new Item.Properties()));
    public static final RegistryObject<Item> PINK_SEAL = ITEMS.register("pink_seal", () -> new PinkSealItem(new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_SEAL = ITEMS.register("purple_seal", () -> new PurpleSealItem(new Item.Properties()));
    public static final RegistryObject<Item> RED_SEAL = ITEMS.register("red_seal", () -> new RedSealItem(new Item.Properties()));
    public static final RegistryObject<Item> CYAN_SEAL = ITEMS.register("cyan_seal", () -> new CyanSealItem(new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_SEAL = ITEMS.register("yellow_seal", () -> new YellowSealItem(new Item.Properties()));
    public static final RegistryObject<Item> BOMB_SEAL = ITEMS.register("bomb_seal", () -> new BombSealItem(new Item.Properties()));
    public static final RegistryObject<Item> COSMIC_SEAL = ITEMS.register("cosmic_seal", () -> new CosmicSealItem(new Item.Properties()));
    public static final RegistryObject<Item> GLASS_SEAL = ITEMS.register("glass_seal", () -> new GlassSealItem(new Item.Properties()));
    public static final RegistryObject<Item> QUANTITE_SEAL = ITEMS.register("quantite_seal", () -> new QuantiteSealItem(new Item.Properties()));
    public static final RegistryObject<Item> VOID_SEAL = ITEMS.register("void_seal", () -> new VoidSealItem(new Item.Properties()));


    public static final RegistryObject<Item> CELESTIAL_STARBOARD = ITEMS.register("celestial_starboard", () -> new CelestialStarboard(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(DASH_ABILITY, 1, 60)

            )
    ));
    public static final RegistryObject<Item> DARKIN_BLADE_SWORD = ITEMS.register("darkin_blade", () -> new DarkinBladeSword(
            Tiers.NETHERITE, 10, -3.15F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )
    ));
    public static final RegistryObject<Item> DARKIN_SCYTHE = ITEMS.register("darkin_scythe", () -> new DarkinScythe(
            Tiers.NETHERITE, 3, -3F,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 4),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 4),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1)
    )
    ));
    public static final RegistryObject<Item> DOOMFIST_GAUNTLET_ITEM = ITEMS.register("doomfist_gauntlet", () -> new DoomfistGauntletItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));
    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(
            Tiers.NETHERITE, 5, -3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 2),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2)

    )
    ));
    public static final RegistryObject<Item> DEPTH_CRUSHER_TRIDENT = ITEMS.register("depth_crusher", () -> new DepthCrusherTrident(
            6, -2.4F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));
//    public static final RegistryObject<Item> MURASAMA_SWORD = ITEMS.register("hf_murasama", () -> new MurasamaSword(
//            Tiers.NETHERITE, 16, -2F,
//            ModRarities.MYTHIC, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1),
//            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
//    )
//    ));
//    public static final RegistryObject<Item> MURAMASA_SWORD = ITEMS.register("muramasa", () -> new MuramasaSword(
//            Tiers.NETHERITE, 3, -2F,
//            ModRarities.MYTHIC, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
//    )
//    ));
//    public static final RegistryObject<Item> TERRA_BLADE_SWORD = ITEMS.register("terra_blade", () -> new TerraBladeSword(
//            Tiers.NETHERITE, 3, -2.2F,
//            ModRarities.LEGENDARY, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1)
//    )
//    ));

    public static final RegistryObject<Item> LAST_PRISM_ITEM = ITEMS.register("last_prism", () -> new LastPrismItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(MANA_COST, 1)
            )
    ));

//    public static final RegistryObject<Item> TERRATOMERE_SWORD = ITEMS.register("terratomere", () -> new TerratomereSword(
//            Tiers.NETHERITE, 7, -2.5F,
//            ModRarities.LEGENDARY, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 2)
//    )));

//    public static final RegistryObject<Item> EXOBLADE_SWORD = ITEMS.register("exoblade", () -> new ExobladeSword(
//            Tiers.NETHERITE, 20, -2.5F,
//            ModRarities.FABLED, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1),
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
//    )
//    ));

    public static final RegistryObject<Item> ROYAL_GUARDIAN_SWORD = ITEMS.register("royal_guardian_sword", () -> new RoyalGuardianSword(
            Tiers.NETHERITE, 50, -3.5F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));

    public static final RegistryObject<Item> BLADE_OF_THE_RUINED_KING = ITEMS.register("blade_of_the_ruined_king", () -> new BladeOfTheRuinedKingSword(
            Tiers.DIAMOND, 9, -2.5F,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 2),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 2)
    )
    ));

    public static final RegistryObject<Item> BIOMECH_REACTOR = ITEMS.register("biomech_reactor", () -> new BiomechReactor(
            20, 0, ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 2)
            )
    ));

    public static final RegistryObject<Item> PLUNDER_CHEST_CHARM = ITEMS.register("plunder_chest_charm", () -> new PlunderChest(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 100)
            )
    ));

    // endregion

    // region Other references
    public static final RegistryObject<Item> WOOLY_BLINDFOLD = ITEMS.register("wooly_blindfold", () -> new WoolyBlindfold(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> SERAPHIM_HALO = ITEMS.register("seraphim_halo", () -> new SeraphimHaloHead(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> RHITTA_AXE = ITEMS.register("rhitta", () -> new RhittaAxe(
            Tiers.DIAMOND, 10, -3F, ModRarities.MYTHIC,
            List.of(new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> FIRST_EXPLOSION_STAFF = ITEMS.register("first_explosion", () -> new FirstExplosionStaff(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));
    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(
            Tiers.DIAMOND, 0, -2.6F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> DULL_KNIFE_DAGGER = ITEMS.register("dull_knife", () -> new DullKnifeSword(
            Tiers.NETHERITE, 4, -2F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1)
    )
    ));
    public static final RegistryObject<Item> GOOD_BOOK = ITEMS.register("good_book", () -> new GoodBook(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )));
    // endregion

    // region Mythology
    public static final RegistryObject<Item> GOLDEN_HEADBAND = ITEMS.register("golden_headband", () -> new GoldenHeadband(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> YATA_NO_KAGAMI = ITEMS.register("yata_no_kagami", () -> new YataNoKagami(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )));
    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(
            13, -3.2F, ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(
            8, -2.1F, ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> GREED_CHARM = ITEMS.register("greed", () -> new Greed(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> PRIDE_CHARM = ITEMS.register("pride", () -> new Pride(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> WRATH_CHARM = ITEMS.register("wrath", () -> new Wrath(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> SLOTH_CHARM = ITEMS.register("sloth", () -> new Sloth(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> GLUTTONY_CHARM = ITEMS.register("gluttony", () -> new Gluttony(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> LUST_CHARM = ITEMS.register("lust", () -> new Lust(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> ENVY_CHARM = ITEMS.register("envy", () -> new Envy(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    // endregion

    // region Original Ideas
    public static final RegistryObject<Item> ENERGY_FOCUSER = ITEMS.register("energy_focuser", () -> new EnergyFocuser(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BRUTESKIN_BELT = ITEMS.register("bruteskin_belt", () -> new BruteskinBelt(
            ModRarities.MYTHIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CRITICAL_THINKING = ITEMS.register("critical_thinking", () -> new CriticalThinking(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> DEADSHOT_BROOCH = ITEMS.register("deadshot_brooch", () -> new DeadshotBrooch(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));

    public static final RegistryObject<Item> ONYX_PHASESABER = ITEMS.register("onyx_phasesaber", () -> new OnyxPhasesaber(
            Tiers.NETHERITE,
            7F,
            -2.8F,
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TOPAZ_PHASESABER = ITEMS.register("topaz_phasesaber", () -> new TopazPhasesaber(
            Tiers.NETHERITE,
            7F,
            -2.8F,
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> RUBY_PHASESABER = ITEMS.register("ruby_phasesaber", () -> new RubyPhasesaber(
            Tiers.NETHERITE,
            7F,
            -2.8F,
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SAPPHIRE_PHASESABER = ITEMS.register("sapphire_phasesaber", () -> new SapphirePhasesaber(
            Tiers.NETHERITE,
            7F,
            -2.8F,
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> HELLSPEC_TIE = ITEMS.register("hellspec_tie", () -> new HellspecTie(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> ARCHMAGES_TRICK = ITEMS.register("archmages_trick", () -> new ArchmagesTrick(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> SOUL_STONE = ITEMS.register("soul_stone", () -> new SoulStone(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> EMERGENCY_FLASK = ITEMS.register("emergency_flask", () -> new EmergencyFlask(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> RING_OF_MANA = ITEMS.register("ring_of_mana", () -> new RingOfMana(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> RING_OF_MANA_PLUS = ITEMS.register("ring_of_mana_plus", () -> new RingOfManaPlus(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CONSERVATIVE_CONCOCTION = ITEMS.register("conservative_concoction", () -> new ConservativeConcoction(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MICROBLADE_BAND = ITEMS.register("microblade_band", () -> new MicrobladeBand(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1)
            )
    ));
    public static final RegistryObject<Item> RUNE_OF_DELTA = ITEMS.register("rune_of_delta", () -> new RuneOfDelta(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> ECHO_CHAMBER = ITEMS.register("echo_chamber", () -> new EchoChamber(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> ONYX_IDOL = ITEMS.register("onyx_idol", () -> new OnyxIdol(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SCRIBES_INDEX = ITEMS.register("scribes_index", () -> new ScribesIndex(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BLACK_HOLE_ORB = ITEMS.register("black_hole_orb", () -> new BlackHoleOrb(
            ModRarities.FABLED,
            List.of(
            )
    ));
    public static final RegistryObject<Item> FORBIDDEN_ORB = ITEMS.register("forbidden_orb", () -> new ForbiddenOrb(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BLOOD_ORB = ITEMS.register("blood_orb", () -> new BloodOrb(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> CROWN_OF_TYRANNY = ITEMS.register("crown_of_tyranny", () -> new CrownOfTyranny(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PORTABLE_QUANTUM_THINGAMABOB = ITEMS.register("portable_quantum_thingamabob", () -> new PortableQuantumThingamabob(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
            )
    ));
    public static final RegistryObject<Item> WARPSLICE_SCABBARD = ITEMS.register("warpslice_scabbard", () -> new WarpsliceScabbard(
            ModRarities.FABLED,
            List.of(
            )
    ));
    public static final RegistryObject<Item> PROFANUM_REACTOR = ITEMS.register("profanum_reactor", () -> new ProfanumReactor(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> DAEMONIUM_WHETSTONE = ITEMS.register("daemonium_whetstone", () -> new DaemoniumWhetstone(
            ModRarities.FABLED,
            List.of(
            )
    ));
    public static final RegistryObject<Item> KNIGHTS_PENDANT = ITEMS.register("knights_pendant", () -> new KnightsPendant(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> THE_OATH = ITEMS.register("the_oath", () -> new TheOath(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> LUCKY_INSOLES = ITEMS.register("lucky_insoles", () -> new LuckyInsoles(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SANGUINE_SIGNET = ITEMS.register("sanguine_signet", () -> new SanguineSignet(
            ModRarities.FABLED,
            List.of(
            )
    ));
    public static final RegistryObject<Item> PHANTOM_FINGER = ITEMS.register("phantom_finger", () -> new PhantomFinger(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> STYGIAN_CHAIN = ITEMS.register("stygian_chain", () -> new StygianChain(
            ModRarities.FABLED,
            List.of(
            )
    ));

    public static final RegistryObject<Item> SILVER_RESPAWN_CARD = ITEMS.register("silver_respawn_card", () -> new SilverRespawnCard(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> DIAMOND_RESPAWN_CARD = ITEMS.register("diamond_respawn_card", () -> new DiamondRespawnCard(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 30 * 60 * 20)
            )
    ));
    public static final RegistryObject<Item> EVIL_KING_RESPAWN_CARD = ITEMS.register("evil_king_respawn_card", () -> new EvilKingRespawnCard(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 15 * 60 * 20)
            )
    ));

    public static final RegistryObject<Item> SILVER_BOOSTER_PACK = ITEMS.register("silver_booster_pack", () -> new SilverBoosterPack(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> DIAMOND_BOOSTER_PACK = ITEMS.register("diamond_booster_pack", () -> new DiamondBoosterPack(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2, 30 * 60 * 20)
            )
    ));
    public static final RegistryObject<Item> EVIL_KING_BOOSTER_PACK = ITEMS.register("evil_king_booster_pack", () -> new EvilKingBoosterPack(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2, 15 * 60 * 20)
            )
    ));
    public static final RegistryObject<Item> HEMOMATIC_LOCKET = ITEMS.register("hemomatic_locket", () -> new HemomaticLocket(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> DAEMONIUM_SEWING_KIT = ITEMS.register("daemonium_sewing_kit", () -> new DaemoniumSewingKit(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> GAMBLERS_CHAIN = ITEMS.register("gamblers_chain", () -> new GamblersChain(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> PINCUSHION = ITEMS.register("pincushion", () -> new Pincushion(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> OLD_GUILLOTINE = ITEMS.register("old_guillotine", () -> new OldGuillotine(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> KNUCKLE_WRAPS = ITEMS.register("knuckle_wraps", () -> new KnuckleWraps(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MANA_SYRINGE = ITEMS.register("mana_syringe", () -> new ManaSyringe(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
            )
    ));
    public static final RegistryObject<Item> SOLDIERS_SYRINGE = ITEMS.register("soldiers_syringe", () -> new SoldiersSyringe(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> TARGET_CUBE = ITEMS.register("target_cube", () -> new TargetCube(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BLOOD_PACK = ITEMS.register("blood_pack", () -> new BloodPack(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BROKEN_CLOCK = ITEMS.register("broken_clock", () -> new BrokenClock(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SHATTERED_CLOCK = ITEMS.register("shattered_clock", () -> new ShatteredClock(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SUNDERED_CLOCK = ITEMS.register("sundered_clock", () -> new SunderedClock(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TIMEKEEPERS_CLOCK = ITEMS.register("timekeepers_clock", () -> new TimekeepersClock(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> THE_CLOCK_OF_FROZEN_TIME = ITEMS.register("the_clock_of_frozen_time", () -> new TheClockOfFrozenTime(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3)
            )
    ));
    public static final RegistryObject<Item> WIRE_CUTTERS = ITEMS.register("wire_cutters", () -> new WireCutters(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> AIR_JORDAN_EARRINGS = ITEMS.register("air_jordan_earrings", () -> new AirJordanEarrings(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> HELL_SPECS = ITEMS.register("hell_specs", () -> new HellSpecs(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> SCOPE_GOGGLES = ITEMS.register("scope_goggles", () -> new ScopeGoggles(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> LENS_MAKERS_GLASSES = ITEMS.register("lens_makers_glasses", () -> new LensMakersGlasses(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> JURY_NULLIFIER = ITEMS.register("jury_nullifier", () -> new JuryNullifier(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> EYE_OF_THE_DRAGON = ITEMS.register("eye_of_the_dragon", () -> new EyeOfTheDragon(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar", () -> new Crowbar(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BRUTAL_HEART = ITEMS.register("brutal_heart", () -> new BrutalHeart(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NINJA_HEART = ITEMS.register("ninja_heart", () -> new NinjaHeart(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));

    public static final RegistryObject<Item> DECK_OF_CARDS = ITEMS.register("deck_of_cards", () -> new DeckOfCards(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1, 420)
            )
    ));
    public static final RegistryObject<Item> VINDICATOR_STEROIDS = ITEMS.register("vindicator_steroids", () -> new VindicatorSteroids(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1, 45 * 20)
            )
    ));
    public static final RegistryObject<Item> DAVYS_ANKLET = ITEMS.register("davys_anklet", () -> new DavysAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)
            )
    ));
    public static final RegistryObject<Item> EMPTY_ANKLET = ITEMS.register("empty_anklet", () -> new EmptyAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> ANKLET_OF_THE_IMPRISONED = ITEMS.register("anklet_of_the_imprisoned", () -> new AnkletOfTheImprisoned(
            ModRarities.CATACLYSMIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> SHARPNESS_ANKLET = ITEMS.register("sharpness_anklet", () -> new SharpnessAnklet(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));

    public static final RegistryObject<Item> DEBUG_ANKLET = ITEMS.register("debug_anklet", () -> new DebugAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)
            )
    ));
    public static final RegistryObject<Item> REDSTONE_ANKLET = ITEMS.register("redstone_anklet", () -> new RedstoneAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> DEVILS_ANKLET = ITEMS.register("devils_anklet", () -> new DevilsAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> BASKETBALL_ANKLET = ITEMS.register("basketball_anklet", () -> new BasketballAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> EMERALD_ANKLET = ITEMS.register("emerald_anklet", () -> new EmeraldAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 200)
            )
    ));
    public static final RegistryObject<Item> RUBY_ANKLET = ITEMS.register("ruby_anklet", () -> new RubyAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> TOPAZ_ANKLET = ITEMS.register("topaz_anklet", () -> new TopazAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SAPPHIRE_ANKLET = ITEMS.register("sapphire_anklet", () -> new SapphireAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> ONYX_ANKLET = ITEMS.register("onyx_anklet", () -> new OnyxAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 60)
            )
    ));
    public static final RegistryObject<Item> ULTRA_DODGE_ANKLET = ITEMS.register("ultra_dodge_anklet", () -> new UltraDodgeAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)
            )
    ));
    public static final RegistryObject<Item> GUNDALFS_ANKLET = ITEMS.register("gundalfs_anklet", () -> new GundalfsAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 40)
            )
    ));
    public static final RegistryObject<Item> TRIAL_ANKLET = ITEMS.register("trial_anklet", () -> new TrialAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> SUPER_DODGE_ANKLET = ITEMS.register("super_dodge_anklet", () -> new SuperDodgeAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> GNOME_KINGS_ANKLET = ITEMS.register("gnome_kings_anklet", () -> new GnomeKingsAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> ANKLENT = ITEMS.register("anklent", () -> new Anklent(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> ANKLE_MONITOR = ITEMS.register("ankle_monitor", () -> new AnkleMonitor(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3)
            )
    ));
    public static final RegistryObject<Item> FIERY_ANKLET = ITEMS.register("fiery_anklet", () -> new FieryAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> SACRED_SPEED_ANKLET = ITEMS.register("sacred_speed_anklet", () -> new SacredSpeedAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> CONDUCTITE_ANKLET = ITEMS.register("conductite_anklet", () -> new ConductiteAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> BLOOD_CLOT_ANKLET = ITEMS.register("blood_clot_anklet", () -> new BloodClotAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> VIRENTIUM_ANKLET = ITEMS.register("virentium_anklet", () -> new VirentiumAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> COSMIC_ANKLET = ITEMS.register("cosmic_anklet", () -> new CosmicAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> NYXIUM_ANKLET = ITEMS.register("nyxium_anklet", () -> new NyxiumAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> VOID_ANKLET = ITEMS.register("void_anklet", () -> new VoidAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> IRONCLAD_ANKLET = ITEMS.register("ironclad_anklet", () -> new IroncladAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1)
            )
    ));
    public static final RegistryObject<Item> GLADIATORS_ANKLET = ITEMS.register("gladiators_anklet", () -> new GladiatorsAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> EXODIUM_ANKLET = ITEMS.register("exodium_anklet", () -> new ExodiumAnklet(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 40)
            )
    ));
    public static final RegistryObject<Item> BIG_STEPPA = ITEMS.register("big_steppa", () -> new BigSteppa(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1)
            )
    ));
    public static final RegistryObject<Item> WINDSWEPT_ANKLET = ITEMS.register("windswept_anklet", () -> new WindsweptAnklet(
            Rarity.EPIC,
            List.of(
            )
    ));


    public static final RegistryObject<Item> SUSPICIOUS_SLOT_MACHINE = ITEMS.register("suspicious_slot_machine", () -> new SuspiciousSlotMachine(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1)
            )
    ));
    public static final RegistryObject<Item> HYPERBOLIC_FEATHER = ITEMS.register("hyperbolic_feather", () -> new HyperbolicFeather(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 80)
            )
    ));
    public static final RegistryObject<Item> CARTON_OF_PRISM_SOLUTION_MILK = ITEMS.register("carton_of_prism_solution_milk", () -> new CartonOfPrismSolutionMilk(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> TRIAL_GUARDIAN_EYEBROWS = ITEMS.register("trial_guardian_eyebrows", () -> new TrialGuardianEyebrows(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> TRIAL_GUARDIAN_HANDS = ITEMS.register("trial_guardian_hands", () -> new TrialGuardianHandsHead(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BLOOD_CHALICE = ITEMS.register("blood_chalice", () -> new BloodChalice(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BLACK_MATTER_NECKLACE = ITEMS.register("black_matter_necklace", () -> new BlackMatterNecklace(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SELF_REPAIR_NEXUS = ITEMS.register("self_repair_nexus", () -> new SelfRepairNexus(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> VAMPIRIC_TALISMAN = ITEMS.register("vampiric_talisman", () -> new VampiricTalisman(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 80)
            )
    ));
    public static final RegistryObject<Item> HEMOGRAFT_NEEDLE = ITEMS.register("hemograft_needle", () -> new HemograftNeedle(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> SANGUINE_SPECTACLES = ITEMS.register("sanguine_spectacles", () -> new SanguineSpectacles(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> VAMPIRE_FANG = ITEMS.register("vampire_fang", () -> new VampireFang(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> PROGENITORS_EARRINGS = ITEMS.register("progenitors_earrings", () -> new ProgenitorsEarrings(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> FALLEN_ANGELS_HALO = ITEMS.register("fallen_angels_halo", () -> new FallenAngelsHalo(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BLOODSTAINED_MIRROR = ITEMS.register("bloodstained_mirror", () -> new BloodstainedMirror(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> INCOGNITO_MODE = ITEMS.register("incognito_mode", () -> new IncognitoMode(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> MINIATURE_ANCHOR = ITEMS.register("miniature_anchor", () -> new MiniatureAnchor(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PAPER_AIRPLANE = ITEMS.register("paper_airplane", () -> new PaperAirplane(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> FIRE_EXTINGUISHER = ITEMS.register("fire_extinguisher", () -> new FireExtinguisher(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> EMERGENCY_MEETING = ITEMS.register("emergency_meeting", () -> new EmergencyMeeting(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1, 20 * 60)
            )
    ));
    public static final RegistryObject<Item> PENCIL_SHARPENER = ITEMS.register("pencil_sharpener", () -> new PencilSharpener(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CHOCOLATE_BAR = ITEMS.register("chocolate_bar", () -> new ChocolateBar(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));

    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(
            Tiers.NETHERITE, 5, -3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> PORTABLE_MINING_RIG = ITEMS.register("portable_mining_rig_charm", () -> new PortableMiningRig(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3)
            )
    ));
    public static final RegistryObject<Item> DUMBBELL = ITEMS.register("dumbbell", () -> new Dumbbell(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BOX_OF_CHOCOLATES = ITEMS.register("box_of_chocolates", () -> new BoxOfChocolates(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BROKEN_HEART = ITEMS.register("broken_heart", () -> new BrokenHeart(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    //    public static final RegistryObject<Item> BIKERS_HELMET = ITEMS.register("bikers_helmet", () -> new BikersHelmet(
//            ModRarities.LEGENDARY,
//            List.of(
//                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
//            )
//    ));
    public static final RegistryObject<Item> ESCAPE_KEY = ITEMS.register("escape_key", () -> new EscapeKey(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MIRACLE_CURE = ITEMS.register("miracle_cure", () -> new MiracleCure(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1, 20 * 60)
            )
    ));
    public static final RegistryObject<Item> ESSENTIAL_OILS = ITEMS.register("essential_oils", () -> new EssentialOils(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> GLASS_HEART = ITEMS.register("glass_heart", () -> new GlassHeart(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CRYPTO_WALLET_CHARM = ITEMS.register("crypto_wallet_charm", () -> new CryptoWallet(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> DIVINE_IMMOLATION = ITEMS.register("divine_immolation", () -> new DivineImmolation(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> LIGHT_SWITCH = ITEMS.register("light_switch", () -> new LightSwitch(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1, 400)
            )
    ));
    public static final RegistryObject<Item> FUZZY_DICE = ITEMS.register("fuzzy_dice", () -> new FuzzyDice(
            ModRarities.PRISMATIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));


    public static final RegistryObject<Item> SOLAR_SYSTEM = ITEMS.register("solar_system", () -> new SolarSystem(
            ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> NANOMACHINES = ITEMS.register("nanomachines", () -> new NanoMachines(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> DUELING_GLOVE = ITEMS.register("dueling_glove", () -> new DuelingGlove(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> DRAGONHEART = ITEMS.register("dragonheart", () -> new DragonHeart(
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> UVOGRE_HEART = ITEMS.register("uvogre_heart", () -> new UvogreHeart(
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> ZOMBIE_HEART = ITEMS.register("zombie_heart", () -> new ZombieHeart(
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> FROZEN_HEART = ITEMS.register("frozen_heart", () -> new FrozenHeart(
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> SECOND_HEART = ITEMS.register("second_heart", () -> new SecondHeart(
            ModRarities.MYTHIC, List.of(
    )
    ));

    public static final RegistryObject<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold", () -> new HeartOfGold(
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> BRAIN_ROT = ITEMS.register("brain_rot", () -> new BrainRot(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> ROAD_RUNNERS_RING = ITEMS.register("road_runners_ring", () -> new RoadRunnersRing(
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> RESPLENDENT_FEATHER_CHARM = ITEMS.register("resplendent_feather_charm", () -> new ResplendentFeather(Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> ABYSSAL_NECKLACE = ITEMS.register("abyssal_necklace", () -> new AbyssalNecklace(
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> OLD_GPU = ITEMS.register("old_gpu", () -> new OldGpuAxe(
            Tiers.DIAMOND, 23, -2.5F,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )
    ));
    public static final RegistryObject<Item> TRUTHSEEKER = ITEMS.register("truthseeker", () -> new TruthseekerSword(
            Tiers.DIAMOND, 0, -2.3F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_KILL, 1)
    )
    ));
    public static final RegistryObject<Item> SUPERNOVA = ITEMS.register("supernova", () -> new SupernovaSword(
            Tiers.NETHERITE, 10, -3.1F,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> SHADOWSTEP = ITEMS.register("shadowstep", () -> new ShadowstepSword(
            Tiers.NETHERITE, -2, -2.6F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadowsItem(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> TERRATON_HAMMER = ITEMS.register("terraton_hammer", () -> new TeratonHammer(
            Tiers.NETHERITE, 20, -2,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> WOODEN_RULER = ITEMS.register("wooden_ruler", () -> new WoodenRulerHammer(
            Tiers.DIAMOND, -1, -3,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )

    ));
    public static final RegistryObject<Item> METAL_RULER = ITEMS.register("metal_ruler", () -> new MetalRulerSword(
            Tiers.DIAMOND, -1, -3,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> DOUBLE_DOWN = ITEMS.register("double_down", () -> new DoubleDown(
            Tiers.DIAMOND, 9, -3.2F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 4)
    )
    ));

    public static final RegistryObject<Item> PAPER_CUT = ITEMS.register("paper_cut", () -> new PaperCutSword(
            Tiers.STONE, 1, -2.4F,
            Rarity.RARE, List.of(
    )
    ));

    public static final RegistryObject<Item> WHISPERWALTZ = ITEMS.register("whisperwaltz", () -> new WhisperwaltzSword(
            Tiers.NETHERITE, -3, 16F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)

    )
    ));
    public static final RegistryObject<Item> PROVIDENCE = ITEMS.register("providence", () -> new Providence(
            ModRarities.MYTHIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SEVENTH_STAR = ITEMS.register("seventh_star", () -> new SeventhStarSword(
            Tiers.NETHERITE, 10, -3.15F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1)
    )
    ));
//    public static final RegistryObject<Item> MARIANAS_TRENCH = ITEMS.register("marianas_trench", () -> new MarianasTrenchSword(
//            Tiers.NETHERITE, 10, -2.4F,
//            ModRarities.FABLED, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1)
//    )
//    ));
//    public static final RegistryObject<Item> CHALLENGER_DEEP = ITEMS.register("challenger_deep", () -> new ChallengerDeepSword(
//            Tiers.NETHERITE, 18, -3,
//            ModRarities.MYTHIC, List.of(
//            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SWING, 1)
//    )
//    ));

    public static final RegistryObject<Item> CREASE_OF_CREATION = ITEMS.register("crease_of_creation", () -> new CreaseOfCreation(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2)
            )
    ));

    public static final RegistryObject<Item> THE_CLOUD = ITEMS.register("the_cloud", () -> new TheCloudItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> EVENT_HORIZON = ITEMS.register("event_horizon", () -> new EventHorizonSpear(
            Tiers.NETHERITE,
            17,
            -3.1F,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
            )
    ));
    // endregion

    // region Armor

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.HELMET,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.CHESTPLATE,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.LEGGINGS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.BOOTS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.HELMET,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.CHESTPLATE,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.LEGGINGS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.BOOTS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    // endregion

    // region Gastronomy Items
    public static final RegistryObject<Item> FRIDGE_CHARM = ITEMS.register("fridge_charm", () -> new Fridge(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SMART_FRIDGE_CHARM = ITEMS.register("smart_fridge_charm", () -> new SmartFridge(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SALT_SHAKER_CHARM = ITEMS.register("salt_shaker_charm", () -> new SaltShaker(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PEPPER_SHAKER_CHARM = ITEMS.register("pepper_shaker_charm", () -> new PepperShaker(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SALT_AND_PEPPER_CHARM = ITEMS.register("salt_and_pepper_charm", () -> new SaltAndPepper(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BAMBOO_STEAMER = ITEMS.register("bamboo_steamer", () -> new BambooSteamer(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SMOKE_STONE = ITEMS.register("smoke_stone", () -> new SmokeStone(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> THE_SMOKEHOUSE = ITEMS.register("the_smokehouse", () -> new TheSmokehouse(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SUGAR_GLAZE = ITEMS.register("sugar_glaze", () -> new SugarGlaze(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> RAINBOW_SPRINKLES = ITEMS.register("rainbow_sprinkles", () -> new RainbowSprinkles(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> ROCK_CANDY_RING = ITEMS.register("rock_candy_ring", () -> new RockCandyRing(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> CARAMEL_CRUNCH_MEDALLION = ITEMS.register("caramel_crunch_medallion", () -> new CaramelCrunchMedallion(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> DUNKED_DONUT = ITEMS.register("dunked_donut", () -> new DunkedDonut(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> LOLLIPOP_OF_ETERNITY = ITEMS.register("lollipop_of_eternity", () -> new LollipopOfEternity(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SEARED_SUGAR_BROOCH = ITEMS.register("seared_sugar_brooch", () -> new SearedSugarBrooch(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));


    public static final RegistryObject<Item> MORTAR_AND_PESTLE_CHARM = ITEMS.register("mortar_and_pestle_charm", () -> new MortarAndPestle(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BUTTER_GAUNTLETS = ITEMS.register("butter_gauntlets", () -> new ButterGauntlets(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1, 60)
            )
    ));
    public static final RegistryObject<Item> TOMATO_SAUCE_CHARM = ITEMS.register("tomato_sauce_charm", () -> new TomatoSauce(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> CHEESE_SAUCE_CHARM = ITEMS.register("cheese_sauce_charm", () -> new CheeseSauce(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PIZZA_SLOP_CHARM = ITEMS.register("pizza_slop_charm", () -> new PizzaSlop(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> HOT_SAUCE_CHARM = ITEMS.register("hot_sauce_charm", () -> new HotSauce(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> OLIVE_OIL_CHARM = ITEMS.register("olive_oil_charm", () -> new OliveOil(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> EXTRA_VIRGIN_OLIVE_OIL_CHARM = ITEMS.register("extra_virgin_olive_oil_charm", () -> new ExtraVirginOliveOil(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    //    public static final RegistryObject<Item> KNIFE_BLOCK_ITEM = ITEMS.register("knife_block", () -> new KnifeBlockItem(
//            Rarity.EPIC,
//            List.of(
//                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
//                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2)
//            )
//    ));
    public static final RegistryObject<Item> SPATULA_HAMMER = ITEMS.register("spatula", () -> new SpatulaHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 3)
    )
    ));


    public static final RegistryObject<Item> THE_GOLDEN_SPATULA_HAMMER = ITEMS.register("the_golden_spatula", () -> new TheGoldenSpatulaHammer(
            Tiers.NETHERITE, 13, -2.3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 3)
    )
    ));

    public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife", () -> new IronKnifeSword(
            Tiers.IRON, 3, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> GOLD_KNIFE = ITEMS.register("gold_knife", () -> new GoldKnifeSword(
            Tiers.GOLD, 4, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new DiamondKnifeSword(
            Tiers.DIAMOND, 5, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> VOID_KNIFE = ITEMS.register("void_knife", () -> new VoidKnifeSword(
            Tiers.NETHERITE, 8, -2.3F,
            ModRarities.MYTHIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 3)
    )
    ));
    public static final RegistryObject<Item> MELONCHOLY_SWORD = ITEMS.register("meloncholy", () -> new MeloncholySword(
            Tiers.IRON, 6, -2.3F,
            ModRarities.LEGENDARY, List.of(
    )
    ));
    public static final RegistryObject<Item> APPLE_CORE_LANCE = ITEMS.register("apple_core", () -> new AppleCoreSpear(
            Tiers.IRON, 8, -2.6F,
            ModRarities.LEGENDARY, List.of(
    )
    ));
    public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan", () -> new FryingPanHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> CHOPSTICK_STAFF = ITEMS.register("chopstick", () -> new ChopstickStaff(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));
    public static final RegistryObject<Item> BAMBOO_STAFF = ITEMS.register("bamboo_staff", () -> new BambooStaff(
            Tiers.DIAMOND, 4, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )
    ));


    public static final RegistryObject<Item> POTATO_MASHER_HAMMER = ITEMS.register("potato_masher", () -> new PotatoMasherHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));


    public static final RegistryObject<Item> WHISK_HAMMER = ITEMS.register("whisk", () -> new WhiskHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(ON_HIT, 1)
    )
    ));


//    public static final RegistryObject<Item> PANDORAS_CAULDRON = ITEMS.register("pandoras_cauldron", () -> new PandorasCauldron(
//            0, 0, ModRarities.MYTHIC,
//            List.of(
//                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
//            )
//    ));
//    public static final RegistryObject<Item> COSMIC_NINJA_STAR = ITEMS.register("cosmic_ninja_star", () -> new CosmicNinjaStar(
//            3, -3, ModRarities.MYTHIC,
//            List.of(
//                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
//            )
//    ));

    public static final RegistryObject<Item> PHOTON = ITEMS.register("photon", () -> new Photon(
            0, 0, ModRarities.DIVINE,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));
    public static final RegistryObject<Item> POUCH_O_PHOTONS = ITEMS.register("pouch_o_photons", () -> new PouchOPhotons(
            0, -3, ModRarities.DIVINE,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));

    public static final RegistryObject<Item> VAMPIRE_KNIVES = ITEMS.register("vampire_knives", () -> new VampireKnives(
            3, 0, ModRarities.STYGIAN,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));
    public static final RegistryObject<Item> DYNAMITE = ITEMS.register("dynamite", () -> new Dynamite(
            0, -3, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> STICKY_DYNAMITE = ITEMS.register("sticky_dynamite", () -> new StickyDynamite(
            0, -3, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> BOUNCY_DYNAMITE = ITEMS.register("bouncy_dynamite", () -> new BouncyDynamite(
            0, -3, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> DECK_OF_FATE = ITEMS.register("deck_of_fate", () -> new DeckOfFate(
            3, -2.5F, ModRarities.GODLY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 7)
            )
    ));
    public static final RegistryObject<Item> PERFUME_BOTTLE = ITEMS.register("perfume_bottle", () -> new PerfumeBottle(
            3, -3.25F, ModRarities.GODLY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));
    public static final RegistryObject<Item> ABSOLUTE_ZERO = ITEMS.register("absolute_zero", () -> new AbsoluteZero(
            3, -3.25F, ModRarities.ICE,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> CRIMSON_DELIGHT = ITEMS.register("crimson_delight", () -> new CrimsonDelight(
            2, -1.5F, Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CANNONBALL_CABBAGE = ITEMS.register("cannonball_cabbage", () -> new CannonballCabbage(
            5, -2.2F, Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> CAVENDISH = ITEMS.register("cavendish", () -> new Cavendish(
            2, -2.2F, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> STICK_OF_BUTTER = ITEMS.register("stick_of_butter", () -> new StickOfButter(
            0, -3F, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> WINTER_MELON = ITEMS.register("winter_melon", () -> new WinterMelon(
            7, -2.75F, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> GOLDEN_PHOENIX = ITEMS.register("golden_phoenix", () -> new GoldenPhoenix(
            9, -2.65F, Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> STICKY_BOMB = ITEMS.register("sticky_bomb", () -> new StickyBomb(
            0, -3.4F, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(ON_RIGHT_CLICK, 1)
            )
    ));
    public static final RegistryObject<Item> ICE_CUBE = ITEMS.register("ice_cube", () -> new IceCube(
            3, -3F, Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> PERMAFROST_CUBE = ITEMS.register("permafrost_cube", () -> new PermafrostCube(
            6, -3F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));

    public static final RegistryObject<Item> CINDER_BLOCK = ITEMS.register("cinder_block", () -> new CinderBlock(
            10, -3.2F, ModRarities.LEGENDARY,
            List.of(
            )
    ));

    public static final RegistryObject<Item> STYROFOAM_CUP = ITEMS.register("styrofoam_cup", () -> new StyrofoamCup(
            1, -3.2F, Rarity.COMMON,
            List.of(new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1)),
            BrutalityModBlocks.STYROFOAM_CUP.get()
    ));

    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new Mug(
            1, -3.2F, Rarity.COMMON,
            List.of(new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1)),
            BrutalityModBlocks.MUG.get()
    ));

    public static final RegistryObject<Item> OVERCLOCKED_TOASTER = ITEMS.register("overclocked_toaster", () -> new OverclockedToaster(
            10, -3.5F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> SCULK_GRENADE = ITEMS.register("sculk_grenade", () -> new SculkGrenade(
            10, -3F, ModRarities.SCULK,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 2)
            )
    ));
    public static final RegistryObject<Item> BEACH_BALL = ITEMS.register("beach_ball", () -> new BeachBall(
            10, -3F, ModRarities.LEGENDARY,
            List.of(
            )
    ));
    public static final RegistryObject<Item> BLAST_BARREL = ITEMS.register("blast_barrel", () -> new BlastBarrel(
            10, -3.4F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));
    public static final RegistryObject<Item> HOLY_HAND_GRENADE = ITEMS.register("holy_hand_grenade", () -> new HolyHandGrenade(
            10, -3.4F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(WHEN_THROWN, 1)
            )
    ));

    // endregion

    // region Math Items

    public static final RegistryObject<Item> PI_CHARM = ITEMS.register("pi_charm", () -> new Pi(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> EXPONENTIAL_CHARM = ITEMS.register("exponential_charm", () -> new ExponentialCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 5)
            )
    ));
    public static final RegistryObject<Item> ADDITION_CHARM = ITEMS.register("addition_charm", () -> new Addition(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> SUBTRACTION_CHARM = ITEMS.register("subtraction_charm", () -> new Subtraction(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MULTIPLICATION_CHARM = ITEMS.register("multiplication_charm", () -> new Multiplication(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> DIVISION_CHARM = ITEMS.register("division_charm", () -> new Division(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SUM_CHARM = ITEMS.register("sum_charm", () -> new Sum(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> SINE_CHARM = ITEMS.register("sine_charm", () -> new Sine(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3)
            )
    ));

    public static final RegistryObject<Item> COSINE_CHARM = ITEMS.register("cosine_charm", () -> new Cosine(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 3)
            )
    ));

    public static final RegistryObject<Item> SCIENTIFIC_CALCULATOR = ITEMS.register("scientific_calculator", () -> new ScientificCalculator(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
            )
    ));
    // endregion

    // region Rage Items
    public static final RegistryObject<Item> BLOOD_STONE = ITEMS.register("blood_stone", () -> new BloodStone(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> RAGE_STONE = ITEMS.register("rage_stone", () -> new RageStone(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> PAIN_CATALYST = ITEMS.register("pain_catalyst", () -> new PainCatalyst(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> RAMPAGE_CLOCK = ITEMS.register("rampage_clock", () -> new RampageClock(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));
    public static final RegistryObject<Item> BLOOD_HOWL_PENDANT = ITEMS.register("blood_howl_pendant", () -> new BloodHowlPendant(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));
    public static final RegistryObject<Item> SPITE_SHARD = ITEMS.register("spite_shard", () -> new SpiteShard(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2, 20)
    )));
    public static final RegistryObject<Item> HATE_SIGIL = ITEMS.register("hate_sigil", () -> new HateSigil(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )));
    public static final RegistryObject<Item> HEART_OF_WRATH = ITEMS.register("heart_of_wrath", () -> new HeartOfWrath(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )));
    public static final RegistryObject<Item> EYE_FOR_VIOLENCE = ITEMS.register("eye_for_violence", () -> new EyeForViolence(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));
    public static final RegistryObject<Item> BATTLE_SCARS = ITEMS.register("battle_scars", () -> new BattleScars(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));
    public static final RegistryObject<Item> MECHANICAL_AORTA = ITEMS.register("mechanical_aorta", () -> new MechanicalAorta(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> BLOOD_PULSE_GAUNTLETS = ITEMS.register("blood_pulse_gauntlets", () -> new BloodPulseGauntlets(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> FURY_BAND = ITEMS.register("fury_band", () -> new FuryBand(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1)
    )));

    public static final RegistryObject<Item> GRUDGE_TOTEM = ITEMS.register("grudge_totem", () -> new PainCatalyst(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 2)
    )));

    public static final RegistryObject<Item> ANGER_MANAGEMENT = ITEMS.register("anger_management", () -> new AngerManagement(
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.ItemDescriptionComponent(PASSIVE, 1),
            new BrutalityTooltipHelper.ItemDescriptionComponent(ACTIVE, 1)
    )));
    // endregion


    // region Magic Items

    public static final RegistryObject<Item> DAEMONIC_TOME = ITEMS.register("daemonic_tome", () ->
            new DaemonicTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> DARKIST_TOME = ITEMS.register("darkist_tome", () ->
            new DarkistTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> EVERGREEN_TOME = ITEMS.register("evergreen_tome", () ->
            new EvergreenTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> COSMIC_TOME = ITEMS.register("cosmic_tome", () ->
            new CosmicTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> UMBRAL_TOME = ITEMS.register("umbral_tome", () ->
            new UmbralTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> VOID_TOME = ITEMS.register("void_tome", () ->
            new VoidTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> EXODIC_TOME = ITEMS.register("exodic_tome", () ->
            new ExodicTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> CELESTIA_TOME = ITEMS.register("celestia_tome", () ->
            new CelestiaTome(ModRarities.LEGENDARY, List.of())
    );
    public static final RegistryObject<Item> BRIMWIELDER_TOME = ITEMS.register("brimwielder_tome", () ->
            new BrimwielderTome(ModRarities.LEGENDARY, List.of())
    );


    // endregion


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
