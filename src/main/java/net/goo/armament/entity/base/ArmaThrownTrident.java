package net.goo.armament.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ArmaThrownTrident extends AbstractArrow {
    public final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(this.getClass(), EntityDataSerializers.BYTE);
    public final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(this.getClass(), EntityDataSerializers.BOOLEAN);
    public ItemStack pickupItem = new ItemStack(Items.TRIDENT);
    public boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ArmaThrownTrident(EntityType<? extends ArmaThrownTrident> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ArmaThrownTrident(EntityType<? extends ArmaThrownTrident> entityType, Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(entityType, pShooter, pLevel);
        this.pickupItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }


    // CHANGE THESE
    public int getLifespan() {
        return 1200;
    }

    public double getGravity() {
        return -0.02D;
    }

    public float getDamage() {
        return 8F;
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    protected SoundEvent getHitEntitySoundEvent() {
        return SoundEvents.TRIDENT_HIT;
    }

    protected ItemStack getPickupItem() {
        return this.pickupItem;
    }

    protected void setPickupItem(ItemStack pickupItem) {
        this.pickupItem = pickupItem;
    }



    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void tick() {

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.tickCount >= getLifespan()) {
            this.discard();
        }

        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, getGravity(), 0.0D));  // Update the DeltaMovement

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) { // Check if hit mob or block and there is an owner

            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.discard();
                }

            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0))); // Base Loyalty + Loyalty level speed
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
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

    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = getDamage();
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.pickupItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity);
        this.dealtDamage = true;
        SoundEvent soundevent = getHitEntitySoundEvent();
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingentity) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity, livingentity);
                }

                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                    this.level().addFreshEntity(lightningbolt);
                    soundevent = SoundEvents.TRIDENT_THUNDER;
                    f1 = 5.0F;
                }
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    public void playerTouch(@NotNull Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
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
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }


}