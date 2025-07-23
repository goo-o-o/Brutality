package net.goo.brutality.entity.projectile.ray;

import net.goo.brutality.client.entity.BrutalityGeoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LastPrismRay extends ThrowableProjectile implements BrutalityGeoEntity {
    //    public static final EntityDataAccessor<Integer> DATA_OWNER_ID = SynchedEntityData.defineId(LastPrismRay.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_MAX_LENGTH = SynchedEntityData.defineId(LastPrismRay.class, EntityDataSerializers.INT);

    public LastPrismRay(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < (256 * 256);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    public void setDataMaxLength(int length) {
        this.entityData.set(DATA_MAX_LENGTH, length);
    }

    public int getDataMaxLength() {
        return this.entityData.get(DATA_MAX_LENGTH);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_MAX_LENGTH, 0);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = this.getOwner();

        if (owner != null) {
            if (owner instanceof Player player && !player.level().isClientSide()) {
                long despawnTime = this.getPersistentData().getLong("DespawnTime");
                if (despawnTime > 0 && this.level().getGameTime() >= despawnTime) {
                    this.discard();
                }

                Vec3 targetPos = player.getEyePosition()
                        .add(player.getLookAngle().scale(3.0));

                Vec3 currentPos = this.position();
                Vector3f newPos = currentPos.lerp(targetPos, 0.2).toVector3f();

                Vec3 movement = new Vec3(newPos).subtract(currentPos);
                this.setDeltaMovement(movement);

            }
        } else this.discard();

    }

//    public Entity getOwner() {
//        return this.level().getEntity(this.entityData.get(DATA_OWNER_ID));
//    }


    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenPlay("spawn")))
                .triggerableAnim("despawn", RawAnimation.begin().thenPlay("despawn"))
        );
    }


    @Override
    public void setOwner(@Nullable Entity pOwner) {
        super.setOwner(pOwner);
    }
}
