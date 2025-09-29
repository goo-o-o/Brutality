package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermafrostCube extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public PermafrostCube(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
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
        return 8;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.GLASS_BREAK;
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
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        level().getEntities(this, this.getBoundingBox().inflate(2.5, 2.5, 2.5)).forEach(e -> {
            e.setTicksFrozen(60);
            e.hurt(damageSources().freeze(), 2);
        });
    }
}