//package net.goo.brutality.entity.projectile.generic;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.entity.base.BrutalityAbstractArrow;
//import net.goo.brutality.network.PacketHandler;
//import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
//import net.goo.brutality.particle.base.AbstractCameraAlignedTrailParticle;
//import net.goo.brutality.registry.BrutalityCapabilities;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.goo.brutality.registry.BrutalityModSounds;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.NotNull;
//import software.bernie.geckolib.core.animatable.GeoAnimatable;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//import software.bernie.geckolib.util.GeckoLibUtil;
//
//public class StarEntity extends BrutalityAbstractArrow implements BrutalityGeoEntity {
//    public static final EntityDataAccessor<Integer> RANDOM_YAW = SynchedEntityData.defineId(StarEntity.class, EntityDataSerializers.INT);
//    public boolean renderForLayer = false;
//
//    public StarEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//    }
//
//    public StarEntity(@NotNull EntityType<StarEntity> pEntityType, Level level, double x, double y, double z) {
//        super(pEntityType, level, x, y, z);
//    }
//
//    @Override
//    protected boolean shouldUpdateArrowCount() {
//        return false;
//    }
//
//    @Override
//    public SoundEvent getHitEntitySound() {
//        return BrutalityModSounds.SQUELCH.get();
//    }
//
//    @Override
//    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
//        return BrutalityModSounds.SHURIKEN_IMPACT.get();
//    }
//
//    @Override
//    public GeoAnimatable cacheItem() {
//        return null;
//    }
//
//    public int getRandomYaw() {
//        return this.entityData.get(RANDOM_YAW);
//    }
//
//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        this.entityData.define(RANDOM_YAW, level().random.nextIntBetweenInclusive(0, 180));
//    }
//
//    @Override
//    public boolean isNoGravity() {
//        return true;
//    }
//
//    @Override
//    public float getAirDrag() {
//        return 1;
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//
//    }
//
//    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this, true);
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
//
//
//    @Override
//    public void tick() {
//        if (firstTick && this.level().isClientSide()) {
//            this.level().addParticle((new AbstractCameraAlignedTrailParticle.OrbData(1, 1, 0,
//                            this.getBbHeight() * 4F, this.getId(), 10)), this.getX(),
//                    this.getY() - this.getBbHeight() / 2, this.getZ(), 0, 0, 0);
//        }
//
//        super.tick();
//
//        if (life > 200 && !inGround) discard();
//    }
//
//    @Override
//    protected int getInGroundLifespan() {
//        return 6000;
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult pResult) {
//        Entity entity = pResult.getEntity();
//
//        entity.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP).ifPresent(cap -> {
//            if (getOwner() instanceof LivingEntity owner) {
//                cap.incrementStarCount(owner.getUUID());
//                if (!level().isClientSide())
//                    PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(entity.getId(), entity));
//            }
//        });
//
//        super.onHitEntity(pResult);
//
//        Vec3 loc = pResult.getLocation();
//        if (level() instanceof ServerLevel serverLevel) {
////            sendSystemMessage(Component.literal("spawned"));
//            serverLevel.sendParticles(BrutalityModParticles.STAR_PARTICLE.get(),
//                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);
//        }
//
//        this.discard();
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult pResult) {
//        Vec3 loc = pResult.getLocation();
//
//        if (level() instanceof ServerLevel serverLevel)
//            serverLevel.sendParticles(BrutalityModParticles.STAR_PARTICLE.get(),
//                    loc.x, loc.y, loc.z, 10, 0.5, 0.5, 0.5, 0);
//
//        super.onHitBlock(pResult);
//    }
//
//    @Override
//    protected @NotNull ItemStack getPickupItem() {
//        return ItemStack.EMPTY;
//    }
//
//
//}
