package net.goo.armament.entity;

import net.goo.armament.Armament;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ArmaVisualTypes {
    public static final ResourceKey<Registry<ArmaVisualType>> VISUALS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Armament.MOD_ID, "arma_visuals"));

    public static final DeferredRegister<ArmaVisualType> VISUALS = DeferredRegister.create(VISUALS_KEY, Armament.MOD_ID);

    public static final RegistryObject<ArmaVisualType> TERRA_BEAM = addVisual(new ArmaVisualType("terra_beam", ArmaVisualModel.FLAT, null, 0, 0, 2.5, true, true, false));

    public static final RegistryObject<ArmaVisualType> CRESCENTIA_STRIKE = addVisual(new ArmaVisualType("crescentia_strike", ArmaVisualModel.FLAT, ArmaVisualAnimation.noAnimWithLifespan(12), 6, 2, 3.5, false, true, true));


    public static RegistryObject<ArmaVisualType> addVisual(ArmaVisualType type) {
        return VISUALS.register(type.getName(), () -> type);
    }

}
