package net.goo.armament.entity;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.SupernovaExplosionEntity;
import net.goo.armament.entity.custom.TestTridentEntity;
import net.goo.armament.entity.custom.ThrownZeusThunderbolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Armament.MOD_ID);
    public static final RegistryObject<EntityType<SupernovaExplosionEntity>> SUPERNOVA_EXPLOSION_ENTITY =
            ENTITY_TYPES.register("supernova_explosion",
                    () -> EntityType.Builder.of(SupernovaExplosionEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .build("supernova_explosion"));

    public static final RegistryObject<EntityType<ThrownZeusThunderbolt>> THROWN_ZEUS_THUNDERBOLT =
            ENTITY_TYPES.register("thrown_zeus_thunderbolt",
                    () -> EntityType.Builder.<ThrownZeusThunderbolt>of(ThrownZeusThunderbolt::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .setUpdateInterval(20)
                            .build("thrown_zeus_thunderbolt"));

    public static final RegistryObject<EntityType<TestTridentEntity>> TEST_TRIDENT_ENTITY =
            ENTITY_TYPES.register("test_trident_entity",
                    () -> EntityType.Builder.<TestTridentEntity>of(TestTridentEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .setUpdateInterval(20)
                            .build("test_trident_entity"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}