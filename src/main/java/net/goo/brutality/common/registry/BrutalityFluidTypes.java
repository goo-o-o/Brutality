package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.fluid.LiquifiedManaFluidType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<FluidType> LIQUIFIED_MANA_TYPE = FLUID_TYPES.register("liquified_mana", () -> new LiquifiedManaFluidType(FluidType.Properties.create()
            .density(-1000).lightLevel(15).canConvertToSource(false).canExtinguish(false).canHydrate(false).canSwim(true).fallDistanceModifier(0.5F)
            .canDrown(false).canPushEntity(true).motionScale(0.01F).temperature(-10).supportsBoating(false).viscosity(4500)
    ));


    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
