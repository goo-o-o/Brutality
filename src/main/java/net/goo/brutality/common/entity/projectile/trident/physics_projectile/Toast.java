package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class Toast extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public Toast(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public Toast(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!(pResult.getEntity() instanceof OverclockedToaster))
            super.onHitEntity(pResult);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
    }

    @Override
    public float getModelHeight() {
        return 2;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.WOOL_HIT;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.WOOL_HIT;
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }


}
