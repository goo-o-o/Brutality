package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Brutality.MOD_ID);

    public static final RegistryObject<ForgeFlowingFluid> LIQUIFIED_MANA_SOURCE = FLUIDS.register("liquified_mana",
            () -> new ForgeFlowingFluid.Source(BrutalityFluids.createProperties())); // Call helper

    public static final RegistryObject<ForgeFlowingFluid> LIQUIFIED_MANA_FLOWING = FLUIDS.register("liquified_mana_flowing",
            () -> new ForgeFlowingFluid.Flowing(BrutalityFluids.createProperties())); // Call helper

    private static ForgeFlowingFluid.Properties createProperties() {
        return new ForgeFlowingFluid.Properties(
                BrutalityFluidTypes.LIQUIFIED_MANA_TYPE,
                LIQUIFIED_MANA_SOURCE,
                LIQUIFIED_MANA_FLOWING)
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2)
                .block(BrutalityBlocks.LIQUIFIED_MANA)
                .bucket(BrutalityItems.LIQUIFIED_MANA_BUCKET);
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}