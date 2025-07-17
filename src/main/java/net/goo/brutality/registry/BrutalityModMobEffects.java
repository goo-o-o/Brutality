package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.mob_effect.*;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityMobEffects {
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


    public static final RegistryObject<MobEffect> PULVERIZED = EFFECTS.register("pulverized",
            () -> new PulverizedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{200, 200, 200})));

    public static final RegistryObject<MobEffect> RUINED = EFFECTS.register("ruined",
            () -> new RuinedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{12, 29, 28})));

    public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned",
            () -> new StunnedEffect(MobEffectCategory.HARMFUL, BrutalityTooltipHelper.rgbToInt(new int[]{255, 255, 0})));




    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
