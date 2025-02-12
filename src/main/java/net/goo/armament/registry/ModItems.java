package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Armament.MOD_ID);

//    public static final RegistryObject<Item> DIVINE_RHITTA_AXE = ITEMS.register("divine_axe_rhitta",
//            () -> new DivineRhittaAxeItem(Tiers.NETHERITE, 8, -3.1F, new Item.Properties(), ModItemCategories.FANTASY));



    public static final RegistryObject<Item> DOOMFIST_GAUNTLET = ITEMS.register("doomfist_gauntlet",
            () -> new DoomfistGauntletItem(new Item.Properties().stacksTo(1), ModItemCategories.TECHNOLOGY));

    public static final RegistryObject<Item> EVENT_HORIZON_LANCE = ITEMS.register("event_horizon",
            () -> new EventHorizonLanceItem(new Item.Properties()
                    .stacksTo(1).fireResistant().setNoRepair().rarity(Rarity.EPIC), ModItemCategories.SPACE));

    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot",
            () -> new JackpotHammerItem(Tiers.NETHERITE, -2.6F, new Item.Properties(), ModItemCategories.SILLY));

    public static final RegistryObject<Item> LEAF_BLOWER = ITEMS.register("leaf_blower",
            () -> new LeafBlowerItem(new Item.Properties().stacksTo(1), ModItemCategories.TECHNOLOGY));

    public static final RegistryObject<Item> QUANTUM_DRILL = ITEMS.register("quantum_drill",
            () -> new QuantumDrillItem(new Item.Properties(), ModItemCategories.TECHNOLOGY));

    public static final RegistryObject<Item> RESONANCE_PICKAXE = ITEMS.register("resonance_pickaxe",
            () -> new ResonancePickaxeItem(Tiers.NETHERITE, 4, -2.2F, new Item.Properties(), ModItemCategories.TECHNOLOGY));

    public static final RegistryObject<Item> SHADOWSTEP_SWORD = ITEMS.register("shadowstep",
            () -> new ShadowstepSword(Tiers.NETHERITE, 5, -2.6F, new Item.Properties(),
                    "shadowstep", ModItemCategories.FANTASY));

    public static final RegistryObject<Item> SUPERNOVA_SWORD = ITEMS.register("supernova",
            () -> new SupernovaSword(Tiers.NETHERITE, 5, -2.6F, new Item.Properties(),
                    "supernova", ModItemCategories.FANTASY));

    public static final RegistryObject<Item> TERRA_BLADE = ITEMS.register("terra_blade",
            () -> new TerraBladeSword(Tiers.NETHERITE, 3, -2.2F, new Item.Properties(),
                    "terra_blade", ModItemCategories.FANTASY));

    public static final RegistryObject<Item> TRUTHSEEKER_SWORD = ITEMS.register("truthseeker",
            () -> new TruthseekerSword(Tiers.NETHERITE, 0, 0, new Item.Properties(), "truthseeker", ModItemCategories.FANTASY));

    public static final RegistryObject<Item> TERRATON_HAMMER = ITEMS.register("terraton_hammer",
            () -> new TerratonHammerItem(Tiers.NETHERITE, 45, 0F, new Item.Properties(), ModItemCategories.FANTASY));

    public static final RegistryObject<Item> VIPER_RAPIER = ITEMS.register("viper",
            () -> new ViperRapierItem(Tiers.NETHERITE, -2, 0F, new Item.Properties(), ModItemCategories.FANTASY));

    public static final RegistryObject<Item> ZEUS_THUNDERBOLT_TRIDENT = ITEMS.register("zeus_thunderbolt",
            () -> new ZeusThunderboltTrident(new Item.Properties(), "zeus_thunderbolt",
                    ModItemCategories.FANTASY));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
