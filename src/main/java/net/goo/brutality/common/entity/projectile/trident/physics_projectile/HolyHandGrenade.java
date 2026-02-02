package net.goo.brutality.common.entity.projectile.trident.physics_projectile;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.goo.brutality.util.ModUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class HolyHandGrenade extends BrutalityAbstractPhysicsThrowingProjectile implements BrutalityGeoEntity {


    public HolyHandGrenade(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public HolyHandGrenade(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
    }

    @Override
    protected int getLifespan() {
        return 60;
    }


    @Override
    public float getModelHeight() {
        return 8;
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
        return 0.25F;
    }

    @Override
    protected int getBounceQuota() {
        return 9;
    }

    @Override
    public float getGravity() {
        return 0.075F;
    }

    @Override
    protected Vec3 getEntityBounceStrength() {
        return super.getEntityBounceStrength().scale(2);
    }

    @Override
    public void tick() {
        if (tickCount == 40)
            triggerAnim("controller", "explode");

        if (tickCount >= getLifespan() && !level().isClientSide()) {
            level().explode(getOwner(), getX(), getY(), getZ(), 5F, false, ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
            discard();
        }

        if (tickCount % 2 == 0)
            level().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, getX(), getY(), getZ(), 0, 0, 0);

        super.tick();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> PlayState.CONTINUE)
                .triggerableAnim("explode", RawAnimation.begin().thenPlayAndHold("explode")));
    }
}
