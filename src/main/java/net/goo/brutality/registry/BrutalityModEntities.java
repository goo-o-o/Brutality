package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.ChairSeatEntity;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.entity.projectile.arrow.LightArrow;
import net.goo.brutality.entity.projectile.beam.TerraBeam;
import net.goo.brutality.entity.projectile.generic.*;
import net.goo.brutality.entity.projectile.ray.ExplosionRay;
import net.goo.brutality.entity.projectile.ray.LastPrismRay;
import net.goo.brutality.entity.projectile.trident.ExobladeBeam;
import net.goo.brutality.entity.projectile.trident.ThrownGungnir;
import net.goo.brutality.entity.projectile.trident.ThrownKnife;
import net.goo.brutality.entity.projectile.trident.ThrownThunderbolt;
import net.goo.brutality.entity.projectile.trident.physics_projectile.*;
import net.goo.brutality.entity.spells.brimwielder.*;
import net.goo.brutality.entity.spells.celestia.LightBinding;
import net.goo.brutality.entity.spells.cosmic.CosmicCataclysmEntity;
import net.goo.brutality.entity.spells.cosmic.MeteorShowerEntity;
import net.goo.brutality.entity.spells.cosmic.SingularityShiftEntity;
import net.goo.brutality.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.entity.spells.umbrancy.CrescentDart;
import net.goo.brutality.entity.spells.umbrancy.CrescentScythe;
import net.goo.brutality.entity.spells.umbrancy.PiercingMoonlight;
import net.goo.brutality.entity.spells.voidwalker.GraviticImplosionEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BrutalityModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Brutality.MOD_ID);

    public static final RegistryObject<EntityType<ChairSeatEntity>> CHAIR_SEAT =
            ENTITY_TYPES.register("chair_seat",
                    () -> EntityType.Builder.<ChairSeatEntity>of(ChairSeatEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .build("chair_seat"));

    public static final RegistryObject<EntityType<SummonedStray>> SUMMONED_STRAY =
            ENTITY_TYPES.register("summoned_stray", () -> EntityType.Builder.<SummonedStray>of(SummonedStray::new, MobCategory.MONSTER)
                    .sized(.6f, 1.8f)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "summoned_stray").toString()));

//    public static final RegistryObject<EntityType<SwordWave>> SWORD_WAVE =
//           ENTITY_TYPES.register("sword_wave", () -> EntityType.Builder.of(SwordWave::new,
//                    MobCategory.MISC).sized(1F, 0.25F).build("sword_wave"));

    public static final RegistryObject<EntityType<SpectralMawEntity>> SPECTRAL_MAW_ENTITY =
            ENTITY_TYPES.register("spectral_maw", () -> EntityType.Builder.<SpectralMawEntity>of(SpectralMawEntity::new,
                    MobCategory.MISC).sized(0.5F, 0.5F).build("spectral_maw"));

    public static final RegistryObject<EntityType<Dynamite>> DYNAMITE =
            ENTITY_TYPES.register("dynamite", () -> EntityType.Builder.of(
                    (EntityType<Dynamite> entityType, Level level) -> new Dynamite(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.125F, 0.125F).build("dynamite"));

    public static final RegistryObject<EntityType<StickyDynamite>> STICKY_DYNAMITE =
            ENTITY_TYPES.register("sticky_dynamite", () -> EntityType.Builder.of(
                    (EntityType<StickyDynamite> entityType, Level level) -> new StickyDynamite(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.125F, 0.125F).build("sticky_dynamite"));

    public static final RegistryObject<EntityType<BouncyDynamite>> BOUNCY_DYNAMITE =
            ENTITY_TYPES.register("bouncy_dynamite", () -> EntityType.Builder.of(
                    (EntityType<BouncyDynamite> entityType, Level level) -> new BouncyDynamite(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.125F, 0.125F).build("bouncy_dynamite"));

    public static final RegistryObject<EntityType<Photon>> PHOTON =
            ENTITY_TYPES.register("photon", () -> EntityType.Builder.of(
                    (EntityType<Photon> entityType, Level level) -> new Photon(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.1F, 0.1F).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("photon"));

    public static final RegistryObject<EntityType<CinderBlock>> CINDER_BLOCK =
            ENTITY_TYPES.register("cinder_block", () -> EntityType.Builder.of(
                    (EntityType<CinderBlock> entityType, Level level) -> new CinderBlock(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("cinder_block"));

    public static final RegistryObject<EntityType<OverclockedToaster>> OVERCLOCKED_TOASTER =
            ENTITY_TYPES.register("overclocked_toaster", () -> EntityType.Builder.of(
                    (EntityType<OverclockedToaster> entityType, Level level) -> new OverclockedToaster(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("overclocked_toaster"));

    public static final RegistryObject<EntityType<Toast>> TOAST =
            ENTITY_TYPES.register("toast", () -> EntityType.Builder.of(
                    (EntityType<Toast> entityType, Level level) -> new Toast(entityType, level, BrutalityDamageTypes.THROWING_PIERCE),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("toast"));

    public static final RegistryObject<EntityType<StickyBomb>> STICKY_BOMB =
            ENTITY_TYPES.register("sticky_bomb", () -> EntityType.Builder.of(
                    (EntityType<StickyBomb> entityType, Level level) -> new StickyBomb(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.125F, 0.125F).build("sticky_bomb"));

    public static final RegistryObject<EntityType<IceCube>> ICE_CUBE =
            ENTITY_TYPES.register("ice_cube", () -> EntityType.Builder.of(
                    (EntityType<IceCube> entityType, Level level) -> new IceCube(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.25F, 0.25F).build("ice_cube"));

    public static final RegistryObject<EntityType<PermafrostCube>> PERMAFROST_CUBE =
            ENTITY_TYPES.register("permafrost_cube", () -> EntityType.Builder.of(
                    (EntityType<PermafrostCube> entityType, Level level) -> new PermafrostCube(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.375F, 0.375F).build("permafrost_cube"));

    public static final RegistryObject<EntityType<BeachBall>> BEACH_BALL =
            ENTITY_TYPES.register("beach_ball", () -> EntityType.Builder.of(
                    (EntityType<BeachBall> entityType, Level level) -> new BeachBall(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(1.125F, 1.125F).build("beach_ball"));


    public static final RegistryObject<EntityType<FateCard>> FATE_CARD =
            ENTITY_TYPES.register("fate_card", () -> EntityType.Builder.of(
                    (EntityType<FateCard> entityType, Level level) -> new FateCard(entityType, level, BrutalityDamageTypes.THROWING_PIERCE),
                    MobCategory.MISC).sized(1F, 0.0625F).build("fate_card"));

    public static final RegistryObject<EntityType<AbsoluteZero>> ABSOLUTE_ZERO =
            ENTITY_TYPES.register("absolute_zero", () -> EntityType.Builder.of(
                    (EntityType<AbsoluteZero> entityType, Level level) -> new AbsoluteZero(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.25F, 0.25F).build("absolute_zero"));

    public static final RegistryObject<EntityType<PerfumeBottle>> PERFUME_BOTTLE =
            ENTITY_TYPES.register("perfume_bottle", () -> EntityType.Builder.of(
                    (EntityType<PerfumeBottle> entityType, Level level) -> new PerfumeBottle(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.25F, 0.25F).build("perfume_bottle"));

    public static final RegistryObject<EntityType<BlastBarrel>> BLAST_BARREL =
            ENTITY_TYPES.register("blast_barrel", () -> EntityType.Builder.of(
                    (EntityType<BlastBarrel> entityType, Level level) -> new BlastBarrel(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(1, 0.75F).build("blast_barrel"));

    public static final RegistryObject<EntityType<SculkGrenade>> SCULK_GRENADE =
            ENTITY_TYPES.register("sculk_grenade", () -> EntityType.Builder.of(
                    (EntityType<SculkGrenade> entityType, Level level) -> new SculkGrenade(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.375F, 0.1875F).build("sculk_grenade"));

    public static final RegistryObject<EntityType<HolyHandGrenade>> HOLY_HAND_GRENADE =
            ENTITY_TYPES.register("holy_hand_grenade", () -> EntityType.Builder.of(
                    (EntityType<HolyHandGrenade> entityType, Level level) -> new HolyHandGrenade(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("holy_hand_grenade"));

    public static final RegistryObject<EntityType<VampireKnife>> VAMPIRE_KNIFE =
            ENTITY_TYPES.register("vampire_knife", () -> EntityType.Builder.of(
                    (EntityType<VampireKnife> entityType, Level level) -> new VampireKnife(entityType, level, BrutalityDamageTypes.THROWING_PIERCE),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("vampire_knife"));

    public static final RegistryObject<EntityType<StyrofoamCup>> STYROFOAM_CUP =
            ENTITY_TYPES.register("styrofoam_cup", () -> EntityType.Builder.of(
                    (EntityType<StyrofoamCup> entityType, Level level) -> new StyrofoamCup(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("styrofoam_cup"));

    public static final RegistryObject<EntityType<Mug>> MUG =
            ENTITY_TYPES.register("mug", () -> EntityType.Builder.of(
                    (EntityType<Mug> entityType, Level level) -> new Mug(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("mug"));

    public static final RegistryObject<EntityType<CannonballCabbage>> CANNONBALL_CABBAGE =
            ENTITY_TYPES.register("cannonball_cabbage", () -> EntityType.Builder.of(
                    (EntityType<CannonballCabbage> entityType, Level level) -> new CannonballCabbage(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("cannonball_cabbage"));

    public static final RegistryObject<EntityType<WinterMelon>> WINTER_MELON =
            ENTITY_TYPES.register("winter_melon", () -> EntityType.Builder.of(
                    (EntityType<WinterMelon> entityType, Level level) -> new WinterMelon(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("winter_melon"));

    public static final RegistryObject<EntityType<Cavendish>> CAVENDISH =
            ENTITY_TYPES.register("cavendish", () -> EntityType.Builder.of(
                    (EntityType<Cavendish> entityType, Level level) -> new Cavendish(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("cavendish"));

    public static final RegistryObject<EntityType<StickOfButter>> STICK_OF_BUTTER =
            ENTITY_TYPES.register("stick_of_butter", () -> EntityType.Builder.of(
                    (EntityType<StickOfButter> entityType, Level level) -> new StickOfButter(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("stick_of_butter"));

    public static final RegistryObject<EntityType<CrimsonDelight>> CRIMSON_DELIGHT =
            ENTITY_TYPES.register("crimson_delight", () -> EntityType.Builder.of(
                    (EntityType<CrimsonDelight> entityType, Level level) -> new CrimsonDelight(entityType, level, BrutalityDamageTypes.THROWING_BLUNT),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("crimson_delight"));

    public static final RegistryObject<EntityType<GoldenPhoenix>> GOLDEN_PHOENIX =
            ENTITY_TYPES.register("golden_phoenix", () -> EntityType.Builder.of(
                    (EntityType<GoldenPhoenix> entityType, Level level) -> new GoldenPhoenix(entityType, level, BrutalityDamageTypes.THROWING_PIERCE),
                    MobCategory.MISC).sized(0.5F, 0.5F).build("golden_phoenix"));


    public static final RegistryObject<EntityType<BiomechReactor>> BIOMECH_REACTOR =
            ENTITY_TYPES.register("biomech_reactor", () -> EntityType.Builder.of(
                    (EntityType<BiomechReactor> entityType, Level level) -> new BiomechReactor(entityType, level, BrutalityDamageTypes.THROWING_PIERCE),
                    MobCategory.MISC).sized(1F, 1F).build("biomech_reactor"));


    public static final RegistryObject<EntityType<StarEntity>> STAR_ENTITY =
            ENTITY_TYPES.register("star", () -> EntityType.Builder.<StarEntity>of(StarEntity::new,
                    MobCategory.MISC).sized(1, 0.0625F).build("star"));

    public static final RegistryObject<EntityType<AbyssProjectile>> ABYSS_PROJECTILE =
            ENTITY_TYPES.register("abyss_projectile", () -> EntityType.Builder.of(AbyssProjectile::new,
                    MobCategory.MISC).sized(0.25F, 0.25F).build("abyss_projectile"));

    public static final RegistryObject<EntityType<ThrownThunderbolt>> THROWN_THUNDERBOLT_ENTITY =
            ENTITY_TYPES.register("thrown_thunderbolt",
                    () -> EntityType.Builder.<ThrownThunderbolt>of(ThrownThunderbolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("thrown_thunderbolt"));

    public static final RegistryObject<EntityType<BrutalityAbstractTrident>> THROWN_GUNGNIR_ENTITY =
            ENTITY_TYPES.register("thrown_gungnir",
                    () -> EntityType.Builder.<BrutalityAbstractTrident>of(ThrownGungnir::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("thrown_gungnir"));

    public static final RegistryObject<EntityType<BrutalityAbstractTrident>> EXOBLADE_BEAM =
            ENTITY_TYPES.register("exoblade_beam",
                    () -> EntityType.Builder.<BrutalityAbstractTrident>of(ExobladeBeam::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("exoblade_beam"));

    public static final RegistryObject<EntityType<ThrownKnife>> THROWN_KNIFE_ENTITY =
            ENTITY_TYPES.register("thrown_knife",
                    () -> EntityType.Builder.<ThrownKnife>of(ThrownKnife::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("thrown_knife"));


    public static final RegistryObject<EntityType<DepthCrusher>> DEPTH_CRUSHER =
            ENTITY_TYPES.register("depth_crusher",
                    () -> EntityType.Builder.of((EntityType<DepthCrusher> entityType, Level level) -> new DepthCrusher(entityType, level), MobCategory.MISC)
                            .sized(1, 2F)
                            .build("depth_crusher"));


    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY =
            ENTITY_TYPES.register("cruel_sun",
                    () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
                            .sized(3.0f, 3.0f)
                            .build("cruel_sun"));

    public static final RegistryObject<EntityType<TerraBeam>> TERRA_BEAM =
            ENTITY_TYPES.register("terra_beam", () -> EntityType.Builder.of(TerraBeam::new,
                    MobCategory.MISC).sized(1F, 3F).build("terra_beam"));

//    public static final RegistryObject<EntityType<ExcaliburBeam>> EXCALIBUR_BEAM =
//            ENTITY_TYPES.register("excalibur_beam", () -> EntityType.Builder.of(ExcaliburBeam::new,
//                            MobCategory.MISC).sized(9F, 1F).setUpdateInterval(1).setTrackingRange(128)
//                    .build("excalibur_beam"));

    public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE_ENTITY =
            ENTITY_TYPES.register("black_hole",
                    () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .setUpdateInterval(1)
                            .build("black_hole"));

    public static final RegistryObject<EntityType<ExplosionRay>> EXPLOSION_RAY =
            ENTITY_TYPES.register("explosion_ray",
                    () -> EntityType.Builder.of(ExplosionRay::new,
                            MobCategory.MISC).sized(0, 0).build("explosion_ray"));

    public static final RegistryObject<EntityType<LastPrismRay>> LAST_PRISM_RAY =
            ENTITY_TYPES.register("last_prism_ray",
                    () -> EntityType.Builder.of(LastPrismRay::new,
                            MobCategory.MISC).sized(0, 0).setUpdateInterval(1).build("last_prism_ray"));


//    public static final RegistryObject<EntityType<SupernovaPortal>> SUPERNOVA_PORTAL =
//            ENTITY_TYPES.register("supernova_portal",
//                    () -> EntityType.Builder.of(SupernovaPortal::new,
//                            MobCategory.MISC).sized(10F, 0.1F).build("supernova_portal"));

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
                    () -> EntityType.Builder.of((EntityType<PiEntity> pEntityType, Level pLevel) -> new PiEntity(pEntityType, pLevel, 0),
                                    MobCategory.MISC).sized(2F, 0.0625F)
                            .setUpdateInterval(1).build("pi_entity"));


    public static final RegistryObject<EntityType<GraviticImplosionEntity>> GRAVITIC_IMPLOSION_ENTITY =
            ENTITY_TYPES.register("gravitic_implosion",
                    () -> EntityType.Builder.of(GraviticImplosionEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("gravitic_implosion"));

    public static final RegistryObject<EntityType<CosmicCataclysmEntity>> COSMIC_CATACLYSM_ENTITY =
            ENTITY_TYPES.register("cosmic_cataclysm",
                    () -> EntityType.Builder.of(CosmicCataclysmEntity::new, MobCategory.MISC)
                            .sized(1, 1)
                            .build("cosmic_cataclysm"));

    public static final RegistryObject<EntityType<MeteorShowerEntity>> METEOR_SHOWER_ENTITY =
            ENTITY_TYPES.register("meteor_shower",
                    () -> EntityType.Builder.of(MeteorShowerEntity::new, MobCategory.MISC)
                            .sized(1, 1)
                            .build("meteor_shower"));

    public static final RegistryObject<EntityType<StarStreamEntity>> STAR_STREAM_ENTITY =
            ENTITY_TYPES.register("star_stream",
                    () -> EntityType.Builder.of(StarStreamEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("star_stream"));

    public static final RegistryObject<EntityType<SingularityShiftEntity>> SINGULARITY_SHIFT_ENTITY =
            ENTITY_TYPES.register("singularity_shift",
                    () -> EntityType.Builder.of(SingularityShiftEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("singularity_shift"));

    public static final RegistryObject<EntityType<AnnihilationEntity>> ANNIHILATION_ENTITY =
            ENTITY_TYPES.register("annihilation",
                    () -> EntityType.Builder.of(AnnihilationEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("annihilation"));

    public static final RegistryObject<EntityType<ChthonicCapsuleEntity>> CHTHONIC_CAPSULE_ENTITY =
            ENTITY_TYPES.register("chthonic_capsule",
                    () -> EntityType.Builder.of(ChthonicCapsuleEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("chthonic_capsule"));

    public static final RegistryObject<EntityType<DestructionEntity>> DESTRUCTION_ENTITY =
            ENTITY_TYPES.register("destruction",
                    () -> EntityType.Builder.of(DestructionEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("destruction"));


    public static final RegistryObject<EntityType<ExtinctionEntity>> EXTINCTION_ENTITY =
            ENTITY_TYPES.register("extinction",
                    () -> EntityType.Builder.of(ExtinctionEntity::new,
                            MobCategory.MISC).sized(0, 0).setUpdateInterval(1).build("extinction"));

    public static final RegistryObject<EntityType<PiercingMoonlight>> PIERCING_MOONLIGHT_ENTITY =
            ENTITY_TYPES.register("piercing_moonlight",
                    () -> EntityType.Builder.of(PiercingMoonlight::new,
                            MobCategory.MISC).sized(0, 0).build("piercing_moonlight"));

    public static final RegistryObject<EntityType<CrescentDart>> CRESCENT_DART_ENTITY =
            ENTITY_TYPES.register("crescent_dart",
                    () -> EntityType.Builder.of(CrescentDart::new,
                            MobCategory.MISC).sized(0.5F, 0.0625F).build("crescent_dart"));

    public static final RegistryObject<EntityType<BrimspikeEntity>> BRIMSPIKE_ENTITY =
            ENTITY_TYPES.register("brimspike",
                    () -> EntityType.Builder.of(BrimspikeEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).setUpdateInterval(1)
                            .build("brimspike"));

    public static final RegistryObject<EntityType<CrescentScythe>> CRESCENT_SCYTHE_ENTITY =
            ENTITY_TYPES.register("crescent_scythe",
                    () -> EntityType.Builder.of(CrescentScythe::new, MobCategory.MISC)
                            .sized(3, 0.0625F)
                            .build("crescent_scythe"));


    public static final RegistryObject<EntityType<LightBinding>> LIGHT_BINDING =
            ENTITY_TYPES.register("light_binding", () -> EntityType.Builder.of(LightBinding::new,
                    MobCategory.MISC).sized(0, 0).build("light_binding"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}