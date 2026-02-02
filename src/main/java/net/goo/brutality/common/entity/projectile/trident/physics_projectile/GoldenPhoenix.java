package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GoldenPhoenix extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {

    public GoldenPhoenix(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public GoldenPhoenix(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public float getModelHeight() {
        return 16;
    }

    @Override
    protected int getLifespan() {
        return 200;
    }

    @Override
    @NotNull
    public SoundEvent getHitGroundSoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }


    @Override
    protected int getBounceQuota() {
        return 1;
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }
}
