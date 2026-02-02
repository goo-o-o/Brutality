package net.goo.brutality.common.entity.projectile.generic;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.goo.brutality.util.ModResources.ABYSS_TRAIL_FX;

public class AbyssProjectile extends ThrowableProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(AbyssProjectile.class, EntityDataSerializers.INT);

    public AbyssProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HOMING_TARGET_ID, -1);
    }

    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void tick() {

        if (firstTick && !(level() instanceof ServerLevel)) {
            EntityEffect abyssTrail = new EntityEffect(ABYSS_TRAIL_FX, this.level(), this, EntityEffect.AutoRotate.NONE);
            abyssTrail.start();
        }


        super.tick();

        if (this.tickCount > 100) discard();
        if (this.entityData.get(HOMING_TARGET_ID) == -1) {
            LivingEntity entity = level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.selector(target -> target != getOwner()),
                    null, getX(), getY(), getZ(), getBoundingBox().inflate(10));

            this.entityData.set(HOMING_TARGET_ID, entity != null ? entity.getId() : -1);
        } else {
            Entity entity = level().getEntity(entityData.get(HOMING_TARGET_ID));
            this.setNoGravity(true);

            if (entity instanceof LivingEntity target) {
                Vec3 targetVec = target.getPosition(1).add(0, entity.getBbHeight() / 2, 0).subtract(getPosition(1));
                double distance = targetVec.length();

                if (distance > 0.01) {
//                        double scale = 0.05 * (distance + 0.1);
                    double scale = 1 / (distance * 2 + 0.1) + 0.25;
                    Vec3 motion = targetVec.normalize().scale(scale);
                    this.addDeltaMovement(motion);

                    if (this.getDeltaMovement().length() > 1) {
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
                    }

                }

            }
        }

    }


    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        entity.invulnerableTime = 0;
        Entity owner = getOwner();
        if (owner != null && entity != owner) {
            entity.hurt(entity.damageSources().indirectMagic(owner, this), 5);
        }
        this.discard();
        super.onHitEntity(pResult);
    }
}
