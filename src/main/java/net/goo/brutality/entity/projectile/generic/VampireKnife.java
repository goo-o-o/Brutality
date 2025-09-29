package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VampireKnife extends ThrowableProjectile implements BrutalityGeoEntity {
    public VampireKnife(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {

        super.tick();
        if (tickCount == 20) triggerAnim("controller", "spin");
        if (tickCount > 40) discard();
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (getOwner() instanceof LivingEntity livingEntity) {
            float damage = (float) livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
            if (livingEntity instanceof Player player)
                pResult.getEntity().hurt(damageSources().playerAttack(player), damage);
            else
                pResult.getEntity().hurt(damageSources().mobAttack(livingEntity), damage);
        } else {
            pResult.getEntity().hurt(damageSources().generic(), 5);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> PlayState.CONTINUE)
                .triggerableAnim("spin", RawAnimation.begin().thenPlayAndHold("spin")));
    }
}
