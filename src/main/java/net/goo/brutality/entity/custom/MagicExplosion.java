package net.goo.brutality.entity.custom;

import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class MagicExplosion extends Entity implements ArmaGeoEntity {

    public MagicExplosion(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    public String geoIdentifier() {
        return "magic_explosion";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setOwner(UUID ownerUUID) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}
