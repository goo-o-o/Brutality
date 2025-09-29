package net.goo.brutality.entity.projectile.generic;

import com.lowdragmc.photon.client.fx.EntityEffect;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.goo.brutality.util.ModResources.VAMPIRE_TRIAL_FX;

public class VampireHealProjectile extends ThrowableProjectile implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(VampireHealProjectile.class, EntityDataSerializers.INT);

    public VampireHealProjectile(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
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
    public void setNoGravity(boolean pNoGravity) {
        super.setNoGravity(true);
    }

    @Override
    public void tick() {

        if (firstTick && !(level() instanceof ServerLevel)) {
            EntityEffect vampireTrail = new EntityEffect(VAMPIRE_TRIAL_FX, this.level(), this, EntityEffect.AutoRotate.NONE);
            vampireTrail.start();
        }


        super.tick();

        if (getOwner() instanceof LivingEntity owner) {
            Vec3 targetVec = owner.getPosition(1).add(0, owner.getBbHeight() / 2, 0).subtract(getPosition(1));
            double distance = targetVec.length();
            if (distance > 0.01) {
                double scale = 1 / (distance * 2 + 0.1) + 0.25;
                Vec3 motion = targetVec.normalize().scale(scale);
                this.addDeltaMovement(motion);

                if (this.getDeltaMovement().length() > 1) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
                }

            }

            if (owner.distanceTo(this) < 0.25 && !level().isClientSide()) {
                owner.heal((float) owner.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.075F);
                discard();
            }
        }

    }


    @Override
    public void onClientRemoval() {
        EntityEffect vampireTrail = new EntityEffect(VAMPIRE_TRIAL_FX, level(), this, EntityEffect.AutoRotate.NONE);
        if (vampireTrail.getRuntime() != null) {
            vampireTrail.getRuntime().destroy(false);
            EntityEffect.CACHE.computeIfPresent(this, (entity, effects) -> {
                effects.remove(vampireTrail);
                return effects.isEmpty() ? null : effects;
            });
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
