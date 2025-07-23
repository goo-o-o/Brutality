package net.goo.brutality.entity.projectile.arrow;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.entity.base.BrutalityArrow;
import net.goo.brutality.registry.BrutalityModParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class LightArrow extends BrutalityArrow implements BrutalityGeoEntity {


    public LightArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected LightArrow(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public LightArrow(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public float getGravity() {
        return 0.005F;
    }

    @Override
    public byte getPierceLevel() {
        return 5;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGroundTime == 10) {
            triggerAnim("controller", "despawn");
        } else if (this.inGroundTime == 30) {
            this.discard();
        }
    }

    @Override
    protected void tickDespawn() {
        ++this.life;
        if (this.life == 200) {
            triggerAnim("controller", "despawn");
        } else if (this.life > 220) {
            this.discard();
        }

    }


    @Override
    public SimpleParticleType getCritParticle() {
        return BrutalityModParticles.SPARKLE_PARTICLE.get();
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", animationState -> PlayState.CONTINUE)
                .triggerableAnim("despawn", RawAnimation.begin().thenPlayAndHold("despawn"))
        );
    }


    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof Mob mob && mob.getMobType().equals(MobType.UNDEAD)) {
            mob.invulnerableTime = 0;
            mob.hurt(mob.damageSources().playerAttack((Player) this.getOwner()), 10);
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void playerTouch(Player pEntity) {
        if (!this.level().isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            if (this.tryPickup(pEntity)) {
                pEntity.take(this, 0);
                this.discard();
            }

        }
    }
}
