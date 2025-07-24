//package net.goo.brutality.entity.projectile.ray;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.registry.BrutalityModSounds;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.level.Level;
//import software.bernie.geckolib.core.animatable.GeoAnimatable;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//import software.bernie.geckolib.core.animation.AnimationController;
//import software.bernie.geckolib.core.animation.RawAnimation;
//import software.bernie.geckolib.util.GeckoLibUtil;
//
//import java.util.UUID;
//
//public class ExplosionRay extends Entity implements BrutalityGeoEntity {
//    private int lifespan = 20 * 8;
//    private int yaw, pitch, circleCount;
//    private static final EntityDataAccessor<Integer> DATA_YAW = SynchedEntityData.defineId(ExplosionRay.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Integer> DATA_PITCH = SynchedEntityData.defineId(ExplosionRay.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Integer> DATA_CIRCLE_COUNT = SynchedEntityData.defineId(ExplosionRay.class, EntityDataSerializers.INT);
//
//    public ExplosionRay(EntityType<?> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    @Override
//    protected void defineSynchedData() {
//        this.entityData.define(DATA_YAW, 0);
//        this.entityData.define(DATA_PITCH, 0);
//        this.entityData.define(DATA_CIRCLE_COUNT, 0);
//    }
//
//    public void setYaw(int yaw) {
//        this.yaw = yaw;
//        this.setSyncedYaw(yaw); // For client sync
//    }
//
//    public void setPitch(int pitch) {
//        this.pitch = pitch;
//        this.setSyncedPitch(pitch); // For client sync
//    }
//
//    public void setCircleCount(int count) {
//        this.circleCount = count;
//        this.setSyncedPitch(count); // For client sync
//    }
//
//    @Override
//    public boolean shouldRender(double pX, double pY, double pZ) {
//        return true;
//    }
//
//    @Override
//    public boolean shouldRenderAtSqrDistance(double pDistance) {
//        return true;
//    }
//
//    private void setSyncedYaw(int yaw) {
//        this.entityData.set(DATA_YAW, yaw);
//    }
//
//    private void setSyncedPitch(int pitch) {
//        this.entityData.set(DATA_PITCH, pitch);
//    }
//
//    private void setSyncedCircleCount(int count) {
//        this.entityData.set(DATA_CIRCLE_COUNT, count);
//    }
//
//    public int getSyncedYaw() {
//        return this.entityData.get(DATA_YAW);
//    }
//
//    public int getSyncedPitch() {
//        return this.entityData.get(DATA_PITCH);
//    }
//
//    public int getSyncedCircleCount() {
//        return this.entityData.get(DATA_CIRCLE_COUNT);
//    }
//
//    @Override
//    protected void readAdditionalSaveData(CompoundTag tag) {
//        this.yaw = tag.getInt("Yaw");
//        this.pitch = tag.getInt("Pitch");
//        this.circleCount = tag.getInt("CircleCount");
//        // Sync with entity data
//        this.setSyncedYaw(this.yaw);
//        this.setSyncedPitch(this.pitch);
//        this.setSyncedCircleCount(this.circleCount);
//    }
//
//    @Override
//    protected void addAdditionalSaveData(CompoundTag tag) {
//        tag.putInt("Yaw", this.yaw);
//        tag.putInt("Pitch", this.pitch);
//        tag.putInt("CircleCount", this.circleCount);
//    }
//
//
//
//    @Override
//    public GeoAnimatable cacheItem() {
//        return null;
//    }
//
//    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
//
//    public void setOwner(UUID ownerUUID) {
//    }
//
//    private boolean soundPlayed = false;
//    @Override
//    public void tick() {
//        super.tick();
//
//        // Server-side sync logic
//        if (!this.level().isClientSide) {
//            if (this.yaw != this.getSyncedYaw()) {
//                this.setSyncedYaw(this.yaw);
//            }
//            if (this.pitch != this.getSyncedPitch()) {
//                this.setSyncedPitch(this.pitch);
//            }
//            if (this.circleCount != this.getSyncedCircleCount()) { // Fixed this comparison
//                this.setSyncedCircleCount(this.circleCount);
//            }
//        }
//        else {
//            // Only play sound once we have valid data AND haven't played it yet
//            if (!soundPlayed && this.tickCount > 2) { // Wait 2 ticks for sync
//                int syncedCount = this.getSyncedCircleCount();
//                float volume = (syncedCount * 3) + 2;
//                this.level().playLocalSound(
//                        this.getX(), this.getY(), this.getZ(), // More precise than getOnPos()
//                        BrutalityModSounds.BIGGER_EXPLOSION.get(),
//                        SoundSource.BLOCKS,
//                        volume,
//                        1.0F,
//                        false
//                );
//                soundPlayed = true;
//            }
//        }
//
//        if (this.tickCount >= lifespan) {
//            discard();
//        }
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, (state) ->
//                state.setAndContinue(RawAnimation.begin().thenPlay("spawn")))
//        );
//    }
//}
