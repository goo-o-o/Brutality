package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.util.RandomDeathMessageDamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrutalityDamageTypes {
    public static final ResourceKey<DamageType> DEMATERIALIZE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "dematerialize")
    );
    public static final ResourceKey<DamageType> LAST_PRISM = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "last_prism")
    );

    public static final ResourceKey<DamageType> THROWING_PIERCE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "throwing_pierce")
    );
    public static final ResourceKey<DamageType> THROWING_BLUNT = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "throwing_blunt")
    );

    public static DamageSource last_prism(Entity indirectEntity) {
        return new DamageSource(indirectEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(LAST_PRISM),
                indirectEntity
        );
    }
    public static DamageSource throwing_pierce(@Nullable Entity indirectEntity, Entity directEntity) {
        return new RandomDeathMessageDamageSource(directEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(THROWING_PIERCE),
                directEntity,
                indirectEntity,
                8
        );
    }

    public static DamageSource throwing_blunt(@Nullable Entity indirectEntity, Entity directEntity) {
        return new RandomDeathMessageDamageSource(directEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(THROWING_BLUNT),
                directEntity,
                indirectEntity,
                8
        );
    }

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
                    new DamageType("brutality.dematerialize", 0.1F));
            helper.register(
                    BrutalityDamageTypes.THROWING_PIERCE.location(),
                    new DamageType("brutality.throwing_pierce", 0.1F));
            helper.register(
                    BrutalityDamageTypes.THROWING_BLUNT.location(),
                    new DamageType("brutality.throwing_blunt", 0.1F));
            helper.register(
                    BrutalityDamageTypes.LAST_PRISM.location(),
                    new DamageType("brutality.last_prism", 0));
        });
    }

}
