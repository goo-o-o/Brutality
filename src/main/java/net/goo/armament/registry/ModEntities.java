package net.goo.armament.registry;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.*;
import net.goo.armament.entity.helper.ArmaEffectEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Armament.MOD_ID);

    public static final RegistryObject<EntityType<ArmaEffectEntity>> ARMA_PROJECTILE_ENTITY = ENTITY_TYPES.register("arma_projectile", () -> EntityType.Builder.of(ArmaEffectEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(64).build(Armament.prefix("arma_projectile").toString()));

    public static final RegistryObject<EntityType<ThrownThunderbolt>> THROWN_THUNDERBOLT_ENTITY =
            ENTITY_TYPES.register("thrown_zeus_thunderbolt",
                    () -> EntityType.Builder.<ThrownThunderbolt>of(ThrownThunderbolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .setUpdateInterval(20)
                            .build("thrown_zeus_thunderbolt"));

    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY =
            ENTITY_TYPES.register("cruel_sun",
                    () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
                            .sized(3.0f, 3.0f)
                            .build("cruel_sun"));

    public static final RegistryObject<EntityType<TerraBeam>> TERRA_BEAM =
            ENTITY_TYPES.register("terra_beam", () -> EntityType.Builder.of(TerraBeam::new,
                    MobCategory.MISC).sized(2F, 1F).build("terra_beam"));

    public static final RegistryObject<EntityType<ExcaliburBeam>> EXCALIBUR_BEAM =
            ENTITY_TYPES.register("excalibur_beam", () -> EntityType.Builder.of(ExcaliburBeam::new,
                    MobCategory.MISC).sized(10F, 1F).build("terra_beam"));

    public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE_ENTITY =
            ENTITY_TYPES.register("black_hole",
                    () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .setUpdateInterval(1)
                            .build("black_hole"));

    public static final RegistryObject<EntityType<SupernovaPortal>> SUPERNOVA_PORTAL =
            ENTITY_TYPES.register("supernova_portal",
                    () -> EntityType.Builder.of(SupernovaPortal::new,
                            MobCategory.MISC).sized(10F, 0.1F).build("supernova_portal"));

    public static final RegistryObject<EntityType<SupernovaAsteroid>> SUPERNOVA_ASTEROID =
            ENTITY_TYPES.register("supernova_asteroid",
                    () -> EntityType.Builder.of((EntityType<SupernovaAsteroid> pEntityType, Level pLevel) -> new SupernovaAsteroid(pEntityType, pLevel, 0),
                            MobCategory.MISC).sized(1F, 1F)
                            .setUpdateInterval(1).build("supernova_asteroid"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}