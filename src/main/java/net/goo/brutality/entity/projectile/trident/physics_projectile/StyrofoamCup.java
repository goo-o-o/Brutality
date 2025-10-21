package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class StyrofoamCup extends BrutalityAbstractPhysicsThrowingProjectile {
    private static final EntityDataAccessor<Integer> CUP_TYPE_INDEX = SynchedEntityData.defineId(StyrofoamCup.class, EntityDataSerializers.INT);
    protected String[] types = new String[]{"", "_water"};
    private static final String CUP_TYPE_KEY = "cupType";

    public StyrofoamCup(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
        this.pickup = Pickup.ALLOWED;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains(CUP_TYPE_KEY)) {
            this.setCupTypeIndex(pCompound.getInt(CUP_TYPE_KEY));
        }
    }

    public void setCupTypeIndex(int index) {
        this.entityData.set(CUP_TYPE_INDEX, index);
    }

    public int getCupIndex() {
        return this.entityData.get(CUP_TYPE_INDEX);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CUP_TYPE_INDEX, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(CUP_TYPE_KEY, getCupIndex());
    }

    @Override
    public String model() {
        return "styrofoam_cup" + types[getCupIndex()];
    }

    @Override
    public float getModelHeight() {
        return 6;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalityModSounds.STYROFOAM_IMPACT.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.STYROFOAM_IMPACT.get();
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }
}
