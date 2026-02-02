package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.registry.BrutalityDamageTypes;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class OverclockedToaster extends BrutalityAbstractThrowingProjectile implements BrutalityGeoEntity {
    private int lastAnim = 1;

    public OverclockedToaster(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public OverclockedToaster(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    public int getInGroundLifespan() {
        return 200;
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
    public void tick() {
        super.tick();

        if (tickCount % 5 == 0 && getOwner() instanceof LivingEntity livingEntity && !inGround) {
            LivingEntity nearest = level().getNearestEntity(LivingEntity.class,
                    TargetingConditions.DEFAULT, livingEntity, getX(), getY(), getZ(), getBoundingBox().inflate(10));
            if (nearest == null) return;


            Toast toast = new Toast(BrutalityEntities.TOAST.get(), level(), BrutalityDamageTypes.THROWING_PIERCE);
            toast.setPos(getX(), getY(), getZ());
            Vec3 target = nearest.getPosition(1).add(0, nearest.getBbHeight() / 2, 0);
            Vec3 origin = getPosition(1).add(0, getBbHeight() / 2, 0);

            float distanceTo = (float) target.distanceTo(origin);

            target.add(0, distanceTo / 5F, 0);

            Vec3 direction = target.subtract(origin).normalize();

            toast.shoot(direction.x, direction.y, direction.z, 1.5F, 0);

            getCapability(BrutalityCapabilities.SEAL_TYPE).ifPresent(toasterCap ->
                    toast.getCapability(BrutalityCapabilities.SEAL_TYPE).ifPresent(toastCap ->
                            toastCap.setSealType(toasterCap.getSealType())));

            toast.setOwner(livingEntity);
            level().addFreshEntity(toast);
            playSound(SoundEvents.DISPENSER_DISPENSE);

            triggerAnim("controller", "shoot_" + lastAnim);
            lastAnim = 3 - lastAnim;
            // So if lastAnim = 2, it'll go to 1, and if lastAnim = 1 it'll go to 2

        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", (state) ->
                PlayState.CONTINUE)
                .triggerableAnim("shoot_1", RawAnimation.begin().thenPlay("shoot_1"))
                .triggerableAnim("shoot_2", RawAnimation.begin().thenPlay("shoot_2"))
        );
    }
}
