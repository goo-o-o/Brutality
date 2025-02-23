package net.goo.armament.entity.base;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.client.entity.model.ArmaGeoProjectileRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.function.Consumer;

public class GenericArmaTridentProjectile extends ThrowableProjectile implements ArmaGeoEntity {
    public String identifier;


    public GenericArmaTridentProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public <T extends Entity & ArmaGeoEntity, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<ArmaGeoProjectileRenderer> rendererClass) {
        ArmaGeoEntity.super.initGeo(consumer, ArmaGeoProjectileRenderer.class);
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
