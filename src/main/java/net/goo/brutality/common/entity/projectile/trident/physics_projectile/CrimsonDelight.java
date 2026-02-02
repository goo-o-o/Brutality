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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CrimsonDelight extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public CrimsonDelight(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public CrimsonDelight(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    protected int getLifespan() {
        return 200;
    }

    @Override
    public float getModelHeight() {
        return 6;
    }


    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }

    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected float getBounciness() {
        return 0.25F;
    }

    @Override
    protected int getBounceQuota() {
        return 3;
    }

}
