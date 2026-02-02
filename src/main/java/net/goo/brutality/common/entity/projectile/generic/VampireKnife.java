package net.goo.brutality.common.entity.projectile.generic;

import net.goo.brutality.common.entity.base.BrutalityAbstractThrowingProjectile;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VampireKnife extends BrutalityAbstractThrowingProjectile {


    public VampireKnife(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, pLevel, damageTypeResourceKey);
    }

    public VampireKnife(EntityType<? extends BrutalityAbstractThrowingProjectile> pEntityType, Player player, Level pLevel, ResourceKey<DamageType> damageTypeResourceKey) {
        super(pEntityType, player, pLevel, damageTypeResourceKey);
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> PlayState.CONTINUE)
                .triggerableAnim("spin", RawAnimation.begin().thenPlayAndHold("spin")));
    }
}
