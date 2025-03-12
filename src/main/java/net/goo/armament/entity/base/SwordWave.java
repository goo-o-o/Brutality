package net.goo.armament.entity.base;

import net.goo.armament.client.entity.ArmaGeoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SwordWave extends Entity implements ArmaGeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float blocksPerSecond = 1;
    private float renderScale = 1;

    public SwordWave(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    public String geoIdentifier() {
        return "sword_wave";
    }

    public float getRenderScale() {
        return renderScale;
    }

    @Override
    public void tick() {
        super.tick();
        renderScale += blocksPerSecond / 20;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public void setSilent(boolean pIsSilent) {
        super.setSilent(true);
    }
}
