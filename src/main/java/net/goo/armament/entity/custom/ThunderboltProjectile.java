package net.goo.armament.entity.custom;

import net.goo.armament.particle.custom.ThunderboltTrailParticle;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModItems;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThunderboltProjectile extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThunderboltProjectile.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThunderboltProjectile.class, EntityDataSerializers.BOOLEAN);
    private ItemStack tridentItem = new ItemStack(ModItems.ZEUS_THUNDERBOLT_TRIDENT.get());
    private boolean dealtDamage;
    private int clientSideReturnTridentTickCount;

    public ThunderboltProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThunderboltProjectile(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(ModEntities.THROWN_ZEUS_THUNDERBOLT_ENTITY.get(), pShooter, pLevel);
        this.tridentItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(pStack));
        this.entityData.set(ID_FOIL, pStack.hasFoil());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
        this.entityData.define(ID_FOIL, false);
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void tick() {
        float ran = 0.04f;
        float r = 0.9F + random.nextFloat() * ran;
        float g = 0.9f + random.nextFloat() * ran;
        float b = 0.5f + random.nextFloat() * ran;
        this.level().addParticle((new ThunderboltTrailParticle.OrbData(r, g, b,2.75f + random.nextFloat() * 0.6f,3.75F + random.nextFloat() * 0.6f,this.getId())), this.getX(), this.getY(), this.getZ() , 0, 0, 0);

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.tickCount >= 1200) {
            this.discard();
        }

        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.02D, 0.0D));  // Update the DeltaMovement

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
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0))); // Base Loyalty + Loyalty level speed
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer);
    }


    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    /**
     * Gets the EntityHitResult representing the entity hit
     */
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.tridentItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level() instanceof ServerLevel) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level().canSeeSky(blockpos)) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (lightningbolt != null) {
                    lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer)entity1 : null);
                    this.level().addFreshEntity(lightningbolt);
                    soundevent = SoundEvents.TRIDENT_THUNDER;
                    f1 = 5.0F;
                }
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    /**
     * The sound made when an entity is hit by this projectile
     */
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }

    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("ThrownThunderbolt", 10)) {
            this.tridentItem = ItemStack.of(pCompound.getCompound("ThrownThunderbolt"));
        }

        this.dealtDamage = pCompound.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(this.tridentItem));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("ThrownThunderbolt", this.tridentItem.save(new CompoundTag()));
        pCompound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}