package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrutalityDamageTypes {
    public static final ResourceKey<DamageType> DEMATERIALIZE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "dematerialize")
    );

    public static DamageSource dematerialize(Entity causer) {
        return new DamageSource(
                causer.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DEMATERIALIZE),
                causer);
    }

    @SubscribeEvent
    public static void registerDamageTypes(RegisterEvent event) {
        event.register(Registries.DAMAGE_TYPE, helper -> {
            helper.register(
                    BrutalityDamageTypes.DEMATERIALIZE.location(),
                    new DamageType("brutality.dematerialize", 0.1F) // Adjust scaling if needed
            );
        });
    }

}
