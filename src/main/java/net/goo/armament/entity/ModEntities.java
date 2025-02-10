package net.goo.armament.entity;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.BlackHoleEntity;
import net.goo.armament.entity.custom.CruelSunEntity;
import net.goo.armament.entity.custom.TerraBeamEntity;
import net.goo.armament.entity.custom.ThrownZeusThunderboltEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Armament.MOD_ID);

    public static final RegistryObject<EntityType<ThrownZeusThunderboltEntity>> THROWN_ZEUS_THUNDERBOLT_ENTITY =
            ENTITY_TYPES.register("thrown_zeus_thunderbolt",
                    () -> EntityType.Builder.<ThrownZeusThunderboltEntity>of(ThrownZeusThunderboltEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .setUpdateInterval(20)
                            .build("thrown_zeus_thunderbolt"));

    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY =
            ENTITY_TYPES.register("cruel_sun",
            () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
                    .sized(3.0f, 3.0f)
                    .build("cruel_sun"));

    public static final RegistryObject<EntityType<TerraBeamEntity>> TERRA_BEAM_ENTITY =
            ENTITY_TYPES.register("terra_beam",
                    () -> EntityType.Builder.of(TerraBeamEntity::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .build("terra_beam"));

    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE_ENTITY =
            ENTITY_TYPES.register("black_hole",
                    () -> EntityType.Builder.of(BlackHoleEntity::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .setUpdateInterval(1)
                            .build("black_hole"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}