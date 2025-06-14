package net.goo.brutality.entity.custom;

import net.goo.brutality.client.entity.ArmaGeoEntity;
import net.goo.brutality.client.renderers.entity.BrutalityEndPortalEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class SupernovaPortal extends Entity implements ArmaGeoEntity {
    private UUID ownerUUID;
    private int lifespan = 20 * 6;

    public SupernovaPortal(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

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

    @Override
    public String geoIdentifier() {
        return "supernova_portal";
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
        this.ownerUUID = ownerUUID;
    }

    @Override
    public void tick() {
        if (this.tickCount >= 60) discard();
    }

    @Override
    public <T extends Entity & ArmaGeoEntity, R extends GeoEntityRenderer<T>> void initGeo(Consumer<EntityRendererProvider<T>> consumer, Class<R> rendererClass) {
        ArmaGeoEntity.super.initGeo(consumer, BrutalityEndPortalEntityRenderer.class);
    }

    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().thenPlay("spawn").thenPlay("idle").thenPlay("despawn"));
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
}
