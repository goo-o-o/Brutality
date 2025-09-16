package net.goo.brutality.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BrutalityShuriken extends BrutalityAbstractArrow implements BrutalityGeoEntity {
    public static final EntityDataAccessor<Integer> RANDOM_YAW = SynchedEntityData.defineId(BrutalityShuriken.class, EntityDataSerializers.INT);
    public boolean renderForLayer = false;

    public BrutalityShuriken(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BrutalityShuriken(@NotNull EntityType<? extends AbstractArrow> pEntityType, Level level, double x, double y, double z) {
        super(pEntityType, level, x, y, z);
    }

    @Override
    protected boolean shouldUpdateArrowCount() {
        return false;
    }

    @Override
    public SoundEvent getHitEntitySound() {
        return BrutalityModSounds.SQUELCH.get();
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BrutalityModSounds.SHURIKEN_IMPACT.get();
    }

    public int getRandomYaw() {
        return this.entityData.get(RANDOM_YAW);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RANDOM_YAW, level().random.nextIntBetweenInclusive(0, 180));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getAirDrag() {
        return 1;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    protected int getInGroundLifespan() {
        return 6000;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


}
