package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeachBall extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public BeachBall(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected int getLifespan() {
        return BrutalityCommonConfig.BEACHBALL_LIFESPAN.get();
    }

    @Override
    public float getDamage(@Nullable LivingEntity livingEntity) {
        return 1;
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public float getModelHeight() {
        return 18;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.PUFFER_FISH_BLOW_OUT;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.PUFFER_FISH_BLOW_OUT;
    }

    @Override
    protected float getBounciness() {
        return 0.9F;
    }

    @Override
    protected int getBounceQuota() {
        return 99;
    }

    @Override
    public float getGravity() {
        return 0.025F;
    }

    @Override
    protected Vec3 getTridentBounceStrength() {
        return new Vec3(-0.8D, -0.8D, -0.8D);
    }

    @Override
    protected int getHitQuota() {
        return 99;
    }
}
