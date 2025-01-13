package net.goo.arma.entity;

import net.goo.arma.Arma;
import net.goo.arma.entity.custom.SupernovaExplosionEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Arma.MOD_ID);


    public static final RegistryObject<EntityType<SupernovaExplosionEntity>> SUPERNOVA_EXPLOSION_ENTITY = ENTITY_TYPES.register(
            "supernova_explosion", () -> EntityType.Builder.of(SupernovaExplosionEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .build("supernova_explosion"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
