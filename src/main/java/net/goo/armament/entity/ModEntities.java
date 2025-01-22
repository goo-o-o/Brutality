package net.goo.armament.entity;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.CruelSun2Entity;
import net.goo.armament.entity.custom.CruelSunEntity;
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

    public static final RegistryObject<EntityType<CruelSunEntity>> CRUEL_SUN_ENTITY = ENTITY_TYPES.register(
            "cruel_sun",
            () -> EntityType.Builder.of(CruelSunEntity::new, MobCategory.MISC)
                    .sized(3.0f, 3.0f)
                    .clientTrackingRange(4)
                    .setUpdateInterval(20)
                    .build("cruel_sun"));

    public static final RegistryObject<EntityType<CruelSun2Entity>> CRUEL_SUN_ENTITY_2 = ENTITY_TYPES.register(
            "cruel_sun_2",
            () -> EntityType.Builder.of(CruelSun2Entity::new, MobCategory.MISC)
                    .sized(3.0f, 3.0f)
                    .clientTrackingRange(4)
                    .setUpdateInterval(20)
                    .build("cruel_sun_2"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}