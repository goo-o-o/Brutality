package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SculkGrenade extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public SculkGrenade(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.pickup = Pickup.DISALLOWED;
    }


    @Override
    public int getInGroundLifespan() {
        return 200;
    }

    @Override
    public float getDamage(@Nullable LivingEntity livingEntity) {
        if (livingEntity != null) {
            livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        }
        return 4;
    }

    @Override
    public float getModelHeight() {
        return 6;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalityModSounds.ZAP.get();
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.ZAP.get();
    }

    @Override
    protected float getBounciness() {
        return 0.6F;
    }

    @Override
    protected int getBounceQuota() {
        return 3;
    }

    @Override
    protected boolean shouldDiscardAfterBounce() {
        return true;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityModParticles.SONIC_WAVE.get(), 2.5F, 50);
        DamageSource damageSource;
        float damage;
        if (getOwner() instanceof LivingEntity living) {
            damageSource = damageSources().mobAttack(living);
            damage = getDamage(living);
        } else if (getOwner() instanceof Player player) {
            damageSource = damageSources().playerAttack(player);
            damage = getDamage(player);
        } else {
            damageSource = damageSources().generic();
            damage = getDamage(null);
        }


        level().addParticle(waveParticleData, getX(), getY(), getZ(), 0, 0, 0);

        if (level() instanceof ServerLevel serverLevel) {
            ModUtils.applyWaveEffect(serverLevel, this, LivingEntity.class, waveParticleData, e -> e != getOwner(), e -> {
                e.invulnerableTime = 0;
                e.hurt(damageSource, damage);
                ((LivingEntity) e).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10));
            });
        }
    }

}
