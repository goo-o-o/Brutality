package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.curios.PiCharmCurio;
import net.goo.brutality.item.weapon.custom.CanopyOfShadows;
import net.goo.brutality.item.armor.NoirArmorItem;
import net.goo.brutality.item.weapon.custom.ShadowstepDagger;
import net.goo.brutality.item.armor.TerraArmorItem;
import net.goo.brutality.item.weapon.custom.TerraBladeSword;
import net.goo.brutality.item.weapon.custom.TerratonHammer;
import net.goo.brutality.item.weapon.custom.*;
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

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Brutality.MOD_ID);


    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(
            Tiers.NETHERITE,
            5,
            -3F,
            new Item.Properties(),
            "atomic_judgement",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> DOOMFIST_GAUNTLET = ITEMS.register("doomfist_gauntlet", () -> new DoomfistGauntletItem(
            new Item.Properties().stacksTo(1),
            "doomfist_gauntlet",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> DARKIN_BLADE = ITEMS.register("darkin_blade", () -> new DarkinBladeSword(
            Tiers.NETHERITE,
            10,
            -3.15F,
            new Item.Properties(),
            "darkin_blade",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
            )
    ));

    public static final RegistryObject<Item> CREASE_OF_CREATION = ITEMS.register("crease_of_creation", () -> new CreaseOfCreationItem(
            new Item.Properties().stacksTo(1),
            "crease_of_creation",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> EVENT_HORIZON_LANCE = ITEMS.register("event_horizon", () -> new EventHorizonLance(
            new Item.Properties(),
            10,
            -3.1F,
            "event_horizon",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> EXCALIBUR_SWORD = ITEMS.register("excalibur", () -> new ExcaliburSword(
            Tiers.NETHERITE,
            0,
            -2.2F,
            new Item.Properties(),
            "excalibur",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
            )
    ));

    public static final RegistryObject<Item> FALLEN_SCYTHE = ITEMS.register("fallen_scythe", () -> new FallenScythe(
            Tiers.NETHERITE,
            -2,
            -3F,
            new Item.Properties(),
            "fallen_scythe",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_KILL, 1)
            )
    ));

    public static final RegistryObject<Item> DARKIN_SCYTHE = ITEMS.register("darkin_scythe", () -> new DarkinScythe(
            Tiers.NETHERITE,
            -2,
            -3F,
            new Item.Properties(),
            "darkin_scythe",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_KILL, 1)
            )
    ));


    public static final RegistryObject<Item> RHITTA_AXE = ITEMS.register("rhitta", () -> new RhittaAxe(
            Tiers.DIAMOND,
            10,
            -3F,
            "rhitta",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> FIRST_EXPLOSION_STAFF = ITEMS.register("first_explosion", () -> new FirstExplosionStaff(
            new Item.Properties().stacksTo(1),
            "first_explosion",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));
    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(
            Tiers.NETHERITE,
            5,
            -3F,
            new Item.Properties(),
            "frostmourne",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)

            )
    ));

    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(
            new Item.Properties(),
            13,
            -3.2F,
            "gungnir",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(
            Tiers.DIAMOND,
            0,
            -2.6F,
            new Item.Properties(),
            "jackpot",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));
    public static final RegistryObject<Item> LEAF_BLOWER = ITEMS.register("leaf_blower", () -> new LeafBlowerItem(
            new Item.Properties().stacksTo(1),
            "leaf_blower",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> HF_MURASAMA = ITEMS.register("hf_murasama", () -> new HFMurasamaSword(
            Tiers.NETHERITE,
            16,
            -2F,
            new Item.Properties(),
            "hf_murasama",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> MURAMASA_SWORD = ITEMS.register("muramasa", () -> new MuramasaSword(
            Tiers.NETHERITE,
            3,
            -2F,
            new Item.Properties(),
            "muramasa",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> PROVIDENCE_BOW = ITEMS.register("providence", () -> new ProvidenceBow(
            new Item.Properties(),
            "providence",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> DOUBLE_DOWN_SWORD = ITEMS.register("double_down", () -> new DoubleDownSword(
            Tiers.DIAMOND,
            12,
            -3.2F,
            new Item.Properties(),
            "double_down",
            ModRarities.LEGENDARY,
            List.of(
            )
    ));

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TERRA

    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.HELMET,
            new Item.Properties(),
            "terra_helmet",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));

    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties(),
            "terra_chestplate",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.LEGGINGS,
            new Item.Properties(),
            "terra_leggings",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA,
            ArmorItem.Type.BOOTS,
            new Item.Properties(),
            "terra_boots",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> TERRA_BLADE = ITEMS.register("terra_blade", () -> new TerraBladeSword(
            Tiers.NETHERITE,
            3,
            -2.2F,
            new Item.Properties(),
            "terra_blade",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
            )
    ));
    public static final RegistryObject<Item> TERRATON_HAMMER = ITEMS.register("terraton_hammer", () -> new TerratonHammer(
            Tiers.NETHERITE,
            20,
            -2,
            new Item.Properties(),
            "terraton_hammer",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    /// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOIR

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.HELMET,
            new Item.Properties(),
            "noir_helmet",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties(),
            "noir_chestplate",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.LEGGINGS,
            new Item.Properties(),
            "noir_leggings",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(FULL_SET_PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR,
            ArmorItem.Type.BOOTS,
            new Item.Properties(),
            "noir_boots",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SWING, 1)
            )
    ));

    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadows(
            new Item.Properties().rarity(ModRarities.LEGENDARY),
            "canopy_of_shadows",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1)
            )
    ));
    public static final RegistryObject<Item> SHADOWSTEP_SWORD = ITEMS.register("shadowstep", () -> new ShadowstepDagger(
            Tiers.NETHERITE,
            -2,
            -2.6F,
            new Item.Properties(),
            "shadowstep",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1)
            )
    ));
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static final RegistryObject<Item> SUPERNOVA_SWORD = ITEMS.register("supernova", () -> new SupernovaSword(
            Tiers.NETHERITE,
            10,
            -3.1F,
            new Item.Properties().rarity(ModRarities.LEGENDARY),
            "supernova",
            ModRarities.MYTHICAL,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> DULL_KNIFE_DAGGER = ITEMS.register("dull_knife", () -> new DullKnifeDagger(
            Tiers.NETHERITE,
            4,
            -2F,
            new Item.Properties().rarity(ModRarities.LEGENDARY),
            "dull_knife",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1)
            )
    ));

    public static final RegistryObject<Item> TRUTHSEEKER_SWORD = ITEMS.register("truthseeker", () -> new TruthseekerSword(
            Tiers.DIAMOND,
            0,
            -2.3F,
            new Item.Properties(),
            "truthseeker",
            ModRarities.LEGENDARY,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_KILL, 1)
            )
    ));

    public static final RegistryObject<Item> ROYAL_GUARDIAN_SWORD = ITEMS.register("royal_guardian_sword", () -> new RoyalGuardianSword(
            Tiers.NETHERITE,
            50,
            -3.5F,
            new Item.Properties(),
            "royal_guardian_sword",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(
            new Item.Properties(),
            8,
            -2.1F,
            "thunderbolt",
            ModRarities.FABLED,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static final RegistryObject<Item> SPATULA_HAMMER = ITEMS.register("spatula", () -> new SpatulaHammer(
            Tiers.IRON,
            0,
            -2.3F,
            new Item.Properties(),
            "spatula",
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));
    public static final RegistryObject<Item> FRYING_PAN_HAMMER = ITEMS.register("frying_pan", () -> new FryingPanHammer(
            Tiers.IRON,
            0,
            -2.3F,
            new Item.Properties(),
            "frying_pan",
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> CABBAGE_TRIDENT = ITEMS.register("cabbage", () -> new CabbageTrident(
            new Item.Properties(),
            0,
            0,
            "cabbage",
            Rarity.COMMON,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> WINTERMELON_TRIDENT = ITEMS.register("wintermelon", () -> new WintermelonTrident(
            new Item.Properties(),
            0,
            0,
            "wintermelon",
            Rarity.COMMON,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_HIT, 1)
            )
    ));

    public static final RegistryObject<Item> KNIFE_BLOCK_ITEM = ITEMS.register("knife_block", () -> new KnifeBlockItem(
            new Item.Properties(),
            "knife_block",
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(ON_RIGHT_CLICK, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2)
            )
    ));

    public static final RegistryObject<Item> PI_CHARM = ITEMS.register("pi_charm", () -> new PiCharmCurio(
            new Item.Properties(),
            "pi_charm",
            Rarity.EPIC,
            List.of(
                    new BrutalityTooltipHelper.DescriptionComponent(LORE, 1),
                    new BrutalityTooltipHelper.DescriptionComponent(PASSIVE, 2)
            )
    ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
