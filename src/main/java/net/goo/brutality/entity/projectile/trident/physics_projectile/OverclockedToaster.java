package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsTrident;
import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.goo.brutality.registry.BrutalityModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OverclockedToaster extends BrutalityAbstractPhysicsTrident implements BrutalityGeoEntity {

    public OverclockedToaster(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
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
        return 4;
    }

    @Override
    public @NotNull SoundEvent getHitGroundSoundEvent() {
        return SoundEvents.METAL_HIT;
    }


    @Override
    public SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.METAL_HIT;
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
    public boolean shouldYawToMovement() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount % 10 == 0 && getOwner() instanceof LivingEntity livingEntity) {
            Toast toast = new Toast(BrutalityModEntities.TOAST.get(), level());
            toast.setPos(getX(), getY(), getZ());
            toast.shootFromRotation(livingEntity, getXRot(), getYRot(), 0, 1, 0);
            level().addFreshEntity(toast);
            playSound(SoundEvents.DISPENSER_DISPENSE);
        }

        this.setYRot((float) (getYRot() + 5 * (Math.PI / 180F)));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

    }
}
