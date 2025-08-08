package net.goo.brutality.entity.projectile.generic;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class CruelSunEntity extends ThrowableProjectile implements BrutalityGeoEntity {
    public CruelSunEntity(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() != null && getOwner().level() == this.level()) {
            List<Entity> nearbyEntities = level().getEntities(this, this.getBoundingBox().inflate(25),
                    e -> e instanceof LivingEntity && !(e instanceof Player && (e.isSpectator() || ((Player) e).isCreative())));

            for (Entity entity : nearbyEntities) {
                if (entity != this && entity != getOwner()) {
                    entity.setSecondsOnFire(5);
                }

            }

            if (getOwner() instanceof Player owner) {

                Vec3 movement = new Vec3(owner.getX(), owner.getY() + 20, owner.getZ()).subtract(this.position());


                this.setDeltaMovement(movement.scale(0.3F));
            }

        } else this.discard();
    }


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}