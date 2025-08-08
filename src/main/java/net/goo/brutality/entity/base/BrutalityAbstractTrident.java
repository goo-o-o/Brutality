package net.goo.brutality.entity.base;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.goo.brutality.client.entity.BrutalityGeoEntity;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

import static net.goo.brutality.util.helpers.EnchantmentHelper.hasInfinity;

public class BrutalityAbstractTrident extends BrutalityAbstractArrow implements BrutalityGeoEntity {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BrutalityAbstractTrident.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BrutalityAbstractTrident.class, EntityDataSerializers.BOOLEAN);
    private ItemStack pickupItem = new ItemStack(Items.TRIDENT);
    protected boolean dealtDamage, collideWithBlocks = true;
    public int clientSideReturnTridentTickCount;
    private int targetsHit = 0;

    protected final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

    public BrutalityAbstractTrident(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public BrutalityAbstractTrident(EntityType<? extends BrutalityAbstractTrident> pEntityType, Player player, Level pLevel) {
        super(pEntityType, player, pLevel);
    }

    public BrutalityAbstractTrident(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends AbstractArrow> trident) {
        super(trident, pShooter, pLevel);
        this.pickupItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }

    // CHANGE THESE
    //==================================================================//

    public int getInGroundLifespan() {
        return 1200;
    }

    public void collideWithBlocks(boolean collideWithBlocks) {
        this.collideWithBlocks = collideWithBlocks;
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

    protected SoundEvent getChannelingLightningSoundEvent() {
        return SoundEvents.TRIDENT_THUNDER;
    }

    protected boolean summonsLightningByDefault() {
        return false;
    }

    protected ItemStack getPickupItem() {
        return this.pickupItem;
    }

    protected Vec3 getTridentBounceStrength() {
        return new Vec3(-0.01D, -0.1D, -0.01D);
    }

    protected int getHitQuota() {
        return 1;
    }

    //==================================================================//


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        this.baseTick();
        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }

        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }


        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptableReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }


        boolean noPhysics = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float) (Math.toDegrees(Mth.atan2(vec3.x, vec3.z))));
            this.setXRot((float) (Math.toDegrees(Mth.atan2(vec3.y, d0))));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);

        if (collideWithBlocks) {
            if (!blockstate.isAir() && !noPhysics) {
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

        if (!this.level().isClientSide && !noPhysics) {
            this.tickDespawn();
        }

        if (this.inGround && !noPhysics) {
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
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
                    assert hitresult instanceof EntityHitResult;
                    Entity entity = ((EntityHitResult) hitresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !noPhysics) {
                    switch (net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                        case SKIP_ENTITY:
                            if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                                this.onHit(hitresult);
                                this.hasImpulse = true;
                                break;
                            }
                            assert entityhitresult != null;
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
                for (int j = 0; j < 4; ++j) {
                    this.level().addParticle(ParticleTypes.CRIT, this.getX() + d5 * (double) j / 4.0D, this.getY() + d6 * (double) j / 4.0D, this.getZ() + d1 * (double) j / 4.0D, -d5, -d6 + 0.2D, -d1);
                }
            }

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (noPhysics) {
                this.setYRot((float) (Mth.atan2(-d5, -d1) * (double) (180F / (float) Math.PI)));
            } else {
                this.setYRot((float) (Mth.atan2(d5, d1) * (double) (180F / (float) Math.PI)));
            }

            this.setXRot((float) (Mth.atan2(d6, d4) * (double) (180F / (float) Math.PI)));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
                }

                f = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.scale(f));
            if (!this.isNoGravity() && !noPhysics) {
                Vec3 vec34 = this.getDeltaMovement();
                this.setDeltaMovement(vec34.x, vec34.y - getGravity(), vec34.z);
            }

            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();
        }


    }

    protected boolean isAcceptableReturnOwner() {
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

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }


    AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (collideWithBlocks)
            super.onHitBlock(pResult);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity target = pResult.getEntity();
        float f = getDamage();
        if (target instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.pickupItem, livingentity.getMobType());
        }

        LivingEntity owner = (LivingEntity) this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, owner == null ? this : owner);
        if (this.targetsHit < getHitQuota() - 1) {
            this.targetsHit += 1;
        } else this.dealtDamage = true;
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

        this.setDeltaMovement(this.getDeltaMovement().multiply(getTridentBounceStrength().x, getTridentBounceStrength().y, getTridentBounceStrength().z));
        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling() || summonsLightningByDefault()) {
            BlockPos blockpos = target.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                    this.level().addFreshEntity(lightningbolt);
                    this.playSound(getChannelingLightningSoundEvent(), 5F, 1);
                }
            }
        }

    }


}