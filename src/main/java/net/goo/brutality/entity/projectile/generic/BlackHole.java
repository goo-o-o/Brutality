package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class BlackHole extends ThrowableProjectile implements BrutalityGeoEntity {
    public BlackHole(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        Vec3 blackHolePos = this.getPosition(1.0F);
        super.tick();
        if (getOwner() != null && getOwner().level() == this.level()) {
            List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(7),
                    e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative())));

            for (Entity entity : nearbyEntities) {
                if (entity != this && entity != getOwner()) {
                    Vec3 targetPos = entity.getPosition(1.0F);
                    Vec3 targetVector = blackHolePos.subtract(targetPos).normalize();

                    targetVector.add(0, this.getBbHeight() * .5F, 0);

                    entity.addDeltaMovement(targetVector.scale(0.15F).scale(BrutalityCommonConfig.BLACK_HOLE_PULL_STRENGTH.get()));
                    if (entity instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                    if (getOwner() instanceof Player owner)
                        entity.hurt(entity.damageSources().playerAttack(owner), BrutalityCommonConfig.BLACK_HOLE_TICK_DAMAGE.get());
                    else
                        entity.hurt(entity.damageSources().flyIntoWall(), BrutalityCommonConfig.BLACK_HOLE_TICK_DAMAGE.get());
                }

            }

            if (getOwner() instanceof Player owner) {
                Vec3 movement = new Vec3(owner.getX(), owner.getY() + 5, owner.getZ()).subtract(this.position());
                this.setDeltaMovement(movement.scale(0.3F));
            }

        } else this.discard();
    }

//    @Override
//    public void onRemovedFromWorld() {
//        List<Player> nearbyPlayers = this.spellLevel().getNearbyPlayers(TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight(), null, this.getBoundingBox().inflate(8));
//        for (Player ignored : nearbyPlayers) {
//            EnvironmentColorManager.resetAllColors();
//        }
//    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}