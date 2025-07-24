//package net.goo.brutality.entity.projectile.trident;
//
//import net.goo.brutality.client.entity.BrutalityGeoEntity;
//import net.goo.brutality.entity.base.BrutalityAbstractTrident;
//import net.goo.brutality.particle.custom.RainbowTrailParticle;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.targeting.TargetingConditions;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.*;
//import software.bernie.geckolib.core.animatable.GeoAnimatable;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//public class ExobladeBeam extends BrutalityAbstractTrident implements BrutalityGeoEntity {
//    private static final EntityDataAccessor<Integer> HOMING_TARGET_ID = SynchedEntityData.defineId(ExobladeBeam.class, EntityDataSerializers.INT);
//
//    public ExobladeBeam(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
//        super(pEntityType, pLevel);
//        this.entityData.set(HOMING_TARGET_ID, -1);
//        this.collideWithBlocks(false);
//    }
//
//    public ExobladeBeam(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
//        super(pLevel, pShooter, pStack, trident);
//    }
//
//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        this.entityData.define(HOMING_TARGET_ID, -1);
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag pCompound) {
//        super.addAdditionalSaveData(pCompound);
//        pCompound.putInt("HomingTargetId", this.entityData.get(HOMING_TARGET_ID));
//    }
//
//    @Override
//    public float getGravity() {
//        return 0;
//    }
//
//    public float getDamage() {
//        return 12F;
//    }
//
//    @Override
//    public int getInGroundLifespan() {
//        return 60;
//    }
//
//    private boolean trailSpawned = false;
//
//    public void tick() {
//        if (!trailSpawned && level().isClientSide) {
//            this.level().addParticle((new RainbowTrailParticle.OrbData(this.getBbHeight() * 0.75F, this.getId(), 10)), this.getX(), this.getY() + getBbHeight() / 2, this.getZ(), 0, 0, 0);
//            trailSpawned = true;
//        }
//        super.tick();
//
//        tickDespawn();
//
//        if (!this.inGround && !this.dealtDamage) {
//            if (this.entityData.get(HOMING_TARGET_ID) == -1) {
//                LivingEntity nearestMob = this.level().getNearestEntity(
//                        LivingEntity.class,
//                        TargetingConditions.forCombat()
//                                .ignoreLineOfSight()
//                                .ignoreInvisibilityTesting()
//                                .selector(mob -> {
//                                    if (mob == this.getOwner()) return false;
//                                    if (mob instanceof Player player)
//                                        return !player.isCreative() && !player.isSpectator();
//                                    return true;
//                                }),
//                        null,
//                        this.getX(), this.getY(), this.getZ(),
//                        new AABB(this.blockPosition()).inflate(5)
//                );
//
//                if (nearestMob != null) {
//                    this.entityData.set(HOMING_TARGET_ID, nearestMob.getId());
//                }
//
//            } else {
//
//                Entity target = level().getEntity(this.entityData.get(HOMING_TARGET_ID));
//
//                if (target != null) {
//                    this.collideWithBlocks = false;
//                    Vec3 toVec = target.getPosition(1F).subtract(this.getPosition(1F));
//                    double distance = toVec.length();
//
//                    if (distance > 0.01) {
////                        double scale = 0.05 * (distance + 0.1);
//                        double scale = 7.5 / (distance * 2 + 0.1) + 0.25;
//                        Vec3 motion = toVec.normalize().scale(scale);
//                        this.addDeltaMovement(motion);
//
//                        if (this.getDeltaMovement().length() > 2) {
//                            this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
//                        }
//
//                    }
//                } else {
//                    this.collideWithBlocks = true;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
//        if (pResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.level() instanceof ServerLevel serverLevel && serverLevel.random.nextBoolean())  {
//            serverLevel.sendParticles(BrutalityModParticles.EXOBLADE_FLASH_PARTICLE.get(), pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z,
//                    1, 0.5, 0.5, 0.5, 0);
//            livingEntity.addEffect(new MobEffectInstance(BrutalityModMobEffects.MIRACLE_BLIGHT.get(), 80, 0, false, false));
//
//        }
//    }
//
//    @Override
//    protected void onHit(HitResult pResult) {
//        this.discard();
//        super.onHit(pResult);
//    }
//
//    @Override
//    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//    }
//
//    @Override
//    public GeoAnimatable cacheItem() {
//        return null;
//    }
//
//    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
//
//}