package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.entity.explosion.BloodExplosion;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.armor.NoirArmorItem;
import net.goo.brutality.common.item.armor.TerraArmorItem;
import net.goo.brutality.common.item.armor.VampireLordArmorItem;
import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.common.item.base.BrutalityBlockItem;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.block.FilingCabinetBlockItem;
import net.goo.brutality.common.item.block.ImportantDocumentsBlockItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.common.item.curios.anklet.*;
import net.goo.brutality.common.item.curios.belt.BattleScars;
import net.goo.brutality.common.item.curios.belt.ScientificCalculator;
import net.goo.brutality.common.item.curios.charm.*;
import net.goo.brutality.common.item.curios.hands.Handcuffs;
import net.goo.brutality.common.item.curios.hands.PerfectCell;
import net.goo.brutality.common.item.curios.hands.PhantomFinger;
import net.goo.brutality.common.item.curios.heart.BrutalHeart;
import net.goo.brutality.common.item.curios.heart.FrozenHeart;
import net.goo.brutality.common.item.curios.heart.RuneOfDelta;
import net.goo.brutality.common.item.curios.necklace.AbyssalNecklace;
import net.goo.brutality.common.item.curios.necklace.BlackMatterNecklace;
import net.goo.brutality.common.item.curios.necklace.BloodHowlPendant;
import net.goo.brutality.common.item.curios.necklace.KnightsPendant;
import net.goo.brutality.common.item.curios.ring.RoadRunnersRing;
import net.goo.brutality.common.item.curios.vanity.TrialGuardianEyebrows;
import net.goo.brutality.common.item.generic.PrisonKey;
import net.goo.brutality.common.item.generic.SpellScroll;
import net.goo.brutality.common.item.generic.StatTrakkerItem;
import net.goo.brutality.common.item.seals.*;
import net.goo.brutality.common.item.weapon.axe.Deathsaw;
import net.goo.brutality.common.item.weapon.axe.OldGpuAxe;
import net.goo.brutality.common.item.weapon.axe.RhittaAxe;
import net.goo.brutality.common.item.weapon.bow.Providence;
import net.goo.brutality.common.item.weapon.generic.CanopyOfShadowsItem;
import net.goo.brutality.common.item.weapon.generic.CreaseOfCreation;
import net.goo.brutality.common.item.weapon.generic.LastPrism;
import net.goo.brutality.common.item.weapon.generic.TheCloud;
import net.goo.brutality.common.item.weapon.hammer.*;
import net.goo.brutality.common.item.weapon.scythe.DarkinScythe;
import net.goo.brutality.common.item.weapon.scythe.Schism;
import net.goo.brutality.common.item.weapon.spear.Caldrith;
import net.goo.brutality.common.item.weapon.spear.EventHorizon;
import net.goo.brutality.common.item.weapon.spear.Rhongomyniad;
import net.goo.brutality.common.item.weapon.staff.BambooStaff;
import net.goo.brutality.common.item.weapon.staff.ChopstickStaff;
import net.goo.brutality.common.item.weapon.sword.*;
import net.goo.brutality.common.item.weapon.sword.phasesaber.BasePhasesaber;
import net.goo.brutality.common.item.weapon.throwing.*;
import net.goo.brutality.common.item.weapon.tome.*;
import net.goo.brutality.common.item.weapon.trident.DepthCrusherTrident;
import net.goo.brutality.common.item.weapon.trident.GungnirTrident;
import net.goo.brutality.common.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.event.mod.client.Keybindings;
import net.goo.brutality.util.CooldownUtils;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.ModTags;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.build_archetypes.GastronomyHelper;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static net.goo.brutality.util.tooltip.ItemDescriptionComponent.ItemDescriptionComponents.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class BrutalityItems {


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

    public static final RegistryObject<Item> VOIDWALKER_SPELL_SCROLL = ITEMS.register("voidwalker_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.VOIDWALKER));
    public static final RegistryObject<Item> BRIMWIELDER_SPELL_SCROLL = ITEMS.register("brimwielder_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.BRIMWIELDER));
    public static final RegistryObject<Item> EVERGREEN_SPELL_SCROLL = ITEMS.register("evergreen_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.EVERGREEN));
    public static final RegistryObject<Item> DARKIST_SPELL_SCROLL = ITEMS.register("darkist_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.DARKIST));
    public static final RegistryObject<Item> COSMIC_SPELL_SCROLL = ITEMS.register("cosmic_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.COSMIC));
    public static final RegistryObject<Item> CELESTIA_SPELL_SCROLL = ITEMS.register("celestia_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.CELESTIA));
    public static final RegistryObject<Item> UMBRAL_SPELL_SCROLL = ITEMS.register("umbral_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.UMBRANCY));
    public static final RegistryObject<Item> EXODIC_SPELL_SCROLL = ITEMS.register("exodic_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.EXODIC));
    public static final RegistryObject<Item> VOLTWEAVER_SPELL_SCROLL = ITEMS.register("voltweaver_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.VOLTWEAVER));
    public static final RegistryObject<Item> DAEMONIC_SPELL_SCROLL = ITEMS.register("daemonic_spell_scroll", () -> new SpellScroll(new Item.Properties()).withSchool(IBrutalitySpell.MagicSchool.DAEMONIC));


    public static final RegistryObject<Item> BASIC_STAT_TRAKKER = ITEMS.register("basic_stat_trakker",
            () -> new StatTrakkerItem(new Item.Properties(), BrutalityCommonConfig.BASIC_STAT_TRAK_CHANCE));

    public static final RegistryObject<Item> GOLDEN_STAT_TRAKKER = ITEMS.register("golden_stat_trakker",
            () -> new StatTrakkerItem(new Item.Properties(), BrutalityCommonConfig.GOLDEN_STAT_TRAK_CHANCE));

    public static final RegistryObject<Item> PRISMATIC_STAT_TRAKKER = ITEMS.register("prismatic_stat_trakker",
            () -> new StatTrakkerItem(new Item.Properties(), BrutalityCommonConfig.PRISMATIC_STAT_TRAK_CHANCE));

    public static final RegistryObject<Item> WATER_COOLER_ITEM = ITEMS.register("water_cooler", () -> new BrutalityBlockItem(BrutalityBlocks.WATER_COOLER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> COFFEE_MACHINE_ITEM = ITEMS.register("coffee_machine", () -> new BrutalityBlockItem(BrutalityBlocks.COFFEE_MACHINE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUPER_SNIFFER_FIGURE_ITEM = ITEMS.register("super_sniffer_figure", () -> new BrutalityBlockItem(BrutalityBlocks.SUPER_SNIFFER_FIGURE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WHITE_FILING_CABINET_ITEM = ITEMS.register("white_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityBlocks.WHITE_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GRAY_FILING_CABINET_ITEM = ITEMS.register("light_gray_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> GRAY_FILING_CABINET_ITEM = ITEMS.register("gray_filing_cabinet", () -> new FilingCabinetBlockItem(BrutalityBlocks.GRAY_FILING_CABINET.get(), new Item.Properties()));
    public static final RegistryObject<Item> IMPORTANT_DOCUMENTS = ITEMS.register("important_documents", () -> new ImportantDocumentsBlockItem(BrutalityBlocks.IMPORTANT_DOCUMENTS_BLOCK.get(), new Item.Properties()));


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
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(DASH_ABILITY, 1, 60))) {
        @Override
        public void onWearerFall(LivingFallEvent event, ItemStack curio) {
            event.setCanceled(true);
        }
    });

    public static final RegistryObject<Item> TSUKUYOMI = ITEMS.register("tsukuyomi", () -> new Tsukuyomi(
            Tiers.NETHERITE, 3, -2.25F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 2),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> AMATERASU = ITEMS.register("amaterasu", () -> new Amaterasu(
            Tiers.NETHERITE, 3, -2.25F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 2),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> DARKIN_BLADE_SWORD = ITEMS.register("darkin_blade", () -> new DarkinBladeSword(
            Tiers.NETHERITE, 10, -3.15F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> PRISMATIC_GREATSWORD = ITEMS.register("prismatic_greatsword", () -> new PrismaticGreatsword(
            Tiers.NETHERITE, 10, -3.2F, BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> WORLD_TREE_SWORD = ITEMS.register("world_tree_sword", () -> new WorldTreeSword(
            Tiers.NETHERITE, 15, -3.5F, BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(LORE, 1))));

    public static final RegistryObject<Item> CRIMSON_SCISSOR_BLADE = ITEMS.register("crimson_scissor_blade", () -> new CrimsonScissorBlade(
            Tiers.NETHERITE, 6, -3F, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(ON_SWING, 2))));

    public static final RegistryObject<Item> RHONGOMYNIAD = ITEMS.register("rhongomyniad", () -> new Rhongomyniad(
            Tiers.NETHERITE, 6, -3F, BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(ON_SWING, 1))));

    public static final RegistryObject<Item> CALDRITH = ITEMS.register("caldrith", () -> new Caldrith(
            Tiers.NETHERITE, 6, -3F, BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));

    public static final RegistryObject<Item> SCHISM = ITEMS.register("schism", () -> new Schism(
            Tiers.NETHERITE, 8, -3.5F, BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(ON_SWING, 1))));

    public static final RegistryObject<Item> SHADOWFLAME_SCISSOR_BLADE = ITEMS.register("shadowflame_scissor_blade", () -> new ShadowflameScissorBlade(
            Tiers.NETHERITE, 6, -3F, BrutalityRarities.NOCTURNAL, List.of(
            new ItemDescriptionComponent(ON_SWING, 1),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> DARKIN_SCYTHE = ITEMS.register("darkin_scythe", () -> new DarkinScythe(
            Tiers.NETHERITE, 3, -3F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_HIT, 4),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 4),
            new ItemDescriptionComponent(PASSIVE, 3),
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1))));

    public static final RegistryObject<Item> FROSTMOURNE_SWORD = ITEMS.register("frostmourne", () -> new FrostmourneSword(
            Tiers.NETHERITE, 5, -3F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 2),
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 2))));

    public static final RegistryObject<Item> DEPTH_CRUSHER_TRIDENT = ITEMS.register("depth_crusher", () -> new DepthCrusherTrident(
            6, -2.4F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2))));

    public static final RegistryObject<Item> LAST_PRISM_ITEM = ITEMS.register("last_prism", () -> new LastPrism(BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(MANA_COST, 1))));

    public static final RegistryObject<Item> ROYAL_GUARDIAN_SWORD = ITEMS.register("royal_guardian_sword", () -> new RoyalGuardianSword(
            Tiers.NETHERITE, 50, -3.5F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> BLADE_OF_THE_RUINED_KING = ITEMS.register("blade_of_the_ruined_king", () -> new BladeOfTheRuinedKingSword(
            Tiers.DIAMOND, 9, -2.5F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_HIT, 2),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 2))));

    public static final RegistryObject<Item> HELLSPEC_CLEAVER = ITEMS.register("hellspec_cleaver", () -> new HellspecCleaver(
            Tiers.DIAMOND, 4, -3F, BrutalityRarities.STYGIAN));

    public static final RegistryObject<Item> CONDUCTITE_CAPACITOR = ITEMS.register("conductite_capacitor", () -> new ConductiteCapacitor(
            Tiers.DIAMOND, 4, -3F, BrutalityRarities.CONDUCTIVE, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> RHITTA_AXE = ITEMS.register("rhitta", () -> new RhittaAxe(
            Tiers.DIAMOND, 10, -3F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> DEATHSAW = ITEMS.register("deathsaw", () -> new Deathsaw(
            Tiers.DIAMOND, 10, -3F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> JACKPOT_HAMMER = ITEMS.register("jackpot", () -> new JackpotHammer(
            Tiers.DIAMOND, 0, -2.6F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> DULL_KNIFE_DAGGER = ITEMS.register("dull_knife", () -> new DullKnifeSword(
            Tiers.NETHERITE, 4, -2F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));

    public static final RegistryObject<Item> GUNGNIR_TRIDENT = ITEMS.register("gungnir", () -> new GungnirTrident(
            13, -3.2F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> THUNDERBOLT_TRIDENT = ITEMS.register("thunderbolt", () -> new ThunderboltTrident(
            8, -2.1F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> ONYX_PHASESABER = ITEMS.register("onyx_phasesaber", () -> new BasePhasesaber(
            Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> TOPAZ_PHASESABER = ITEMS.register("topaz_phasesaber", () -> new BasePhasesaber(
            Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> RUBY_PHASESABER = ITEMS.register("ruby_phasesaber", () -> new BasePhasesaber(
            Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SAPPHIRE_PHASESABER = ITEMS.register("sapphire_phasesaber", () -> new BasePhasesaber(
            Tiers.NETHERITE, 7F, -2.8F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ATOMIC_JUDGEMENT_HAMMER = ITEMS.register("atomic_judgement", () -> new AtomicJudgementHammer(
            Tiers.NETHERITE, 5, -3F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_HIT, 1),
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> OLD_GPU = ITEMS.register("old_gpu", () -> new OldGpuAxe(
            Tiers.DIAMOND, 23, -2.5F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> SUPERNOVA = ITEMS.register("supernova", () -> new SupernovaSword(
            Tiers.NETHERITE, 10, -3.1F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> SHADOWSTEP = ITEMS.register("shadowstep", () -> new ShadowstepSword(
            Tiers.NETHERITE, -2, -2.6F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> CANOPY_OF_SHADOWS = ITEMS.register("canopy_of_shadows", () -> new CanopyOfShadowsItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> WOODEN_RULER = ITEMS.register("wooden_ruler", () -> new WoodenRulerHammer(
            Tiers.DIAMOND, -1, -3, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> METAL_RULER = ITEMS.register("metal_ruler", () -> new MetalRulerSword(
            Tiers.DIAMOND, -1, -3, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> DOUBLE_DOWN = ITEMS.register("double_down", () -> new DoubleDown(
            Tiers.DIAMOND, 9, -3.2F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 4))));

    public static final RegistryObject<Item> PAPER_CUT = ITEMS.register("paper_cut", () -> new BrutalitySwordItem(
            Tiers.STONE, 1, -2.4F, Rarity.RARE));

    public static final RegistryObject<Item> WHISPERWALTZ = ITEMS.register("whisperwaltz", () -> new WhisperwaltzSword(
            Tiers.NETHERITE, -3, 16F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 1)

    )));
    public static final RegistryObject<Item> PROVIDENCE = ITEMS.register("providence", () -> new Providence(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SEVENTH_STAR = ITEMS.register("seventh_star", () -> new SeventhStarSword(
            Tiers.NETHERITE, 10, -3.15F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_SWING, 1))));

    public static final RegistryObject<Item> CREASE_OF_CREATION = ITEMS.register("crease_of_creation", () -> new CreaseOfCreation(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2))));

    public static final RegistryObject<Item> SPATULA_HAMMER = ITEMS.register("spatula", () -> new SpatulaHammer(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 3))));

    public static final RegistryObject<Item> THE_GOLDEN_SPATULA_HAMMER = ITEMS.register("the_golden_spatula", () -> new TheGoldenSpatulaHammer(
            Tiers.NETHERITE, 13, -2.3F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 3))));

    public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife", () -> new IronKnifeSword(
            Tiers.IRON, 3, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 2))));

    public static final RegistryObject<Item> GOLD_KNIFE = ITEMS.register("gold_knife", () -> new GoldKnifeSword(
            Tiers.GOLD, 4, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 2))));

    public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new DiamondKnifeSword(
            Tiers.DIAMOND, 5, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 2))));

    public static final RegistryObject<Item> VOID_KNIFE = ITEMS.register("void_knife", () -> new VoidKnifeSword(
            Tiers.NETHERITE, 8, -2.3F, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 3))));

    public static final RegistryObject<Item> MELONCHOLY_SWORD = ITEMS.register("meloncholy", () -> new MeloncholySword(
            Tiers.IRON, 6, -2.3F, BrutalityRarities.LEGENDARY));

    public static final RegistryObject<Item> APPLE_CORE_LANCE = ITEMS.register("apple_core", () -> new BrutalitySwordItem(
            Tiers.IRON, 8, -2.6F, BrutalityRarities.LEGENDARY, new Item.Properties().defaultDurability(100).durability(100)));

    public static final RegistryObject<Item> FRYING_PAN = ITEMS.register("frying_pan", () -> new FryingPanHammer(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> CHOPSTICK_STAFF = ITEMS.register("chopstick", () -> new ChopstickStaff(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BAMBOO_STAFF = ITEMS.register("bamboo_staff", () -> new BambooStaff(
            Tiers.DIAMOND, 4, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> POTATO_MASHER = ITEMS.register("potato_masher", () -> new PotatoMasher(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> WHISK_HAMMER = ITEMS.register("whisk", () -> new WhiskHammer(
            Tiers.IRON, 2, -2.3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> PHOTON = ITEMS.register("photon", () -> new BrutalityThrowingItem(
            0, 0, BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2)),
            BrutalityEntities.PHOTON).initialVelocity(5));

    public static final RegistryObject<Item> POUCH_O_PHOTONS = ITEMS.register("pouch_o_photons", () -> new PouchOPhotons(
            0, -3, BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2)),
            BrutalityEntities.PHOTON).initialVelocity(7.5F).inaccuracy(3));

    public static final RegistryObject<Item> VAMPIRE_KNIVES = ITEMS.register("vampire_knives", () -> new VampireKnives(
            3, 0, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2)),
            BrutalityEntities.VAMPIRE_KNIFE).attackType(BrutalityCategories.AttackType.PIERCE).initialVelocity(3));

    public static final RegistryObject<Item> DYNAMITE = ITEMS.register("dynamite", () -> new BrutalityThrowingItem(
            0, -3, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.DYNAMITE));

    public static final RegistryObject<Item> STICKY_DYNAMITE = ITEMS.register("sticky_dynamite", () -> new BrutalityThrowingItem(
            0, -3, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.STICKY_DYNAMITE));

    public static final RegistryObject<Item> BOUNCY_DYNAMITE = ITEMS.register("bouncy_dynamite", () -> new BrutalityThrowingItem(
            0, -3, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.BOUNCY_DYNAMITE));

    public static final RegistryObject<Item> DECK_OF_FATE = ITEMS.register("deck_of_fate", () -> new BrutalityThrowingItem(
            3, -2.5F, BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 7)),
            BrutalityEntities.FATE_CARD).initialVelocity(2).attackType(BrutalityCategories.AttackType.PIERCE));
    public static final RegistryObject<Item> PERFUME_BOTTLE = ITEMS.register("perfume_bottle", () -> new BrutalityThrowingItem(
            3, -3.25F, BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2)),
            BrutalityEntities.PERFUME_BOTTLE));

    public static final RegistryObject<Item> ABSOLUTE_ZERO = ITEMS.register("absolute_zero", () -> new BrutalityThrowingItem(
            3, -3.25F, BrutalityRarities.GLACIAL, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.ABSOLUTE_ZERO));

    public static final RegistryObject<Item> CRIMSON_DELIGHT = ITEMS.register("crimson_delight", () -> new BrutalityThrowingItem(
            2, -1.5F, Rarity.EPIC,
            BrutalityEntities.CRIMSON_DELIGHT));

    public static final RegistryObject<Item> CANNONBALL_CABBAGE = ITEMS.register("cannonball_cabbage", () -> new BrutalityThrowingItem(
            5, -2.2F, Rarity.EPIC,
            BrutalityEntities.CANNONBALL_CABBAGE));

    public static final RegistryObject<Item> CAVENDISH = ITEMS.register("cavendish", () -> new BrutalityThrowingItem(
            2, -2.2F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.CAVENDISH));

    public static final RegistryObject<Item> STICK_OF_BUTTER = ITEMS.register("stick_of_butter", () -> new BrutalityThrowingItem(
            0, -3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.STICK_OF_BUTTER));

    public static final RegistryObject<Item> WINTER_MELON = ITEMS.register("winter_melon", () -> new BrutalityThrowingItem(
            7, -2.75F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.WINTER_MELON));

    public static final RegistryObject<Item> GOLDEN_PHOENIX = ITEMS.register("golden_phoenix", () -> new BrutalityThrowingItem(
            9, -2.65F, Rarity.EPIC,
            BrutalityEntities.GOLDEN_PHOENIX));

    public static final RegistryObject<Item> STICKY_BOMB = ITEMS.register("sticky_bomb", () -> new StickyBomb(
            0, -3.4F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1)),
            BrutalityEntities.STICKY_BOMB));

    public static final RegistryObject<Item> BIOMECH_REACTOR = ITEMS.register("biomech_reactor", () -> new BrutalityThrowingItem(
            20, -3, BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1),
            new ItemDescriptionComponent(ON_HIT, 2)),
            BrutalityEntities.BIOMECH_REACTOR).initialVelocity(0.75F));


    public static final RegistryObject<Item> ICE_CUBE = ITEMS.register("ice_cube", () -> new BrutalityThrowingItem(
            3, -3F, Rarity.EPIC, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.ICE_CUBE));

    public static final RegistryObject<Item> PERMAFROST_CUBE = ITEMS.register("permafrost_cube", () -> new BrutalityThrowingItem(
            6, -3F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.PERMAFROST_CUBE));

    public static final RegistryObject<Item> CINDER_BLOCK = ITEMS.register("cinder_block", () -> new BrutalityThrowingItem(
            10, -3.2F, BrutalityRarities.LEGENDARY,
            BrutalityEntities.CINDER_BLOCK));

    public static final RegistryObject<Item> STYROFOAM_CUP = ITEMS.register("styrofoam_cup", () -> new StyrofoamCup(
            1, -3.2F, Rarity.COMMON, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.STYROFOAM_CUP, BrutalityBlocks.STYROFOAM_CUP.get()));

    public static final RegistryObject<Item> MUG = ITEMS.register("mug", () -> new Mug(
            1, -3.2F, Rarity.COMMON, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 2),
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.MUG, BrutalityBlocks.MUG.get()));

    public static final RegistryObject<Item> OVERCLOCKED_TOASTER = ITEMS.register("overclocked_toaster", () -> new BrutalityThrowingItem(
            10, -3.5F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.OVERCLOCKED_TOASTER));

    public static final RegistryObject<Item> SCULK_GRENADE = ITEMS.register("sculk_grenade", () -> new BrutalityThrowingItem(
            10, -3F, BrutalityRarities.GLOOMY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 2)),
            BrutalityEntities.SCULK_GRENADE).initialVelocity(1.25F));

    public static final RegistryObject<Item> BEACH_BALL = ITEMS.register("beach_ball", () -> new BrutalityThrowingItem(
            0, -3F, BrutalityRarities.LEGENDARY,
            BrutalityEntities.BEACH_BALL).initialVelocity(0.3F));

    public static final RegistryObject<Item> BLAST_BARREL = ITEMS.register("blast_barrel", () -> new BrutalityThrowingItem(
            10, -3.4F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.BLAST_BARREL).initialVelocity(0.75F));

    public static final RegistryObject<Item> HOLY_HAND_GRENADE = ITEMS.register("holy_hand_grenade", () -> new BrutalityThrowingItem(
            10, -3.4F, BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(WHEN_THROWN, 1)),
            BrutalityEntities.HOLY_HAND_GRENADE));


    public static final RegistryObject<Item> DAEMONIC_TOME = ITEMS.register("daemonic_tome", () -> new DaemonicTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> DARKIST_TOME = ITEMS.register("darkist_tome", () -> new DarkistTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> EVERGREEN_TOME = ITEMS.register("evergreen_tome", () -> new EvergreenTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> COSMIC_TOME = ITEMS.register("cosmic_tome", () -> new CosmicTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> UMBRAL_TOME = ITEMS.register("umbral_tome", () -> new UmbralTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> VOIDWALKER_TOME = ITEMS.register("voidwalker_tome", () -> new VoidTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> EXODIC_TOME = ITEMS.register("exodic_tome", () -> new ExodicTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> CELESTIA_TOME = ITEMS.register("celestia_tome", () -> new CelestiaTome(BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> BRIMWIELDER_TOME = ITEMS.register("brimwielder_tome", () -> new BrimwielderTome(BrutalityRarities.LEGENDARY));


    public static final RegistryObject<Item> PLUNDER_CHEST = ITEMS.register("plunder_chest", () -> new PlunderChest(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 100))) {
    });

    public static final RegistryObject<Item> WOOLY_BLINDFOLD = ITEMS.register("wooly_blindfold", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY));

    public static final RegistryObject<Item> SERAPHIM_HALO = ITEMS.register("seraphim_halo", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).followHeadRotations(false).translateIfSneaking(false));

    public static final RegistryObject<Item> GOOD_BOOK = ITEMS.register("good_book", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> GOLDEN_HEADBAND = ITEMS.register("golden_headband", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY));

    public static final RegistryObject<Item> YATA_NO_KAGAMI = ITEMS.register("yata_no_kagami", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            float negated = amount * 0.15F;
            if (source.getEntity() != null) {
                source.getEntity().hurt(source.getEntity().damageSources().indirectMagic(wearer, null), negated);
            }
            return Math.max(0, amount - negated);
        }
    });

    public static final RegistryObject<Item> GREED = ITEMS.register("greed", () -> new Greed(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> PRIDE = ITEMS.register("pride", () -> new Pride(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> WRATH = ITEMS.register("wrath", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 50, ADDITION),
            new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> SLOTH = ITEMS.register("sloth", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim) {
                livingVictim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
                livingVictim.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 0));
                livingVictim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0));
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> GLUTTONY = ITEMS.register("gluttony", () -> new Gluttony(BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            return stack.getOrCreateTag().getInt(SOULS) * 0.01F + amount;
        }
    });

    public static final RegistryObject<Item> LUST = ITEMS.register("lust", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ENVY = ITEMS.register("envy", () -> new BrutalityCurioItem(BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(LORE, 1))));

    public static final RegistryObject<Item> ENERGY_FOCUSER = ITEMS.register("energy_focuser", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> BRUTESKIN_BELT = ITEMS.register("bruteskin_belt", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, -0.5, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.5, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.2, ADDITION)));

    public static final RegistryObject<Item> CRITICAL_THINKING = ITEMS.register("critical_thinking", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> DEADSHOT_BROOCH = ITEMS.register("deadshot_brooch", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> HELLSPEC_TIE = ITEMS.register("hellspec_tie", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SOUL_STONE = ITEMS.register("soul_stone", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> EMERGENCY_FLASK = ITEMS.register("emergency_flask", () -> new EmergencyFlask(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> RING_OF_MANA = ITEMS.register("ring_of_mana", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 40, ADDITION)));

    public static final RegistryObject<Item> RING_OF_MANA_PLUS = ITEMS.register("ring_of_mana_plus", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 60, ADDITION)));

    public static final RegistryObject<Item> CONSERVATIVE_CONCOCTION = ITEMS.register("conservative_concoction", () -> new ConservativeConcoction(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> MICROBLADE_BAND = ITEMS.register("microblade_band", () -> new BrutalityCurioItem(
            BrutalityRarities.DARK, List.of(
            new ItemDescriptionComponent(LORE, 1)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 1, ADDITION)));

    public static final RegistryObject<Item> RUNE_OF_DELTA = ITEMS.register("rune_of_delta", () -> new RuneOfDelta(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ECHO_CHAMBER = ITEMS.register("echo_chamber", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ONYX_IDOL = ITEMS.register("onyx_idol", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.literal(String.format("%.2f", pStack.getOrCreateTag().getFloat("mana")) + " / 200").withStyle(ChatFormatting.BLUE));
        }
    });

    public static final RegistryObject<Item> SCRIBES_INDEX = ITEMS.register("scribes_index", () -> new ScribesIndex(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BLACK_HOLE_ORB = ITEMS.register("black_hole_orb", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), -0.9, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.MANA_REGEN.get(), 10, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> FORBIDDEN_ORB = ITEMS.register("forbidden_orb", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() instanceof Player player && ManaHelper.getCurrentManaPercentage(player) < 0.25)
                player.kill();
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 2, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.MANA_COST.get(), 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.MANA_REGEN.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> BLOOD_ORB = ITEMS.register("blood_orb", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            if (wearer instanceof Player player)
                ManaHelper.modifyManaValue(player, amount * 7.5F);
            return amount;
        }
    });

    public static final RegistryObject<Item> CROWN_OF_TYRANNY = ITEMS.register("crown_of_tyranny", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim)
                return amount * (1 + (1 - livingVictim.getHealth() / livingVictim.getMaxHealth())) * 0.75F;
            return amount;
        }
    });

    public static final RegistryObject<Item> PORTABLE_QUANTUM_THINGAMABOB = ITEMS.register("portable_quantum_thingamabob", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            attacker.addEffect(new MobEffectInstance(TerramityModMobEffects.MIRRORING.get(), 20));
            return amount;
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 10, ADDITION)));

    public static final RegistryObject<Item> WARPSLICE_SCABBARD = ITEMS.register("warpslice_scabbard", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 7, ADDITION),
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.2, MULTIPLY_BASE)));

    public static final RegistryObject<Item> PROFANUM_REACTOR = ITEMS.register("profanum_reactor", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(TerramityModMobEffects.VULNERABLE.get(), 41));
        }
    }.withAttributes(new AttributeContainer(BrutalityAttributes.MANA_REGEN.get(), 15, ADDITION)));

    public static final RegistryObject<Item> FURY_BATTERY = ITEMS.register("fury_battery", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 85, ADDITION)));

    public static final RegistryObject<Item> DAEMONIUM_WHETSTONE = ITEMS.register("daemonium_whetstone", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.13, MULTIPLY_BASE)));

    public static final RegistryObject<Item> KNIGHTS_PENDANT = ITEMS.register("knights_pendant", () -> new KnightsPendant(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> THE_OATH = ITEMS.register("the_oath", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            victim.hurt(victim.damageSources().indirectMagic(attacker, null), amount * 0.1F);
            victim.invulnerableTime = 0;
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), 0.15, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.CELESTIA_SCHOOL_LEVEL.get(), 1, ADDITION)));

    public static final RegistryObject<Item> LUCKY_INSOLES = ITEMS.register("lucky_insoles", () -> new LuckyInsoles(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SANGUINE_SIGNET = ITEMS.register("sanguine_signet", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, 2, ADDITION),
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.025, ADDITION)));

    public static final RegistryObject<Item> PHANTOM_FINGER = ITEMS.register("phantom_finger", () -> new PhantomFinger(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1)))
            .withAttributes(
                    new AttributeContainer(ForgeMod.ENTITY_REACH.get(), 2, ADDITION),
                    new AttributeContainer(ForgeMod.BLOCK_REACH.get(), 2, ADDITION)));

    public static final RegistryObject<Item> STYGIAN_CHAIN = ITEMS.register("stygian_chain", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(Attributes.ARMOR, -6, ADDITION),
            new AttributeContainer(Attributes.ARMOR_TOUGHNESS, -4, ADDITION),
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.15, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> SILVER_RESPAWN_CARD = ITEMS.register("silver_respawn_card", () -> new RespawnCard(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> DIAMOND_RESPAWN_CARD = ITEMS.register("diamond_respawn_card", () -> new RespawnCard(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 30 * 60 * 20))));

    public static final RegistryObject<Item> EVIL_KING_RESPAWN_CARD = ITEMS.register("evil_king_respawn_card", () -> new RespawnCard(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 15 * 60 * 20))));

    public static final RegistryObject<Item> SILVER_BOOSTER_PACK = ITEMS.register("silver_booster_pack", () -> new BoosterPack(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> DIAMOND_BOOSTER_PACK = ITEMS.register("diamond_booster_pack", () -> new BoosterPack.Upgraded(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 2, 30 * 60 * 20))));

    public static final RegistryObject<Item> EVIL_KING_BOOSTER_PACK = ITEMS.register("evil_king_booster_pack", () -> new BoosterPack.Upgraded(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 2, 15 * 60 * 20))));

    public static final RegistryObject<Item> HEMOMATIC_LOCKET = ITEMS.register("hemomatic_locket", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHeal(LivingEntity healed, ItemStack stack, float amount) {
            healed.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0));
            return super.onWearerHeal(healed, stack, amount);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.025, ADDITION)));

    public static final RegistryObject<Item> BOILING_BLOOD = ITEMS.register("boiling_blood", () -> new BoilingBlood(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BEAD_OF_LIFE = ITEMS.register("bead_of_life", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            wearer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 2));
            return amount;
        }
    });

    public static final RegistryObject<Item> DAEMONIUM_SEWING_KIT = ITEMS.register("daemonium_sewing_kit", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 3, ADDITION)));

    public static final RegistryObject<Item> GAMBLERS_CHAIN = ITEMS.register("gamblers_chain", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.LUCK, 1.5, ADDITION)));

    public static final RegistryObject<Item> PINCUSHION = ITEMS.register("pincushion", () -> new Pincushion(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> OLD_GUILLOTINE = ITEMS.register("old_guillotine", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim && livingVictim.getHealth() <= 5) victim.kill();
            return amount;
        }
    });

    public static final RegistryObject<Item> KNUCKLE_WRAPS = ITEMS.register("knuckle_wraps", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            ModUtils.modifyEffect(attacker, BrutalityEffects.PRECISION.get(),
                    new ModUtils.ModValue(80, true), new ModUtils.ModValue(3, false),
                    null, living -> living.addEffect(new MobEffectInstance(BrutalityEffects.PRECISION.get(), 80, 0)), null);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> MANA_SYRINGE = ITEMS.register("mana_syringe", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (attacker instanceof Player player)
                ManaHelper.modifyManaValue(player, amount * 0.25F);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 20, ADDITION)));

    public static final RegistryObject<Item> PRISMATIC_ORB = ITEMS.register("prismatic_orb", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.SPELL_COOLDOWN.get(), 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> SUSPICIOUSLY_LARGE_HANDLE = ITEMS.register("suspiciously_large_handle", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> SAD_UVOGRE = ITEMS.register("sad_uvogre", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim.getBbHeight() < attacker.getBbHeight())
                return amount * 1.25F;
            return amount;
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), 0.35, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> SOLDIERS_SYRINGE = ITEMS.register("soldiers_syringe", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.ATTACK_SPEED, 0.2, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> RAGE_BAIT = ITEMS.register("rage_bait", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.RAGE_LEVEL.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> TARGET_CUBE = ITEMS.register("target_cube", () -> new TargetCube(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BLOOD_PACK = ITEMS.register("blood_pack", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, -3, ADDITION), new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.125, ADDITION)));

    public static final RegistryObject<Item> BROKEN_CLOCK = ITEMS.register("broken_clock", () -> new BaseBrokenClock(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SHATTERED_CLOCK = ITEMS.register("shattered_clock", () -> new BaseBrokenClock(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SUNDERED_CLOCK = ITEMS.register("sundered_clock", () -> new BaseBrokenClock(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> TIMEKEEPERS_CLOCK = ITEMS.register("timekeepers_clock", () -> new BaseBrokenClock(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> THE_CLOCK_OF_FROZEN_TIME = ITEMS.register("the_clock_of_frozen_time", () -> new BaseBrokenClock(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> WIRE_CUTTERS = ITEMS.register("wire_cutters", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.05, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> AIR_JORDAN_EARRINGS = ITEMS.register("air_jordan_earrings", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.JUMP_HEIGHT.get(), 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.4, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> HELL_SPECS = ITEMS.register("hell_specs", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.BRIMWIELDER_SCHOOL_LEVEL.get(), 1, ADDITION)));

    public static final RegistryObject<Item> SCOPE_GOGGLES = ITEMS.register("scope_goggles", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.2, MULTIPLY_BASE)));

    public static final RegistryObject<Item> LENS_MAKERS_GLASSES = ITEMS.register("lens_makers_glasses", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> JURY_NULLIFIER = ITEMS.register("jury_nullifier", () -> new BrutalityCurioItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), -1.5, ADDITION),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim && livingVictim.getHealth() / livingVictim.getMaxHealth() > 0.9)
                return amount * 1.4F;
            return amount;
        }
    });

    public static final RegistryObject<Item> EYE_OF_THE_DRAGON = ITEMS.register("eye_of_the_dragon", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.01, ADDITION),
            new AttributeContainer(BrutalityAttributes.LETHALITY.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.025, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.025, ADDITION),
            new AttributeContainer(BrutalityAttributes.SWORD_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.SPEAR_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.AXE_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.SCYTHE_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.HAMMER_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.PIERCING_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 1, ADDITION),
            new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 1, ADDITION)));

    public static final RegistryObject<Item> BRUTAL_HEART = ITEMS.register("brutal_heart", () -> new BrutalHeart(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> NINJA_HEART = ITEMS.register("ninja_heart", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 0.2, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.2, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.STEALTH.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> ENDER_DRAGON_STEM_CELLS = ITEMS.register("ender_dragon_stem_cells", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 100, ADDITION),
            new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 1, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.AXE_DAMAGE.get(), 3, ADDITION),
            new AttributeContainer(BrutalityAttributes.SWORD_DAMAGE.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> DECK_OF_CARDS = ITEMS.register("deck_of_cards", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ACTIVE, 1, 420, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null))))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.SPELL_DAMAGE.get(), 0.13, MULTIPLY_BASE)));

    public static final RegistryObject<Item> VINDICATOR_STEROIDS = ITEMS.register("vindicator_steroids", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ACTIVE, 1, 45 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> STRESS_PILLS = ITEMS.register("stress_pills", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(ACTIVE, 1, 60 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))) {
        @Override
        public boolean canEquip(SlotContext slotContext, ItemStack stack) {
            return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler -> !handler.isEquipped(BrutalityItems.SEROTONIN_PILLS.get())).orElse(true);
        }
    });

    public static final RegistryObject<Item> SEROTONIN_PILLS = ITEMS.register("serotonin_pills", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(ACTIVE, 2, 60 * 20, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))) {
        @Override
        public boolean canEquip(SlotContext slotContext, ItemStack stack) {
            return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler -> !handler.isEquipped(BrutalityItems.STRESS_PILLS.get())).orElse(true);
        }
    });

    public static final RegistryObject<Item> DAVYS_ANKLET = ITEMS.register("davys_anklet", () -> new DavysAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> EMPTY_ANKLET = ITEMS.register("empty_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.05, ADDITION)));

    public static final RegistryObject<Item> ANKLET_OF_THE_IMPRISONED = ITEMS.register("anklet_of_the_imprisoned", () -> new AnkletOfTheImprisoned(
            BrutalityRarities.CATACLYSMIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> SHARPNESS_ANKLET = ITEMS.register("sharpness_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), -0.05, ADDITION),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 3, ADDITION),
            new AttributeContainer(BrutalityAttributes.PIERCING_DAMAGE.get(), 0.2, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> DEBUG_ANKLET = ITEMS.register("debug_anklet", () -> new DebugAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.125, ADDITION)));

    public static final RegistryObject<Item> REDSTONE_ANKLET = ITEMS.register("redstone_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.125, ADDITION),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 2, ADDITION)));

    public static final RegistryObject<Item> DEVILS_ANKLET = ITEMS.register("devils_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.HEXED.get(), 20));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.25, ADDITION)));

    public static final RegistryObject<Item> BASKETBALL_ANKLET = ITEMS.register("basketball_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.167, ADDITION),
            new AttributeContainer(BrutalityAttributes.JUMP_HEIGHT.get(), 2, ADDITION)));

    public static final RegistryObject<Item> EMERALD_ANKLET = ITEMS.register("emerald_anklet", () -> new EmeraldAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 200)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> RUBY_ANKLET = ITEMS.register("ruby_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> TOPAZ_ANKLET = ITEMS.register("topaz_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void onWearerFall(LivingFallEvent event, ItemStack curio) {
            if (ModUtils.getSyncedPseudoRandom(event.getEntity()).nextFloat() < 0.9F)
                event.setCanceled(true);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> SAPPHIRE_ANKLET = ITEMS.register("sapphire_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> ONYX_ANKLET = ITEMS.register("onyx_anklet", () -> new OnyxAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 60)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> ULTRA_DODGE_ANKLET = ITEMS.register("ultra_dodge_anklet", () -> new UltraDodgeAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.25, ADDITION),
                    new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.25, ADDITION)));

    public static final RegistryObject<Item> GUNDALFS_ANKLET = ITEMS.register("gundalfs_anklet", () -> new GundalfsAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 40)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> TRIAL_ANKLET = ITEMS.register("trial_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
            dodger.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(Attributes.ATTACK_KNOCKBACK, 1, ADDITION)));

    public static final RegistryObject<Item> SUPER_DODGE_ANKLET = ITEMS.register("super_dodge_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.GIANT_SNIFFERS_HOOF_ACTIVE_ABILITY.get(), 60));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.275, MULTIPLY_BASE)));

    public static final RegistryObject<Item> GNOME_KINGS_ANKLET = ITEMS.register("gnome_kings_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 400, 2));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(Attributes.ATTACK_SPEED, 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> ANKLENT = ITEMS.register("anklent", () -> new Anklent(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ANKLE_MONITOR = ITEMS.register("ankle_monitor", () -> new AnkleMonitor(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 3)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.35, ADDITION)));

    public static final RegistryObject<Item> FIERY_ANKLET = ITEMS.register("fiery_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.FIRE, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            if (source.getEntity() != null) source.getEntity().setSecondsOnFire(2);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> SACRED_SPEED_ANKLET = ITEMS.register("sacred_speed_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.DIVINE, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 4));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> CONDUCTITE_ANKLET = ITEMS.register("conductite_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.CONDUCTIVE, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(TerramityModMobEffects.AMPED.get(), 60, 0));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> BLOOD_CLOT_ANKLET = ITEMS.register("blood_clot_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.025, ADDITION)));

    public static final RegistryObject<Item> VIRENTIUM_ANKLET = ITEMS.register("virentium_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(ForgeMod.SWIM_SPEED.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> COSMIC_ANKLET = ITEMS.register("cosmic_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.MYTHIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.5, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> NYXIUM_ANKLET = ITEMS.register("nyxium_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.NOCTURNAL, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(BrutalityEffects.FRUGAL_MANA.get(), 80, 9, false, false));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 50, ADDITION),
            new AttributeContainer(BrutalityAttributes.MANA_REGEN.get(), 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> VOID_ANKLET = ITEMS.register("void_anklet", () -> new BrutalityAnkletItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 80, 0, false, false));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.25, ADDITION)));

    public static final RegistryObject<Item> IRONCLAD_ANKLET = ITEMS.register("ironclad_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1))) {
        @Override
        public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
            dodger.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, false, false));
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, -0.05, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ARMOR, 0.1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> GLADIATORS_ANKLET = ITEMS.register("gladiators_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.05, ADDITION),
            new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 3.5, ADDITION),
            new AttributeContainer(BrutalityAttributes.PIERCING_DAMAGE.get(), 2.5, ADDITION),
            new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), -4, ADDITION)));

    public static final RegistryObject<Item> EXODIUM_ANKLET = ITEMS.register("exodium_anklet", () -> new ExodiumAnklet(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_SUCCESSFUL_DODGE, 1, 100)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> BIG_STEPPA = ITEMS.register("big_steppa", () -> new BrutalityAnkletItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.1, ADDITION),
                    new AttributeContainer(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5, ADDITION)));

    public static final RegistryObject<Item> WINDSWEPT_ANKLET = ITEMS.register("windswept_anklet", () -> new BrutalityAnkletItem(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(BrutalityAttributes.DODGE_CHANCE.get(), 0.15, ADDITION),
            new AttributeContainer(BrutalityAttributes.JUMP_HEIGHT.get(), 0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> HYPERBOLIC_FEATHER = ITEMS.register("hyperbolic_feather", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 80))) {
        @Override
        public void onWearerFall(LivingFallEvent event, ItemStack curio) {
            if (event.getEntity() instanceof Player player)
                CooldownUtils.validateCooldown(player, curio.getItem(), 80, () ->
                        DelayedTaskScheduler.queueServerWork(event.getEntity().level(), 1, () ->
                                player.heal(2 * player.calculateFallDamage(event.getDistance(), event.getDamageMultiplier()))));
        }
    });

    public static final RegistryObject<Item> CARTON_OF_PRISM_SOLUTION_MILK = ITEMS.register("carton_of_prism_solution_milk", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0) {
                LivingEntity wearer = slotContext.entity();
                wearer.addEffect(new MobEffectInstance(TerramityModMobEffects.IMMUNITY.get(), 41, 0));
                wearer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 41, 1));
            }
        }
    }.withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, -0.4, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> TRIAL_GUARDIAN_EYEBROWS = ITEMS.register("trial_guardian_eyebrows", () -> new TrialGuardianEyebrows(
            BrutalityRarities.LEGENDARY));
    public static final RegistryObject<Item> TRIAL_GUARDIAN_HANDS = ITEMS.register("trial_guardian_hands", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY));

    public static final RegistryObject<Item> BLOOD_CHALICE = ITEMS.register("blood_chalice", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.OMNIVAMP.get(), 0.05, ADDITION)));
    public static final RegistryObject<Item> BLACK_MATTER_NECKLACE = ITEMS.register("black_matter_necklace", () -> new BlackMatterNecklace(
            BrutalityRarities.DARK, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SELF_REPAIR_NEXUS = ITEMS.register("self_repair_nexus", () -> new SelfRepairNexus(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.05, ADDITION),
                    new AttributeContainer(Attributes.MAX_HEALTH, 4, ADDITION)));

    public static final RegistryObject<Item> VAMPIRIC_TALISMAN = ITEMS.register("vampiric_talisman", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 80))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (attacker instanceof Player playerAttacker)
                CooldownUtils.validateCurioCooldown(playerAttacker, curio.getItem(), 80, () -> attacker.heal(amount * 0.5F));
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.05, ADDITION)));

    public static final RegistryObject<Item> HEMOGRAFT_NEEDLE = ITEMS.register("hemograft_needle", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.05, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> SANGUINE_SPECTACLES = ITEMS.register("sanguine_spectacles", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.OMNIVAMP.get(), 0.025, ADDITION)));

    public static final RegistryObject<Item> VAMPIRE_FANG = ITEMS.register("vampire_fang", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.05, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.25, ADDITION),
            new AttributeContainer(Attributes.MAX_HEALTH, -0.2, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PROGENITORS_EARRINGS = ITEMS.register("progenitors_earrings", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.25, ADDITION),
            new AttributeContainer(Attributes.ARMOR, -0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> FALLEN_ANGELS_HALO = ITEMS.register("fallen_angels_halo", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.075, ADDITION),
            new AttributeContainer(BrutalityAttributes.SCYTHE_DAMAGE.get(), 0.25, MULTIPLY_BASE)));

    public static final RegistryObject<Item> BLOODSTAINED_MIRROR = ITEMS.register("bloodstained_mirror", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            if (source.getEntity() != null) {
                source.getEntity().hurt(source.getEntity().damageSources().indirectMagic(wearer, null), amount * 0.1F);
            }
            return super.onWearerHurt(wearer, stack, source, amount);
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.075, ADDITION),
            new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> INCOGNITO_MODE = ITEMS.register("incognito_mode", () -> new BrutalityCurioItem(
            BrutalityRarities.DARK).withAttributes(
            new AttributeContainer(BrutalityAttributes.STEALTH.get(), 0.65F, ADDITION)));

    public static final RegistryObject<Item> MINIATURE_ANCHOR = ITEMS.register("miniature_anchor", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
                if (slotContext.entity().isUnderWater()) {
                    slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 11, 3));
                }
            }
        }
    }
            .withAttributes(
                    new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.25, MULTIPLY_TOTAL),
                    new AttributeContainer(ForgeMod.SWIM_SPEED.get(), -0.9F, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PAPER_AIRPLANE = ITEMS.register("paper_airplane", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.25, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PACK_OF_CIGARETTES = ITEMS.register("pack_of_cigarettes", () -> new BrutalityCurioItem(
            BrutalityRarities.DARK).withAttributes(
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), -0.5, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TAKEN.get(), -0.25, ADDITION)));

    public static final RegistryObject<Item> FIRE_EXTINGUISHER = ITEMS.register("fire_extinguisher", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> LUCKY_BOOKMARK = ITEMS.register("lucky_bookmark", () -> new LuckyBookmark(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> EMERGENCY_MEETING = ITEMS.register("emergency_meeting", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ACTIVE, 1, 20 * 60, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> PENCIL_SHARPENER = ITEMS.register("pencil_sharpener", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.PIERCING_DAMAGE.get(), 0.15, MULTIPLY_BASE)));
    public static final RegistryObject<Item> CHOCOLATE_BAR = ITEMS.register("chocolate_bar", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.15, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ATTACK_SPEED, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PORTABLE_MINING_RIG = ITEMS.register("portable_mining_rig", () -> new PortableMiningRig(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> DUMBBELL = ITEMS.register("dumbbell", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), 0.2, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, -0.05, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ATTACK_SPEED, -0.05, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> BOX_OF_CHOCOLATES = ITEMS.register("box_of_chocolates", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.STEALTH.get(), 0.15, ADDITION)));

    public static final RegistryObject<Item> BROKEN_HEART = ITEMS.register("broken_heart", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, -3, ADDITION),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.TENACITY.get(), 0.1, ADDITION)));

    public static final RegistryObject<Item> ESCAPE_KEY = ITEMS.register("escape_key", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
                LivingEntity livingEntity = slotContext.entity();
                if (livingEntity.getHealth() / livingEntity.getMaxHealth() <= 0.25) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 11, 1, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 11, 0, false, false));
                }
            }
        }
    });

    public static final RegistryObject<Item> MIRACLE_CURE = ITEMS.register("miracle_cure", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ACTIVE, 1, 20 * 60))));

    public static final RegistryObject<Item> ESSENTIAL_OILS = ITEMS.register("essential_oils", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity() != null && slotContext.entity().tickCount % 10 == 0) {
                LivingEntity livingEntity = slotContext.entity();
                livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.OILED.get(), 11, 1, false, false));
            }
        }
    });

    public static final RegistryObject<Item> GLASS_HEART = ITEMS.register("glass_heart", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(Attributes.ARMOR, -1, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_DAMAGE.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> CRYPTO_WALLET = ITEMS.register("crypto_wallet", () -> new CryptoWallet(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> DIVINE_IMMOLATION = ITEMS.register("divine_immolation", () -> new BrutalityCurioItem(
            BrutalityRarities.FIRE).withAttributes(
            new AttributeContainer(BrutalityAttributes.CRITICAL_STRIKE_CHANCE.get(), 1, MULTIPLY_BASE),
            new AttributeContainer(Attributes.MAX_HEALTH, -0.99, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 1, MULTIPLY_TOTAL),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.35, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> LIGHT_SWITCH = ITEMS.register("light_switch", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ACTIVE, 1, 400, DistExecutor.unsafeRunForDist(() -> Keybindings::getActiveAbilityKey, () -> () -> null)))));


    public static final RegistryObject<Item> FUZZY_DICE = ITEMS.register("fuzzy_dice", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SOLAR_SYSTEM = ITEMS.register("solar_system", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).followBodyRotations(false).translateIfSneaking(false));

    public static final RegistryObject<Item> NANOMACHINES = ITEMS.register("nanomachines", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && (source.is(DamageTypeTags.IS_PROJECTILE) || source.is(DamageTypes.MOB_ATTACK) ||
                    source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO))) {
                return amount * 0.5F;
            }
            return amount;
        }
    });

    public static final RegistryObject<Item> DUELING_GLOVE = ITEMS.register("dueling_glove", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (victim == attacker.level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, attacker,
                    attacker.getX(), attacker.getY(0.5), attacker.getZ(), attacker.getBoundingBox().inflate(100)))
                return amount * 1.5F;
            return amount;
        }

        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            LivingEntity nearest = wearer.level().getNearestEntity(LivingEntity.class,
                    TargetingConditions.DEFAULT.range(100).selector(e -> e != wearer && !(e instanceof ArmorStand)),
                    wearer, wearer.getX(), wearer.getY(), wearer.getZ(), wearer.getBoundingBox().inflate(100));

            if (nearest != null && nearest != source.getEntity()) {
                return amount * 1.5F;
            }
            return amount;
        }
    });

    public static final RegistryObject<Item> DRAGONHEART = ITEMS.register("dragonheart", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0) {
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 41, 2));
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 41, 0));
            }
        }
    });

    public static final RegistryObject<Item> UVOGRE_HEART = ITEMS.register("uvogre_heart", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 10 == 0 && slotContext.entity().getHealth() < slotContext.entity().getMaxHealth() / 4)
                slotContext.entity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 11, 3));
        }
    });

    public static final RegistryObject<Item> ZOMBIE_HEART = ITEMS.register("zombie_heart", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim)
                livingVictim.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> FROZEN_HEART = ITEMS.register("frozen_heart", () -> new FrozenHeart(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SECOND_HEART = ITEMS.register("second_heart", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC).withAttributes(
            new AttributeContainer(Attributes.MAX_HEALTH, 12, ADDITION)));

    public static final RegistryObject<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold", () -> new BrutalityCurioItem(
            BrutalityRarities.MYTHIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            float toHeal = amount * 0.25F;
            float maxPossible = Math.max(wearer.getMaxHealth() - wearer.getAbsorptionAmount(), 0);
            float finalToHeal = Math.min(toHeal, maxPossible);
            DelayedTaskScheduler.queueServerWork(wearer.level(), 1, () -> wearer.setAbsorptionAmount(wearer.getAbsorptionAmount() + finalToHeal));
            return toHeal;
        }
    });

    public static final RegistryObject<Item> BRAIN_ROT = ITEMS.register("brain_rot", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (victim instanceof LivingEntity livingVictim)
                livingVictim.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> ROAD_RUNNERS_RING = ITEMS.register("road_runners_ring", () -> new RoadRunnersRing(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> RESPLENDENT_FEATHER = ITEMS.register("resplendent_feather", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            slotContext.entity().heal(slotContext.entity().level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT.selector(Entity::isOnFire), slotContext.entity(), slotContext.entity().getBoundingBox().inflate(7)).size() * 0.15F);
        }
    });

    public static final RegistryObject<Item> ABYSSAL_NECKLACE = ITEMS.register("abyssal_necklace", () -> new AbyssalNecklace(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> THE_CLOUD = ITEMS.register("the_cloud", () -> new TheCloud(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(ON_SHIFT_RIGHT_CLICK, 1),
            new ItemDescriptionComponent(ON_RIGHT_CLICK, 1))));

    public static final RegistryObject<Item> PRISON_KEY = ITEMS.register("prison_key", () -> new PrisonKey(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(ON_LEFT_CLICKING_ENTITY, 1))));

    public static final RegistryObject<Item> HANDCUFFS = ITEMS.register("handcuffs", () -> new Handcuffs(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> EVENT_HORIZON = ITEMS.register("event_horizon", () -> new EventHorizon(
            Tiers.NETHERITE, 17, -3.1F, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(ON_HOLD_RIGHT_CLICK, 2),
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> PUREBLOOD = ITEMS.register("pureblood", () -> new Pureblood(
            Tiers.NETHERITE, 13, -3.25F, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(ON_HIT, 1))));

    public static final RegistryObject<Item> NOIR_HELMET = ITEMS.register("noir_helmet", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR, ArmorItem.Type.HELMET, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> NOIR_CHESTPLATE = ITEMS.register("noir_chestplate", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR, ArmorItem.Type.CHESTPLATE, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> NOIR_LEGGINGS = ITEMS.register("noir_leggings", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR, ArmorItem.Type.LEGGINGS, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> NOIR_BOOTS = ITEMS.register("noir_boots", () -> new NoirArmorItem(
            BrutalityArmorMaterials.NOIR, ArmorItem.Type.BOOTS, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> VAMPIRE_LORD_HELMET = ITEMS.register("vampire_lord_crown", () -> new VampireLordArmorItem(
            BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.HELMET, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 3),
            new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> VAMPIRE_LORD_CHESTPLATE = ITEMS.register("vampire_lord_chestplate", () -> new VampireLordArmorItem(
            BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.CHESTPLATE, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 3),
            new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> VAMPIRE_LORD_LEGGINGS = ITEMS.register("vampire_lord_leggings", () -> new VampireLordArmorItem(
            BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.LEGGINGS, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 3),
            new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));

    public static final RegistryObject<Item> VAMPIRE_LORD_BOOTS = ITEMS.register("vampire_lord_boots", () -> new VampireLordArmorItem(
            BrutalityArmorMaterials.VAMPIRE_LORD, ArmorItem.Type.BOOTS, BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 3),
            new ItemDescriptionComponent(FULL_SET_ACTIVE, 3, 80, DistExecutor.unsafeRunForDist(() -> Keybindings::getArmorSetBonusAbilityKey, () -> () -> null)))));


    public static final RegistryObject<Item> TERRA_HELMET = ITEMS.register("terra_helmet", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA, ArmorItem.Type.HELMET, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> TERRA_CHESTPLATE = ITEMS.register("terra_chestplate", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA, ArmorItem.Type.CHESTPLATE, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> TERRA_LEGGINGS = ITEMS.register("terra_leggings", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA, ArmorItem.Type.LEGGINGS, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> TERRA_BOOTS = ITEMS.register("terra_boots", () -> new TerraArmorItem(
            BrutalityArmorMaterials.TERRA, ArmorItem.Type.BOOTS, BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(FULL_SET_PASSIVE, 1))));

    public static final RegistryObject<Item> FRIDGE = ITEMS.register("fridge", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));
    public static final RegistryObject<Item> SMART_FRIDGE = ITEMS.register("smart_fridge", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SALT_SHAKER = ITEMS.register("salt_shaker", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SALTED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> PEPPER_SHAKER = ITEMS.register("pepper_shaker", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.PEPPERED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> SALT_AND_PEPPER = ITEMS.register("salt_and_pepper", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SALTED.get(), 120, 1);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.PEPPERED.get(), 120, 1);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> BAMBOO_STEAMER = ITEMS.register("bamboo_steamer", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.STEAMED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> SMOKE_STONE = ITEMS.register("smoke_stone", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SMOKED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> THE_SMOKEHOUSE = ITEMS.register("the_smokehouse", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SMOKED.get(), 80, 1);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> SUGAR_GLAZE = ITEMS.register("sugar_glaze", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.GLAZED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> RAINBOW_SPRINKLES = ITEMS.register("rainbow_sprinkles", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SPRINKLED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> ROCK_CANDY_RING = ITEMS.register("rock_candy_ring", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CANDIED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> CARAMEL_CRUNCH_MEDALLION = ITEMS.register("caramel_crunch_medallion", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CANDIED.get(), 80, 1);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CARAMELIZED.get(), 80, 1);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> DUNKED_DONUT = ITEMS.register("dunked_donut", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.GLAZED.get(), 80, 1);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SPRINKLED.get(), 80, 1);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> LOLLIPOP_OF_ETERNITY = ITEMS.register("lollipop_of_eternity", () -> new BrutalityCurioItem(
            BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.GLAZED.get(), 120, 2);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CARAMELIZED.get(), 120, 2);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SPRINKLED.get(), 120, 2);
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CANDIED.get(), 120, 2);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> ICE_CREAM_SANDWICH = ITEMS.register("ice_cream_sandwich", () -> new BrutalityCurioItem(
            BrutalityRarities.GLACIAL, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> CROWN_OF_DOMINATION = ITEMS.register("crown_of_domination", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> FACE_PIE = ITEMS.register("face_pie", () -> new BrutalityCurioItem(
            BrutalityRarities.GLACIAL).withAttributes(
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> AQUA_RULER = ITEMS.register("aqua_ruler", () -> new BrutalityCurioItem(
            BrutalityRarities.DIVINE).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_MANA.get(), 100, ADDITION),
            new AttributeContainer(BrutalityAttributes.SPELL_DAMAGE.get(), 0.15, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.CAST_TIME.get(), 0.15, MULTIPLY_BASE),
            new AttributeContainer(ForgeMod.SWIM_SPEED.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> OMNIDIRECTIONAL_MOVEMENT_GEAR = ITEMS.register("omnidirectional_movement_gear", () -> new OmnidirectionalMovementGear(
            BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> QUANTUM_LUBRICANT = ITEMS.register("quantum_lubricant", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(BrutalityAttributes.GROUND_FRICTION.get(), -0.5, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.ARMOR_PENETRATION.get(), 0.15, MULTIPLY_BASE)));

    public static final RegistryObject<Item> AEROPHOBIC_NANOCOATING = ITEMS.register("aerophobic_nanocoating", () -> new BrutalityCurioItem(
            BrutalityRarities.DARK).withAttributes(
            new AttributeContainer(BrutalityAttributes.AIR_FRICTION.get(), -0.5, MULTIPLY_BASE),
            new AttributeContainer(Attributes.ARMOR, 4, ADDITION),
            new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 2, ADDITION)));

    public static final RegistryObject<Item> INERTIA_BOOSTER = ITEMS.register("inertia_booster", () -> new BrutalityCurioItem(
            BrutalityRarities.CONDUCTIVE).withAttributes(
            new AttributeContainer(BrutalityAttributes.GROUND_FRICTION.get(), -0.25, MULTIPLY_BASE),
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> ELBOW_GREASE = ITEMS.register("elbow_grease", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.AIR_FRICTION.get(), -0.25, MULTIPLY_BASE),
            new AttributeContainer(Attributes.ATTACK_SPEED, 0.15, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> EARTHEN_BLESSING = ITEMS.register("earthen_blessing", () -> new BrutalityCurioItem(
            BrutalityRarities.DARK).withAttributes(
            new AttributeContainer(BrutalityAttributes.GROUND_FRICTION.get(), 0.25, MULTIPLY_BASE),
            new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.25, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.BLUNT_DAMAGE.get(), 0.1, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.HAMMER_DAMAGE.get(), 0.1, MULTIPLY_BASE)));

    public static final RegistryObject<Item> PETROLEUM_JELLY = ITEMS.register("petroleum_jelly", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.GROUND_FRICTION.get(), -0.25, MULTIPLY_BASE),
            new AttributeContainer(Attributes.MAX_HEALTH, 3, ADDITION)));

    public static final RegistryObject<Item> ZEPHYR_IN_A_BOTTLE = ITEMS.register("zephyr_in_a_bottle", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY).withAttributes(
            new AttributeContainer(BrutalityAttributes.AIR_FRICTION.get(), -0.25, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.JUMP_HEIGHT.get(), 0.5, ADDITION),
            new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(ForgeMod.ENTITY_GRAVITY.get(), -0.1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PORTABLE_TRAMPOLINE = ITEMS.register("portable_trampoline", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> MASK_OF_MADNESS = ITEMS.register("mask_of_madness", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 50, ADDITION),
            new AttributeContainer(BrutalityAttributes.LIFESTEAL.get(), 0.5, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TAKEN.get(), 0.25, MULTIPLY_BASE),
            new AttributeContainer(BrutalityAttributes.SLASH_DAMAGE.get(), 8, ADDITION)));

    public static final RegistryObject<Item> PERFECT_CELL = ITEMS.register("perfect_cell", () -> new PerfectCell(
            BrutalityRarities.GLACIAL, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> OMEGA_GAUNTLET = ITEMS.register("omega_gauntlet", () -> new BrutalityCurioItem(
            BrutalityRarities.FABLED).withAttributes(
            new AttributeContainer(Attributes.ATTACK_KNOCKBACK, 1.5, ADDITION),
            new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 0.25, MULTIPLY_BASE)));

    public static final RegistryObject<Item> SEARED_SUGAR_BROOCH = ITEMS.register("seared_sugar_brooch", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.CARAMELIZED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (weapon.is(ModTags.Items.GASTRONOMIST_ITEMS) && victim instanceof LivingEntity livingVictim) {
                livingVictim.addEffect(new MobEffectInstance(BrutalityEffects.PULVERIZED.get(), 3, 3));
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> BUTTER_GAUNTLETS = ITEMS.register("butter_gauntlets", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 60))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            if (curio.is(ModTags.Items.GASTRONOMIST_ITEMS) && victim instanceof LivingEntity livingVictim && attacker instanceof Player playerAttacker) {
                if (AttributeCalculationHelper.Luck.roll(attacker, 0.1F, 0.1F))
                    CooldownUtils.validateCurioCooldown(playerAttacker, curio.getItem(), 60, () ->
                            livingVictim.addEffect(new MobEffectInstance(BrutalityEffects.STUNNED.get(), 4, 0)));
            }
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SLICKED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> CHEESE_SAUCE = ITEMS.register("cheese_sauce", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SLICKED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> PIZZA_SLOP = ITEMS.register("pizza_slop", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.SLICKED.get(), 120, 1);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> HOT_SAUCE = ITEMS.register("hot_sauce", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            if (slotContext.entity().tickCount % 40 == 0)
                slotContext.entity().addEffect(new MobEffectInstance(BrutalityEffects.HOT_AND_SPICY.get(), 41, 1));
        }
    });

    public static final RegistryObject<Item> OLIVE_OIL = ITEMS.register("olive_oil", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.OILED.get(), 60, 0);
            return super.onWearerMeleeHit(attacker, weapon, curio, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> EXTRA_VIRGIN_OLIVE_OIL = ITEMS.register("extra_virgin_olive_oil", () -> new BrutalityCurioItem(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            GastronomyHelper.applyEffect(attacker, victim, BrutalityEffects.OILED.get(), 120, 3);
            return super.onWearerHit(attacker, stack, victim, source, amount);
        }
    });

    public static final RegistryObject<Item> DIVERGENT_RECURSOR = ITEMS.register("divergent_recursor", () -> new BaseRecursor(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> CONVERGENT_RECURSOR = ITEMS.register("convergent_recursor", () -> new BaseRecursor(
            BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> INFINITE_RECURSOR = ITEMS.register("infinite_recursor", () -> new BaseRecursor(
            BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> APPRENTICES_MANUAL_TO_BASIC_MULTICASTING = ITEMS.register("apprentices_manual_to_basic_multicasting", () -> new BrutalityCurioItem(
            BrutalityRarities.LEGENDARY, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> WIZARDS_GUIDEBOOK_TO_ADVANCED_MULTICASTING = ITEMS.register("wizards_guidebook_to_advanced_multicasting", () -> new BrutalityCurioItem(
            BrutalityRarities.PRISMATIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> ARCHMAGES_THESIS_TO_MASTERFUL_MULTICASTING = ITEMS.register("archmages_thesis_to_masterful_multicasting", () -> new BrutalityCurioItem(
            BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> PARAGON_OF_THE_FIRST_MAGE = ITEMS.register("paragon_of_the_first_mage", () -> new BrutalityCurioItem(
            BrutalityRarities.GODLY, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))));

    public static final RegistryObject<Item> PI = ITEMS.register("pi", () -> new Pi(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 2)))
            .withAttributes(
                    new AttributeContainer(Attributes.MOVEMENT_SPEED, 0.0314, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_SPEED, 0.0314, MULTIPLY_TOTAL), new AttributeContainer(Attributes.ATTACK_DAMAGE, 3.14, ADDITION)));

    public static final RegistryObject<Item> EXPONENTIAL_CHARM = ITEMS.register("exponential_charm", () -> new BrutalityMathFunctionCurio(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 5)))
            .withAttributes(
                    new AttributeContainer(Attributes.KNOCKBACK_RESISTANCE, 0.0271828F, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.LUCK, 0.0271828F, MULTIPLY_TOTAL),
                    new AttributeContainer(Attributes.ARMOR, 2.71828F, ADDITION),
                    new AttributeContainer(Attributes.ARMOR_TOUGHNESS, 2.71828F, ADDITION),
                    new AttributeContainer(Attributes.MAX_HEALTH, 2.71828F, ADDITION)));

    public static final RegistryObject<Item> ADDITION_CHARM = ITEMS.register("addition", () -> new BrutalityMathFunctionCurio(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 3, ADDITION)));


    public static final RegistryObject<Item> SUBTRACTION = ITEMS.register("subtraction", () -> new BrutalityMathFunctionCurio(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            return Math.max(0, amount - 3);
        }
    });


    public static final RegistryObject<Item> MULTIPLICATION = ITEMS.register("multiplication", () -> new BrutalityMathFunctionCurio(
            Rarity.EPIC).withAttributes(
            new AttributeContainer(Attributes.ATTACK_DAMAGE, 0.175, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> DIVISION = ITEMS.register("division", () -> new BrutalityMathFunctionCurio(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            return Math.max(0, amount / 1.1F);
        }
    });

    public static final RegistryObject<Item> SUM = ITEMS.register("sum", () -> new Sum(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putFloat(SUM_DAMAGE, Math.min(150, tag.getFloat(SUM_DAMAGE) + Math.min(10, amount / 2)));
            return amount;
        }

        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            float stored = stack.getOrCreateTag().getFloat(SUM_DAMAGE);
            stack.getOrCreateTag().putFloat(SUM_DAMAGE, 0);
            return stored;
        }
    });

    public static final RegistryObject<Item> SINE = ITEMS.register("sine", () -> new Sine(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 3))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (attacker.level() instanceof ServerLevel serverLevel)
                return amount + Sine.getCurrentBonus(serverLevel);
            return amount;
        }
    });

    public static final RegistryObject<Item> COSINE = ITEMS.register("cosine", () -> new Cosine(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 3))));

    public static final RegistryObject<Item> SCIENTIFIC_CALCULATOR = ITEMS.register("scientific_calculator", () -> new ScientificCalculator(
            Rarity.EPIC, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BLOOD_STONE = ITEMS.register("blood_stone", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 2))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            attacker.addEffect(new MobEffectInstance(TerramityModMobEffects.LIFESTEAL.get(), 30, 0));
            return amount * 1.3F;
        }
    });

    public static final RegistryObject<Item> RAGE_STONE = ITEMS.register("rage_stone", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerMeleeHit(LivingEntity attacker, ItemStack weapon, ItemStack curio, Entity victim, DamageSource source, float amount) {
            return amount * 1.3F;
        }
    }.withAttributes(
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> PAIN_CATALYST = ITEMS.register("pain_catalyst", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> RAMPAGE_CLOCK = ITEMS.register("rampage_clock", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> BLOOD_HOWL_PENDANT = ITEMS.register("blood_howl_pendant", () -> new BloodHowlPendant(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> SPITE_SHARD = ITEMS.register("spite_shard", () -> new SpiteShard(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1, 20)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 50, ADDITION)));

    public static final RegistryObject<Item> HATE_SIGIL = ITEMS.register("hate_sigil", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 100, ADDITION),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 0.5, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> HEART_OF_WRATH = ITEMS.register("heart_of_wrath", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.MAX_RAGE.get(), 150, ADDITION),
            new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 1, MULTIPLY_TOTAL)));

    public static final RegistryObject<Item> EYE_FOR_VIOLENCE = ITEMS.register("eye_for_violence", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> BATTLE_SCARS = ITEMS.register("battle_scars", () -> new BattleScars(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));


    public static final RegistryObject<Item> MECHANICAL_AORTA = ITEMS.register("mechanical_aorta", () -> new MechanicalAorta(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(LORE, 1),
            new ItemDescriptionComponent(PASSIVE, 1)))
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.DAMAGE_TO_RAGE_RATIO.get(), 2, MULTIPLY_TOTAL), new AttributeContainer(
                            BrutalityAttributes.RAGE_TIME.get(), -0.5, MULTIPLY_TOTAL)));


    public static final RegistryObject<Item> BLOOD_PULSE_GAUNTLETS = ITEMS.register("blood_pulse_gauntlets", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))) {
        @Override
        public float onWearerHit(LivingEntity attacker, ItemStack stack, Entity victim, DamageSource source, float amount) {
            if (attacker.hasEffect(BrutalityEffects.ENRAGED.get())) {
                BloodExplosion explosion = new BloodExplosion(attacker.level(), attacker, null, null, source.getSourcePosition().x, source.getSourcePosition().y, source.getSourcePosition().z, 3, false, Level.ExplosionInteraction.NONE);
                ModExplosionHelper.Server.explode(explosion, attacker.level(), true);
            }
            return amount;
        }
    }
            .withAttributes(
                    new AttributeContainer(BrutalityAttributes.RAGE_LEVEL.get(), 2, ADDITION)));

    public static final RegistryObject<Item> BROKEN_CONTROLLER = ITEMS.register("broken_controller", () -> new BrutalityCurioItem(
            BrutalityRarities.CONDUCTIVE).withAttributes(
            new AttributeContainer(BrutalityAttributes.RAGE_LEVEL.get(), 3, ADDITION)));

    public static final RegistryObject<Item> FURY_BAND = ITEMS.register("fury_band", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN).withAttributes(
            new AttributeContainer(BrutalityAttributes.RAGE_TIME.get(), 0.75, MULTIPLY_TOTAL),
            new AttributeContainer(BrutalityAttributes.RAGE_LEVEL.get(), 1, ADDITION)));

    public static final RegistryObject<Item> GRUDGE_TOTEM = ITEMS.register("grudge_totem", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1))));

    public static final RegistryObject<Item> ANGER_MANAGEMENT = ITEMS.register("anger_management", () -> new BrutalityCurioItem(
            BrutalityRarities.STYGIAN, List.of(
            new ItemDescriptionComponent(PASSIVE, 1),
            new ItemDescriptionComponent(ACTIVE, 1, DistExecutor.unsafeRunForDist(() -> Keybindings::getRageActivateKey, () -> () -> null)))));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
