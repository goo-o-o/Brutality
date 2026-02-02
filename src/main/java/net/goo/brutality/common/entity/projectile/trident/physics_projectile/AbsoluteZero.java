package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.network.clientbound.ClientboundParticlePacket;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.ModUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AbsoluteZero extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public AbsoluteZero(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public AbsoluteZero(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
    }


    @Override
    public float getModelHeight() {
        return 8;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.SPLASH_POTION_BREAK;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.SPLASH_POTION_BREAK;
    }

    @Override
    protected float getBounciness() {
        return 0.0F;
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    @Override
    protected boolean shouldDiscardAfterBounce() {
        return true;
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        discard();
        Vec3 loc = hitResult.getLocation();

        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.FROSTMOURNE_WAVE.get(), 5, 20);

        if (this.level() instanceof ServerLevel serverLevel) {
            PacketHandler.sendToNearbyClients(serverLevel, loc.x, loc.y, loc.z, 128, new ClientboundParticlePacket(
                    waveParticleData, true, (float) loc.x, (float) loc.y + 0.1F, (float) loc.z, 0, 0, 0,
                    0, 0, 0, 1
            ));

            ModUtils.applyWaveEffect(serverLevel, this, Entity.class, waveParticleData, e -> e != getOwner(),
                    e -> {
                        e.setTicksFrozen(40);
                        if (e instanceof LivingEntity living) {
                            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
                        }
                    });

        }
    }

}
