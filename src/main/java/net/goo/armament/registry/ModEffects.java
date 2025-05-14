package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.goo.armament.mob_effect.RadiationEffect;
import net.goo.armament.mob_effect.StoneformEffect;
import net.goo.armament.util.helpers.ModTooltipHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Armament.MOD_ID);

    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, ModTooltipHelper.rgbToInt(new int[]{0, 250, 68})));
    public static final RegistryObject<MobEffect> STONEFORM = EFFECTS.register("stoneform",
            () -> new StoneformEffect(MobEffectCategory.BENEFICIAL, ModTooltipHelper.rgbToInt(new int[]{210, 160, 100})));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
