//package net.goo.brutality.registry;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.entity.base.BrutalityAbstractTrident;
//import net.goo.brutality.entity.base.BrutalityArrow;
//import net.goo.brutality.entity.mobs.SummonedStray;
//import net.goo.brutality.entity.projectile.arrow.LightArrow;
//import net.goo.brutality.entity.projectile.beam.TerraBeam;
//import net.goo.brutality.entity.projectile.generic.*;
//import net.goo.brutality.entity.projectile.ray.ExplosionRay;
//import net.goo.brutality.entity.projectile.ray.LastPrismRay;
//import net.goo.brutality.entity.projectile.trident.ExobladeBeam;
//import net.goo.brutality.entity.projectile.trident.ThrownGungnir;
//import net.goo.brutality.entity.projectile.trident.ThrownKnife;
//import net.goo.brutality.entity.projectile.trident.ThrownThunderbolt;
//import net.goo.brutality.entity.projectile.trident.physics_projectile.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.MobCategory;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//
//public class BrutalityModEntities {
//    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
//            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Brutality.MOD_ID);
//
//    public static final RegistryObject<EntityType<SummonedStray>> SUMMONED_STRAY =
//            ENTITY_TYPES.register("summoned_stray", () -> EntityType.Builder.<SummonedStray>of(SummonedStray::new, MobCategory.MONSTER)
//                    .sized(.6f, 1.8f)
//                    .clientTrackingRange(64)
//                    .build(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "summoned_stray").toString()));
//
////    public static final RegistryObject<EntityType<SwordWave>> SWORD_WAVE =
////            ENTITY_TYPES.register("sword_wave", () -> EntityType.Builder.of(SwordWave::new,
////                    MobCategory.MISC).sized(1F, 0.25F).build("sword_wave"));
//
//    public static final RegistryObject<EntityType<SpectralMawEntity>> SPECTRAL_MAW_ENTITY =
//            ENTITY_TYPES.register("spectral_maw", () -> EntityType.Builder.<SpectralMawEntity>of(SpectralMawEntity::new,
//                    MobCategory.MISC).sized(0.5F, 0.5F).build("spectral_maw"));
//
//    public static final RegistryObject<EntityType<StarEntity>> STAR_ENTITY =
//            ENTITY_TYPES.register("star", () -> EntityType.Builder.<StarEntity>of(StarEntity::new,
//                    MobCategory.MISC).sized(1, 0.0625F).build("star"));
//
//    public static final RegistryObject<EntityType<DepthCrusherProjectile>> DEPTH_CRUSHER_PROJECTILE =
//            ENTITY_TYPES.register("depth_crusher_projectile", () -> EntityType.Builder.of(DepthCrusherProjectile::new,
//                    MobCategory.MISC).sized(0.25F, 0.25F).build("depth_crusher_projectile"));
//
//    public static final RegistryObject<EntityType<ThrownThunderbolt>> THROWN_THUNDERBOLT_ENTITY =
//            ENTITY_TYPES.register("thrown_thunderbolt",
//                    () -> EntityType.Builder.<ThrownThunderbolt>of(ThrownThunderbolt::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_thunderbolt"));
//
//
//    public static final RegistryObject<EntityType<BrutalityAbstractTrident>> THROWN_GUNGNIR_ENTITY =
//            ENTITY_TYPES.register("thrown_gungnir",
//                    () -> EntityType.Builder.<BrutalityAbstractTrident>of(ThrownGungnir::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_gungnir"));
//
//    public static final RegistryObject<EntityType<BrutalityAbstractTrident>> EXOBLADE_BEAM =
//            ENTITY_TYPES.register("exoblade_beam",
//                    () -> EntityType.Builder.<BrutalityAbstractTrident>of(ExobladeBeam::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("exoblade_beam"));
//
//    public static final RegistryObject<EntityType<ThrownKnife>> THROWN_KNIFE_ENTITY =
//            ENTITY_TYPES.register("thrown_knife",
//                    () -> EntityType.Builder.<ThrownKnife>of(ThrownKnife::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_knife"));
//
//    public static final RegistryObject<EntityType<ThrownStyrofoamCup>> THROWN_STYROFOAM_CUP =
//            ENTITY_TYPES.register("thrown_styrofoam_cup",
//                    () -> EntityType.Builder.<ThrownStyrofoamCup>of(ThrownStyrofoamCup::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_styrofoam_cup"));
//
//    public static final RegistryObject<EntityType<ThrownCabbage>> THROWN_CABBAGE_ENTITY =
//            ENTITY_TYPES.register("thrown_cabbage",
//                    () -> EntityType.Builder.<ThrownCabbage>of(ThrownCabbage::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_cabbage"));
//
//    public static final RegistryObject<EntityType<ThrownWintermelon>> THROWN_WINTERMELON_ENTITY =
//            ENTITY_TYPES.register("thrown_wintermelon",
//                    () -> EntityType.Builder.<ThrownWintermelon>of(ThrownWintermelon::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_wintermelon"));
//    public static final RegistryObject<EntityType<ThrownBanana>> THROWN_BANANA_ENTITY =
//            ENTITY_TYPES.register("thrown_banana",
//                    () -> EntityType.Builder.<ThrownBanana>of(ThrownBanana::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_banana"));
//    public static final RegistryObject<EntityType<ThrownButter>> THROWN_BUTTER_ENTITY =
//            ENTITY_TYPES.register("thrown_butter",
//                    () -> EntityType.Builder.<ThrownButter>of(ThrownButter::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_butter"));
//    public static final RegistryObject<EntityType<ThrownApple>> THROWN_APPLE_ENTITY =
//            ENTITY_TYPES.register("thrown_apple",
//                    () -> EntityType.Builder.<ThrownApple>of(ThrownApple::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_apple"));
//    public static final RegistryObject<EntityType<ThrownDurian>> THROWN_DURIAN_ENTITY =
//            ENTITY_TYPES.register("thrown_durian",
//                    () -> EntityType.Builder.<ThrownDurian>of(ThrownDurian::new, MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_durian"));
//
//    public static final RegistryObject<EntityType<ThrownBiomechReactor>> THROWN_BIOMECH_REACTOR =
//            ENTITY_TYPES.register("thrown_biomech_reactor",
//                    () -> EntityType.Builder.<ThrownBiomechReactor>of(ThrownBiomechReactor::new, MobCategory.MISC)
//                            .sized(1F, 1F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_biomech_reactor"));
//
//    public static final RegistryObject<EntityType<ThrownDepthCrusher>> THROWN_DEPTH_CRUSHER =
//            ENTITY_TYPES.register("thrown_depth_crusher",
//                    () -> EntityType.Builder.<ThrownDepthCrusher>of(ThrownDepthCrusher::new, MobCategory.MISC)
//                            .sized(1, 2F)
//                            .clientTrackingRange(64)
//                            .setUpdateInterval(20)
//                            .build("thrown_depth_crusher"));
//
//
//
//    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY =
//            ENTITY_TYPES.register("cruel_sun",
//                    () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
//                            .sized(3.0f, 3.0f)
//                            .build("cruel_sun"));
//
//    public static final RegistryObject<EntityType<TerraBeam>> TERRA_BEAM =
//            ENTITY_TYPES.register("terra_beam", () -> EntityType.Builder.of(TerraBeam::new,
//                    MobCategory.MISC).sized(1F, 3F).build("terra_beam"));
//
////    public static final RegistryObject<EntityType<ExcaliburBeam>> EXCALIBUR_BEAM =
////            ENTITY_TYPES.register("excalibur_beam", () -> EntityType.Builder.of(ExcaliburBeam::new,
////                            MobCategory.MISC).sized(9F, 1F).setUpdateInterval(1).setTrackingRange(128)
////                    .build("excalibur_beam"));
//
//    public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE_ENTITY =
//            ENTITY_TYPES.register("black_hole",
//                    () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
//                            .sized(1F, 1F)
//                            .setUpdateInterval(1)
//                            .build("black_hole"));
//
//    public static final RegistryObject<EntityType<ExplosionRay>> EXPLOSION_RAY =
//            ENTITY_TYPES.register("explosion_ray",
//                    () -> EntityType.Builder.of(ExplosionRay::new,
//                            MobCategory.MISC).sized(0, 0).build("explosion_ray"));
//
//    public static final RegistryObject<EntityType<LastPrismRay>> LAST_PRISM_RAY =
//            ENTITY_TYPES.register("last_prism_ray",
//                    () -> EntityType.Builder.of(LastPrismRay::new,
//                            MobCategory.MISC).sized(0, 0).setUpdateInterval(1).build("last_prism_ray"));
//
////    public static final RegistryObject<EntityType<SupernovaPortal>> SUPERNOVA_PORTAL =
////            ENTITY_TYPES.register("supernova_portal",
////                    () -> EntityType.Builder.of(SupernovaPortal::new,
////                            MobCategory.MISC).sized(10F, 0.1F).build("supernova_portal"));
//
//    public static final RegistryObject<EntityType<SupernovaAsteroid>> SUPERNOVA_ASTEROID =
//            ENTITY_TYPES.register("supernova_asteroid",
//                    () -> EntityType.Builder.of((EntityType<SupernovaAsteroid> pEntityType, Level pLevel) -> new SupernovaAsteroid(pEntityType, pLevel, 0),
//                                    MobCategory.MISC).sized(1F, 1F)
//                            .setUpdateInterval(1).build("supernova_asteroid"));
//
//    public static final RegistryObject<EntityType<? extends BrutalityArrow>> LIGHT_ARROW =
//            ENTITY_TYPES.register("light_arrow",
//                    () -> EntityType.Builder.of((EntityType<BrutalityArrow> pEntityType, Level pLevel) -> new LightArrow(pEntityType, pLevel), MobCategory.MISC)
//                            .sized(0.5F, 0.5F)
//                            .clientTrackingRange(4)
//                            .setUpdateInterval(20)
//                            .build("light_arrow"));
//
//
//    public static final RegistryObject<EntityType<PiEntity>> PI_ENTITY =
//            ENTITY_TYPES.register("pi_entity",
//                    () -> EntityType.Builder.of((EntityType<PiEntity> pEntityType, Level pLevel) -> new PiEntity(pEntityType, pLevel,0),
//                                    MobCategory.MISC).sized(2F, 0.0625F)
//                            .setUpdateInterval(1).build("pi_entity"));
//
//    public static void register(IEventBus eventBus) {
//        ENTITY_TYPES.register(eventBus);
//    }
//
//}