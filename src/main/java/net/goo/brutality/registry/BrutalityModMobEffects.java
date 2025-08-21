package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.mob_effect.*;
import net.goo.brutality.mob_effect.gastronomy.*;
import net.goo.brutality.mob_effect.gastronomy.dry.*;
import net.goo.brutality.mob_effect.gastronomy.wet.*;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Brutality.MOD_ID);

    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{0, 250, 68})));
    public static final RegistryObject<MobEffect> STONEFORM = EFFECTS.register("stoneform",
            () -> new StoneformEffect(MobEffectCategory.BENEFICIAL, BrutalityTooltipHelper.rgbToInt(new int[]{210, 160, 100})));


    public static final RegistryObject<MobEffect> NEUTRAL = EFFECTS.register("neutral",
            () -> new NeutralEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{150, 150, 150})));
    public static final RegistryObject<MobEffect> HAPPY = EFFECTS.register("happy",
            () -> new HappyEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 220, 20})));
    public static final RegistryObject<MobEffect> SAD = EFFECTS.register("sad",
            () -> new SadEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{0, 50, 150})));
    public static final RegistryObject<MobEffect> ANGRY = EFFECTS.register("angry",
            () -> new AngryEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 40, 0})));

    public static final RegistryObject<MobEffect> ENRAGED = EFFECTS.register("enraged",
            () -> new EnragedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 0, 0})));


    public static final RegistryObject<MobEffect> THE_VOID = EFFECTS.register("the_void",
            () -> new TheVoidEffect(MobEffectCategory.BENEFICIAL, BrutalityTooltipHelper.rgbToInt(new int[]{0, 0, 0})));

    public static final RegistryObject<MobEffect> PULVERIZED = EFFECTS.register("pulverized",
            () -> new PulverizedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 200, 200})));

    public static final RegistryObject<MobEffect> RUINED = EFFECTS.register("ruined",
            () -> new RuinedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{12, 29, 28})));

    public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned",
            () -> new StunnedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 255, 0})));

    public static final RegistryObject<MobEffect> MIRACLE_BLIGHT = EFFECTS.register("miracle_blight",
            () -> new MiracleBlightEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 255, 0})));
    public static final RegistryObject<MobEffect> CAFFEINATED = EFFECTS.register("caffeinated",
            () -> new CaffeinatedEffect(MobEffectCategory.BENEFICIAL, BrutalityTooltipHelper.rgbToInt(new int[]{140, 33, 0})));
    public static final RegistryObject<MobEffect> HOT_AND_SPICY = EFFECTS.register("hot_and_spicy",
            () -> new HotAndSpicyEffect(MobEffectCategory.BENEFICIAL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 68, 13})));

    public static final RegistryObject<MobEffect> SALTED = EFFECTS.register("salted",
            () -> new SaltedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 255, 255})));
    public static final RegistryObject<MobEffect> PEPPERED = EFFECTS.register("peppered",
            () -> new PepperedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{126, 116, 93})));
    public static final RegistryObject<MobEffect> SCORED = EFFECTS.register("scored",
            () -> new ScoredEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 200, 200})));
    public static final RegistryObject<MobEffect> MASHED = EFFECTS.register("mashed",
            () -> new MashedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 200, 200})));

    public static final RegistryObject<MobEffect> SLICKED = EFFECTS.register("slicked",
            () -> new SlickedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{222, 131, 4})));
    public static final RegistryObject<MobEffect> OILED = EFFECTS.register("oiled",
            () -> new OiledEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{80, 82, 37})));
    public static final RegistryObject<MobEffect> MARINATED = EFFECTS.register("marinated",
            () -> new MarinatedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{125, 150, 0})));
    public static final RegistryObject<MobEffect> STEAMED = EFFECTS.register("steamed",
            () -> new SteamedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{189, 205, 222})));
    public static final RegistryObject<MobEffect> SMOKED = EFFECTS.register("smoked",
            () -> new SmokedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{75, 75, 75})));
    public static final RegistryObject<MobEffect> CANDIED = EFFECTS.register("candied",
            () -> new CandiedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255,192,203})));
    public static final RegistryObject<MobEffect> CARAMELIZED = EFFECTS.register("caramelized",
            () -> new CaramelizedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{204, 156, 80})));
    public static final RegistryObject<MobEffect> GLAZED = EFFECTS.register("glazed",
            () -> new GlazedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255,255,255})));
    public static final RegistryObject<MobEffect> SPRINKLED = EFFECTS.register("sprinkled",
            () -> new SprinkledEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{97, 255, 160})));

    public static final RegistryObject<MobEffect> GRACE = EFFECTS.register("grace",
            () -> new GraceEffect(MobEffectCategory.BENEFICIAL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 253, 153})));
    public static final RegistryObject<MobEffect> LIGHT_BOUND = EFFECTS.register("light_bound",
            () -> new LightBoundEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 253, 153})));

    public static final RegistryObject<MobEffect> WEIGHTLESSNESS = EFFECTS.register("weightlessness",
            () -> new WeightlessnessEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{0, 200, 0}), -0.1));
    public static final RegistryObject<MobEffect> HYPERGRAVITY = EFFECTS.register("hypergravity",
            () -> new HypergravityEffect(MobEffectCategory.NEUTRAL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 0, 0}), 0.25));



    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
