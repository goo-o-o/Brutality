package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.armor.NoirArmorItem;
import net.goo.brutality.item.armor.TerraArmorItem;
import net.goo.brutality.item.curios.*;
import net.goo.brutality.item.material.HighFrequencyAlloyItem;
import net.goo.brutality.item.material.PocketBlackHoleItem;
import net.goo.brutality.item.weapon.generic.TheCloudItem;
import net.goo.brutality.item.weapon.axe.OldGpuAxe;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.bow.ProvidenceBow;
import net.goo.brutality.item.weapon.generic.*;
import net.goo.brutality.item.weapon.hammer.*;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.scythe.FallenScythe;
import net.goo.brutality.item.weapon.sword.*;
import net.goo.brutality.item.weapon.trident.*;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.DescriptionComponents.*;

public class BrutalityItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Brutality.MOD_ID);

    public static final RegistryObject<Item> POCKET_BLACK_HOLE = ITEMS.register("pocket_black_hole", () -> new PocketBlackHoleItem(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_FREQUENCY_ALLOY = ITEMS.register("high_frequency_alloy", () -> new HighFrequencyAlloyItem(new Item.Properties()));


    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(
            Tiers.NETHERITE, 5, -3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> DOOMFIST_GAUNTLET_ITEM = ITEMS.register("doomfist_gauntlet", () -> new DoomfistGauntletItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> DARKIN_BLADE_SWORD = ITEMS.register("darkin_blade", () -> new DarkinBladeSword(
            Tiers.NETHERITE, 10, -3.15F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> SEVENTH_STAR_SWORD = ITEMS.register("seventh_star", () -> new SeventhStarSword(
            Tiers.NETHERITE, 10, -3.15F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
    )
    ));

    public static final RegistryObject<Item> CREASE_OF_CREATION_ITEM = ITEMS.register("crease_of_creation", () -> new CreaseOfCreationItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> THE_CLOUD_ITEM = ITEMS.register("the_cloud", () -> new TheCloudItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> EVENT_HORIZON_LANCE = ITEMS.register("event_horizon", () -> new EventHorizonTrident(
            17,
            -3.1F,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> EXCALIBUR_SWORD = ITEMS.register("excalibur", () -> new ExcaliburSword(
            Tiers.NETHERITE, 0, -2.2F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
    )
    ));

    public static final RegistryObject<Item> FALLEN_SCYTHE = ITEMS.register("fallen_scythe", () -> new FallenScythe(
            Tiers.NETHERITE, -2, -3F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_KILL, 1)
    )
    ));

    public static final RegistryObject<Item> DARKIN_SCYTHE = ITEMS.register("darkin_scythe", () -> new DarkinScythe(
            Tiers.NETHERITE, 3, -3F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 4),
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 4),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 3),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1)
    )
    ));


    public static final RegistryObject<Item> RHITTA_AXE = ITEMS.register("rhitta", () -> new RhittaAxe(
            Tiers.DIAMOND, 10, -3F, ModRarities.MYTHICAL,
            List.of(new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> FIRST_EXPLOSION_STAFF = ITEMS.register("first_explosion", () -> new FirstExplosionStaff(
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));
    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(
            Tiers.NETHERITE, 5, -3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 2),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2)

    )
    ));

    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(
            13, -3.2F, ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(WHEN_THROWN, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(
            Tiers.DIAMOND, 0, -2.6F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> LEAF_BLOWER = ITEMS.register("leaf_blower", () -> new LeafBlowerItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> MURASAMA_SWORD = ITEMS.register("hf_murasama", () -> new MurasamaSword(
            Tiers.NETHERITE, 16, -2F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> MURAMASA_SWORD = ITEMS.register("muramasa", () -> new MuramasaSword(
            Tiers.NETHERITE, 3, -2F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
    )
    ));

    public static final RegistryObject<Item> PROVIDENCE_BOW = ITEMS.register("providence", () -> new ProvidenceBow(
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> DOUBLE_DOWN_SWORD = ITEMS.register("double_down", () -> new DoubleDownSword(
            Tiers.DIAMOND, 9, -3.2F,
            ModRarities.LEGENDARY, List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 4)
    )
    ));

    public static final RegistryObject<Item> PAPER_CUT_SWORD = ITEMS.register("paper_cut", () -> new PaperCutSword(
            Tiers.STONE, 1, -2.4F,
            Rarity.RARE, List.of(
    )
    ));

    public static final RegistryObject<Item> WHISPERWALTZ_SWORD = ITEMS.register("whisperwaltz", () -> new WhisperwaltzSword(
            Tiers.NETHERITE, -3, 16F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)

    )
    ));

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TERRA

    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.HELMET,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.CHESTPLATE,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.LEGGINGS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.BOOTS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> TERRA_BLADE_SWORD = ITEMS.register("terra_blade", () -> new TerraBladeSword(
            Tiers.NETHERITE, 3, -2.2F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
    )
    ));

    public static final RegistryObject<Item> LAST_PRISM_ITEM = ITEMS.register("last_prism", () -> new LastPrismItem(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> TERRATOMERE_SWORD = ITEMS.register("terratomere", () -> new TerratomereSword(
            Tiers.NETHERITE, 7, -2.5F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 2)
    )));

    public static final RegistryObject<Item> EXOBLADE_SWORD = ITEMS.register("exoblade", () -> new ExobladeSword(
            Tiers.NETHERITE, 20, -2.5F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )

    ));
    public static final RegistryObject<Item> TERRATON_HAMMER = ITEMS.register("terraton_hammer", () -> new TerratonHammer(
            Tiers.NETHERITE, 20, -2,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));
    public static final RegistryObject<Item> WOODEN_RULER = ITEMS.register("wooden_ruler", () -> new WoodenRulerHammer(
            Tiers.DIAMOND, -1, -3,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
    )

    ));
    public static final RegistryObject<Item> METAL_RULER = ITEMS.register("metal_ruler", () -> new MetalRulerSword(
            Tiers.DIAMOND, -1, -3,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
    )
    ));

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOIR

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.HELMET,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.CHESTPLATE,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.LEGGINGS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.BOOTS,
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadowsItem(
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SHADOWSTEP_SWORD = ITEMS.register("shadowstep", () -> new ShadowstepSword(
            Tiers.NETHERITE, -2, -2.6F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1)
    )
    ));
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static final RegistryObject<Item> SUPERNOVA_SWORD = ITEMS.register("supernova", () -> new SupernovaSword(
            Tiers.NETHERITE, 10, -3.1F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));

    public static final RegistryObject<Item> DULL_KNIFE_DAGGER = ITEMS.register("dull_knife", () -> new DullKnifeSword(
            Tiers.NETHERITE, 4, -2F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1)
    )
    ));

    public static final RegistryObject<Item> TRUTHSEEKER_SWORD = ITEMS.register("truthseeker", () -> new TruthseekerSword(
            Tiers.DIAMOND, 0, -2.3F,
            ModRarities.LEGENDARY, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_KILL, 1)
    )
    ));

    public static final RegistryObject<Item> ROYAL_GUARDIAN_SWORD = ITEMS.register("royal_guardian_sword", () -> new RoyalGuardianSword(
            Tiers.NETHERITE, 50, -3.5F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));

    public static final RegistryObject<Item> BLADE_OF_THE_RUINED_KING = ITEMS.register("blade_of_the_ruined_king", () -> new BladeOfTheRuinedKingSword(
            Tiers.DIAMOND, 9, -2.5F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 2),
            new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 2)
    )
    ));

    public static final RegistryObject<Item> OLD_GPU_AXE = ITEMS.register("old_gpu", () -> new OldGpuAxe(
            Tiers.DIAMOND, 23, -2.5F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(
            8, -2.1F, ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> SPATULA_HAMMER = ITEMS.register("spatula", () -> new SpatulaHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 3)
    )
    ));


    public static final RegistryObject<Item> THE_GOLDEN_SPATULA_HAMMER = ITEMS.register("the_golden_spatula", () -> new TheGoldenSpatulaHammer(
            Tiers.NETHERITE, 13, -2.3F,
            ModRarities.FABLED, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 3)
    )
    ));

    public static final RegistryObject<Item> IRON_KNIFE_SWORD = ITEMS.register("iron_knife", () -> new IronKnifeSword(
            Tiers.IRON, 3, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> GOLD_KNIFE_SWORD = ITEMS.register("gold_knife", () -> new GoldKnifeSword(
            Tiers.GOLD, 4, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> DIAMOND_KNIFE_SWORD = ITEMS.register("diamond_knife", () -> new DiamondKnifeSword(
            Tiers.DIAMOND, 5, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 2)
    )
    ));
    public static final RegistryObject<Item> VOID_KNIFE_SWORD = ITEMS.register("void_knife", () -> new VoidKnifeSword(
            Tiers.NETHERITE, 8, -2.3F,
            ModRarities.MYTHICAL, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 3)
    )
    ));
    public static final RegistryObject<Item> FRYING_PAN_HAMMER = ITEMS.register("frying_pan", () -> new FryingPanHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));


    public static final RegistryObject<Item> POTATO_MASHER_HAMMER = ITEMS.register("potato_masher", () -> new PotatoMasherHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));


    public static final RegistryObject<Item> WHISK_HAMMER = ITEMS.register("whisk", () -> new WhiskHammer(
            Tiers.IRON, 2, -2.3F,
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
    )
    ));

    public static final RegistryObject<Item> CABBAGE_TRIDENT = ITEMS.register("cabbage", () -> new CabbageTrident(
            0, 0, Rarity.COMMON,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(WHEN_THROWN, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> WINTERMELON_TRIDENT = ITEMS.register("wintermelon", () -> new WintermelonTrident(
            0, 0, Rarity.COMMON,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(WHEN_THROWN, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> BIOMECH_REACTOR_TRIDENT = ITEMS.register("biomech_reactor", () -> new BiomechReactorTrident(
            0, 0, ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(WHEN_THROWN, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 2)
            )
    ));
    public static final RegistryObject<Item> DEPTH_CRUSHER_TRIDENT = ITEMS.register("depth_crusher", () -> new DepthCrusherTrident(
            6, -2.4F, ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(WHEN_THROWN, 2)
            )
    ));

    public static final RegistryObject<Item> KNIFE_BLOCK_ITEM = ITEMS.register("knife_block", () -> new KnifeBlockItem(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2)
            )
    ));

    public static final RegistryObject<Item> SALT_SHAKER_CHARM = ITEMS.register("salt_shaker_charm", () -> new SaltShakerCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PEPPER_SHAKER_CHARM = ITEMS.register("pepper_shaker_charm", () -> new PepperShakerCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SALT_AND_PEPPER_CHARM = ITEMS.register("salt_and_pepper_charm", () -> new SaltAndPepperCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MORTAR_AND_PESTLE_CHARM = ITEMS.register("mortar_and_pestle_charm", () -> new MortarAndPestleCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> BUTTER_GAUNTLETS_HANDS = ITEMS.register("butter_gauntlets_hands", () -> new ButterGauntletsHands(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));
    public static final RegistryObject<Item> TOMATO_SAUCE_CHARM = ITEMS.register("tomato_sauce_charm", () -> new TomatoSauceCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> CHEESE_SAUCE_CHARM = ITEMS.register("cheese_sauce_charm", () -> new CheeseSauceCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> PIZZA_SLOP_CHARM = ITEMS.register("pizza_slop_charm", () -> new PizzaSlopCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> HOT_SAUCE_CHARM = ITEMS.register("hot_sauce_charm", () -> new HotSauceCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> OLIVE_OIL_CHARM = ITEMS.register("olive_oil_charm", () -> new OliveOilCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> EXTRA_VIRGIN_OLIVE_OIL = ITEMS.register("extra_virgin_olive_oil_charm", () -> new ExtraVirginOliveOilCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> PI_CHARM = ITEMS.register("pi_charm", () -> new PiCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> EXPONENTIAL_CHARM = ITEMS.register("exponential_charm", () -> new ExponentialCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 5)
            )
    ));
    public static final RegistryObject<Item> ADDITION_CHARM = ITEMS.register("addition_charm", () -> new AdditionCharm(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> SUBTRACTION_CHARM = ITEMS.register("subtraction_charm", () -> new SubtractionCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> MULTIPLICATION_CHARM = ITEMS.register("multiplication_charm", () -> new MultiplicationCharm(
            Rarity.EPIC,
            List.of(
            )
    ));
    public static final RegistryObject<Item> DIVISION_CHARM = ITEMS.register("division_charm", () -> new DivisionCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SUM_CHARM = ITEMS.register("sum_charm", () -> new SumCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> SINE_CHARM = ITEMS.register("sine_charm", () -> new SineCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 3)
            )
    ));

    public static final RegistryObject<Item> COSINE_CHARM = ITEMS.register("cosine_charm", () -> new CosineCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 3)
            )
    ));

    public static final RegistryObject<Item> SCIENTIFIC_CALCULATOR_BELT = ITEMS.register("scientific_calculator_belt", () -> new ScientificCalculatorBelt(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> ROAD_RUNNERS_RING = ITEMS.register("road_runners_ring", () -> new RoadRunnersRing(
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
    )
    ));

    public static final RegistryObject<Item> RESPLENDENT_FEATHER_CHARM = ITEMS.register("resplendent_feather_charm", () -> new ResplendentFeatherCharm(Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
    )
    ));
    public static final RegistryObject<Item> ABYSSAL_NECKLACE = ITEMS.register("abyssal_necklace", () -> new AbyssalNecklace(
            Rarity.EPIC, List.of(
            new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
            new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
    )
    ));

    public static final RegistryObject<Item> NANOMACHINES_HANDS = ITEMS.register("nanomachines_hands", () -> new NanoMachinesHands(
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> FRIDGE_CHARM = ITEMS.register("fridge_charm", () -> new FridgeCharm(
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SMART_FRIDGE_CHARM = ITEMS.register("smart_fridge_charm", () -> new SmartFridgeCharm(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));





    public static final RegistryObject<Item> PORTABLE_MINING_RIG_CHARM = ITEMS.register("portable_mining_rig_charm", () -> new PortableMiningRigCharm(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 3)
            )
    ));
    public static final RegistryObject<Item> CRYPTO_WALLET_CHARM = ITEMS.register("crypto_wallet_charm", () -> new CryptoWalletCharm(
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));


    //    public static final RegistryObject<Item> POCKET_BLACK_HOLE_CHARM = ITEMS.register("pocket_black_hole_charm", () -> new PocketBlackHoleCharm(
//          ,"pocket_black_hole_charm",Rarity.EPIC,List.of(
//                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
//                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
//            )
//    ));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
