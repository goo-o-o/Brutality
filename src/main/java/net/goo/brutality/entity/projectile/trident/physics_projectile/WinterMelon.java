package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.sounds.SoundEvent;
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

public class WinterMelon extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public WinterMelon(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WinterMelon(EntityType<? extends BrutalityAbstractTrident> pEntityType, Player player, Level pLevel) {
        super(pEntityType, player, pLevel);
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
    }
    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return BrutalityModSounds.SQUELCH.get();
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
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        Vec3 location = hitResult.getLocation();
        for (int i = 0; i < 16; i++) {
            this.level().addParticle(BrutalityModParticles.WINTERMELON_PARTICLE.get(),
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
        return 1;
    }

    @Override
    protected int getLifespan() {
         return 200;
    }
}
