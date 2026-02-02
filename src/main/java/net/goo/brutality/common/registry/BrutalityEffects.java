package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.mob_effect.*;
import net.goo.brutality.common.mob_effect.gastronomy.CaffeinatedEffect;
import net.goo.brutality.common.mob_effect.gastronomy.HotAndSpicyEffect;
import net.goo.brutality.common.mob_effect.gastronomy.MashedEffect;
import net.goo.brutality.common.mob_effect.gastronomy.ScoredEffect;
import net.goo.brutality.common.mob_effect.gastronomy.dry.*;
import net.goo.brutality.common.mob_effect.gastronomy.wet.*;
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Brutality.MOD_ID);

    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 0, 250, 68)));
    public static final RegistryObject<MobEffect> STONEFORM = EFFECTS.register("stoneform",
            () -> new StoneformEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 210, 160, 100)));


    public static final RegistryObject<MobEffect> NEUTRAL = EFFECTS.register("neutral",
            () -> new NeutralEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 150, 150, 150)));
    public static final RegistryObject<MobEffect> HAPPY = EFFECTS.register("happy",
            () -> new HappyEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 255, 220, 20)));
    public static final RegistryObject<MobEffect> SAD = EFFECTS.register("sad",
            () -> new SadEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 0, 50, 150)));
    public static final RegistryObject<MobEffect> ANGRY = EFFECTS.register("angry",
            () -> new AngryEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 255, 40, 0)));

    public static final RegistryObject<MobEffect> ENRAGED = EFFECTS.register("enraged",
            () -> new EnragedEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 255, 0, 0)));
    public static final RegistryObject<MobEffect> TRANQUILITY = EFFECTS.register("tranquility",
            () -> new TranquilityEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 0, 255, 255)));


    public static final RegistryObject<MobEffect> THE_VOID = EFFECTS.register("the_void",
            () -> new TheVoidEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 0, 0)));

    public static final RegistryObject<MobEffect> PULVERIZED = EFFECTS.register("pulverized",
            () -> new PulverizedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 200, 200)));

    public static final RegistryObject<MobEffect> RUINED = EFFECTS.register("ruined",
            () -> new RuinedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 12, 29, 28)));

    public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned",
            () -> new StunnedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 0)));

    public static final RegistryObject<MobEffect> MIRACLE_BLIGHT = EFFECTS.register("miracle_blight",
            () -> new MiracleBlightEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 255, 0)));
    public static final RegistryObject<MobEffect> CAFFEINATED = EFFECTS.register("caffeinated",
            () -> new CaffeinatedEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 140, 33, 0)));
    public static final RegistryObject<MobEffect> HOT_AND_SPICY = EFFECTS.register("hot_and_spicy",
            () -> new HotAndSpicyEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 68, 13)));

    public static final RegistryObject<MobEffect> SALTED = EFFECTS.register("salted",
            () -> new SaltedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 255)));
    public static final RegistryObject<MobEffect> PEPPERED = EFFECTS.register("peppered",
            () -> new PepperedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 126, 116, 93)));
    public static final RegistryObject<MobEffect> SCORED = EFFECTS.register("scored",
            () -> new ScoredEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 200, 200)));
    public static final RegistryObject<MobEffect> MASHED = EFFECTS.register("mashed",
            () -> new MashedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 200, 200)));

    public static final RegistryObject<MobEffect> SLICKED = EFFECTS.register("slicked",
            () -> new SlickedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 222, 131, 4)));
    public static final RegistryObject<MobEffect> OILED = EFFECTS.register("oiled",
            () -> new OiledEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 80, 82, 37)));
    public static final RegistryObject<MobEffect> STEAMED = EFFECTS.register("steamed",
            () -> new SteamedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 189, 205, 222)));
    public static final RegistryObject<MobEffect> SMOKED = EFFECTS.register("smoked",
            () -> new SmokedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 75, 75, 75)));
    public static final RegistryObject<MobEffect> CANDIED = EFFECTS.register("candied",
            () -> new CandiedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 192, 203)));
    public static final RegistryObject<MobEffect> CARAMELIZED = EFFECTS.register("caramelized",
            () -> new CaramelizedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 204, 156, 80)));
    public static final RegistryObject<MobEffect> GLAZED = EFFECTS.register("glazed",
            () -> new GlazedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 255)));
    public static final RegistryObject<MobEffect> SPRINKLED = EFFECTS.register("sprinkled",
            () -> new SprinkledEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 97, 255, 160)));

    public static final RegistryObject<MobEffect> GRACE = EFFECTS.register("grace",
            () -> new GraceEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 253, 153)));
    public static final RegistryObject<MobEffect> LIGHT_BOUND = EFFECTS.register("light_bound",
            () -> new LightBoundEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 253, 153)));

    public static final RegistryObject<MobEffect> WEIGHTLESSNESS = EFFECTS.register("weightlessness",
            () -> new WeightlessnessEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 0, 200, 0), -0.1));
    public static final RegistryObject<MobEffect> HYPERGRAVITY = EFFECTS.register("hypergravity",
            () -> new HypergravityEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 200, 0, 0), 0.25));

    public static final RegistryObject<MobEffect> MANA_FATIGUE = EFFECTS.register("mana_fatigue",
            () -> new ManaFatigueEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 200, 0, 0)));
    public static final RegistryObject<MobEffect> FRUGAL_MANA = EFFECTS.register("frugal_mana",
            () -> new FrugalManaEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 0, 200, 0)));

    public static final RegistryObject<MobEffect> ULTRA_DODGE = EFFECTS.register("ultra_dodge",
            () -> new UltraDodgeEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 245, 255)));
    public static final RegistryObject<MobEffect> DODGE_COOLDOWN = EFFECTS.register("dodge_cooldown",
            () -> new DodgeCooldownEffect(MobEffectCategory.HARMFUL, -16750951));

    public static final RegistryObject<MobEffect> PRECISION = EFFECTS.register("precision",
            () -> new PrecisionEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 0, 0)));

    public static final RegistryObject<MobEffect> MAGIC_POWER = EFFECTS.register("magic_power",
            () -> new MagicPowerEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 200, 0, 0)));
    public static final RegistryObject<MobEffect> MAGIC_SICKNESS = EFFECTS.register("magic_sickness",
            () -> new MagicSicknessEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 0, 200, 0)));

    public static final RegistryObject<MobEffect> SIPHONED = EFFECTS.register("siphoned",
            () -> new SiphonedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 0, 0)));
    public static final RegistryObject<MobEffect> TERRAMITICULOSIS = EFFECTS.register("terramiticulosis",
            () -> new TerramiticulosisEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 255)));


    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
