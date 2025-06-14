package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.goo.brutality.entity.base.SwordWave;
import net.goo.brutality.entity.custom.*;
import net.goo.brutality.entity.custom.arrow.LightArrow;
import net.goo.brutality.entity.custom.beam.ExcaliburBeam;
import net.goo.brutality.entity.custom.beam.TerraBeam;
import net.goo.brutality.entity.custom.trident.ThrownGungnir;
import net.goo.brutality.entity.custom.trident.ThrownKnife;
import net.goo.brutality.entity.custom.trident.ThrownThunderbolt;
import net.goo.brutality.entity.custom.trident.physics_projectile.ThrownCabbage;
import net.goo.brutality.entity.custom.trident.physics_projectile.ThrownWintermelon;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<EntityType<SummonedStray>> SUMMONED_STRAY =
            ENTITY_TYPES.register("summoned_stray", () -> EntityType.Builder.<SummonedStray>of(SummonedStray::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(Brutality.MOD_ID, "summoned_stray").toString()));

    public static final RegistryObject<EntityType<SwordWave>> SWORD_WAVE =
            ENTITY_TYPES.register("sword_wave", () -> EntityType.Builder.of(SwordWave::new,
                    MobCategory.MISC).sized(1F, 0.25F).build("sword_wave"));

    public static final RegistryObject<EntityType<ThrownThunderbolt>> THROWN_THUNDERBOLT_ENTITY =
            ENTITY_TYPES.register("thrown_thunderbolt",
                    () -> EntityType.Builder.<ThrownThunderbolt>of(ThrownThunderbolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .setUpdateInterval(20)
                            .build("thrown_thunderbolt"));


    public static final RegistryObject<EntityType<BrutalityAbstractTrident>> THROWN_GUNGNIR_ENTITY =
            ENTITY_TYPES.register("thrown_gungnir",
                    () -> EntityType.Builder.<BrutalityAbstractTrident>of(ThrownGungnir::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .setUpdateInterval(20)
                            .build("thrown_gungnir"));

    public static final RegistryObject<EntityType<ThrownKnife>> THROWN_KNIFE_ENTITY =
            ENTITY_TYPES.register("thrown_knife",
                    () -> EntityType.Builder.<ThrownKnife>of(ThrownKnife::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .setUpdateInterval(20)
                            .build("thrown_knife"));

    public static final RegistryObject<EntityType<ThrownCabbage>> THROWN_CABBAGE_ENTITY =
            ENTITY_TYPES.register("thrown_cabbage",
                    () -> EntityType.Builder.<ThrownCabbage>of(ThrownCabbage::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .setUpdateInterval(20)
                            .build("thrown_cabbage"));

    public static final RegistryObject<EntityType<ThrownWintermelon>> THROWN_WINTERMELON_ENTITY =
            ENTITY_TYPES.register("thrown_wintermelon",
                    () -> EntityType.Builder.<ThrownWintermelon>of(ThrownWintermelon::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .setUpdateInterval(20)
                            .build("thrown_wintermelon"));

    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY =
            ENTITY_TYPES.register("cruel_sun",
                    () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
                            .sized(3.0f, 3.0f)
                            .build("cruel_sun"));

    public static final RegistryObject<EntityType<TerraBeam>> TERRA_BEAM =
            ENTITY_TYPES.register("terra_beam", () -> EntityType.Builder.of(TerraBeam::new,
                    MobCategory.MISC).sized(1F, 3F).build("terra_beam"));

    public static final RegistryObject<EntityType<ExcaliburBeam>> EXCALIBUR_BEAM =
            ENTITY_TYPES.register("excalibur_beam", () -> EntityType.Builder.of(ExcaliburBeam::new,
                            MobCategory.MISC).sized(9F, 1F).setUpdateInterval(1).setTrackingRange(128)
                    .build("excalibur_beam"));

    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE_ENTITY =
            ENTITY_TYPES.register("black_hole",
                    () -> EntityType.Builder.of(BlackHoleEntity::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .setUpdateInterval(1)
                            .build("black_hole"));

    public static final RegistryObject<EntityType<ExplosionRay>> EXPLOSION_RAY =
            ENTITY_TYPES.register("explosion_ray",
                    () -> EntityType.Builder.of(ExplosionRay::new,
                            MobCategory.MISC).sized(1, 1).build("explosion_ray"));

    public static final RegistryObject<EntityType<MagicExplosion>> MAGIC_EXPLOSION =
            ENTITY_TYPES.register("magic_explosion",
                    () -> EntityType.Builder.of(MagicExplosion::new,
                            MobCategory.MISC).sized(1, 1).build("magic_explosion"));

    public static final RegistryObject<EntityType<SupernovaPortal>> SUPERNOVA_PORTAL =
            ENTITY_TYPES.register("supernova_portal",
                    () -> EntityType.Builder.of(SupernovaPortal::new,
                            MobCategory.MISC).sized(10F, 0.1F).build("supernova_portal"));

    public static final RegistryObject<EntityType<SupernovaAsteroid>> SUPERNOVA_ASTEROID =
            ENTITY_TYPES.register("supernova_asteroid",
                    () -> EntityType.Builder.of((EntityType<SupernovaAsteroid> pEntityType, Level pLevel) -> new SupernovaAsteroid(pEntityType, pLevel, 0),
                                    MobCategory.MISC).sized(1F, 1F)
                            .setUpdateInterval(1).build("supernova_asteroid"));

    public static final RegistryObject<EntityType<? extends BrutalityArrow>> LIGHT_ARROW =
            ENTITY_TYPES.register("light_arrow",
                    () -> EntityType.Builder.of((EntityType<BrutalityArrow> pEntityType, Level pLevel) -> new LightArrow(pEntityType, pLevel), MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .setUpdateInterval(20)
                            .build("light_arrow"));


    public static final RegistryObject<EntityType<PiEntity>> PI_ENTITY =
            ENTITY_TYPES.register("pi_entity",
                    () -> EntityType.Builder.of((EntityType<PiEntity> pEntityType, Level pLevel) -> new PiEntity(pEntityType, pLevel,0),
                                    MobCategory.MISC).sized(2F, 0.0625F)
                            .setUpdateInterval(1).build("pi_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}