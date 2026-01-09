package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.event.mod.client.Keybindings;
import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.armor.NoirArmorItem;
import net.goo.brutality.item.armor.TerraArmorItem;
import net.goo.brutality.item.armor.VampireLordArmorItem;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.item.base.BrutalityBlockItem;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.block.FilingCabinetBlockItem;
import net.goo.brutality.item.block.ImportantDocumentsBlockItem;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.item.curios.BrutalityMathCurio;
import net.goo.brutality.item.curios.anklet.*;
import net.goo.brutality.item.curios.belt.BattleScars;
import net.goo.brutality.item.curios.belt.MiniatureAnchor;
import net.goo.brutality.item.curios.belt.ScientificCalculator;
import net.goo.brutality.item.curios.charm.*;
import net.goo.brutality.item.curios.hands.Handcuffs;
import net.goo.brutality.item.curios.hands.PerfectCell;
import net.goo.brutality.item.curios.hands.PhantomFinger;
import net.goo.brutality.item.curios.hands.SuspiciouslyLargeHandle;
import net.goo.brutality.item.curios.heart.BrutalHeart;
import net.goo.brutality.item.curios.heart.FrozenHeart;
import net.goo.brutality.item.curios.heart.RuneOfDelta;
import net.goo.brutality.item.curios.necklace.AbyssalNecklace;
import net.goo.brutality.item.curios.necklace.BlackMatterNecklace;
import net.goo.brutality.item.curios.necklace.BloodHowlPendant;
import net.goo.brutality.item.curios.necklace.KnightsPendant;
import net.goo.brutality.item.curios.ring.RoadRunnersRing;
import net.goo.brutality.item.curios.vanity.TrialGuardianEyebrows;
import net.goo.brutality.item.generic.PrisonKey;
import net.goo.brutality.item.seals.*;
import net.goo.brutality.item.weapon.axe.Deathsaw;
import net.goo.brutality.item.weapon.axe.OldGpuAxe;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.bow.Providence;
import net.goo.brutality.item.weapon.generic.CanopyOfShadowsItem;
import net.goo.brutality.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.item.weapon.generic.LastPrism;
import net.goo.brutality.item.weapon.generic.TheCloud;
import net.goo.brutality.item.weapon.hammer.*;
import net.goo.brutality.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.item.weapon.scythe.Schism;
import net.goo.brutality.item.weapon.spear.Caldrith;
import net.goo.brutality.item.weapon.spear.EventHorizon;
import net.goo.brutality.item.weapon.spear.Rhongomyniad;
import net.goo.brutality.item.weapon.staff.BambooStaff;
import net.goo.brutality.item.weapon.staff.ChopstickStaff;
import net.goo.brutality.item.weapon.sword.*;
import net.goo.brutality.item.weapon.sword.phasesaber.BasePhasesaber;
import net.goo.brutality.item.weapon.throwing.*;
import net.goo.brutality.item.weapon.tome.*;
import net.goo.brutality.item.weapon.trident.DepthCrusherTrident;
import net.goo.brutality.item.weapon.trident.GungnirTrident;
import net.goo.brutality.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.util.helpers.AttributeContainer;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent.ItemDescriptionComponents.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class BrutalityModItems {

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Brutality.MOD_ID);

    public static final RegistryObject<Item> POCKET_BLACK_HOLE = ITEMS.register("pocket_black_hole", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_FREQUENCY_ALLOY = ITEMS.register("high_frequency_alloy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUANTITE_INGOT = ITEMS.register("quantite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> UNBRIDLED_RAGE = ITEMS.register("unbridled_rage", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_SLIME_BALL = ITEMS.register("blue_slime_ball", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PINK_SLIME_BALL = ITEMS.register("pink_slime_ball", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> WATER_COOLER_ITEM = ITEMS.register("water_cooler", () -> new BrutalityBlockItem(BrutalityModBlocks.WATER_COOLER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> COFFEE_MACHINE_ITEM = ITEMS.register("coffee_machine", () -> new BrutalityBlockItem(BrutalityModBlocks.COFFEE_MACHINE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUPER_SNIFFER_FIGURE_ITEM = ITEMS.register("super_sniffer_figure", () -> new BrutalityBlockItem(BrutalityModBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WHITE_FILING_CABINET_ITEM = ITEMS.register("white_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityModBlocks.WHITE_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GRAY_FILING_CABINET_ITEM = ITEMS.register("light_gray_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> GRAY_FILING_CABINET_ITEM = ITEMS.register("gray_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityModBlocks.GRAY_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> IMPORTANT_DOCUMENTS = ITEMS.register("important_documents", () -> new ImportantDocumentsBlockItem(BrutalityModBlocks.IMPORTANT_DOCUMENTS_BLOCK.get(), new Item.Properties()));


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


    public static final RegistryObject<Item> CELESTIAL_STARBOARD = ITEMS.register("celestial_starboard", () -> new CelestialStarboard(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(DASH_ABILITY, 1, 60))));
    public static final RegistryObject<Item> TSUKUYOMI = ITEMS.register("tsukuyomi", () -> new Tsukuyomi(Tiers.NETHERITE, 3, -2.25F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 2), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> AMATERASU = ITEMS.register("amaterasu", () -> new Amaterasu(Tiers.NETHERITE, 3, -2.25F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 2), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> DARKIN_BLADE_SWORD = ITEMS.register("darkin_blade", () -> new DarkinBladeSword(Tiers.NETHERITE, 10, -3.15F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> PRISMATIC_GREATSWORD = ITEMS.register("prismatic_greatsword", () -> new PrismaticGreatsword(Tiers.NETHERITE, 10, -3.2F, ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> WORLD_TREE_SWORD = ITEMS.register("world_tree_sword", () -> new WorldTreeSword(Tiers.NETHERITE, 15, -3.5F, ModRarities.GODLY, List.of(new ItemDescriptionComponent(LORE, 1))));
    public static final RegistryObject<Item> CRIMSON_SCISSOR_BLADE = ITEMS.register("crimson_scissor_blade", () -> new CrimsonScissorBlade(Tiers.NETHERITE, 6, -3F, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(ON_SWING, 2))));
    public static final RegistryObject<Item> RHONGOMYNIAD = ITEMS.register("rhongomyniad", () -> new Rhongomyniad(Tiers.NETHERITE, 6, -3F, ModRarities.DIVINE, List.of(new ItemDescriptionComponent(ON_SWING, 1))));
    public static final RegistryObject<Item> CALDRITH = ITEMS.register("caldrith", () -> new Caldrith(Tiers.NETHERITE, 6, -3F, ModRarities.DIVINE, List.of(new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));
    public static final RegistryObject<Item> SCHISM = ITEMS.register("schism", () -> new Schism(Tiers.NETHERITE, 8, -3.5F, ModRarities.DIVINE, List.of(new ItemDescriptionComponent(ON_SWING, 1))));
    public static final RegistryObject<Item> SHADOWFLAME_SCISSOR_BLADE = ITEMS.register("shadowflame_scissor_blade", () -> new ShadowflameScissorBlade(Tiers.NETHERITE, 6, -3F, ModRarities.NOCTURNAL, List.of(new ItemDescriptionComponent(ON_SWING, 1), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> DARKIN_SCYTHE = ITEMS.register("darkin_scythe", () -> new DarkinScythe(Tiers.NETHERITE, 3, -3F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_HIT, 4), new ItemDescriptionComponent(ON_RIGHT_CLICK, 4), new ItemDescriptionComponent(PASSIVE, 3), new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1))));
    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(Tiers.NETHERITE, 5, -3F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_RIGHT_CLICK, 2), new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2))));
    public static final RegistryObject<Item> DEPTH_CRUSHER_TRIDENT = ITEMS.register("depth_crusher", () -> new DepthCrusherTrident(6, -2.4F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2))));
    public static final RegistryObject<Item> LAST_PRISM_ITEM = ITEMS.register("last_prism", () -> new LastPrism(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1), new ItemDescriptionComponent(MANA_COST, 1))));
    public static final RegistryObject<Item> ROYAL_GUARDIAN_SWORD = ITEMS.register("royal_guardian_sword", () -> new RoyalGuardianSword(Tiers.NETHERITE, 50, -3.5F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> BLADE_OF_THE_RUINED_KING = ITEMS.register("blade_of_the_ruined_king", () -> new BladeOfTheRuinedKingSword(Tiers.DIAMOND, 9, -2.5F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_HIT, 2), new ItemDescriptionComponent(ON_RIGHT_CLICK, 2))));
    public static final RegistryObject<Item> HELLSPEC_CLEAVER = ITEMS.register("hellspec_cleaver", () -> new HellspecCleaver(Tiers.DIAMOND, 4, -3F, ModRarities.STYGIAN));
    public static final RegistryObject<Item> CONDUCTITE_CAPACITOR = ITEMS.register("conductite_capacitor", () -> new ConductiteCapacitor(Tiers.DIAMOND, 4, -3F, ModRarities.CONDUCTIVE, List.of(new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> RHITTA_AXE = ITEMS.register("rhitta", () -> new RhittaAxe(Tiers.DIAMOND, 10, -3F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> DEATHSAW = ITEMS.register("deathsaw", () -> new Deathsaw(Tiers.DIAMOND, 10, -3F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(Tiers.DIAMOND, 0, -2.6F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> DULL_KNIFE_DAGGER = ITEMS.register("dull_knife", () -> new DullKnifeSword(Tiers.NETHERITE, 4, -2F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));
    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(13, -3.2F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(8, -2.1F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(ON_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1), new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> ONYX_PHASESABER = ITEMS.register("onyx_phasesaber", () -> new BasePhasesaber(Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> TOPAZ_PHASESABER = ITEMS.register("topaz_phasesaber", () -> new BasePhasesaber(Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> RUBY_PHASESABER = ITEMS.register("ruby_phasesaber", () -> new BasePhasesaber(Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SAPPHIRE_PHASESABER = ITEMS.register("sapphire_phasesaber", () -> new BasePhasesaber(Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(Tiers.NETHERITE, 5, -3F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_HIT, 1), new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2), new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> OLD_GPU = ITEMS.register("old_gpu", () -> new OldGpuAxe(Tiers.DIAMOND, 23, -2.5F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> SUPERNOVA = ITEMS.register("supernova", () -> new SupernovaSword(Tiers.NETHERITE, 10, -3.1F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> SHADOWSTEP = ITEMS.register("shadowstep", () -> new ShadowstepSword(Tiers.NETHERITE, -2, -2.6F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_RIGHT_CLICK, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadowsItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> WOODEN_RULER = ITEMS.register("wooden_ruler", () -> new WoodenRulerHammer(Tiers.DIAMOND, -1, -3, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))

    ));
    public static final RegistryObject<Item> METAL_RULER = ITEMS.register("metal_ruler", () -> new MetalRulerSword(Tiers.DIAMOND, -1, -3, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> DOUBLE_DOWN = ITEMS.register("double_down", () -> new DoubleDown(Tiers.DIAMOND, 9, -3.2F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 4))));
    public static final RegistryObject<Item> PAPER_CUT = ITEMS.register("paper_cut", () -> new BrutalitySwordItem(Tiers.STONE, 1, -2.4F, Rarity.RARE));

    public static final RegistryObject<Item> WHISPERWALTZ = ITEMS.register("whisperwaltz", () -> new WhisperwaltzSword(Tiers.NETHERITE, -3, 16F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 1)

    )));
    public static final RegistryObject<Item> PROVIDENCE = ITEMS.register("providence", () -> new Providence(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SEVENTH_STAR = ITEMS.register("seventh_star", () -> new SeventhStarSword(Tiers.NETHERITE, 10, -3.15F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_SWING, 1))));

    public static final RegistryObject<Item> CREASE_OF_CREATION = ITEMS.register("crease_of_creation", () -> new CreaseOfCreation(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2))));

    public static final RegistryObject<Item> SPATULA_HAMMER = ITEMS.register("spatula", () -> new SpatulaHammer(Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 3))));
    public static final RegistryObject<Item> THE_GOLDEN_SPATULA_HAMMER = ITEMS.register("the_golden_spatula", () -> new TheGoldenSpatulaHammer(Tiers.NETHERITE, 13, -2.3F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 3))));

    public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife", () -> new IronKnifeSword(Tiers.IRON, 3, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 2))));
    public static final RegistryObject<Item> GOLD_KNIFE = ITEMS.register("gold_knife", () -> new GoldKnifeSword(Tiers.GOLD, 4, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 2))));
    public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new DiamondKnifeSword(Tiers.DIAMOND, 5, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 2))));
    public static final RegistryObject<Item> VOID_KNIFE = ITEMS.register("void_knife", () -> new VoidKnifeSword(Tiers.NETHERITE, 8, -2.3F, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_HIT, 3))));
    public static final RegistryObject<Item> MELONCHOLY_SWORD = ITEMS.register("meloncholy", () -> new MeloncholySword(Tiers.IRON, 6, -2.3F, ModRarities.LEGENDARY));
    public static final RegistryObject<Item> APPLE_CORE_LANCE = ITEMS.register("apple_core", () -> new BrutalitySwordItem(Tiers.IRON, 8, -2.6F, ModRarities.LEGENDARY, new Item.Properties().defaultDurability(100).durability(100)));
    public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan", () -> new FryingPanHammer(Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> CHOPSTICK_STAFF = ITEMS.register("chopstick", () -> new ChopstickStaff(Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BAMBOO_STAFF = ITEMS.register("bamboo_staff", () -> new BambooStaff(Tiers.DIAMOND, 4, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> POTATO_MASHER_HAMMER = ITEMS.register("potato_masher", () -> new PotatoMasherHammer(Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1))));


    public static final RegistryObject<Item> WHISK_HAMMER = ITEMS.register("whisk", () -> new WhiskHammer(Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_HIT, 1))));


    public static final RegistryObject<Item> PHOTON = ITEMS.register("photon", () -> new BrutalityThrowingItem(0, 0, ModRarities.DIVINE, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2)), BrutalityModEntities.PHOTON).initialVelocity(5));

    public static final RegistryObject<Item> POUCH_O_PHOTONS = ITEMS.register("pouch_o_photons", () -> new PouchOPhotons(0, -3, ModRarities.DIVINE, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2)), BrutalityModEntities.PHOTON).initialVelocity(7.5F).inaccuracy(3));

    public static final RegistryObject<Item> VAMPIRE_KNIVES = ITEMS.register("vampire_knives", () -> new VampireKnives(3, 0, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2)), BrutalityModEntities.VAMPIRE_KNIFE).attackType(BrutalityCategories.AttackType.PIERCE).initialVelocity(3));

    public static final RegistryObject<Item> DYNAMITE = ITEMS.register("dynamite", () -> new BrutalityThrowingItem(0, -3, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.DYNAMITE));

    public static final RegistryObject<Item> STICKY_DYNAMITE = ITEMS.register("sticky_dynamite", () -> new BrutalityThrowingItem(0, -3, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.STICKY_DYNAMITE));

    public static final RegistryObject<Item> BOUNCY_DYNAMITE = ITEMS.register("bouncy_dynamite", () -> new BrutalityThrowingItem(0, -3, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.BOUNCY_DYNAMITE));

    public static final RegistryObject<Item> DECK_OF_FATE = ITEMS.register("deck_of_fate", () -> new BrutalityThrowingItem(3, -2.5F, ModRarities.GODLY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 7)), BrutalityModEntities.FATE_CARD).initialVelocity(2).attackType(BrutalityCategories.AttackType.PIERCE));
    public static final RegistryObject<Item> PERFUME_BOTTLE = ITEMS.register("perfume_bottle", () -> new BrutalityThrowingItem(3, -3.25F, ModRarities.GODLY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2)), BrutalityModEntities.PERFUME_BOTTLE));

    public static final RegistryObject<Item> ABSOLUTE_ZERO = ITEMS.register("absolute_zero", () -> new BrutalityThrowingItem(3, -3.25F, ModRarities.GLACIAL, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.ABSOLUTE_ZERO));

    public static final RegistryObject<Item> CRIMSON_DELIGHT = ITEMS.register("crimson_delight", () -> new BrutalityThrowingItem(2, -1.5F, Rarity.EPIC, BrutalityModEntities.CRIMSON_DELIGHT));

    public static final RegistryObject<Item> CANNONBALL_CABBAGE = ITEMS.register("cannonball_cabbage", () -> new BrutalityThrowingItem(5, -2.2F, Rarity.EPIC, BrutalityModEntities.CANNONBALL_CABBAGE));

    public static final RegistryObject<Item> CAVENDISH = ITEMS.register("cavendish", () -> new BrutalityThrowingItem(2, -2.2F, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.CANNONBALL_CABBAGE));


    public static final RegistryObject<Item> STICK_OF_BUTTER = ITEMS.register("stick_of_butter", () -> new BrutalityThrowingItem(0, -3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.STICK_OF_BUTTER));

    public static final RegistryObject<Item> WINTER_MELON = ITEMS.register("winter_melon", () -> new BrutalityThrowingItem(7, -2.75F, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.WINTER_MELON));

    public static final RegistryObject<Item> GOLDEN_PHOENIX = ITEMS.register("golden_phoenix", () -> new BrutalityThrowingItem(9, -2.65F, Rarity.EPIC, BrutalityModEntities.GOLDEN_PHOENIX));

    public static final RegistryObject<Item> STICKY_BOMB = ITEMS.register("sticky_bomb", () -> new StickyBomb(0, -3.4F, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1), new ItemDescriptionComponent(ON_RIGHT_CLICK, 1)), BrutalityModEntities.STICKY_BOMB));

    public static final RegistryObject<Item> BIOMECH_REACTOR = ITEMS.register("biomech_reactor", () -> new BrutalityThrowingItem(20, -3, ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1), new ItemDescriptionComponent(ON_HIT, 2)), BrutalityModEntities.BIOMECH_REACTOR).initialVelocity(0.75F));


    public static final RegistryObject<Item> ICE_CUBE = ITEMS.register("ice_cube", () -> new BrutalityThrowingItem(3, -3F, Rarity.EPIC, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.ICE_CUBE));

    public static final RegistryObject<Item> PERMAFROST_CUBE = ITEMS.register("permafrost_cube", () -> new BrutalityThrowingItem(6, -3F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.PERMAFROST_CUBE));

    public static final RegistryObject<Item> CINDER_BLOCK = ITEMS.register("cinder_block", () -> new BrutalityThrowingItem(10, -3.2F, ModRarities.LEGENDARY, BrutalityModEntities.CINDER_BLOCK));

    public static final RegistryObject<Item> STYROFOAM_CUP = ITEMS.register("styrofoam_cup", () -> new StyrofoamCup(1, -3.2F, Rarity.COMMON, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.STYROFOAM_CUP, BrutalityModBlocks.STYROFOAM_CUP.get()));

    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new Mug(1, -3.2F, Rarity.COMMON, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 2), new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.MUG, BrutalityModBlocks.MUG.get()));

    public static final RegistryObject<Item> OVERCLOCKED_TOASTER = ITEMS.register("overclocked_toaster", () -> new BrutalityThrowingItem(10, -3.5F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.OVERCLOCKED_TOASTER));


    public static final RegistryObject<Item> SCULK_GRENADE = ITEMS.register("sculk_grenade", () -> new BrutalityThrowingItem(10, -3F, ModRarities.GLOOMY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 2)), BrutalityModEntities.SCULK_GRENADE).initialVelocity(1.25F));

    public static final RegistryObject<Item> BEACH_BALL = ITEMS.register("beach_ball", () -> new BrutalityThrowingItem(10, -3F, ModRarities.LEGENDARY, BrutalityModEntities.BEACH_BALL).initialVelocity(0.3F));

    public static final RegistryObject<Item> BLAST_BARREL = ITEMS.register("blast_barrel", () -> new BrutalityThrowingItem(10, -3.4F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.BLAST_BARREL).initialVelocity(0.75F));

    public static final RegistryObject<Item> HOLY_HAND_GRENADE = ITEMS.register("holy_hand_grenade", () -> new BrutalityThrowingItem(10, -3.4F, ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(WHEN_THROWN, 1)), BrutalityModEntities.HOLY_HAND_GRENADE));


    public static final RegistryObject<Item> DAEMONIC_TOME = ITEMS.register("daemonic_tome", () -> new DaemonicTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> DARKIST_TOME = ITEMS.register("darkist_tome", () -> new DarkistTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> EVERGREEN_TOME = ITEMS.register("evergreen_tome", () -> new EvergreenTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> COSMIC_TOME = ITEMS.register("cosmic_tome", () -> new CosmicTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> UMBRAL_TOME = ITEMS.register("umbral_tome", () -> new UmbralTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> VOID_TOME = ITEMS.register("void_tome", () -> new VoidTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> EXODIC_TOME = ITEMS.register("exodic_tome", () -> new ExodicTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> CELESTIA_TOME = ITEMS.register("celestia_tome", () -> new CelestiaTome(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> BRIMWIELDER_TOME = ITEMS.register("brimwielder_tome", () -> new BrimwielderTome(ModRarities.LEGENDARY));


    public static final RegistryObject<Item> PLUNDER_CHEST = ITEMS.register("plunder_chest", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1, 100))));
    public static final RegistryObject<Item> WOOLY_BLINDFOLD = ITEMS.register("wooly_blindfold", () -> new BrutalityCurioItem(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> SERAPHIM_HALO = ITEMS.register("seraphim_halo", () -> new BrutalityCurioItem(ModRarities.LEGENDARY) {
        @Override
        public boolean followHeadRotations() {
            return false;
        }

        @Override
        public boolean translateIfSneaking() {
            return false;
        }
    });

    public static final RegistryObject<Item> GOOD_BOOK = ITEMS.register("good_book", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> GOLDEN_HEADBAND = ITEMS.register("golden_headband", () -> new BrutalityCurioItem(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> YATA_NO_KAGAMI = ITEMS.register("yata_no_kagami", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> GREED = ITEMS.register("greed", () -> new Greed(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> PRIDE = ITEMS.register("pride", () -> new Pride(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> WRATH = ITEMS.register("wrath", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 50, ADDITION), new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), 0.25, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.25, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> SLOTH = ITEMS.register("sloth", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> GLUTTONY = ITEMS.register("gluttony", () -> new Gluttony(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> LUST = ITEMS.register("lust", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ENVY = ITEMS.register("envy", () -> new Envy(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ENERGY_FOCUSER = ITEMS.register("energy_focuser", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BRUTESKIN_BELT = ITEMS.register("bruteskin_belt", () -> new BrutalityCurioItem(ModRarities.MYTHIC).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, -0.5, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.5, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.2, MULTIPLY_BASE)));
    public static final RegistryObject<Item> CRITICAL_THINKING = ITEMS.register("critical_thinking", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> DEADSHOT_BROOCH = ITEMS.register("deadshot_brooch", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> HELLSPEC_TIE = ITEMS.register("hellspec_tie", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SOUL_STONE = ITEMS.register("soul_stone", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> EMERGENCY_FLASK = ITEMS.register("emergency_flask", () -> new EmergencyFlask(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> RING_OF_MANA = ITEMS.register("ring_of_mana", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 40, ADDITION)));
    public static final RegistryObject<Item> RING_OF_MANA_PLUS = ITEMS.register("ring_of_mana_plus", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 60, ADDITION)));
    public static final RegistryObject<Item> CONSERVATIVE_CONCOCTION = ITEMS.register("conservative_concoction", () -> new ConservativeConcoction(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> MICROBLADE_BAND = ITEMS.register("microblade_band", () -> new BrutalityCurioItem(ModRarities.DARK, List.of(new ItemDescriptionComponent(LORE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.LETHALITY.get(), 1, ADDITION)));
    public static final RegistryObject<Item> RUNE_OF_DELTA = ITEMS.register("rune_of_delta", () -> new RuneOfDelta(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ECHO_CHAMBER = ITEMS.register("echo_chamber", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ONYX_IDOL = ITEMS.register("onyx_idol", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(String.format("%.2f", pStack.getOrCreateTag().getFloat("mana")) + " / 200").withStyle(ChatFormatting.BLUE));
        }
    });
    public static final RegistryObject<Item> SCRIBES_INDEX = ITEMS.register("scribes_index", () -> new ScribesIndex(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BLACK_HOLE_ORB = ITEMS.register("black_hole_orb", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), -0.9, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.MANA_REGEN.get(), 10, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> FORBIDDEN_ORB = ITEMS.register("forbidden_orb", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 2, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.MANA_COST.get(), 0.5, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.MANA_REGEN.get(), 0.5, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> BLOOD_ORB = ITEMS.register("blood_orb", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> CROWN_OF_TYRANNY = ITEMS.register("crown_of_tyranny", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> PORTABLE_QUANTUM_THINGAMABOB = ITEMS.register("portable_quantum_thingamabob", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HIT, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.LETHALITY.get(), 10, ADDITION)));
    public static final RegistryObject<Item> WARPSLICE_SCABBARD = ITEMS.register("warpslice_scabbard", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(BrutalityModAttributes.LETHALITY.get(), 7, ADDITION), new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.2, MULTIPLY_BASE)));
    public static final RegistryObject<Item> PROFANUM_REACTOR = ITEMS.register("profanum_reactor", () -> new ProfanumReactor(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> FURY_BATTERY = ITEMS.register("fury_battery", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 85, ADDITION)));
    public static final RegistryObject<Item> DAEMONIUM_WHETSTONE = ITEMS.register("daemonium_whetstone", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.13, MULTIPLY_BASE)));
    public static final RegistryObject<Item> KNIGHTS_PENDANT = ITEMS.register("knights_pendant", () -> new KnightsPendant(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> THE_OATH = ITEMS.register("the_oath", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.BLUNT_DAMAGE.get(), 0.15, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.CELESTIA_SCHOOL_LEVEL.get(), 1, ADDITION)));
    public static final RegistryObject<Item> LUCKY_INSOLES = ITEMS.register("lucky_insoles", () -> new LuckyInsoles(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SANGUINE_SIGNET = ITEMS.register("sanguine_signet", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, 2, ADDITION), new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.025, MULTIPLY_BASE)));
    public static final RegistryObject<Item> PHANTOM_FINGER = ITEMS.register("phantom_finger", () -> new PhantomFinger(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(ForgeMod.ENTITY_REACH.get(), 2, ADDITION), new AttributeContainer(ForgeMod.BLOCK_REACH.get(), 2, ADDITION)));
    public static final RegistryObject<Item> STYGIAN_CHAIN = ITEMS.register("stygian_chain", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(Attributes.ARMOR, -6, ADDITION), new AttributeContainer(Attributes.ARMOR_TOUGHNESS, -4, ADDITION), new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> SILVER_RESPAWN_CARD = ITEMS.register("silver_respawn_card", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> DIAMOND_RESPAWN_CARD = ITEMS.register("diamond_respawn_card", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 1, 30 * 60 * 20))));
    public static final RegistryObject<Item> EVIL_KING_RESPAWN_CARD = ITEMS.register("evil_king_respawn_card", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 1, 15 * 60 * 20))));
    public static final RegistryObject<Item> SILVER_BOOSTER_PACK = ITEMS.register("silver_booster_pack", () -> new SilverBoosterPack(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> DIAMOND_BOOSTER_PACK = ITEMS.register("diamond_booster_pack", () -> new DiamondBoosterPack(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 2, 30 * 60 * 20))));
    public static final RegistryObject<Item> EVIL_KING_BOOSTER_PACK = ITEMS.register("evil_king_booster_pack", () -> new EvilKingBoosterPack(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 2, 15 * 60 * 20))));
    public static final RegistryObject<Item> HEMOMATIC_LOCKET = ITEMS.register("hemomatic_locket", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.025, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BOILING_BLOOD = ITEMS.register("boiling_blood", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.6, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BEAD_OF_LIFE = ITEMS.register("bead_of_life", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> DAEMONIUM_SEWING_KIT = ITEMS.register("daemonium_sewing_kit", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.LETHALITY.get(), 3, ADDITION)));
    public static final RegistryObject<Item> GAMBLERS_CHAIN = ITEMS.register("gamblers_chain", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.LUCK, 1.5, ADDITION)));
    public static final RegistryObject<Item> PINCUSHION = ITEMS.register("pincushion", () -> new Pincushion(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> OLD_GUILLOTINE = ITEMS.register("old_guillotine", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> KNUCKLE_WRAPS = ITEMS.register("knuckle_wraps", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> MANA_SYRINGE = ITEMS.register("mana_syringe", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_HIT, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 20, ADDITION)));
    public static final RegistryObject<Item> PRISMATIC_ORB = ITEMS.register("prismatic_orb", () -> new BrutalityCurioItem(ModRarities.PRISMATIC).withAttributes(new AttributeContainer(BrutalityModAttributes.SPELL_COOLDOWN_REDUCTION.get(), 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> SUSPICIOUSLY_LARGE_HANDLE = ITEMS.register("suspiciously_large_handle", () -> new SuspiciouslyLargeHandle(ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> SAD_UVOGRE = ITEMS.register("sad_uvogre", () -> new BrutalityCurioItem(ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.BLUNT_DAMAGE.get(), 0.35, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> SOLDIERS_SYRINGE = ITEMS.register("soldiers_syringe", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.ATTACK_SPEED, 0.2, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> RAGE_BAIT = ITEMS.register("rage_bait", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.RAGE_LEVEL.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.25, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> TARGET_CUBE = ITEMS.register("target_cube", () -> new TargetCube(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BLOOD_PACK = ITEMS.register("blood_pack", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, -3, ADDITION), new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.125, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BROKEN_CLOCK = ITEMS.register("broken_clock", () -> new BrokenClock(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SHATTERED_CLOCK = ITEMS.register("shattered_clock", () -> new BrokenClock(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SUNDERED_CLOCK = ITEMS.register("sundered_clock", () -> new BrokenClock(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> TIMEKEEPERS_CLOCK = ITEMS.register("timekeepers_clock", () -> new BrokenClock(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> THE_CLOCK_OF_FROZEN_TIME = ITEMS.register("the_clock_of_frozen_time", () -> new BrokenClock(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 3))));
    public static final RegistryObject<Item> WIRE_CUTTERS = ITEMS.register("wire_cutters", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.05, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> AIR_JORDAN_EARRINGS = ITEMS.register("air_jordan_earrings", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.JUMP_HEIGHT.get(), 0.5, MULTIPLY_TOTAL), new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.4, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> HELL_SPECS = ITEMS.register("hell_specs", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(), 1, ADDITION)));
    public static final RegistryObject<Item> SCOPE_GOGGLES = ITEMS.register("scope_goggles", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.2, MULTIPLY_BASE)));
    public static final RegistryObject<Item> LENS_MAKERS_GLASSES = ITEMS.register("lens_makers_glasses", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> JURY_NULLIFIER = ITEMS.register("jury_nullifier", () -> new BrutalityCurioItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), -1.5, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.25, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> EYE_OF_THE_DRAGON = ITEMS.register("eye_of_the_dragon", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.01, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.LETHALITY.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.025, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.025, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.SWORD_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.SPEAR_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.AXE_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.SCYTHE_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.HAMMER_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.PIERCING_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.SLASH_DAMAGE.get(), 1, ADDITION), new AttributeContainer(BrutalityModAttributes.SLASH_DAMAGE.get(), 1, ADDITION)));
    public static final RegistryObject<Item> BRUTAL_HEART = ITEMS.register("brutal_heart", () -> new BrutalHeart(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> NINJA_HEART = ITEMS.register("ninja_heart", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.SLASH_DAMAGE.get(), 0.2, MULTIPLY_TOTAL), new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.2, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.ENTITY_VISIBILITY.get(), -0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> ENDER_DRAGON_STEM_CELLS = ITEMS.register("ender_dragon_stem_cells", () -> new BrutalityCurioItem(ModRarities.PRISMATIC).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 100, ADDITION), new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), 1, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.AXE_DAMAGE.get(), 3, ADDITION), new AttributeContainer(BrutalityModAttributes.SWORD_DAMAGE.get(), 0.5, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> DECK_OF_CARDS = ITEMS.register("deck_of_cards", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ACTIVE, 1, 420, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))).withAttributes(new AttributeContainer(BrutalityModAttributes.SPELL_DAMAGE.get(), 0.13, MULTIPLY_BASE)));
    public static final RegistryObject<Item> VINDICATOR_STEROIDS = ITEMS.register("vindicator_steroids", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ACTIVE, 1, 45 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));
    public static final RegistryObject<Item> STRESS_PILLS = ITEMS.register("stress_pills", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(ACTIVE, 1, 60 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))) {
        @Override
        public boolean canEquip(SlotContext slotContext, ItemStack stack) {
            return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler -> !handler.isEquipped(BrutalityModItems.SEROTONIN_PILLS.get())).orElse(true);
        }
    });
    public static final RegistryObject<Item> SEROTONIN_PILLS = ITEMS.register("serotonin_pills", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(ACTIVE, 2, 60 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))) {
        @Override
        public boolean canEquip(SlotContext slotContext, ItemStack stack) {
            return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler -> !handler.isEquipped(BrutalityModItems.STRESS_PILLS.get())).orElse(true);
        }
    });
    public static final RegistryObject<Item> DAVYS_ANKLET = ITEMS.register("davys_anklet", () -> new DavysAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> EMPTY_ANKLET = ITEMS.register("empty_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.05, MULTIPLY_BASE)));
    public static final RegistryObject<Item> ANKLET_OF_THE_IMPRISONED = ITEMS.register("anklet_of_the_imprisoned", () -> new AnkletOfTheImprisoned(ModRarities.CATACLYSMIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> SHARPNESS_ANKLET = ITEMS.register("sharpness_anklet", () -> new BrutalityAnkletItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), -0.05, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_DAMAGE, 3, ADDITION), new AttributeContainer(BrutalityModAttributes.PIERCING_DAMAGE.get(), 0.2, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> DEBUG_ANKLET = ITEMS.register("debug_anklet", () -> new DebugAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.125, MULTIPLY_BASE)));
    public static final RegistryObject<Item> REDSTONE_ANKLET = ITEMS.register("redstone_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.125, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_DAMAGE, 2, ADDITION)));
    public static final RegistryObject<Item> DEVILS_ANKLET = ITEMS.register("devils_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.HEXED.get(), 20));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.25, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BASKETBALL_ANKLET = ITEMS.register("basketball_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.167, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.JUMP_HEIGHT.get(), 2, ADDITION)));
    public static final RegistryObject<Item> EMERALD_ANKLET = ITEMS.register("emerald_anklet", () -> new EmeraldAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 200))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> RUBY_ANKLET = ITEMS.register("ruby_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> TOPAZ_ANKLET = ITEMS.register("topaz_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> SAPPHIRE_ANKLET = ITEMS.register("sapphire_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> ONYX_ANKLET = ITEMS.register("onyx_anklet", () -> new OnyxAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 60))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));
    public static final RegistryObject<Item> ULTRA_DODGE_ANKLET = ITEMS.register("ultra_dodge_anklet", () -> new UltraDodgeAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.25, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.25, MULTIPLY_BASE)));
    public static final RegistryObject<Item> GUNDALFS_ANKLET = ITEMS.register("gundalfs_anklet", () -> new GundalfsAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 40))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> TRIAL_ANKLET = ITEMS.register("trial_anklet", () -> new BrutalityAnkletItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
            dodger.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_KNOCKBACK, 1, ADDITION)));

    public static final RegistryObject<Item> SUPER_DODGE_ANKLET = ITEMS.register("super_dodge_anklet", () -> new BrutalityAnkletItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.GIANT_SNIFFERS_HOOF_ACTIVE_ABILITY.get(), 60));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.275, MULTIPLY_BASE)));

    public static final RegistryObject<Item> GNOME_KINGS_ANKLET = ITEMS.register("gnome_kings_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 2));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_SPEED, 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> ANKLENT = ITEMS.register("anklent", () -> new Anklent(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ANKLE_MONITOR = ITEMS.register("ankle_monitor", () -> new AnkleMonitor(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 3))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.35, MULTIPLY_BASE)));

    public static final RegistryObject<Item> FIERY_ANKLET = ITEMS.register("fiery_anklet", () -> new BrutalityAnkletItem(ModRarities.FIRE, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            if (source.getEntity() != null) source.getEntity().setSecondsOnFire(2);
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> SACRED_SPEED_ANKLET = ITEMS.register("sacred_speed_anklet", () -> new BrutalityAnkletItem(ModRarities.DIVINE, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 4));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> CONDUCTITE_ANKLET = ITEMS.register("conductite_anklet", () -> new BrutalityAnkletItem(ModRarities.CONDUCTIVE, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.AMPED.get(), 60, 0));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> BLOOD_CLOT_ANKLET = ITEMS.register("blood_clot_anklet", () -> new BrutalityAnkletItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.025, MULTIPLY_BASE)));
    public static final RegistryObject<Item> VIRENTIUM_ANKLET = ITEMS.register("virentium_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(ForgeMod.SWIM_SPEED.get(), 1, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> COSMIC_ANKLET = ITEMS.register("cosmic_anklet", () -> new BrutalityAnkletItem(ModRarities.MYTHIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.5, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> NYXIUM_ANKLET = ITEMS.register("nyxium_anklet", () -> new BrutalityAnkletItem(ModRarities.NOCTURNAL, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(BrutalityModMobEffects.FRUGAL_MANA.get(), 80, 9, false, false));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 50, ADDITION), new AttributeContainer(BrutalityModAttributes.MANA_REGEN.get(), 0.15, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> VOID_ANKLET = ITEMS.register("void_anklet", () -> new BrutalityAnkletItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 80, 0, false, false));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.25, MULTIPLY_BASE)));


    public static final RegistryObject<Item> IRONCLAD_ANKLET = ITEMS.register("ironclad_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, false, false));
        }
    }.withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE), new AttributeContainer(Attributes.MOVEMENT_SPEED, -0.05, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ARMOR, 0.1, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> GLADIATORS_ANKLET = ITEMS.register("gladiators_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.05, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.SLASH_DAMAGE.get(), 3.5, ADDITION), new AttributeContainer(BrutalityModAttributes.PIERCING_DAMAGE.get(), 2.5, ADDITION), new AttributeContainer(BrutalityModAttributes.BLUNT_DAMAGE.get(), -4, ADDITION)));
    public static final RegistryObject<Item> EXODIUM_ANKLET = ITEMS.register("exodium_anklet", () -> new ExodiumAnklet(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BIG_STEPPA = ITEMS.register("big_steppa", () -> new BrutalityAnkletItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.1, MULTIPLY_BASE), new AttributeContainer(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5, ADDITION)));
    public static final RegistryObject<Item> WINDSWEPT_ANKLET = ITEMS.register("windswept_anklet", () -> new BrutalityAnkletItem(Rarity.EPIC).withAttributes(new AttributeContainer(BrutalityModAttributes.DODGE_CHANCE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.JUMP_HEIGHT.get(), 0.25, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> HYPERBOLIC_FEATHER = ITEMS.register("hyperbolic_feather", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1, 80))));
    public static final RegistryObject<Item> CARTON_OF_PRISM_SOLUTION_MILK = ITEMS.register("carton_of_prism_solution_milk", () -> new CartonOfPrismSolutionMilk(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, -0.4, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> TRIAL_GUARDIAN_EYEBROWS = ITEMS.register("trial_guardian_eyebrows", () -> new TrialGuardianEyebrows(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> TRIAL_GUARDIAN_HANDS = ITEMS.register("trial_guardian_hands", () -> new BrutalityCurioItem(ModRarities.LEGENDARY));
    public static final RegistryObject<Item> BLOOD_CHALICE = ITEMS.register("blood_chalice", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.OMNIVAMP.get(), 0.05, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BLACK_MATTER_NECKLACE = ITEMS.register("black_matter_necklace", () -> new BlackMatterNecklace(ModRarities.DARK, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SELF_REPAIR_NEXUS = ITEMS.register("self_repair_nexus", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.05, MULTIPLY_BASE), new AttributeContainer(Attributes.MAX_HEALTH, 4, ADDITION)));
    public static final RegistryObject<Item> VAMPIRIC_TALISMAN = ITEMS.register("vampiric_talisman", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1, 80))).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.05, MULTIPLY_BASE)));
    public static final RegistryObject<Item> HEMOGRAFT_NEEDLE = ITEMS.register("hemograft_needle", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.05, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> SANGUINE_SPECTACLES = ITEMS.register("sanguine_spectacles", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.OMNIVAMP.get(), 0.025, MULTIPLY_BASE)));
    public static final RegistryObject<Item> VAMPIRE_FANG = ITEMS.register("vampire_fang", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.05, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.MAX_HEALTH, -0.2, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> PROGENITORS_EARRINGS = ITEMS.register("progenitors_earrings", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.ARMOR, -0.25, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> FALLEN_ANGELS_HALO = ITEMS.register("fallen_angels_halo", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.075, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.SCYTHE_DAMAGE.get(), 0.25, MULTIPLY_BASE)));
    public static final RegistryObject<Item> BLOODSTAINED_MIRROR = ITEMS.register("bloodstained_mirror", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.075, MULTIPLY_BASE), new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.15, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> INCOGNITO_MODE = ITEMS.register("incognito_mode", () -> new BrutalityCurioItem(ModRarities.DARK).withAttributes(new AttributeContainer(BrutalityModAttributes.ENTITY_VISIBILITY.get(), -0.65F, MULTIPLY_BASE)));

    public static final RegistryObject<Item> MINIATURE_ANCHOR = ITEMS.register("miniature_anchor", () -> new MiniatureAnchor(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.25, MULTIPLY_TOTAL), new AttributeContainer(ForgeMod.SWIM_SPEED.get(), -0.9F, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> PAPER_AIRPLANE = ITEMS.register("paper_airplane", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PACK_OF_CIGARETTES = ITEMS.register("pack_of_cigarettes", () -> new BrutalityCurioItem(ModRarities.DARK).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), -0.5, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.DAMAGE_TAKEN.get(), -0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> FIRE_EXTINGUISHER = ITEMS.register("fire_extinguisher", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> LUCKY_BOOKMARK = ITEMS.register("lucky_bookmark", () -> new LuckyBookmark(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> EMERGENCY_MEETING = ITEMS.register("emergency_meeting", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ACTIVE, 1, 20 * 60, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> PENCIL_SHARPENER = ITEMS.register("pencil_sharpener", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.PIERCING_DAMAGE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> CHOCOLATE_BAR = ITEMS.register("chocolate_bar", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.15, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_SPEED, 0.15, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> PORTABLE_MINING_RIG = ITEMS.register("portable_mining_rig", () -> new PortableMiningRig(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> DUMBBELL = ITEMS.register("dumbbell", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.BLUNT_DAMAGE.get(), 0.2, MULTIPLY_TOTAL), new AttributeContainer(Attributes.MOVEMENT_SPEED, -0.05, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_SPEED, -0.05, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> BOX_OF_CHOCOLATES = ITEMS.register("box_of_chocolates", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL), new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.ENTITY_VISIBILITY.get(), -0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> BROKEN_HEART = ITEMS.register("broken_heart", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, -3, ADDITION), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.TENACITY.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> ESCAPE_KEY = ITEMS.register("escape_key", () -> new EscapeKey(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> MIRACLE_CURE = ITEMS.register("miracle_cure", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ACTIVE, 1, 20 * 60))));

    public static final RegistryObject<Item> ESSENTIAL_OILS = ITEMS.register("essential_oils", () -> new EssentialOils(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> GLASS_HEART = ITEMS.register("glass_heart", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(Attributes.ARMOR, -1, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.5, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> CRYPTO_WALLET = ITEMS.register("crypto_wallet", () -> new CryptoWallet(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> DIVINE_IMMOLATION = ITEMS.register("divine_immolation", () -> new BrutalityCurioItem(ModRarities.FIRE).withAttributes(new AttributeContainer(BrutalityModAttributes.CRITICAL_STRIKE_CHANCE.get(), 1, MULTIPLY_BASE), new AttributeContainer(Attributes.MAX_HEALTH, -0.99, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_DAMAGE, 1, MULTIPLY_TOTAL), new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.35, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> LIGHT_SWITCH = ITEMS.register("light_switch", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ACTIVE, 1, 400, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));


    public static final RegistryObject<Item> FUZZY_DICE = ITEMS.register("fuzzy_dice", () -> new BrutalityCurioItem(ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SOLAR_SYSTEM = ITEMS.register("solar_system", () -> new BrutalityCurioItem(ModRarities.LEGENDARY) {
        @Override
        public boolean followBodyRotations() {
            return false;
        }

        @Override
        public boolean translateIfSneaking() {
            return false;
        }
    });


    public static final RegistryObject<Item> NANOMACHINES = ITEMS.register("nanomachines", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> DUELING_GLOVE = ITEMS.register("dueling_glove", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> DRAGONHEART = ITEMS.register("dragonheart", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0) {
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 41, 2));
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 41, 0));
            }
        }
    });

    public static final RegistryObject<Item> UVOGRE_HEART = ITEMS.register("uvogre_heart", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 10 == 0 && slotContext.entity().getHealth() < slotContext.entity().getMaxHealth() / 4)
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 11, 3));
        }
    });

    public static final RegistryObject<Item> ZOMBIE_HEART = ITEMS.register("zombie_heart", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> FROZEN_HEART = ITEMS.register("frozen_heart", () -> new FrozenHeart(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SECOND_HEART = ITEMS.register("second_heart", () -> new BrutalityCurioItem(ModRarities.MYTHIC).withAttributes(new AttributeContainer(Attributes.MAX_HEALTH, 12, ADDITION)));

    public static final RegistryObject<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold", () -> new BrutalityCurioItem(ModRarities.MYTHIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BRAIN_ROT = ITEMS.register("brain_rot", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ROAD_RUNNERS_RING = ITEMS.register("road_runners_ring", () -> new RoadRunnersRing(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> RESPLENDENT_FEATHER = ITEMS.register("resplendent_feather", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            slotContext.entity().heal(slotContext.entity().level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT.selector(Entity::isOnFire), slotContext.entity(), slotContext.entity().getBoundingBox().inflate(7)).size() * 0.15F);
        }
    });

    public static final RegistryObject<Item> ABYSSAL_NECKLACE = ITEMS.register("abyssal_necklace", () -> new AbyssalNecklace(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> THE_CLOUD = ITEMS.register("the_cloud", () -> new TheCloud(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1), new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));
    public static final RegistryObject<Item> PRISON_KEY = ITEMS.register("prison_key", () -> new PrisonKey(Rarity.EPIC, List.of(new ItemDescriptionComponent(ON_LEFT_CLICKING_ENTITY, 1))));

    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", () -> new Handcuffs(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 3))));


    public static final RegistryObject<Item> EVENT_HORIZON = ITEMS.register("event_horizon", () -> new EventHorizon(Tiers.NETHERITE, 17, -3.1F, ModRarities.FABLED, List.of(new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2), new ItemDescriptionComponent(ON_HIT, 1))));
    public static final RegistryObject<Item> PUREBLOOD = ITEMS.register("pureblood", () -> new Pureblood(Tiers.NETHERITE, 13, -3.25F, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(BrutalityArmorMaterials.NOIR, ArmorItem.Type.HELMET, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(BrutalityArmorMaterials.NOIR, ArmorItem.Type.CHESTPLATE, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(BrutalityArmorMaterials.NOIR, ArmorItem.Type.LEGGINGS, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(BrutalityArmorMaterials.NOIR, ArmorItem.Type.BOOTS, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> VAMPIRE_LORD_HELMET = ITEMS.register("vampire_lord_crown", () -> new VampireLordArmorItem(BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.HELMET, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 3), new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));
    public static final RegistryObject<Item> VAMPIRE_LORD_CHESTPLATE = ITEMS.register("vampire_lord_chestplate", () -> new VampireLordArmorItem(BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.CHESTPLATE, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 3), new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));
    public static final RegistryObject<Item> VAMPIRE_LORD_LEGGINGS = ITEMS.register("vampire_lord_leggings", () -> new VampireLordArmorItem(BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.LEGGINGS, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 3), new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));
    public static final RegistryObject<Item> VAMPIRE_LORD_BOOTS = ITEMS.register("vampire_lord_boots", () -> new VampireLordArmorItem(BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.BOOTS, ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 3), new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));


    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(BrutalityArmorMaterials.TERRA, ArmorItem.Type.HELMET, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(BrutalityArmorMaterials.TERRA, ArmorItem.Type.CHESTPLATE, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(BrutalityArmorMaterials.TERRA, ArmorItem.Type.LEGGINGS, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));
    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(BrutalityArmorMaterials.TERRA, ArmorItem.Type.BOOTS, ModRarities.FABLED, List.of(new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> FRIDGE = ITEMS.register("fridge", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SMART_FRIDGE = ITEMS.register("smart_fridge", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SALT_SHAKER = ITEMS.register("salt_shaker", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> PEPPER_SHAKER = ITEMS.register("pepper_shaker", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SALT_AND_PEPPER = ITEMS.register("salt_and_pepper", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BAMBOO_STEAMER = ITEMS.register("bamboo_steamer", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SMOKE_STONE = ITEMS.register("smoke_stone", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> THE_SMOKEHOUSE = ITEMS.register("the_smokehouse", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SUGAR_GLAZE = ITEMS.register("sugar_glaze", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> RAINBOW_SPRINKLES = ITEMS.register("rainbow_sprinkles", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ROCK_CANDY_RING = ITEMS.register("rock_candy_ring", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> CARAMEL_CRUNCH_MEDALLION = ITEMS.register("caramel_crunch_medallion", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> DUNKED_DONUT = ITEMS.register("dunked_donut", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> LOLLIPOP_OF_ETERNITY = ITEMS.register("lollipop_of_eternity", () -> new BrutalityCurioItem(ModRarities.GODLY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> ICE_CREAM_SANDWICH = ITEMS.register("ice_cream_sandwich", () -> new BrutalityCurioItem(ModRarities.GLACIAL, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> CROWN_OF_DOMINATION = ITEMS.register("crown_of_domination", () -> new BrutalityCurioItem(ModRarities.FABLED, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> FACE_PIE = ITEMS.register("face_pie", () -> new BrutalityCurioItem(ModRarities.GLACIAL).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> AQUA_RULER = ITEMS.register("aqua_ruler", () -> new BrutalityCurioItem(ModRarities.DIVINE).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_MANA.get(), 100, ADDITION), new AttributeContainer(BrutalityModAttributes.SPELL_DAMAGE.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.CAST_TIME_REDUCTION.get(), 0.15, MULTIPLY_BASE), new AttributeContainer(ForgeMod.SWIM_SPEED.get(), 1, MULTIPLY_TOTAL)));
    public static final RegistryObject<Item> OMNIDIRECTIONAL_MOVEMENT_GEAR = ITEMS.register("omnidirectional_movement_gear", () -> new BrutalityCurioItem(ModRarities.GODLY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> QUANTUM_LUBRICANT = ITEMS.register("quantum_lubricant", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(BrutalityModAttributes.GROUND_FRICTION.get(), -0.5, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.ARMOR_PENETRATION.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> AEROPHOBIC_NANOCOATING = ITEMS.register("aerophobic_nanocoating", () -> new BrutalityCurioItem(ModRarities.DARK).withAttributes(new AttributeContainer(BrutalityModAttributes.AIR_FRICTION.get(), -0.5, MULTIPLY_BASE), new AttributeContainer(Attributes.ARMOR, 4, ADDITION), new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 2, ADDITION)));
    public static final RegistryObject<Item> INERTIA_BOOSTER = ITEMS.register("inertia_booster", () -> new BrutalityCurioItem(ModRarities.CONDUCTIVE).withAttributes(new AttributeContainer(BrutalityModAttributes.GROUND_FRICTION.get(), -0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> ELBOW_GREASE = ITEMS.register("elbow_grease", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.AIR_FRICTION.get(), -0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.ATTACK_SPEED, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> EARTHEN_BLESSING = ITEMS.register("earthen_blessing", () -> new BrutalityCurioItem(ModRarities.DARK).withAttributes(new AttributeContainer(BrutalityModAttributes.GROUND_FRICTION.get(), 0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.25, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.BLUNT_DAMAGE.get(), 0.1, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.HAMMER_DAMAGE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> PETROLEUM_JELLY = ITEMS.register("petroleum_jelly", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.GROUND_FRICTION.get(), -0.25, MULTIPLY_BASE), new AttributeContainer(Attributes.MAX_HEALTH, 3, ADDITION)));


    public static final RegistryObject<Item> ZEPHYR_IN_A_BOTTLE = ITEMS.register("zephyr_in_a_bottle", () -> new BrutalityCurioItem(ModRarities.LEGENDARY).withAttributes(new AttributeContainer(BrutalityModAttributes.AIR_FRICTION.get(), -0.25, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.JUMP_HEIGHT.get(), 0.5, ADDITION), new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.5, MULTIPLY_TOTAL), new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.1, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> PORTABLE_TRAMPOLINE = ITEMS.register("portable_trampoline", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> MASK_OF_MADNESS = ITEMS.register("mask_of_madness", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 50, ADDITION), new AttributeContainer(BrutalityModAttributes.LIFESTEAL.get(), 0.025, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.DAMAGE_TAKEN.get(), 0.25, MULTIPLY_BASE), new AttributeContainer(BrutalityModAttributes.SLASH_DAMAGE.get(), 8, ADDITION)));


    public static final RegistryObject<Item> PERFECT_CELL = ITEMS.register("perfect_cell", () -> new PerfectCell(ModRarities.GLACIAL, List.of(new ItemDescriptionComponent(PASSIVE, 2))));


    public static final RegistryObject<Item> OMEGA_GAUNTLET = ITEMS.register("omega_gauntlet", () -> new BrutalityCurioItem(ModRarities.FABLED).withAttributes(new AttributeContainer(Attributes.ATTACK_KNOCKBACK, 1.5, ADDITION), new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), 0.25, MULTIPLY_BASE)));

    public static final RegistryObject<Item> SEARED_SUGAR_BROOCH = ITEMS.register("seared_sugar_brooch", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> BUTTER_GAUNTLETS = ITEMS.register("butter_gauntlets", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 1, 60))));
    public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> CHEESE_SAUCE = ITEMS.register("cheese_sauce", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> PIZZA_SLOP = ITEMS.register("pizza_slop", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> HOT_SAUCE = ITEMS.register("hot_sauce", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(BrutalityModMobEffects.HOT_AND_SPICY.get(), 41, 1));
        }
    });

    public static final RegistryObject<Item> OLIVE_OIL = ITEMS.register("olive_oil", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> EXTRA_VIRGIN_OLIVE_OIL = ITEMS.register("extra_virgin_olive_oil", () -> new BrutalityCurioItem(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> DIVERGENT_RECURSOR = ITEMS.register("divergent_recursor", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> CONVERGENT_RECURSOR = ITEMS.register("convergent_recursor", () -> new BrutalityCurioItem(ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(PASSIVE, 3))));
    public static final RegistryObject<Item> INFINITE_RECURSOR = ITEMS.register("infinite_recursor", () -> new BrutalityCurioItem(ModRarities.GODLY, List.of(new ItemDescriptionComponent(PASSIVE, 3))));
    public static final RegistryObject<Item> APPRENTICES_MANUAL_TO_BASIC_MULTICASTING = ITEMS.register("apprentices_manual_to_basic_multicasting", () -> new BrutalityCurioItem(ModRarities.LEGENDARY, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> WIZARDS_GUIDEBOOK_TO_ADVANCED_MULTICASTING = ITEMS.register("wizards_guidebook_to_advanced_multicasting", () -> new BrutalityCurioItem(ModRarities.PRISMATIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> ARCHMAGES_THESIS_TO_MASTERFUL_MULTICASTING = ITEMS.register("archmages_thesis_to_masterful_multicasting", () -> new BrutalityCurioItem(ModRarities.GODLY, List.of(new ItemDescriptionComponent(PASSIVE, 2))));
    public static final RegistryObject<Item> PARAGON_OF_THE_FIRST_MAGE = ITEMS.register("paragon_of_the_first_mage", () -> new BrutalityCurioItem(ModRarities.GODLY, List.of(new ItemDescriptionComponent(PASSIVE, 2))));


    public static final RegistryObject<Item> PI = ITEMS.register("pi", () -> new Pi(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 2))).withAttributes(new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.0314, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_SPEED, 0.0314, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_DAMAGE, 3.14, ADDITION)));

    public static final RegistryObject<Item> EXPONENTIAL_CHARM = ITEMS.register("exponential_charm", () -> new BrutalityMathCurio(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 5))) {
        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            if (slotContext.entity() instanceof Player player) {
                player.getCapability(BrutalityCapabilities.PLAYER_COMBO_CAP).ifPresent(cap -> {
                    cap.resetAll();
                    PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(player.getId(), player));
                });
            }
        }
    }.withAttributes(new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.0271828F, MULTIPLY_TOTAL), new AttributeContainer(Attributes.LUCK, 0.0271828F, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ARMOR, 2.71828F, ADDITION), new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 2.71828F, ADDITION), new AttributeContainer(Attributes.MAX_HEALTH, 2.71828F, ADDITION)));


    public static final RegistryObject<Item> ADDITION_CHARM = ITEMS.register("addition", () -> new BrutalityMathCurio(Rarity.EPIC).withAttributes(new AttributeContainer(Attributes.ATTACK_DAMAGE, 3, ADDITION)));


    public static final RegistryObject<Item> SUBTRACTION = ITEMS.register("subtraction", () -> new BrutalityMathCurio(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> MULTIPLICATION = ITEMS.register("multiplication", () -> new BrutalityMathCurio(Rarity.EPIC).withAttributes(new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.175, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> DIVISION = ITEMS.register("division", () -> new BrutalityMathCurio(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> SUM = ITEMS.register("sum", () -> new Sum(Rarity.EPIC, List.of(new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> SINE = ITEMS.register("sine", () -> new Sine(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> COSINE = ITEMS.register("cosine", () -> new Cosine(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> SCIENTIFIC_CALCULATOR = ITEMS.register("scientific_calculator", () -> new ScientificCalculator(Rarity.EPIC, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> BLOOD_STONE = ITEMS.register("blood_stone", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> RAGE_STONE = ITEMS.register("rage_stone", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PAIN_CATALYST = ITEMS.register("pain_catalyst", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> RAMPAGE_CLOCK = ITEMS.register("rampage_clock", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> BLOOD_HOWL_PENDANT = ITEMS.register("blood_howl_pendant", () -> new BloodHowlPendant(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SPITE_SHARD = ITEMS.register("spite_shard", () -> new SpiteShard(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1, 20))).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 50, ADDITION)));

    public static final RegistryObject<Item> HATE_SIGIL = ITEMS.register("hate_sigil", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 100, ADDITION), new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> HEART_OF_WRATH = ITEMS.register("heart_of_wrath", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.MAX_RAGE.get(), 150, ADDITION), new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> EYE_FOR_VIOLENCE = ITEMS.register("eye_for_violence", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BATTLE_SCARS = ITEMS.register("battle_scars", () -> new BattleScars(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> MECHANICAL_AORTA = ITEMS.register("mechanical_aorta", () -> new MechanicalAorta(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(LORE, 1), new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get(), 2, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), -0.5, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> BLOOD_PULSE_GAUNTLETS = ITEMS.register("blood_pulse_gauntlets", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))).withAttributes(new AttributeContainer(BrutalityModAttributes.RAGE_LEVEL.get(), 2, ADDITION)));

    public static final RegistryObject<Item> BROKEN_CONTROLLER = ITEMS.register("broken_controller", () -> new BrutalityCurioItem(ModRarities.CONDUCTIVE).withAttributes(new AttributeContainer(BrutalityModAttributes.RAGE_LEVEL.get(), 3, ADDITION)));

    public static final RegistryObject<Item> FURY_BAND = ITEMS.register("fury_band", () -> new BrutalityCurioItem(ModRarities.STYGIAN).withAttributes(new AttributeContainer(BrutalityModAttributes.RAGE_TIME.get(), 0.75, MULTIPLY_TOTAL), new AttributeContainer(BrutalityModAttributes.RAGE_LEVEL.get(), 1, ADDITION)));

    public static final RegistryObject<Item> GRUDGE_TOTEM = ITEMS.register("grudge_totem", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ANGER_MANAGEMENT = ITEMS.register("anger_management", () -> new BrutalityCurioItem(ModRarities.STYGIAN, List.of(new ItemDescriptionComponent(PASSIVE, 1), new ItemDescriptionComponent(ACTIVE, 1, DistExecutor.unsafeRunForDist(() -> Keybindings::getRageActivateKey, () -> () -> null)))));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
