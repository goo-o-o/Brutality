package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.mob_effect.*;
import net.goo.brutality.common.mob_effect.gastronomy.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BrutalityEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Brutality.MOD_ID);

    public static final RegistryObject<MobEffect> DESPAIR = EFFECTS.register("despair",
            () -> new BaseMobEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 0, 0, 0)));
    public static final RegistryObject<MobEffect> HOPE = EFFECTS.register("hope",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 200, 0)));
    public static final RegistryObject<MobEffect> FORTITUDE = EFFECTS.register("fortitude",
            () -> new FortitudeEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 255, 0)));
    public static final RegistryObject<MobEffect> RESILIENCE = EFFECTS.register("resilience",
            () -> new ResilienceEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 255, 0)));

    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 0, 250, 68)));
    public static final RegistryObject<MobEffect> STONEFORM = EFFECTS.register("stoneform",
            () -> new StoneformEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 210, 160, 100)));

    public static final RegistryObject<MobEffect> FRACTIONED = EFFECTS.register("fractioned",
            () -> new FractionedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 255)));

    public static final RegistryObject<MobEffect> AVARICE = EFFECTS.register("avarice",
            () -> new AvariceEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 255, 255, 0)));
    public static final RegistryObject<MobEffect> BLOCKCHAINED = EFFECTS.register("blockchained",
            () -> new BlockchainedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 0)));

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

    public static final RegistryObject<MobEffect> REDACTED = EFFECTS.register("redacted",
            () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 0, 0)));

    public static final RegistryObject<MobEffect> PULVERIZED = EFFECTS.register("pulverized",
            () -> new PulverizedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 200, 200)));

    public static final RegistryObject<MobEffect> RUINED = EFFECTS.register("ruined",
            () -> new BaseMobEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 12, 29, 28)));

    public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned",
            () -> new StunnedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 0)));

    public static final RegistryObject<MobEffect> MIRACLE_BLIGHT = EFFECTS.register("miracle_blight",
            () -> new MiracleBlightEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 255, 0)));

    public static final RegistryObject<MobEffect> CAFFEINATED = EFFECTS.register("caffeinated",
            () -> new CaffeinatedEffect(MobEffectCategory.BENEFICIAL, GastronomyEffect.Type.WET, 0F, 0F));
    public static final RegistryObject<MobEffect> HOT_AND_SPICY = EFFECTS.register("hot_and_spicy",
            () -> new HotAndSpicyEffect(MobEffectCategory.BENEFICIAL, GastronomyEffect.Type.WET, 0.05F, 0F));

    public static final RegistryObject<MobEffect> SCORED = registerGastronomyEffect("scored", () -> new GastronomyEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.0F, 0.0F));
    public static final RegistryObject<MobEffect> MASHED = registerGastronomyEffect("mashed", () -> new GastronomyEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0.0F, 0.0F));

    public static final RegistryObject<MobEffect> SEARED = registerGastronomyEffect("seared", () -> new SearedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.05F, 0.05F));
    public static final RegistryObject<MobEffect> BARK = registerGastronomyEffect("bark", () -> new BarkEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.05F, 0.05F));
    public static final RegistryObject<MobEffect> FRIED = registerGastronomyEffect("fried", () -> new FriedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.05F, 0.05F));
    public static final RegistryObject<MobEffect> SALTED = registerGastronomyEffect("salted", () -> new GastronomyParticleEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.25F, 0.05F, BrutalityParticles.SALT_PARTICLE));
    public static final RegistryObject<MobEffect> PEPPERED = registerGastronomyEffect("peppered", () -> new GastronomyParticleEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.25F, 0.05F, BrutalityParticles.PEPPER_PARTICLE));
    public static final RegistryObject<MobEffect> SLICKED = registerGastronomyEffect("slicked", () -> new SlickedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0.05F, 0.15F, BrutalityParticles.SLICKED_PARTICLE));
    public static final RegistryObject<MobEffect> OILED = registerGastronomyEffect("oiled", () -> new OiledEffect(MobEffectCategory.NEUTRAL, GastronomyEffect.Type.WET, 0.1F, 0F, BrutalityParticles.OILED_PARTICLE));
    public static final RegistryObject<MobEffect> STEAMED = registerGastronomyEffect("steamed", () -> new SteamedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0F, 0F, BrutalityParticles.STEAM_PARTICLE));
    public static final RegistryObject<MobEffect> SMOKED = registerGastronomyEffect("smoked", () -> new GastronomyParticleEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.05F, 0.2F, () -> ParticleTypes.LARGE_SMOKE));
    public static final RegistryObject<MobEffect> CANDIED = registerGastronomyEffect("candied", () -> new CandiedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.05F, 0.01F));
    public static final RegistryObject<MobEffect> CARAMELIZED = registerGastronomyEffect("caramelized", () -> new GastronomyEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0.125F, 0F));
    public static final RegistryObject<MobEffect> GLAZED = registerGastronomyEffect("glazed", () -> new GlazedEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0.05F, 0.1F));
    public static final RegistryObject<MobEffect> SPRINKLED = registerGastronomyEffect("sprinkled", () -> new GastronomyEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.DRY, 0.15F, 0.05F));
    public static final RegistryObject<MobEffect> PICKLED = registerGastronomyEffect("pickled", () -> new GastronomyEffect(MobEffectCategory.HARMFUL, GastronomyEffect.Type.WET, 0.05F, 0.1F));

    public static final RegistryObject<MobEffect> GRACE = EFFECTS.register("grace",
            () -> new GraceEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 253, 153)));
    public static final RegistryObject<MobEffect> LIGHT_BOUND = EFFECTS.register("light_bound",
            () -> new LightBoundEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 253, 153)));

    public static final RegistryObject<MobEffect> WEIGHTLESSNESS = EFFECTS.register("weightlessness",
            () -> new WeightlessnessEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 0, 200, 0), -0.1));
    public static final RegistryObject<MobEffect> HYPERGRAVITY = EFFECTS.register("hypergravity",
            () -> new HypergravityEffect(MobEffectCategory.NEUTRAL, FastColor.ARGB32.color(255, 200, 0, 0), 0.25));


    public static final RegistryObject<MobEffect> ULTRA_DODGE = EFFECTS.register("ultra_dodge",
            () -> new UltraDodgeEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 245, 255)));
    public static final RegistryObject<MobEffect> DODGE_COOLDOWN = EFFECTS.register("dodge_cooldown",
            () -> new DodgeCooldownEffect(MobEffectCategory.HARMFUL, -16750951));

    public static final RegistryObject<MobEffect> PRECISION = EFFECTS.register("precision",
            () -> new PrecisionEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 255, 0, 0)));

    // Increases spell damage 1% per levels
    public static final RegistryObject<MobEffect> ARCANE_SURGE = EFFECTS.register("arcane_surge",
            () -> new ArcaneSurgeEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 200, 0)));
    // Reduces spell damage 1% per levels
    public static final RegistryObject<MobEffect> MANA_BLIGHT = EFFECTS.register("mana_blight",
            () -> new ManaBlightEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 0, 0)));
    // Reduces mana regen 10% per levels
    public static final RegistryObject<MobEffect> CALCIFIED_PATHWAYS = EFFECTS.register("calcified_pathways",
            () -> new CalcifiedPathwaysEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 0, 0)));
    // Increases mana regen 10% per levels
    public static final RegistryObject<MobEffect> CELESTIAL_FLUX = EFFECTS.register("celestial_flux",
            () -> new CelestialFluxEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 200, 0)));
    // Increases mana cost 10% per levels
    public static final RegistryObject<MobEffect> ARCANE_BURNOUT = EFFECTS.register("arcane_burnout",
            () -> new ArcaneBurnoutEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 0, 0)));
    // Reduces mana cost 10% per levels
    public static final RegistryObject<MobEffect> ETHERIC_FLOW = EFFECTS.register("etheric_flow",
            () -> new EthericFlowEffect(MobEffectCategory.BENEFICIAL, FastColor.ARGB32.color(255, 0, 200, 0)));


    public static final RegistryObject<MobEffect> SIPHONED = EFFECTS.register("siphoned",
            () -> new SiphonedEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 200, 0, 0)));
    public static final RegistryObject<MobEffect> TERRAMITICULOSIS = EFFECTS.register("terramiticulosis",
            () -> new BaseMobEffect(MobEffectCategory.HARMFUL, FastColor.ARGB32.color(255, 255, 255, 255)));


    private static RegistryObject<MobEffect> registerGastronomyEffect(String name, Supplier<GastronomyEffect> effectSupplier) {
        return EFFECTS.register(name, () -> {
            GastronomyEffect effect = effectSupplier.get();
            effect.initAttribute(name);
            return effect;
        });
    }

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
