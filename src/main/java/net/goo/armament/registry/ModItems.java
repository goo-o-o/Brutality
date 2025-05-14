package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.noir.CanopyOfShadows;
import net.goo.armament.item.noir.NoirArmorItem;
import net.goo.armament.item.noir.ShadowstepSword;
import net.goo.armament.item.terra.TerraArmorItem;
import net.goo.armament.item.terra.TerraBladeSword;
import net.goo.armament.item.terra.TerratonHammer;
import net.goo.armament.item.weapon.custom.*;
import net.goo.armament.item.weapon.unused.ViperRapierItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Armament.MOD_ID);


    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(
            Tiers.NETHERITE,
            -5,
            -3F,
            new Item.Properties(),
            "atomic_judgement",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            3));

    public static final RegistryObject<Item> DOOMFIST_GAUNTLET = ITEMS.register("doomfist_gauntlet", () -> new DoomfistGauntletItem(
            new Item.Properties().stacksTo(1),
            "doomfist_gauntlet",
            ModItemCategories.TEMP,
            ModRarities.LEGENDARY,
            1));

    public static final RegistryObject<Item> EVENT_HORIZON_LANCE = ITEMS.register("event_horizon", () -> new EventHorizonLance(
            new Item.Properties(),
            "event_horizon",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            2));

    public static final RegistryObject<Item> EXCALIBUR_SWORD = ITEMS.register("excalibur", () -> new ExcaliburSword(
            Tiers.NETHERITE,
            0,
            -2.2F,
            new Item.Properties(),
            "excalibur",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            1));

    public static final RegistryObject<Item> FALLEN_SCYTHE = ITEMS.register("fallen_scythe", () -> new FallenScythe(
            Tiers.NETHERITE,
            -2,
            -3F,
            new Item.Properties(),
            "fallen_scythe",
            ModItemCategories.TEMP,
            ModRarities.MYTHIC,
            1));

    public static final RegistryObject<Item> FIRST_EXPLOSION_STAFF = ITEMS.register("first_explosion", () -> new FirstExplosionStaff(
            new Item.Properties().stacksTo(1),
            "first_explosion",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            1));

    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(
            Tiers.NETHERITE,
            5,
            -3F,
            new Item.Properties(),
            "frostmourne",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            3));

    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(
            new Item.Properties(),
            "gungnir",
            ModItemCategories.TEMP,
            ModRarities.MYTHIC,
            2));

    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(
            Tiers.DIAMOND,
            0,
            -2.6F,
            new Item.Properties(),
            "jackpot",
            ModItemCategories.TEMP,
            ModRarities.LEGENDARY,
            1));

    public static final RegistryObject<Item> LEAF_BLOWER = ITEMS.register("leaf_blower", () -> new LeafBlowerItem(
            new Item.Properties().stacksTo(1),
            "leaf_blower",
            ModItemCategories.TEMP,
            ModRarities.LEGENDARY,
            1));

    public static final RegistryObject<Item> MURASAMA_SWORD = ITEMS.register("murasama", () -> new MurasamaSword(
            Tiers.NETHERITE,
            3,
            -2F,
            new Item.Properties(),
            "murasama",
            ModItemCategories.TEMP,
            ModRarities.MYTHIC,
            2));


    public static final RegistryObject<Item> PROVIDENCE_BOW = ITEMS.register("providence", () -> new ProvidenceBow(
            new Item.Properties(),
            "providence",
            ModItemCategories.TEMP,
            ModRarities.MYTHIC,
            2));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TERRA

    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(
            ArmorItem.Type.HELMET,
            new Item.Properties(),
            "terra_helmet",
            ModItemCategories.TERRA,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties(),
            "terra_chestplate",
            ModItemCategories.TERRA,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(
            ArmorItem.Type.LEGGINGS,
            new Item.Properties(),
            "terra_leggings",
            ModItemCategories.TERRA,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(
            ArmorItem.Type.BOOTS,
            new Item.Properties(),
            "terra_boots",
            ModItemCategories.TERRA,
            ModRarities.FABLED,
            1));

    public static final RegistryObject<Item> TERRA_BLADE = ITEMS.register("terra_blade", () -> new TerraBladeSword(
            Tiers.NETHERITE,
            3,
            -2.2F,
            new Item.Properties(),
            "terra_blade",
            ModItemCategories.TERRA,
            ModRarities.LEGENDARY,
            1));

    public static final RegistryObject<Item> TERRATON_HAMMER = ITEMS.register("terraton_hammer", () -> new TerratonHammer(
            Tiers.NETHERITE,
            20,
            -2,
            new Item.Properties(),
            "terraton_hammer",
            ModItemCategories.TERRA,
            ModRarities.MYTHIC,
            2));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // NOIR

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(
            ArmorItem.Type.HELMET,
            new Item.Properties(),
            "noir_helmet",
            ModItemCategories.NOIR,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(
            ArmorItem.Type.CHESTPLATE,
            new Item.Properties(),
            "noir_chestplate",
            ModItemCategories.NOIR,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(
            ArmorItem.Type.LEGGINGS,
            new Item.Properties(),
            "noir_leggings",
            ModItemCategories.NOIR,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(
            ArmorItem.Type.BOOTS,
            new Item.Properties(),
            "noir_boots",
            ModItemCategories.NOIR,
            ModRarities.FABLED,
            1));
    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadows(
            new Item.Properties().rarity(ModRarities.LEGENDARY),
            "canopy_of_shadows",
            ModItemCategories.NOIR,
            ModRarities.MYTHIC,
            1));
    public static final RegistryObject<Item> SHADOWSTEP_SWORD = ITEMS.register("shadowstep", () -> new ShadowstepSword(
            Tiers.NETHERITE,
            -2,
            -2.6F,
            new Item.Properties(),
            "shadowstep",
            ModItemCategories.NOIR,
            ModRarities.LEGENDARY,
            2));

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public static final RegistryObject<Item> SUPERNOVA_SWORD = ITEMS.register("supernova", () -> new SupernovaSword(
            Tiers.NETHERITE,
            5,
            -2.6F,
            new Item.Properties().rarity(ModRarities.LEGENDARY),
            "supernova",
            ModItemCategories.TEMP,
            ModRarities.MYTHIC,
            2));



    public static final RegistryObject<Item> TRUTHSEEKER_SWORD = ITEMS.register("truthseeker", () -> new TruthseekerSword(
            Tiers.DIAMOND,
            0,
            -2.3F,
            new Item.Properties(),
            "truthseeker",
            ModItemCategories.TEMP,
            ModRarities.LEGENDARY,
            1));



    public static final RegistryObject<Item> VIPER_RAPIER = ITEMS.register("viper", () -> new ViperRapierItem(
            Tiers.NETHERITE,
            -2,
            0, new Item.Properties(),
            "viper",
            ModItemCategories.TEMP,
            ModRarities.LEGENDARY,
            2));

    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(
            new Item.Properties(),
            "thunderbolt",
            ModItemCategories.TEMP,
            ModRarities.FABLED,
            3));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
