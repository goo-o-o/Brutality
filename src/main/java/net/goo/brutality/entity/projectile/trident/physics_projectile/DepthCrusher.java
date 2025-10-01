package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsProjectile;
import net.goo.brutality.entity.projectile.generic.AbyssProjectile;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class DepthCrusher extends BrutalityAbstractPhysicsProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> DATA_FINAL_ROLL = SynchedEntityData.defineId(DepthCrusher.class, EntityDataSerializers.INT);

    public DepthCrusher(EntityType<? extends AbstractArrow> trident, LivingEntity pShooter, Level pLevel) {
        super(trident, pShooter, pLevel);
        this.entityData.set(DATA_FINAL_ROLL, level().random.nextIntBetweenInclusive(-50, 50));
        this.pitch = 0;
    }

    public DepthCrusher(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.entityData.set(DATA_FINAL_ROLL, level().random.nextIntBetweenInclusive(-50, 50));
        this.pitch = 0;
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FINAL_ROLL, 0);
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
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        for (int i = 0; i < 3; i++) {
            AbyssProjectile projectile = new AbyssProjectile(BrutalityModEntities.ABYSS_PROJECTILE.get(), level());
            projectile.shoot(Mth.nextFloat(random, -0.1F, 0.1F), 1, Mth.nextFloat(random, -0.1F, 0.1F),
                    Mth.nextFloat(random, 0.5F, 0.65F), 1);
            projectile.setPos(getX(), getY() + getBbHeight() / 2, getZ());
            projectile.setOwner(getOwner());
            level().addFreshEntity(projectile);
        }
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return random.nextIntBetweenInclusive(0, 100) < 1 ? BrutalityModSounds.METAL_PIPE.get() : SoundEvents.TRIDENT_HIT_GROUND;
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
