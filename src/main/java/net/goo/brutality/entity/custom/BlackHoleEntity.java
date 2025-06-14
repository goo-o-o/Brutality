package net.goo.brutality.entity.custom;

import net.goo.brutality.client.entity.ArmaGeoEntity;
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
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public class BlackHoleEntity extends ThrowableProjectile implements ArmaGeoEntity {
    public BlackHoleEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
//        List<Player> nearbyPlayers = this.level().getNearbyPlayers(TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight(), null, this.getBoundingBox().inflate(8));
//        for (Player ignored : nearbyPlayers) {
//            EnvironmentColorManager.resetAllColors();
//        }
//    }

    @Override
    public String geoIdentifier() {
        return "black_hole";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }


}