package net.goo.brutality.entity.base;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BrutalityRay extends Projectile implements BrutalityGeoEntity {
    public static final EntityDataAccessor<Integer> DATA_MAX_LENGTH = SynchedEntityData.defineId(BrutalityRay.class, EntityDataSerializers.INT);
    public boolean shouldFollowOwner = true;

    protected BrutalityRay(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_MAX_LENGTH, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", (state) ->
                state.setAndContinue(RawAnimation.begin().thenPlay("spawn")))
                .triggerableAnim("despawn", RawAnimation.begin().thenPlay("despawn"))
        );
    }


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }


    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < (256 * 256);
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    public void setDataMaxLength(int length) {
        this.entityData.set(DATA_MAX_LENGTH, length);
    }

    public int getDataMaxLength() {
        return this.entityData.get(DATA_MAX_LENGTH);
    }

    @Override
    public void tick() {




        Entity owner = this.getOwner();

        if (shouldFollowOwner) {
            if (owner != null) {
                if (owner instanceof Player player) {
                    Vec3 vec3 = this.getDeltaMovement();
                    double d2 = this.getX() + vec3.x;
                    double d0 = this.getY() + vec3.y;
                    double d1 = this.getZ() + vec3.z;
                    this.setDeltaMovement(vec3.scale(0.99F));

                    this.setPos(d2, d0, d1);

                    Vec3 targetPos = player.getEyePosition().add(player.getLookAngle().scale(3.0));

                    Vec3 currentPos = this.position();
                    Vector3f newPos = currentPos.lerp(targetPos, 0.2).toVector3f();

                    Vec3 movement = new Vec3(newPos).subtract(currentPos);
                    this.setDeltaMovement(movement);
                }
            } else this.discard();
        }
        super.tick();

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
}
