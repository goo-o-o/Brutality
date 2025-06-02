package net.goo.armament.entity.custom.trident;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.goo.armament.client.entity.ArmaGeoEntity;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import javax.annotation.Nullable;

import static net.goo.armament.util.helpers.EnchantmentHelper.hasInfinity;

public class ThrownGungnir extends AbstractArrow implements ArmaGeoEntity {
    public static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownGungnir.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownGungnir.class, EntityDataSerializers.BOOLEAN);
    public ItemStack pickupItem = new ItemStack(Items.TRIDENT);
    public boolean dealtDamage;
    public int clientSideReturnTridentTickCount, targetsHit = 0, homingCooldown = 0, attackCount = 5; // Attack count needs to be higher by 1 as I'm too lazy to change the rest of the codes logic
    public LivingEntity target;
    private boolean hasBeenShot;
    private boolean leftOwner;
    private BlockState lastState;
    private int life;
    private final IntOpenHashSet ignoredEntities = new IntOpenHashSet();
    private boolean noTarget;

    public ThrownGungnir(EntityType<? extends ThrownGungnir> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownGungnir(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntities.THROWN_GUNGNIR_ENTITY.get(), pShooter, pLevel);
        this.pickupItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }

    // CHANGE THESE
    //==================================================================//

    public int getLifespan() {
        return 1200;
    }

    public double getGravity() {
        return -0.025D;
    }

    @Override
    public String geoIdentifier() {
        return "thrown_gungnir";
    }


    public float getDamage() {
        return 6F;
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    protected SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.TOTEM_USE;
    }

    protected SoundEvent getChannelingLightningSoundEvent() {
        return SoundEvents.TRIDENT_THUNDER;
    }

    protected boolean summonsLightningByDefault() {
        return false;
    }

    protected ItemStack getPickupItem() {
        return this.pickupItem;
    }

    protected void setPickupItem(ItemStack pickupItem) {
        this.pickupItem = pickupItem;
    }

    //==================================================================//


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        this.baseTick();
        /////////////
        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }

        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        //////////////
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Math.toDegrees(Mth.atan2(vec3.x, vec3.z))));
            this.setXRot((float) (Math.toDegrees(Mth.atan2(vec3.y, d0))));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
        //////////////////// BLOCK COLLISION
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);

        if (this.target == null && (this.targetsHit >= attackCount || this.targetsHit == 0)) {
            if (!blockstate.isAir() && !flag) {
                VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
                if (!voxelshape.isEmpty()) {
                    Vec3 vec31 = this.position();

                    for (AABB aabb : voxelshape.toAabbs()) {
                        if (aabb.move(blockpos).contains(vec31)) {
                            this.inGround = true;
                            break;
                        }
                    }
                }
            }
        }

        if (this.shakeTime > 0) {
            --this.shakeTime;
        }

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
            this.clearFire();
        }

        if (this.inGround && !flag) {
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            } else if (!this.level().isClientSide) {
                this.tickDespawn();
            }

            ++this.inGroundTime;
        } else {
            this.inGroundTime = 0;
            Vec3 vec32 = this.position();
            Vec3 vec33 = vec32.add(vec3);
            HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            while (!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) hitresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
                    switch (net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                        case SKIP_ENTITY:
                            if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                                this.onHit(hitresult);
                                this.hasImpulse = true;
                                break;
                            }
                            ignoredEntities.add(entityhitresult.getEntity().getId());
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT_NO_DAMAGE:
                            this.discard();
                            entityhitresult = null; // Don't process any further
                            break;
                        case STOP_AT_CURRENT:
                            this.setPierceLevel((byte) 0);
                        case DEFAULT:
                            this.onHit(hitresult);
                            this.hasImpulse = true;
                            break;
                    }
                }

                if (entityhitresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                hitresult = null;
            }

            if (this.isRemoved())
                return;

            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            if (this.isCritArrow()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.CRIT, this.getX() + d5 * (double) i / 4.0D, this.getY() + d6 * (double) i / 4.0D, this.getZ() + d1 * (double) i / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float) (Mth.atan2(-d5, -d1) * (double) (180F / (float) Math.PI)));
            } else {
                this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));
            }

            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            float f1 = 0.05F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f2 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
                }

                f = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.scale(f));
            if (!this.isNoGravity() && !flag) {
                Vec3 vec34 = this.getDeltaMovement();
                this.setDeltaMovement(vec34.x, vec34.y - (double) 0.05F, vec34.z);
            }

            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();
        }
        ////////

        if (this.homingCooldown >= 0) {
            this.homingCooldown--;
        }

        if (this.targetsHit >= attackCount) {
            if (this.inGroundTime > 4) {
                this.dealtDamage = true;
            }
            this.target = null;
        }

        if (this.tickCount >= getLifespan()) {
            this.discard();
        }

        if (this.target != null && this.target.isDeadOrDying()) {
            this.targetsHit = 5;
            this.dealtDamage = true;
            this.target = null;
        }

        if (!this.inGround) {
            if (this.tickCount % 5 == 0 && this.target == null && this.targetsHit < attackCount) {
                AABB searchArea = new AABB(this.position(), this.position()).inflate(15);
                target = level().getNearestEntity(LivingEntity.class,
                        TargetingConditions.DEFAULT.selector(
                                livingEntity -> {
                                    if (livingEntity instanceof Player player) {
                                       return !player.isCreative() && !player.isSpectator();
                                    }
                                    return true;
                                }
                        )
                        , ((LivingEntity) this.getOwner()), this.getX(), this.getY(), this.getZ(), searchArea);
            }
        } else this.noTarget = true;

        if (target != null && this.targetsHit < attackCount && this.homingCooldown <= 0) {
            Vec3 toTargetVec = target.getEyePosition().subtract(this.position());
            this.setPosRaw(this.getX(), this.getY() + toTargetVec.y * 0.015D, this.getZ());
            if (this.level().isClientSide) {
                this.yOld = this.getY();
            }

            double d0 = 0.5D; // Homing strength
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(toTargetVec.normalize().scale(d0))); // Base Loyalty + Loyalty level speed

        }

        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, getGravity(), 0.0D));  // Update the DeltaMovement
        Entity owner = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics() || this.noTarget) && owner != null) { // Check if hit mob or block and there is an owner
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.discard();
                }
            } else {
                // Ensure the trident returns even if it is in the ground
                if (this.inGround) {
                    this.inGround = false; // Reset inGround to allow movement
                }

                this.setNoPhysics(true);
                Vec3 toOwnerVec = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + toOwnerVec.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(toOwnerVec.normalize().scale(d0))); // Base Loyalty + Loyalty level speed
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

    }

    public boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.pickupItem);
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }


    protected void onHitEntity(EntityHitResult pResult) {
        Entity target = pResult.getEntity();
        float f = getDamage();
        if (target instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.pickupItem, livingentity.getMobType());
        }

        LivingEntity owner = (LivingEntity) this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, owner == null ? this : target);
        this.targetsHit++;
        if (this.targetsHit >= attackCount) {
            this.dealtDamage = true;
        }

        this.homingCooldown = 15;
        SoundEvent soundevent = getHitEntitySoundEvent();
        if (target.hurt(damagesource, f)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (target instanceof LivingEntity livingentity && owner != null) {
                EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                EnchantmentHelper.doPostDamageEffects(owner, target);
                this.doPostHurtEffects(livingentity);
            }
        }

        RandomSource random = this.level().getRandom();
        float range = 0.5F;
        this.setDeltaMovement(this.getDeltaMovement().multiply(0, -0.15, 0).add(ModUtils.nextFloatBetweenInclusive(random, -range, range), 0.3, ModUtils.nextFloatBetweenInclusive(random, -range, range)));

        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling() || summonsLightningByDefault()) {
            BlockPos blockpos = target.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                    this.level().addFreshEntity(lightningbolt);
                    soundevent = getChannelingLightningSoundEvent();
                    f1 = 5.0F;
                }
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    public void playerTouch(Player pEntity) {
        if (!this.level().isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            if (hasInfinity(this.getPickupItem())) {
                pEntity.take(this, 0);
                this.discard();
            } else if (this.tryPickup(pEntity)) {
                pEntity.take(this, 1);
                this.discard();
            }
        }
    }

    protected boolean tryPickup(Player pPlayer) {
        ItemStack tridentStack = this.getPickupItem();
        tridentStack.hurtAndBreak(this.targetsHit, pPlayer, (livingEntity) -> {
            livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(tridentStack);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains(this.toString(), 10)) {
            this.pickupItem = ItemStack.of(pCompound.getCompound(this.toString()));
        }

        this.dealtDamage = pCompound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.pickupItem));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put(this.toString(), this.pickupItem.save(new CompoundTag()));
        pCompound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }


    @Override
    public GeoAnimatable cacheItem() {
        return null;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected void onHitBlock(BlockHitResult pResult) {

        if (this.target == null && (this.targetsHit >= attackCount || this.targetsHit == 0)) {
            Vec3 vec3 = pResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
            this.setDeltaMovement(vec3);
            Vec3 vec31 = vec3.normalize().scale(0.05F);
            this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
            this.inGround = true;
        }

        this.setCritArrow(false);
        this.setPierceLevel((byte) 0);
        this.setShotFromCrossbow(false);
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for (Entity entity1 : this.level().getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (p_37272_) -> !p_37272_.isSpectator() && p_37272_.isPickable())) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
        this.life = 0;
    }
}