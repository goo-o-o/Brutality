package net.goo.brutality.entity.custom.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ThrownDepthCrusher extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> DATA_FINAL_ROLL = SynchedEntityData.defineId(ThrownDepthCrusher.class, EntityDataSerializers.INT);

    public ThrownDepthCrusher(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(pLevel, pShooter, pStack, trident);
        this.entityData.set(DATA_FINAL_ROLL, level().random.nextIntBetweenInclusive(-50, 50));

        this.pitch = 0;
    }

    public ThrownDepthCrusher(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FINAL_ROLL, 0);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public int getInGroundLifespan() {
        return 40;
    }

    @Override
    public void tick() {
        super.tick();
        if (inGround) {
            this.roll = this.entityData.get(DATA_FINAL_ROLL);
        } else if (getDeltaMovement().length() > 0.1)
            this.yaw = -yaw;
    }

    @Override
    protected float getRotationSpeed() {
        return 90;
    }

    @Override
    protected boolean lockPitch() {
        return true;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return random.nextIntBetweenInclusive(0, 100) < 1 ? ModSounds.METAL_PIPE.get() : SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void setPierceLevel(byte pPierceLevel) {
        super.setPierceLevel((byte) 100);
    }

    @Override
    protected float getBounciness() {
        return 0.85F;
    }

    @Override
    public float getModelHeight() {
        return 29;
    }
}
