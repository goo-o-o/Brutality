package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.goo.armament.mob_effect.RadiationEffect;
import net.goo.armament.util.ModUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Armament.MOD_ID);

    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation",
            () -> new RadiationEffect(MobEffectCategory.HARMFUL, ModUtils.ModTooltipHelper.rgbToInt(new int[]{0, 250, 68}))
    );

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
