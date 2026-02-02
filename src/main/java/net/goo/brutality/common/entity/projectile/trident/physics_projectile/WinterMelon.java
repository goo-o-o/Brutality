package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WinterMelon extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public WinterMelon(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public WinterMelon(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }
    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalitySounds.SQUELCH.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

       level().getEntities(this, this.getBoundingBox().inflate(2.5), e -> e instanceof LivingEntity).forEach(living -> {
           ((LivingEntity) living).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, true));
           living.setTicksFrozen(20);
       });
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);

        Vec3 location = hitResult.getLocation();
        for (int i = 0; i < 16; i++) {
            this.level().addParticle(BrutalityParticles.WINTERMELON_PARTICLE.get(),
                    location.x, location.y, location.z,
                    (random.nextDouble() - 0.5) * 0.3,
                    random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.3);
        }
    }

    @Override
    protected float getBounciness() {
        return 0.15F;
    }

    @Override
    public float getModelHeight() {
        return 16;
    }

    @Override
    protected int getBounceQuota() {
        return 3;
    }

    @Override
    protected int getLifespan() {
         return 200;
    }
}
